/*
 * @(#)AiffFileWriter.java	1.21 03/12/19
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.media.sound;

import java.util.Vector;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.DataInputStream;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.RandomAccessFile;
import java.io.SequenceInputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;

//$$fb this class is buggy. Should be replaced in future.

/**
 * AIFF file writer.
 *
 * @version 1.21 03/12/19
 * @author Jan Borgersen
 */
public class AiffFileWriter extends SunFileWriter {

    /**
     * AIFF type
     */
    private static final AudioFileFormat.Type aiffTypes[] = {
	AudioFileFormat.Type.AIFF
    };


    /**
     * Constructs a new AiffFileWriter object.
     */
    public AiffFileWriter() {
	super(aiffTypes);
    }


    // METHODS TO IMPLEMENT AudioFileWriter

    public AudioFileFormat.Type[] getAudioFileTypes(AudioInputStream stream) {

	AudioFileFormat.Type[] filetypes = new AudioFileFormat.Type[types.length];
	System.arraycopy(types, 0, filetypes, 0, types.length);

	// make sure we can write this stream
	AudioFormat format = stream.getFormat();
	AudioFormat.Encoding encoding = format.getEncoding();

	if( (AudioFormat.Encoding.ALAW.equals(encoding)) ||
	    (AudioFormat.Encoding.ULAW.equals(encoding)) ||
	    (AudioFormat.Encoding.PCM_SIGNED.equals(encoding)) ||
	    (AudioFormat.Encoding.PCM_UNSIGNED.equals(encoding)) ) {

	    return filetypes;
	}

	return new AudioFileFormat.Type[0];
    }


    public int write(AudioInputStream stream, AudioFileFormat.Type fileType, OutputStream out) throws IOException {

	//$$fb the following check must come first ! Otherwise
	// the next frame length check may throw an IOException and
	// interrupt iterating File Writers. (see bug 4351296)

	// throws IllegalArgumentException if not supported
	AiffFileFormat aiffFileFormat = (AiffFileFormat)getAudioFileFormat(fileType, stream);

	// we must know the total data length to calculate the file length
	if( stream.getFrameLength() == AudioSystem.NOT_SPECIFIED ) {
	    throw new IOException("stream length not specified");
	}

	int bytesWritten = writeAiffFile(stream, aiffFileFormat, out);
	return bytesWritten;
    }


    public int write(AudioInputStream stream, AudioFileFormat.Type fileType, File out) throws IOException {

	// throws IllegalArgumentException if not supported
	AiffFileFormat aiffFileFormat = (AiffFileFormat)getAudioFileFormat(fileType, stream);

	// first write the file without worrying about length fields
	FileOutputStream fos = new FileOutputStream( out );	// throws IOException
	BufferedOutputStream bos = new BufferedOutputStream( fos, bisBufferSize );
	int bytesWritten = writeAiffFile(stream, aiffFileFormat, bos );
	bos.close();

	// now, if length fields were not specified, calculate them,
	// open as a random access file, write the appropriate fields,
	// close again....
	if( aiffFileFormat.getByteLength()== AudioSystem.NOT_SPECIFIED ) {

	    // $$kk: 10.22.99: jan: please either implement this or throw an exception!
	    // $$fb: 2001-07-13: done. Fixes Bug 4479981
	    int ssndBlockSize		= (aiffFileFormat.getFormat().getChannels() * aiffFileFormat.getFormat().getSampleSizeInBits());

	    int aiffLength=bytesWritten;
	    int ssndChunkSize=aiffLength-aiffFileFormat.getHeaderSize()+16;
	    long dataSize=ssndChunkSize-16;
	    int numFrames=(int) (dataSize*8/ssndBlockSize);

	    RandomAccessFile raf=new RandomAccessFile(out, "rw");
	    // skip FORM magic
	    raf.skipBytes(4);
	    raf.writeInt(aiffLength-8);
	    // skip aiff2 magic, fver chunk, comm magic, comm size, channel count,
	    raf.skipBytes(4+aiffFileFormat.getFverChunkSize()+4+4+2);
	    // write frame count
	    raf.writeInt(numFrames);
	    // skip sample size, samplerate, SSND magic
	    raf.skipBytes(2+10+4);
	    raf.writeInt(ssndChunkSize-8);
	    // that's all
	    raf.close();
	}

	return bytesWritten;
    }


    // -----------------------------------------------------------------------

    /**
     * Returns the AudioFileFormat describing the file that will be written from this AudioInputStream.
     * Throws IllegalArgumentException if not supported.
     */
    private AudioFileFormat getAudioFileFormat(AudioFileFormat.Type type, AudioInputStream stream) {

	AudioFormat format = null;
	AiffFileFormat fileFormat = null;
	AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;

	AudioFormat streamFormat = stream.getFormat();
	AudioFormat.Encoding streamEncoding = streamFormat.getEncoding();


	float sampleRate;
	int sampleSizeInBits;
	int channels;
	int frameSize;
	float frameRate;
	int fileSize;
	boolean convert8to16 = false;

	if( !types[0].equals(type) ) {
	    throw new IllegalArgumentException("File type " + type + " not supported.");
	}

	if( (AudioFormat.Encoding.ALAW.equals(streamEncoding)) ||
	    (AudioFormat.Encoding.ULAW.equals(streamEncoding)) ) {

	    if( streamFormat.getSampleSizeInBits()==8 ) {

		encoding = AudioFormat.Encoding.PCM_SIGNED;
		sampleSizeInBits=16;
		convert8to16 = true;

	    } else {

		// can't convert non-8-bit ALAW,ULAW
		throw new IllegalArgumentException("Encoding " + streamEncoding + " supported only for 8-bit data.");
	    }
	} else if ( streamFormat.getSampleSizeInBits()==8 ) {

	    encoding = AudioFormat.Encoding.PCM_UNSIGNED;
	    sampleSizeInBits=8;

	} else {

	    encoding = AudioFormat.Encoding.PCM_SIGNED;
	    sampleSizeInBits=streamFormat.getSampleSizeInBits();
	}


	format = new AudioFormat( encoding,
				  streamFormat.getSampleRate(),
				  sampleSizeInBits,
				  streamFormat.getChannels(),
				  streamFormat.getFrameSize(),
				  streamFormat.getFrameRate(),
				  true);	// AIFF is big endian


	if( stream.getFrameLength()!=AudioSystem.NOT_SPECIFIED ) {
	    if( convert8to16 ) {
		fileSize = (int)stream.getFrameLength()*streamFormat.getFrameSize()*2 + AiffFileFormat.AIFF_HEADERSIZE;
	    } else {
		fileSize = (int)stream.getFrameLength()*streamFormat.getFrameSize() + AiffFileFormat.AIFF_HEADERSIZE;
	    }
	} else {
	    fileSize = AudioSystem.NOT_SPECIFIED;
	}

	fileFormat = new AiffFileFormat( AudioFileFormat.Type.AIFF,
					 fileSize,
					 format,
					 (int)stream.getFrameLength() );

	return fileFormat;
    }


    private int writeAiffFile(InputStream in, AiffFileFormat aiffFileFormat, OutputStream out) throws IOException {

	int bytesRead = 0;
	int bytesWritten = 0;
	InputStream fileStream = getFileStream(aiffFileFormat, in);
	byte buffer[] = new byte[bisBufferSize];
	int maxLength = aiffFileFormat.getByteLength();

	while( (bytesRead = fileStream.read( buffer )) >= 0 ) {
	    if (maxLength>0) {
		if( bytesRead < maxLength ) {
		    out.write( buffer, 0, (int)bytesRead );
		    bytesWritten += bytesRead;
		    maxLength -= bytesRead;
		} else {
		    out.write( buffer, 0, (int)maxLength );
		    bytesWritten += maxLength;
		    maxLength = 0;
		    break;
		}

	    } else {
		out.write( buffer, 0, (int)bytesRead );
		bytesWritten += bytesRead;
	    }
	}

	return bytesWritten;
    }

    private InputStream getFileStream(AiffFileFormat aiffFileFormat, InputStream audioStream) throws IOException  {

	// private method ... assumes aiffFileFormat is a supported file format

	AudioFormat format = aiffFileFormat.getFormat();
	AudioFormat streamFormat = null;
	AudioFormat.Encoding encoding = null;

	//$$fb a little bit nicer handling of constants

	//int headerSize          = 54;
	int headerSize          = aiffFileFormat.getHeaderSize();

	//int fverChunkSize       = 0;
	int fverChunkSize       = aiffFileFormat.getFverChunkSize();
	//int commChunkSize       = 26;
	int commChunkSize       = aiffFileFormat.getCommChunkSize();
	int aiffLength          = -1;
	int ssndChunkSize       = -1;
	//int ssndOffset			= headerSize - 16;
	int ssndOffset			= aiffFileFormat.getSsndChunkOffset();
	short channels = (short) format.getChannels();
	short sampleSize = (short) format.getSampleSizeInBits();
	int ssndBlockSize		= (channels * sampleSize);
	int numFrames			= aiffFileFormat.getFrameLength();
	long dataSize            = -1;
	if( numFrames != AudioSystem.NOT_SPECIFIED) {
	    dataSize = (long) numFrames * ssndBlockSize / 8;
	    ssndChunkSize = (int)dataSize + 16;
	    aiffLength = (int)dataSize+headerSize;
	}
	float sampleFramesPerSecond = format.getSampleRate();
	int compCode = AiffFileFormat.AIFC_PCM;

	byte header[] = null;
	ByteArrayInputStream headerStream = null;
	ByteArrayOutputStream baos = null;
	DataOutputStream dos = null;
	SequenceInputStream aiffStream = null;
	InputStream codedAudioStream = audioStream;

	// if we need to do any format conversion, do it here....

	if( audioStream instanceof AudioInputStream ) {

	    streamFormat = ((AudioInputStream)audioStream).getFormat();
	    encoding = streamFormat.getEncoding();


	    // $$jb: Note that AIFF samples are ALWAYS signed
	    if( (AudioFormat.Encoding.PCM_UNSIGNED.equals(encoding)) ||
		( (AudioFormat.Encoding.PCM_SIGNED.equals(encoding)) && !streamFormat.isBigEndian() ) ) {

		// plug in the transcoder to convert to PCM_SIGNED. big endian
		codedAudioStream = AudioSystem.getAudioInputStream( new AudioFormat (
										     AudioFormat.Encoding.PCM_SIGNED,
										     streamFormat.getSampleRate(),
										     streamFormat.getSampleSizeInBits(),
										     streamFormat.getChannels(),
										     streamFormat.getFrameSize(),
										     streamFormat.getFrameRate(),
										     true ),
								    (AudioInputStream)audioStream );

	    } else if( (AudioFormat.Encoding.ULAW.equals(encoding)) ||
		       (AudioFormat.Encoding.ALAW.equals(encoding)) ) {

		if( streamFormat.getSampleSizeInBits() != 8 ) {
		    throw new IllegalArgumentException("unsupported encoding");
		}

				//$$fb 2001-07-13: this is probably not what we want:
				//     writing PCM when ULAW/ALAW is requested. AIFC is able to write ULAW !

				// plug in the transcoder to convert to PCM_SIGNED_BIG_ENDIAN
		codedAudioStream = AudioSystem.getAudioInputStream( new AudioFormat (
										     AudioFormat.Encoding.PCM_SIGNED,
										     streamFormat.getSampleRate(),
										     streamFormat.getSampleSizeInBits() * 2,
										     streamFormat.getChannels(),
										     streamFormat.getFrameSize() * 2,
										     streamFormat.getFrameRate(),
										     true ),
								    (AudioInputStream)audioStream );
	    }
	}


	// Now create an AIFF stream header...
	baos = new ByteArrayOutputStream();
	dos = new DataOutputStream(baos);

	// Write the outer FORM chunk
	dos.writeInt(AiffFileFormat.AIFF_MAGIC);
	dos.writeInt( (aiffLength-8) );
	dos.writeInt(AiffFileFormat.AIFF_MAGIC2);

	// Write a FVER chunk - only for AIFC
	//dos.writeInt(FVER_MAGIC);
	//dos.writeInt( (fverChunkSize-8) );
	//dos.writeInt(FVER_TIMESTAMP);

	// Write a COMM chunk
	dos.writeInt(AiffFileFormat.COMM_MAGIC);
	dos.writeInt( (commChunkSize-8) );
	dos.writeShort(channels);
	dos.writeInt(numFrames);
	dos.writeShort(sampleSize);
	write_ieee_extended(dos, sampleFramesPerSecond);   // 10 bytes

	//Only for AIFC
	//dos.writeInt(compCode);
	//dos.writeInt(compCode);
	//dos.writeShort(0);

	// Write the SSND chunk header
	dos.writeInt(AiffFileFormat.SSND_MAGIC);
	dos.writeInt( (ssndChunkSize-8) );
	// ssndOffset and ssndBlockSize set to 0 upon
	// recommendation in "Sound Manager" chapter in
	// "Inside Macintosh Sound", pp 2-87  (from Babu)
	dos.writeInt(0);	// ssndOffset
	dos.writeInt(0);	// ssndBlockSize

	// Concat this with the audioStream and return it

	dos.close();
	header = baos.toByteArray();
	headerStream = new ByteArrayInputStream( header );

	aiffStream = new SequenceInputStream(headerStream,codedAudioStream);

	return aiffStream;

    }




    // HELPER METHODS

    private static final int DOUBLE_MANTISSA_LENGTH = 52;
    private static final int DOUBLE_EXPONENT_LENGTH = 11;
    private static final long DOUBLE_SIGN_MASK     = 0x8000000000000000L;
    private static final long DOUBLE_EXPONENT_MASK = 0x7FF0000000000000L;
    private static final long DOUBLE_MANTISSA_MASK = 0x000FFFFFFFFFFFFFL;
    private static final int DOUBLE_EXPONENT_OFFSET = 1023;

    private static final int EXTENDED_EXPONENT_OFFSET = 16383;
    private static final int EXTENDED_MANTISSA_LENGTH = 63;
    private static final int EXTENDED_EXPONENT_LENGTH = 15;
    private static final long EXTENDED_INTEGER_MASK = 0x8000000000000000L;

    /**
     * Extended precision IEEE floating-point conversion routine.
     * @argument DataOutputStream
     * @argument double
     * @exception IOException
     */
    private void write_ieee_extended(DataOutputStream dos, float f) throws IOException {
	/* The special cases NaN, Infinity and Zero are ignored, since
	   they do not represent useful sample rates anyway.
	   Denormalized number aren't handled, too. Below, there is a cast
	   from float to double. We hope that in this conversion,
	   numbers are normalized. Numbers that cannot be normalized are
	   ignored, too, as they, too, do not represent useful sample rates. */
	long doubleBits = Double.doubleToLongBits((double) f);

	long sign = (doubleBits & DOUBLE_SIGN_MASK)
	    >> (DOUBLE_EXPONENT_LENGTH + DOUBLE_MANTISSA_LENGTH);
	long doubleExponent = (doubleBits & DOUBLE_EXPONENT_MASK)
	    >> DOUBLE_MANTISSA_LENGTH;
	long doubleMantissa = doubleBits & DOUBLE_MANTISSA_MASK;

	long extendedExponent = doubleExponent - DOUBLE_EXPONENT_OFFSET
	    + EXTENDED_EXPONENT_OFFSET;
	long extendedMantissa = doubleMantissa
	    << (EXTENDED_MANTISSA_LENGTH - DOUBLE_MANTISSA_LENGTH);
	long extendedSign = sign << EXTENDED_EXPONENT_LENGTH;
	short extendedBits79To64 = (short) (extendedSign | extendedExponent);
	long extendedBits63To0 = EXTENDED_INTEGER_MASK | extendedMantissa;

	dos.writeShort(extendedBits79To64);
	dos.writeLong(extendedBits63To0);
    }


}

