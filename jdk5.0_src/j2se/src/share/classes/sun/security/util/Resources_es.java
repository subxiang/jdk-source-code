/*
 * @(#)Resources_es.java	1.9 04/02/27
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package sun.security.util;

/**
 * <p> This class represents the <code>ResourceBundle</code>
 * for KeyTool,PolicyTool and javax.security.auth.*.
 *
 * @version 1.11, 01/30/01
 */
public class Resources_es extends java.util.ListResourceBundle {

    private static final Object[][] contents = {

	// shared (from jarsigner)
	{" ", " "},
	{"  ", "  "},
	{"      ", "      "},
	{", ", ", "},
	// shared (from keytool)
	{"\n", "\n"},
	{"*******************************************",
		"*******************************************"},
	{"*******************************************\n\n",
		"*******************************************\n\n"},

	// keytool
	{"keytool error: ", "error de keytool: "},
	{"Illegal option:  ", "Opci\u00f3n no permitida:  "},
        {"-keystore must be NONE if -storetype is PKCS11",
                "-keystore debe ser NONE si -storetype es PKCS11"},
        {"-storepasswd and -keypasswd commands not supported if -storetype is PKCS11",
                "-storepasswd y -keypasswd no pueden utilizarse si -storetype es PKCS11"},
        {"-keypass and -new can not be specified if -storetype is PKCS11",
                        "-keypass y -new no pueden especificarse si -storetype es PKCS11"},
        {"if -protected is specified, then -storepass, -keypass, and -new must not be specified",
                "si se especifica -protected, no deben especificarse -storepass, -keypass ni -new"},
	{"Validity must be greater than zero",
		"La validez debe ser mayor que cero"},
	{"provName not a provider", "{0} no es un proveedor"},
	{"Must not specify both -v and -rfc with 'list' command",
		"No se deben especificar -v y -rfc simult\u00e1neamente con el comando 'list'"},
	{"Key password must be at least 6 characters",
		"La contrase\u00f1a clave debe tener al menos 6 caracteres"},
	{"New password must be at least 6 characters",
		"La nueva contrase\u00f1a debe tener al menos 6 caracteres"},
	{"Keystore file exists, but is empty: ",
		"El archivo de almac\u00e9n de claves existe, pero est\u00e1 vac\u00edo "},
	{"Keystore file does not exist: ",
		"El archivo de almac\u00e9n de claves no existe: "},
	{"Must specify destination alias", "Se debe especificar alias de destino"},
	{"Must specify alias", "Se debe especificar alias"},
	{"Keystore password must be at least 6 characters",
		"La contrase\u00f1a del almac\u00e9n de claves debe tener al menos 6 caracteres"},
	{"Enter keystore password:  ", "Escriba la contrase\u00f1a del almac\u00e9n de claves:  "},
	{"Keystore password is too short - must be at least 6 characters",
	 "La contrase\u00f1a del almac\u00e9n de claves es demasiado corta, debe tener al menos 6 caracteres"},
	{"Too many failures - try later", "Demasiados fallos; int\u00e9ntelo m\u00e1s adelante"},
	{"Certification request stored in file <filename>",
		"Solicitud de certificaci\u00f3n almacenada en el archivo <{0}>"},
	{"Submit this to your CA", "Enviar a la CA"},
	{"Certificate stored in file <filename>",
		"Certificado almacenado en el archivo <{0}>"},
	{"Certificate reply was installed in keystore",
		"Se ha instalado la respuesta del certificado en el almac\u00e9n de claves"},
	{"Certificate reply was not installed in keystore",
		"No se ha instalado la respuesta del certificado en el almac\u00e9n de claves"},
	{"Certificate was added to keystore",
		"Se ha a\u00f1adido el certificado al almac\u00e9n de claves"},
	{"Certificate was not added to keystore",
		"No se ha a\u00f1adido el certificado al almac\u00e9n de claves"},
	{"[Storing ksfname]", "[Almacenando {0}]"},
	{"alias has no public key (certificate)",
		"{0} no tiene clave p\u00fablica (certificado)"},
	{"Cannot derive signature algorithm",
		"No se puede derivar el algoritmo de firma"},
	{"Alias <alias> does not exist",
		"El alias <{0}> no existe"},
	{"Alias <alias> has no certificate",
		"El alias <{0}> no tiene certificado"},
	{"Key pair not generated, alias <alias> already exists",
		"No se ha generado el par de claves, el alias <{0}> ya existe"},
	{"Cannot derive signature algorithm",
		"No se puede derivar el algoritmo de firma"},
	{"Generating keysize bit keyAlgName key pair and self-signed certificate (sigAlgName)\n\tfor: x500Name",
		"Generando bit {0} par de claves {1} y certificado autofirmado ({2})\n\tpara: {3}"},
	{"Enter key password for <alias>", "Escriba la contrase\u00f1a clave para <{0}>"},
	{"\t(RETURN if same as keystore password):  ",
		"\t(INTRO si es la misma contrase\u00f1a que la del almac\u00e9n de claves):  "},
	{"Key password is too short - must be at least 6 characters",
		"La contrase\u00f1a clave es demasiado corta; debe tener al menos 6 caracteres"},
	{"Too many failures - key not added to keystore",
		"Demasiados fallos; no se ha agregado la clave al almac\u00e9n de claves"},
	{"Destination alias <dest> already exists",
		"El alias de destino <{0}> ya existe"},
	{"Password is too short - must be at least 6 characters",
		"La contrase\u00f1a es demasiado corta; debe tener al menos 6 caracteres"},
	{"Too many failures. Key entry not cloned",
		"Demasiados errores. No se ha copiado la entrada de clave"},
	{"key password for <alias>", "contrase\u00f1a clave para <{0}>"},
	{"Keystore entry for <id.getName()> already exists",
		"La entrada de almac\u00e9n de claves para <{0}> ya existe"},
	{"Creating keystore entry for <id.getName()> ...",
		"Creando entrada de almac\u00e9n de claves para <{0}> ..."},
	{"No entries from identity database added",
		"No se han agregado entradas de la base de datos de identidades"},
	{"Alias name: alias", "Nombre de alias: {0}"},
	{"Creation date: keyStore.getCreationDate(alias)",
		"Fecha de creaci\u00f3n: {0,date}"},
	{"alias, keyStore.getCreationDate(alias), ",
		"{0}, {1,date}, "},
	{"alias, ", "{0}, "},
	{"Entry type: keyEntry", "Tipo de entrada: keyEntry"},
	{"keyEntry,", "keyEntry,"},
	{"Certificate chain length: ", "Longitud de la cadena de certificado: "},
	{"Certificate[(i + 1)]:", "Certificado[{0,number,integer}]:"},
	{"Certificate fingerprint (MD5): ", "Huella digital de certificado (MD5): "},
	{"Entry type: trustedCertEntry\n", "Tipo de entrada: trustedCertEntry\n"},
	{"trustedCertEntry,", "trustedCertEntry,"},
	{"Keystore type: ", "Tipo de almac\u00e9n de claves: "},
	{"Keystore provider: ", "Proveedor de almac\u00e9n de claves: "},
	{"Your keystore contains keyStore.size() entry",
		"Su almac\u00e9n de claves contiene entrada {0,number,integer}"},
	{"Your keystore contains keyStore.size() entries",
		"Su almac\u00e9n de claves contiene {0,number,integer} entradas"},
	{"Failed to parse input", "Error al analizar la entrada"},
	{"Empty input", "Entrada vac\u00eda"},
	{"Not X.509 certificate", "No es un certificado X.509"},
	{"Cannot derive signature algorithm",
		"No se puede derivar el algoritmo de firma"},
	{"alias has no public key", "{0} no tiene clave p\u00fablica"},
	{"alias has no X.509 certificate", "{0} no tiene certificado X.509"},
	{"New certificate (self-signed):", "Nuevo certificado (autofirmado):"},
	{"Reply has no certificates", "La respuesta no tiene certificados"},
	{"Certificate not imported, alias <alias> already exists",
		"Certificado no importado, el alias <{0}> ya existe"},
	{"Input not an X.509 certificate", "La entrada no es un certificado X.509"},
	{"Certificate already exists in keystore under alias <trustalias>",
		"El certificado ya existe en el almac\u00e9n de claves con el alias <{0}>"},
	{"Do you still want to add it? [no]:  ",
		"\u00bfA\u00fan desea agregarlo? [no]:  "},
	{"Certificate already exists in system-wide CA keystore under alias <trustalias>",
		"El certificado ya existe en el almac\u00e9n de claves de la CA del sistema, con el alias <{0}>"},
	{"Do you still want to add it to your own keystore? [no]:  ",
		"\u00bfA\u00fan desea agregarlo a su propio almac\u00e9n de claves? [no]:  "},
	{"Trust this certificate? [no]:  ", "\u00bfConfiar en este certificado? [no]:  "},
	{"YES", "S\u00cd"},
	{"New prompt: ", "Nuevo {0}: "},
	{"Passwords must differ", "Las contrase\u00f1as deben ser distintas"},
	{"Re-enter new prompt: ", "Vuelva a escribir el nuevo {0}: "},
	{"They don't match; try again", "No concuerdan; int\u00e9ntelo de nuevo"},
	{"Enter prompt alias name:  ", "Escriba el nombre de alias de {0}:  "},
	{"Enter alias name:  ", "Escriba el nombre de alias:  "},
	{"\t(RETURN if same as for <otherAlias>)",
		"\t(INTRO si es el mismo que para <{0}>)"},
	{"*PATTERN* printX509Cert",
		"Propietario: {0}\nEmisor: {1}\nN\u00famero de serie: {2}\nV\u00e1lido desde: {3} hasta: {4}\nHuellas digitales del certificado:\n\t MD5:  {5}\n\t SHA1: {6}"},
	{"What is your first and last name?",
		"\u00bfCu\u00e1les son su nombre y su apellido?"},
	{"What is the name of your organizational unit?",
		"\u00bfCu\u00e1l es el nombre de su unidad de organizaci\u00f3n?"},
	{"What is the name of your organization?",
		"\u00bfCu\u00e1l es el nombre de su organizaci\u00f3n?"},
	{"What is the name of your City or Locality?",
		"\u00bfCu\u00e1l es el nombre de su ciudad o localidad?"},
	{"What is the name of your State or Province?",
		"\u00bfCu\u00e1l es el nombre de su estado o provincia?"},
	{"What is the two-letter country code for this unit?",
		"\u00bfCu\u00e1l es el c\u00f3digo de pa\u00eds de dos letras de la unidad?"},
	{"Is <name> correct?", "\u00bfEs correcto {0}?"},
	{"no", "no"},
	{"yes", "s\u00ed"},
	{"y", "y"},
	{"  [defaultValue]:  ", "  [{0}]:  "},
	{"Alias <alias> has no (private) key",
		"El alias <{0}> no tiene clave (privada)"},
	{"Recovered key is not a private key",
		"La clave recuperada no es una clave privada"},
	{"*****************  WARNING WARNING WARNING  *****************",
	    "*****************  ADVERTENCIA ADVERTENCIA ADVERTENCIA  *****************"},
	{"* The integrity of the information stored in your keystore  *",
	    "* La integridad de la informaci\u00f3n almacenada en su almac\u00e9n de claves  *"},
	{"* has NOT been verified!  In order to verify its integrity, *",
	    "* NO se ha comprobado. Para comprobar dicha integridad, *"},
	{"* you must provide your keystore password.                  *",
	    "deber\u00e1 proporcionar su contrase\u00f1a de almac\u00e9n de claves.                  *"},
	{"Certificate reply does not contain public key for <alias>",
		"La respuesta de certificado no contiene una clave p\u00fablica para <{0}>"},
	{"Incomplete certificate chain in reply",
		"Cadena de certificado incompleta en la respuesta"},
	{"Certificate chain in reply does not verify: ",
		"La cadena de certificado de la respuesta no verifica: "},
	{"Top-level certificate in reply:\n",
		"Certificado de nivel superior en la respuesta:\n"},
	{"... is not trusted. ", "... no es de confianza. "},
	{"Install reply anyway? [no]:  ", "\u00bfinstalar respuesta de todos modos? [no]:  "},
	{"NO", "NO"},
	{"Public keys in reply and keystore don't match",
		"Las claves p\u00fablicas en la respuesta y en el almac\u00e9n de claves no coinciden"},
	{"Certificate reply and certificate in keystore are identical",
		"La respuesta del certificado y el certificado en el almac\u00e9n de claves son id\u00e9nticos"},
	{"Failed to establish chain from reply",
		"No se ha podido establecer una cadena a partir de la respuesta"},
	{"n", "n"},
	{"Wrong answer, try again", "Respuesta incorrecta, vuelva a intentarlo"},
	{"keytool usage:\n", "sintaxis de keytool:\n"},
	{"-certreq     [-v] [-protected]",
		"-certreq     [-v] [-protected]"},
	{"\t     [-alias <alias>] [-sigalg <sigalg>]",
		"\t     [-alias <alias>] [-sigalg <algoritmo_firma>]"},
	{"\t     [-file <csr_file>] [-keypass <keypass>]",
		"\t     [-file <archivo_csr>] [-keypass <contrase\u00f1a_clave>]"},
	{"\t     [-storetype <storetype>] [-providerName <name>]",
                "\t     [-storetype <tipo_almac\u00e9n>] [-providerName <nombre>]"},
	{"\t     [-providerClass <provider_class_name> [-providerArg <arg>]] ...",
                "\t     [-providerClass <nombre_clase_proveedor> [-providerArg <arg>]] ..."},
	{"-delete      [-v] [-protected] -alias <alias>",
		"-delete      [-v] [-protected] -alias <alias>"},
	{"-export      [-v] [-rfc] [-protected]",
		"-export      [-v] [-rfc] [-protected]"},
	{"\t     [-alias <alias>] [-file <cert_file>]",
		"\t     [-alias <alias>] [-file <archivo_cert>]"},
	{"-genkey      [-v] [-protected]",
		"-genkey      [-v] [-protected]"},

	{"\t     [-alias <alias>]", "\t     [-alias <alias>]"},
        {"\t     [-keyalg <keyalg>] [-keysize <keysize>]",
                "\t     [-keyalg <algoritmo_clave>] [-keysize <tama\u00f1o_clave>]"},
        {"\t     [-sigalg <sigalg>] [-dname <dname>]",
                "\t     [-sigalg <algoritmo_firma>] [-dname <nombre_d>]"},
        {"\t     [-validity <valDays>] [-keypass <keypass>]",
                "\t     [-validity <d\u00edas_validez>] [-keypass <contrase\u00f1a_clave>]"},

	{"-help", "-help"},
	{"-identitydb  [-v] [-protected]",
		"-identitydb  [-v] [-protected]"},
	{"\t     [-file <idb_file>]", "\t     [-file <archvo_idb>]"},
	{"-import      [-v] [-noprompt] [-trustcacerts] [-protected]",
		"-import      [-v] [-noprompt] [-trustcacerts] [-protected]"},
	{"\t     [-alias <alias>]", "\t     [-alias <alias>]"},
	{"\t     [-file <cert_file>] [-keypass <keypass>]",
		"\t     [-file <archivo_cert>] [-keypass <contrase\u00f1a_clave>]"},
	{"-keyclone    [-v] [-protected]",
		"-keyclone    [-v] [-protected]"},
	{"\t     [-alias <alias>] -dest <dest_alias>",
		"\t     [-alias <alias>] -dest <alias_destino>"},
	{"\t     [-keypass <keypass>] [-new <new_keypass>]",
		"\t     [-keypass <contrase\u00f1a_clave>] [-new <nueva_contrase\u00f1a_clave>]"},
	{"-keypasswd   [-v] [-alias <alias>]",
		"-keypasswd   [-v] [-alias <alias>]"},
	{"\t     [-keypass <old_keypass>] [-new <new_keypass>]",
		"\t     [-keypass <contrase\u00f1a_clave_antigua>] [-new <nueva_contrase\u00f1a_clave>]"},
	{"\t     [-keystore <keystore>] [-storepass <storepass>]",
		"\t     [-keystore <almac\u00e9n_claves>] [-storepass <contrase\u00f1a_almac\u00e9n>]"},
	{"-list        [-v | -rfc] [-protected]",
		"-list        [-v | -rfc] [-protected]"},
	{"\t     [-alias <alias>]",
		"\t     [-alias <alias>]"},
	{"-printcert   [-v] [-file <cert_file>]",
		"-printcert   [-v] [-file <archivo_certif>]"},
	{"-selfcert    [-v] [-protected]",
		"-selfcert    [-v] [-protected]"},
	{"\t     [-dname <dname>] [-validity <valDays>]",
		"\t     [-dname <nombre_d>] [-validity <d\u00edas_validez>]"},
	{"\t     [-keypass <keypass>] [-sigalg <sigalg>]",
                        "\t     [-keypass <contrase\u00f1a_clave>] [-sigalg <algoritmo_firma>]"},
	{"-storepasswd [-v] [-new <new_storepass>]",
		"-storepasswd [-v] [-new <nueva_contrase\u00f1a_almac\u00e9n>]"},

	// policytool
	{"Warning: A public key for alias 'signers[i]' does not exist.",
		"Advertencia: No existe una clave p\u00fablica para el alias {0}."},
	{"Warning: Class not found: ",
		"Advertencia: Clase no encontrada: "},
	{"Policy File opened successfully",
		"Archivo de normas abierto satisfactoriamente"},
	{"null Keystore name", "nombre de almac\u00e9n de claves nulo"},
	{"Warning: Unable to open Keystore: ",
		"Advertencia: No se puede abrir el almac\u00e9n de claves: "},
	{"Illegal option: ", "Opci\u00f3n no permitida: "},
	{"Usage: policytool [options]", "Sintaxis: policytool [opciones]"},
	{"  [-file <file>]    policy file location",
		"  [-file <archivo>]    ubicaci\u00f3n del archivo de normas"},
	{"New", "Nuevo"},
	{"Open", "Abrir"},
	{"Save", "Guardar"},
	{"Save As", "Guardar como"},
	{"View Warning Log", "Ver registro de advertencias"},
	{"Exit", "Salir"},
	{"Add Policy Entry", "Agregar entrada de norma"},
	{"Edit Policy Entry", "Editar entrada de norma"},
	{"Remove Policy Entry", "Eliminar entrada de norma"},
	{"Change KeyStore", "Cambiar almac\u00e9n de claves"},
	{"Add Public Key Alias", "Agregar alias de clave p\u00fablico"},
	{"Remove Public Key Alias", "Eliminar alias de clave p\u00fablico"},
	{"File", "Archivo"},
	{"Edit", "Editar"},
	{"Policy File:", "Archivo de normas:"},
	{"Keystore:", "Almac\u00e9n de claves:"},
	{"Error parsing policy file policyFile: pppe.getMessage()",
		"Error al analizar el archivo de normas {0}: {1}"},
	{"Could not find Policy File: ", "No se ha podido encontrar el archivo de normas: "},
	{"Policy Tool", "Herramienta de normas"},
	{"Errors have occurred while opening the policy configuration.  View the Warning Log for more information.",
		"Ha habido errores al abrir la configuraci\u00f3n de normas.  V\u00e9ase el registro de advertencias para obtener m\u00e1s informaci\u00f3n."},
	{"Error", "Error"},
	{"OK", "Aceptar"},
	{"Status", "Estado"},
	{"Warning", "Advertencia"},
	{"Permission:                                                       ",
		"Permiso:                                                       "},
	{"Target Name:                                                    ",
		"Nombre de destino:                                                    "},
	{"library name", "nombre de la biblioteca"},
	{"package name", "nombre del paquete"},
	{"property name", "nombre de la propiedad"},
	{"provider name", "nombre del proveedor"},
	{"Actions:                                                             ",
		"Acciones:                                                             "},
	{"OK to overwrite existing file filename?",
		"\u00bfSobrescribir el archivo existente {0}?"},
	{"Cancel", "Cancelar"},
	{"CodeBase:", "Base de c\u00f3digos:"},
	{"SignedBy:", "Firmado por:"},
	{"  Add Permission", "  Agregar permiso"},
	{"  Edit Permission", "  Editar permiso"},
	{"Remove Permission", "Eliminar permiso"},
	{"Done", "Terminar"},
	{"New KeyStore URL:", "URL de nuevo almac\u00e9n de claves:"},
	{"New KeyStore Type:", "Tipo de nuevo almac\u00e9n de claves:"},
	{"Permissions", "Permisos"},
	{"  Edit Permission:", "  Editar permiso:"},
	{"  Add New Permission:", "  Agregar permiso nuevo:"},
	{"Signed By:", "Firmado por:"},
	{"Permission and Target Name must have a value",
		"Permiso y Nombre de destino deben tener un valor"},
	{"Remove this Policy Entry?", "\u00bfEliminar esta entrada de norma?"},
	{"Overwrite File", "Sobrescribir archivo"},
	{"Policy successfully written to filename",
		"Norma escrita satisfactoriamente en {0}"},
	{"null filename", "nombre de archivo nulo"},
	{"filename not found", "{0} no encontrado"},
	{"     Save changes?", "     \u00bfGuardar cambios?"},
	{"Yes", "S\u00ed"},
	{"No", "No"},
	{"Error: Could not open policy file, filename, because of parsing error: pppe.getMessage()",
		"Error: No se ha podido abrir el archivo de normas, {0}, a causa de un error de an\u00e1lisis: {1}"},
	{"Permission could not be mapped to an appropriate class",
		"No se ha podido reasignar el permiso a una clase apropiada"},
	{"Policy Entry", "Entrada de norma"},
	{"Save Changes", "Guardar cambios"},
	{"No Policy Entry selected", "No se ha seleccionado entrada de norma"},
	{"Keystore", "Almac\u00e9n de claves"},
	{"KeyStore URL must have a valid value",
		"URL del almac\u00e9n de claves debe tener un valor v\u00e1lido"},
	{"Invalid value for Actions", "Valor no v\u00e1lido para Acciones"},
	{"No permission selected", "No se ha seleccionado un permiso"},
	{"Warning: Invalid argument(s) for constructor: ",
		"Advertencia: Argumentos no v\u00e1lidos para constructor: "},
	{"Add Principal", "Agregar principal"},
	{"Edit Principal", "Editar principal"},
	{"Remove Principal", "Eliminar principal"},
	{"Principal Type:", "Tipo de principal:"},
        {"Principal Name:", "Nombre de principal:"},
	{"Illegal Principal Type", "Tipo de principal no permitido"},
	{"No principal selected", "No se ha seleccionado principal"},
	{"Principals:", "Principales:"},
	{"Principals", "Principales"},
	{"  Add New Principal:", "  Agregar nuevo principal:"},
	{"  Edit Principal:", "  Editar principal:"},
	{"name", "nombre"},
	{"Cannot Specify Principal with a Wildcard Class without a Wildcard Name",
	    "No se puede especificar principal con una clase de comod\u00edn sin un nombre de comod\u00edn"},
	{"Cannot Specify Principal without a Class",
	    "No se puede especificar principal sin una clase"},

        {"Cannot Specify Principal without a Name",
            "No se puede especificar principal sin un nombre"},

	// javax.security.auth.PrivateCredentialPermission
	{"invalid null input(s)", "entradas nulas no v\u00e1lidas"},
	{"actions can only be 'read'", "las acciones s\u00f3lo pueden 'leerse'"},
	{"permission name [name] syntax invalid: ",
		"sintaxis de nombre de permiso [{0}] no v\u00e1lida: "},
	{"Credential Class not followed by a Principal Class and Name",
		"Clase de credencial no va seguida de una clase y nombre de principal"},
	{"Principal Class not followed by a Principal Name",
		"La clase de principal no va seguida de un nombre de principal"},
	{"Principal Name must be surrounded by quotes",
		"El nombre de principal debe ir entre comillas"},
	{"Principal Name missing end quote",
		"Faltan las comillas finales en el nombre de principal"},
	{"PrivateCredentialPermission Principal Class can not be a wildcard (*) value if Principal Name is not a wildcard (*) value",
		"La clase de principal PrivateCredentialPermission no puede ser un valor comod\u00edn (*) si el nombre de principal no lo es tambi\u00e9n"},
	{"CredOwner:\n\tPrincipal Class = class\n\tPrincipal Name = name",
		"CredOwner:\n\tClase de principal = {0}\n\tNombre de principal = {1}"},

	// javax.security.auth.x500
	{"provided null name", "se ha proporcionado un nombre nulo"},

	// javax.security.auth.Subject
	{"invalid null AccessControlContext provided",
		"se ha proporcionado un AccessControlContext nulo no v\u00e1lido"},
	{"invalid null action provided", "se ha proporcionado una acci\u00f3n nula no v\u00e1lida"},
	{"invalid null Class provided", "se ha proporcionado una clase nula no v\u00e1lida"},
	{"Subject:\n", "Asunto:\n"},
	{"\tPrincipal: ", "\tPrincipal: "},
	{"\tPublic Credential: ", "\tCredencial p\u00fablica: "},
	{"\tPrivate Credentials inaccessible\n",
		"\tCredenciales privadas inaccesibles\n"},
	{"\tPrivate Credential: ", "\tCredencial privada: "},
	{"\tPrivate Credential inaccessible\n",
		"\tCredencial privada inaccesible\n"},
	{"Subject is read-only", "El asunto es de s\u00f3lo lectura"},
	{"attempting to add an object which is not an instance of java.security.Principal to a Subject's Principal Set",
		"intentando agregar un objeto que no es un ejemplar de java.security.Principal al conjunto principal de un asunto"},
	{"attempting to add an object which is not an instance of class",
		"intentando agregar un objeto que no es un ejemplar de {0}"},

	// javax.security.auth.login.AppConfigurationEntry
	{"LoginModuleControlFlag: ", "LoginModuleControlFlag: "},

	// javax.security.auth.login.LoginContext
	{"Invalid null input: name", "Entrada nula no v\u00e1lida: nombre"},
	{"No LoginModules configured for name",
	 "No se han configurado LoginModules para {0}"},
	{"invalid null Subject provided", "se ha proporcionado un asunto nulo no v\u00e1lido"},
	{"invalid null CallbackHandler provided",
		"se ha proporcionado CallbackHandler nulo no v\u00e1lido"},
	{"null subject - logout called before login",
		"asunto nulo - se ha llamado a fin de sesi\u00f3n antes del inicio de sesi\u00f3n"},
	{"unable to instantiate LoginModule, module, because it does not provide a no-argument constructor",
		"no se puede lanzar un ejemplar de LoginModule, {0}, porque no incluye un constructor no-argumento"},
	{"unable to instantiate LoginModule",
		"no se puede lanzar un ejemplar de LoginModule"},
	{"unable to find LoginModule class: ",
		"no se puede encontrar la clase LoginModule: "},
	{"unable to access LoginModule: ",
		"no se puede acceder a LoginModule: "},
	{"Login Failure: all modules ignored",
		"Fallo en inicio de sesi\u00f3n: se ha hecho caso omiso de todos los m\u00f3dulos"},

	// sun.security.provider.PolicyFile

	{"java.security.policy: error parsing policy:\n\tmessage",
		"java.security.policy: error de an\u00e1lisis de {0}:\n\t{1}"},
	{"java.security.policy: error adding Permission, perm:\n\tmessage",
		"java.security.policy: error al agregar Permiso, {0}:\n\t{1}"},
	{"java.security.policy: error adding Entry:\n\tmessage",
		"java.security.policy: error al agregar Entrada:\n\t{0}"},
	{"alias name not provided (pe.name)", "no se ha proporcionado nombre de alias ({0})"},
	{"unable to perform substitution on alias, suffix",
		"no se puede realizar la sustituci\u00f3n en el alias, {0}"},
	{"substitution value, prefix, unsupported",
		"valor de sustituci\u00f3n, {0}, no soportado"},
	{"(", "("},
	{")", ")"},
	{"type can't be null","el tipo no puede ser nulo"},

	// sun.security.provider.PolicyParser
	{"keystorePasswordURL can not be specified without also specifying keystore",
		"keystorePasswordURL no puede especificarse sin especificar tambi\u00e9n el almac\u00e9n de claves"},
	{"expected keystore type", "se esperaba un tipo de almac\u00e9n de claves"},
	{"expected keystore provider", "se esperaba un proveedor de almac\u00e9n de claves"},
	{"multiple Codebase expressions",
	        "expresiones m\u00faltiples de base de c\u00f3digos"},
        {"multiple SignedBy expressions","expresiones m\u00faltiples de SignedBy"},
	{"SignedBy has empty alias","SignedBy tiene un alias vac\u00edo"},
	{"can not specify Principal with a wildcard class without a wildcard name",
		"no se puede especificar Principal con una clase de comod\u00edn sin un nombre de comod\u00edn"},
	{"expected codeBase or SignedBy or Principal",
		"se esperaba base de c\u00f3digos o SignedBy o Principal"},
	{"expected permission entry", "se esperaba un permiso de entrada"},
	{"number ", "n\u00famero "},
	{"expected [expect], read [end of file]",
		"se esperaba [{0}], se ha le\u00eddo [end of file]"},
	{"expected [;], read [end of file]",
		"se esperaba [;], se ha le\u00eddo [end of file]"},
	{"line number: msg", "l\u00ednea {0}: {1}"},
	{"line number: expected [expect], found [actual]",
		"l\u00ednea {0}: se esperaba [{1}], se ha encontrado [{2}]"},
	{"null principalClass or principalName",
		"principalClass o principalName nulos"},

	// sun.security.pkcs11.SunPKCS11
	{"PKCS11 Token [providerName] Password: ",
		"Contrase\u00f1a de la tarjeta de claves PKCS11 [{0}]: "},

	/* --- DEPRECATED --- */
	// javax.security.auth.Policy
	{"unable to instantiate Subject-based policy",
		"no se puede lanzar un ejemplar de la norma basada en Asunto"}
    };


    /**
     * Returns the contents of this <code>ResourceBundle</code>.
     *
     * <p>
     *
     * @return the contents of this <code>ResourceBundle</code>.
     */
    public Object[][] getContents() {
	return contents;
    }
}


