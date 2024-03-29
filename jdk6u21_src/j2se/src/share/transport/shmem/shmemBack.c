/*
 * @(#)shmemBack.c	1.22 10/03/23
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
#include <string.h>

#include "jdwpTransport.h"
#include "shmemBase.h"
#include "sysShmem.h"
#include "sys.h"

/*
 * The Shared Memory Transport Library.
 *
 * This module is an implementation of the Java Debug Wire Protocol Transport
 * Service Provider Interface - see src/share/javavm/export/jdwpTransport.h.
 */
 
static SharedMemoryTransport *transport = NULL;  /* maximum of 1 transport */ 
static SharedMemoryConnection *connection = NULL;  /* maximum of 1 connection */ 
static jdwpTransportCallback *callbacks;
static jboolean initialized;
static struct jdwpTransportNativeInterface_ interface;
static jdwpTransportEnv single_env = (jdwpTransportEnv)&interface;

/*
 * Thread-local index to the per-thread error message
 */
static int tlsIndex;

/*
 * Return an error and record the error message associated with
 * the error. Note the if (1==1) { } usage here is to avoid 
 * compilers complaining that a statement isn't reached which
 * will arise if the semicolon (;) appears after the macro,
 */
#define RETURN_ERROR(err, msg)		\
	if (1==1) {			\
            setLastError(err, msg);	\
            return err;			\
        }

/*
 * Return an I/O error and record the error message.
 */
#define RETURN_IO_ERROR(msg)	RETURN_ERROR(JDWPTRANSPORT_ERROR_IO_ERROR, msg);


/*
 * Set the error message for this thread. If the error is an I/O
 * error then augment the supplied error message with the textual
 * representation of the I/O error.
 */
static void
setLastError(int err, char *newmsg) {
    char buf[255];
    char *msg;
    
    /* get any I/O first in case any system calls override errno */
    if (err == JDWPTRANSPORT_ERROR_IO_ERROR) {
	if (shmemBase_getlasterror(buf, sizeof(buf)) != SYS_OK) {
	    buf[0] = '\0';
	}
    }

    /* free any current error */
    msg = (char *)sysTlsGet(tlsIndex);
    if (msg != NULL) {
        (*callbacks->free)(msg);
    }

    /* 
     * For I/O errors append the I/O error message with to the
     * supplied message. For all other errors just use the supplied
     * message.
     */
    if (err == JDWPTRANSPORT_ERROR_IO_ERROR) {
	char *join_str = ": ";
	int msg_len = strlen(newmsg) + strlen(join_str) + strlen(buf) + 3;	
	msg = (*callbacks->alloc)(msg_len);
	if (msg != NULL) {
	    strcpy(msg, newmsg);
	    strcat(msg, join_str);
	    strcat(msg, buf);
	}
    } else {
	msg = (*callbacks->alloc)(strlen(newmsg)+1);
        if (msg != NULL) {
            strcpy(msg, newmsg); 
        }
    }
    
    /* Put a pointer to the message in TLS */    
    sysTlsPut(tlsIndex, msg);    
}

static jdwpTransportError
handshake() 
{
    char *hello = "JDWP-Handshake";
    unsigned int i;

    for (i=0; i<strlen(hello); i++) {
	jbyte b;
	int rv = shmemBase_receiveByte(connection, &b);
	if (rv != 0) {
	    RETURN_IO_ERROR("receive failed during handshake");
	}
	if ((char)b != hello[i]) {
	    RETURN_IO_ERROR("handshake failed - debugger sent unexpected message");
	}
    }

    for (i=0; i<strlen(hello); i++) {
	int rv = shmemBase_sendByte(connection, (jbyte)hello[i]);
	if (rv != 0) {
	    RETURN_IO_ERROR("write failed during handshake");
	}
    }

    return JDWPTRANSPORT_ERROR_NONE;
}


/*
 * Return the capabilities of the shared memory transport. The shared
 * memory transport supports both the attach and accept timeouts but
 * doesn't support a handshake timeout.
 */
static jdwpTransportError JNICALL
shmemGetCapabilities(jdwpTransportEnv* env, JDWPTransportCapabilities *capabilitiesPtr)
{
    JDWPTransportCapabilities result;

    memset(&result, 0, sizeof(result));
    result.can_timeout_attach = JNI_TRUE;
    result.can_timeout_accept = JNI_TRUE;
    result.can_timeout_handshake = JNI_FALSE;

    *capabilitiesPtr = result;

    return JDWPTRANSPORT_ERROR_NONE;
}


static jdwpTransportError JNICALL
shmemStartListening(jdwpTransportEnv* env, const char *address, char **actualAddress)
{
    jint rc;
    
    if (connection != NULL || transport != NULL) {
	RETURN_ERROR(JDWPTRANSPORT_ERROR_ILLEGAL_STATE, "already connected or already listening");
    }
    
    rc = shmemBase_listen(address, &transport);

    /*
     * If a name was selected by the function above, find it and return
     * it in place of the original arg. 
     */
    if (rc == SYS_OK) { 
        char *name;
        char *name2;
        rc = shmemBase_name(transport, &name);
        if (rc == SYS_OK) {
            name2 = (callbacks->alloc)(strlen(name) + 1);
            if (name2 == NULL) {
                RETURN_ERROR(JDWPTRANSPORT_ERROR_OUT_OF_MEMORY, "out of memory");
            } else {
                strcpy(name2, name);
                *actualAddress = name2;
            }
        }
    } else {
	RETURN_IO_ERROR("failed to create shared memory listener");
    }
    return JDWPTRANSPORT_ERROR_NONE;
}

static jdwpTransportError JNICALL
shmemAccept(jdwpTransportEnv* env, jlong acceptTimeout, jlong handshakeTimeout) 
{
    jint rc;

    if (connection != NULL) {
	RETURN_ERROR(JDWPTRANSPORT_ERROR_ILLEGAL_STATE, "already connected");
    }
    if (transport == NULL) {
	RETURN_ERROR(JDWPTRANSPORT_ERROR_ILLEGAL_STATE, "transport not listening");
    }
    
    rc = shmemBase_accept(transport, (long)acceptTimeout, &connection);
    if (rc != SYS_OK) {
        if (rc == SYS_TIMEOUT) {
	    RETURN_ERROR(JDWPTRANSPORT_ERROR_TIMEOUT, "Timed out waiting for connection");
	} else {
	    RETURN_IO_ERROR("failed to accept shared memory connection");
	}
    }

    rc = handshake();
    if (rc != JDWPTRANSPORT_ERROR_NONE) {
	shmemBase_closeConnection(connection);
	connection = NULL;
    }
    return rc;
}

static jdwpTransportError JNICALL
shmemStopListening(jdwpTransportEnv* env) 
{
    if (transport != NULL) {
	shmemBase_closeTransport(transport);
	transport = NULL;
    }
    return JDWPTRANSPORT_ERROR_NONE;
}

static jdwpTransportError JNICALL
shmemAttach(jdwpTransportEnv* env, const char *address, jlong attachTimeout, jlong handshakeTimeout)
{
    jint rc;

    if (connection != NULL) {
	RETURN_ERROR(JDWPTRANSPORT_ERROR_ILLEGAL_STATE, "already connected");
    }    
    rc = shmemBase_attach(address, (long)attachTimeout, &connection);    
    if (rc != SYS_OK) {
        if (rc == SYS_NOMEM) {
	    RETURN_ERROR(JDWPTRANSPORT_ERROR_OUT_OF_MEMORY, "out of memory");
	}
	if (rc == SYS_TIMEOUT) {
            RETURN_ERROR(JDWPTRANSPORT_ERROR_TIMEOUT, "Timed out waiting to attach");
	}
	RETURN_IO_ERROR("failed to attach to shared memory connection");
    }

    rc = handshake();
    if (rc != JDWPTRANSPORT_ERROR_NONE) {
	shmemBase_closeConnection(connection);
	connection = NULL;
    }
    return rc;
}

static jdwpTransportError JNICALL
shmemWritePacket(jdwpTransportEnv* env, const jdwpPacket *packet) 
{
    if (packet == NULL) {
	RETURN_ERROR(JDWPTRANSPORT_ERROR_ILLEGAL_ARGUMENT, "packet is null");
    }
    if (packet->type.cmd.len < 11) {
	RETURN_ERROR(JDWPTRANSPORT_ERROR_ILLEGAL_ARGUMENT, "invalid length");
    }
    if (connection == NULL) {
	RETURN_ERROR(JDWPTRANSPORT_ERROR_ILLEGAL_STATE, "not connected");
    }
    if (shmemBase_sendPacket(connection, packet) == SYS_OK) {
	return JDWPTRANSPORT_ERROR_NONE;
    } else {
	RETURN_IO_ERROR("write packet failed");
    }
}

static jdwpTransportError JNICALL
shmemReadPacket(jdwpTransportEnv* env, jdwpPacket *packet) 
{
    if (packet == NULL) {
	RETURN_ERROR(JDWPTRANSPORT_ERROR_ILLEGAL_ARGUMENT, "packet is null");
    }
    if (connection == NULL) {
	RETURN_ERROR(JDWPTRANSPORT_ERROR_ILLEGAL_STATE, "not connected");
    }
    if (shmemBase_receivePacket(connection, packet) == SYS_OK) {
	return JDWPTRANSPORT_ERROR_NONE;
    } else {
	RETURN_IO_ERROR("receive packet failed");
    }
}

static jboolean JNICALL
shmemIsOpen(jdwpTransportEnv* env)
{
    if (connection != NULL) {
	return JNI_TRUE;
    } else {
	return JNI_FALSE;
    }
}

static jdwpTransportError JNICALL
shmemClose(jdwpTransportEnv* env) 
{
    SharedMemoryConnection* current_connection = connection;
    if (current_connection != NULL) {
        connection = NULL;
	shmemBase_closeConnection(current_connection);
    }
    return JDWPTRANSPORT_ERROR_NONE;
}

/*
 * Return the error message for this thread.
 */
static jdwpTransportError  JNICALL
shmemGetLastError(jdwpTransportEnv* env, char **msgP) 
{
    char *msg = (char *)sysTlsGet(tlsIndex);
    if (msg == NULL) {
	return JDWPTRANSPORT_ERROR_MSG_NOT_AVAILABLE;
    }
    *msgP = (*callbacks->alloc)(strlen(msg)+1);
    if (*msgP == NULL) {
	return JDWPTRANSPORT_ERROR_OUT_OF_MEMORY;
    }
    strcpy(*msgP, msg);
    return JDWPTRANSPORT_ERROR_NONE;  
}

JNIEXPORT jint JNICALL 
jdwpTransport_OnLoad(JavaVM *vm, jdwpTransportCallback* cbTablePtr,
		     jint version, jdwpTransportEnv** result)
{
    if (version != JDWPTRANSPORT_VERSION_1_0) {
	return JNI_EVERSION;
    }
    if (initialized) {
	/* 
	 * This library doesn't support multiple environments (yet)
	 */
	return JNI_EEXIST;
    }
    initialized = JNI_TRUE;

    /* initialize base shared memory system */
   (void) shmemBase_initialize(vm, cbTablePtr);

    /* save callbacks */
    callbacks = cbTablePtr;

    /* initialize interface table */
    interface.GetCapabilities = &shmemGetCapabilities;
    interface.Attach = &shmemAttach;
    interface.StartListening = &shmemStartListening;
    interface.StopListening = &shmemStopListening;
    interface.Accept = &shmemAccept;
    interface.IsOpen = &shmemIsOpen;
    interface.Close = &shmemClose;
    interface.ReadPacket = &shmemReadPacket;
    interface.WritePacket = &shmemWritePacket;
    interface.GetLastError = &shmemGetLastError;
    *result = &single_env;

    /* initialized TLS */
    tlsIndex = sysTlsAlloc();

    return JNI_OK;
}
