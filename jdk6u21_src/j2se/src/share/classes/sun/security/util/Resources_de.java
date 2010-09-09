/*
 * @(#)Resources_de.java	1.14 10/03/23
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package sun.security.util;

/**
 * <p> This class represents the <code>ResourceBundle</code>
 * for javax.security.auth and sun.security.
 *
 * @version 1.14, 03/23/10
 */
public class Resources_de extends java.util.ListResourceBundle {

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
	{"keytool error: ", "Keytool-Fehler: "},
	{"Illegal option:  ", "Unzul\u00e4ssige Option:  "},
        {"Try keytool -help","Verwenden Sie den Befehl keytool -help"},
        {"Command option <flag> needs an argument.", "Befehlsoption {0} ben\u00f6tigt ein Argument."},
        {"Warning:  Different store and key passwords not supported for PKCS12 KeyStores. Ignoring user-specified <command> value.",
                "Warnung: Keine Unterst\u00fctzung f\u00fcr unterschiedliche Speicher- und Schl\u00fcsselpassw\u00f6rter bei PKCS12 KeyStores. Der benutzerdefinierte Wert {0} wird ignoriert."},
	{"-keystore must be NONE if -storetype is {0}",
		"-keystore muss NONE sein, wenn -storetype gleich {0} ist"},
        {"Too may retries, program terminated",
                 "Zu viele erneute Versuche, das Programm wird beendet."},
	{"-storepasswd and -keypasswd commands not supported if -storetype is {0}",
		"Die Befehle -storepasswd und -keypasswd werden nicht unterst\u00fctzt, wenn -storetype gleich {0} ist"},
	{"-keypasswd commands not supported if -storetype is PKCS12",
		"Befehle des Typs -keypasswd werden nicht unterst\u00fctzt, wenn -storetype gleich PKCS12"},
	{"-keypass and -new can not be specified if -storetype is {0}",
		"Die Befehle -keypass und -new k\u00f6nnen nicht spezifiziert werden, wenn -storetype gleich {0} ist"},
	{"if -protected is specified, then -storepass, -keypass, and -new must not be specified",
		"Wenn -protected angegeben ist, d\u00fcrfen -storepass, -keypass und -new nicht angegeben werden"},
	{"if -srcprotected is specified, then -srcstorepass and -srckeypass must not be specified",
		"wenn -srcprotected angegeben ist, d\u00fcrfen -srcstorepass und -srckeypass nicht angegeben sein"},
	{"if keystore is not password protected, then -storepass, -keypass, and -new must not be specified",
		"Wenn der Keystore nicht passwortgesch\u00fctzt ist, d\u00fcrfen -storepass, -keypass und -new nicht spezifiziert werden"},
	{"if source keystore is not password protected, then -srcstorepass and -srckeypass must not be specified",
		"Wenn der Quell-Keystore nicht passwortgesch\u00fctzt ist, d\u00fcrfen -srcstorepass und -srckeypass nicht spezifiziert werden"},
	{"Validity must be greater than zero",
		"G\u00fcltigkeit muss gr\u00f6\u00dfer als Null sein"},
	{"provName not a provider", "{0} kein Provider"},
	{"Usage error: no command provided", "Verwendungsfehler: kein Befehl angegeben"},
	{"Usage error, <arg> is not a legal command", "Verwendungsfehler: {0} ist kein g\u00fcltiger Befehl"},
	{"Source keystore file exists, but is empty: ", "Datei f\u00fcr Quell-Keystore ist zwar vorhanden, aber leer: "},
	{"Please specify -srckeystore", "Geben Sie \u0096srckeystore an"},
	{"Must not specify both -v and -rfc with 'list' command",
		"-v und -rfc d\u00fcrfen bei Befehl 'list' nicht beide angegeben werden"},
	{"Key password must be at least 6 characters",
		"Schl\u00fcsselpasswort muss mindestens 6 Zeichen lang sein"},
	{"New password must be at least 6 characters",
		"Neues Passwort muss mindest 6 Zeichen lang sein"},
	{"Keystore file exists, but is empty: ",
		"Keystore-Datei vorhanden, aber leer: "},
	{"Keystore file does not exist: ",
		"Keystore-Datei nicht vorhanden: "},
	{"Must specify destination alias", "Zielalias muss angegeben werden."},
	{"Must specify alias", "Alias muss angegeben werden."},
	{"Keystore password must be at least 6 characters",
		"Keystore-Passwort muss mindestens 6 Zeichen lang sein."},
	{"Enter keystore password:  ", "Geben Sie das Keystore-Passwort ein:  "},
	{"Enter source keystore password:  ", "Geben Sie das Passwort f\u00fcr den Quell-Keystore ein:  "},
        {"Enter destination keystore password:  ", "Geben Sie das Passwort f\u00fcr den Ziel-Keystore ein:  "},
	{"Keystore password is too short - must be at least 6 characters",
	 "Keystore-Passwort zu kurz - muss mindestens 6 Zeichen lang sein."},
        {"Unknown Entry Type", "Unbekannter Eintragstyp"},
        {"Too many failures. Alias not changed", "Zu viele Fehler. Alias nicht ge\u00e4ndert"},
        {"Entry for alias <alias> successfully imported.",
                 "Eintrag f\u00fcr Alias {0} erfolgreich importiert."},
        {"Entry for alias <alias> not imported.", "Eintrag f\u00fcr Alias {0} nicht importiert."},
        {"Problem importing entry for alias <alias>: <exception>.\nEntry for alias <alias> not imported.",
                 "Fehler beim Importieren des Eintrags f\u00fcr Alias {0}: {1}.\nEintrag f\u00fcr Alias {0} nicht importiert."},
        {"Import command completed:  <ok> entries successfully imported, <fail> entries failed or cancelled",
                 "Importbefehl abgeschlossen:  {0} Eintr\u00e4ge erfolgreich importiert, Fehler oder Abbruch bei {1} Eintr\u00e4gen"},
        {"Warning: Overwriting existing alias <alias> in destination keystore",
                 "Warnung: \u00dcberschreiben von vorhandenem Alias {0} in Ziel-Keystore"},
        {"Existing entry alias <alias> exists, overwrite? [no]:  ",
                 "Eintrags-Alias {0} bereits vorhanden. \u00dcberschreiben? [Nein]:  "},
	{"Too many failures - try later", "Zu viele Fehler - versuchen Sie es sp\u00e4ter noch einmal."},
	{"Certification request stored in file <filename>",
		"Zertifizierungsanforderung in Datei <{0}> gespeichert."},
	{"Submit this to your CA", "Reichen Sie dies bei Ihrem CA ein."},
        {"if alias not specified, destalias, srckeypass, and destkeypass must not be specified",
            "wenn kein Alias angegeben ist, m\u00fcssen destalias, srckeypass und destkeypass nicht angegeben werden"},
	{"Certificate stored in file <filename>",
		"Zertifikat in Datei <{0}> gespeichert."},
	{"Certificate reply was installed in keystore",
		"Zertifikatantwort wurde in Keystore installiert."},
	{"Certificate reply was not installed in keystore",
		"Zertifikatantwort wurde nicht in Keystore installiert."},
	{"Certificate was added to keystore",
		"Zertifikat wurde zu Keystore hinzugef\u00fcgt."},
	{"Certificate was not added to keystore",
		"Zertifikat wurde nicht zu Keystore hinzugef\u00fcgt."},
	{"[Storing ksfname]", "[{0} wird gesichert.]"},
	{"alias has no public key (certificate)",
		"{0} hat keinen \u00f6ffentlichen Schl\u00fcssel (Zertifikat)."},
	{"Cannot derive signature algorithm",
		"Signaturalgorithmus kann nicht abgeleitet werden."},
	{"Alias <alias> does not exist",
		"Alias <{0}> existiert nicht."},
	{"Alias <alias> has no certificate",
		"Alias <{0}> hat kein Zertifikat."},
	{"Key pair not generated, alias <alias> already exists",
		"Schl\u00fcsselpaar wurde nicht erzeugt, Alias <{0}> ist bereits vorhanden."},
	{"Cannot derive signature algorithm",
		"Signaturalgorithmus kann nicht abgeleitet werden."},
	{"Generating keysize bit keyAlgName key pair and self-signed certificate (sigAlgName) with a validity of validality days\n\tfor: x500Name",
                "Erstellen von Schl\u00fcsselpaar (Typ {1}, {0} Bit) und selbstunterzeichnetem Zertifikat ({2}) mit einer G\u00fcltigkeit von {3} Tagen\n\tf\u00fcr: {4}"},
	{"Enter key password for <alias>", "Geben Sie das Passwort f\u00fcr <{0}> ein."},
	{"\t(RETURN if same as keystore password):  ",
		"\t(EINGABETASTE, wenn Passwort dasselbe wie f\u00fcr Keystore):  "},
	{"Key password is too short - must be at least 6 characters",
		"Schl\u00fcsselpasswort zu kurz - muss mindestens 6 Zeichen lang sein."},
	{"Too many failures - key not added to keystore",
		"Zu viele Fehler - Schl\u00fcssel wurde nicht zu Keystore hinzugef\u00fcgt."},
	{"Destination alias <dest> already exists",
		"Zielalias <{0}> bereits vorhanden"},
	{"Password is too short - must be at least 6 characters",
		"Passwort zu kurz - muss mindestens 6 Zeichen lang sein"},
	{"Too many failures. Key entry not cloned",
		"Zu viele Fehler. Schl\u00fcsseleingabe wurde nicht dupliziert."},
	{"key password for <alias>", "Schl\u00fcsselpasswort f\u00fcr <{0}>"},
	{"Keystore entry for <id.getName()> already exists",
		"Keystore-Eintrag f\u00fcr <{0}> bereits vorhanden"},
	{"Creating keystore entry for <id.getName()> ...",
		"Keystore-Eintrag f\u00fcr <{0}> wird erstellt ..."},
	{"No entries from identity database added",
		"Keine Eintr\u00e4ge von Identit\u00e4tsdatenbank hinzugef\u00fcgt"},
	{"Alias name: alias", "Aliasname: {0}"},
	{"Creation date: keyStore.getCreationDate(alias)",
		"Erstellungsdatum: {0,date}"},
	{"alias, keyStore.getCreationDate(alias), ",
		"{0}, {1,date}, "},
	{"alias, ", "{0}, "},
	{"Entry type: <type>", "Eintragstyp: {0}"},
	{"Certificate chain length: ", "Zertifikatskettenl\u00e4nge: "},
	{"Certificate[(i + 1)]:", "Zertifikat[{0,number,integer}]:"},
	{"Certificate fingerprint (MD5): ", "Zertifikatsfingerabdruck (MD5): "},
	{"Entry type: trustedCertEntry\n", "Eintragstyp: trustedCertEntry\n"},
	{"trustedCertEntry,", "trustedCertEntry,"},
	{"Keystore type: ", "Keystore-Typ: "},
	{"Keystore provider: ", "Keystore-Provider: "},
	{"Your keystore contains keyStore.size() entry",
		"Ihr Keystore enth\u00e4lt {0,number,integer} Eintrag/-\u00e4ge."},
	{"Your keystore contains keyStore.size() entries",
		"Ihr Keystore enth\u00e4lt {0,number,integer} Eintr\u00e4ge."},
	{"Failed to parse input", "Eingabe konnte nicht analysiert werden."},
	{"Empty input", "Leere Eingabe"},
	{"Not X.509 certificate", "Kein X.509-Zertifikat"},
	{"Cannot derive signature algorithm",
		"Signaturalgorithmus kann nicht abgeleitet werden."},
	{"alias has no public key", "{0} hat keinen \u00f6ffentlichen Schl\u00fcssel."},
	{"alias has no X.509 certificate", "{0} hat kein X.509-Zertifikat."},
	{"New certificate (self-signed):", "Neues Zertifikat (selbstsigniert):"},
	{"Reply has no certificates", "Antwort hat keine Zertifikate."},
	{"Certificate not imported, alias <alias> already exists",
		"Zertifikat nicht importiert, Alias <{0}> bereits vorhanden"},
	{"Input not an X.509 certificate", "Eingabe kein X.509-Zertifikat"},
	{"Certificate already exists in keystore under alias <trustalias>",
		"Zertifikat in Keystore bereits unter Alias <{0}> vorhanden"},
	{"Do you still want to add it? [no]:  ",
		"M\u00f6chten Sie es trotzdem hinzuf\u00fcgen? [Nein]:  "},
	{"Certificate already exists in system-wide CA keystore under alias <trustalias>",
		"Zertifikat in systemweiten CA-Keystore bereits unter Alias <{0}> vorhanden."},
	{"Do you still want to add it to your own keystore? [no]:  ",
		"M\u00f6chten Sie es trotzdem zu Ihrem eigenen Keystore hinzuf\u00fcgen? [Nein]:  "},
	{"Trust this certificate? [no]:  ", "Diesem Zertifikat vertrauen? [Nein]:  "},
	{"YES", "JA"},
	{"New prompt: ", "Neues {0}: "},
	{"Passwords must differ", "Passw\u00f6rter m\u00fcssen sich unterscheiden"},
	{"Re-enter new prompt: ", "Neues {0} nochmals eingeben: "},
	{"Re-enter new password: ", "Geben Sie das Passwort erneut ein: "},
	{"They don't match. Try again", "Keine \u00dcbereinstimmung. Versuchen Sie es erneut."},
	{"Enter prompt alias name:  ", "Geben Sie den Aliasnamen von {0} ein:  "},
        {"Enter new alias name\t(RETURN to cancel import for this entry):  ",
                 "Geben Sie einen neuen Alias-Namen ein.\t(Dr\u00fccken Sie die Eingabetaste, um das Importieren dieses Eintrags abzubrechen.):  "},
	{"Enter alias name:  ", "Geben Sie den Aliasnamen ein:  "},
	{"\t(RETURN if same as for <otherAlias>)",
		"\t(EINGABETASTE, wenn selber Name wie f\u00fcr <{0}>)"},
	{"*PATTERN* printX509Cert",
		"Eigner: {0}\nAussteller: {1}\nSeriennummer: {2}\nG\u00fcltig von: {3} bis: {4}\nDigitaler Fingerabdruck des Zertifikats:\n\t MD5:  {5}\n\t SHA1: {6}\n\t Unterschrift-Algorithmusname: {7}\n\t Version: {8}"},
	{"What is your first and last name?",
		"Wie lautet Ihr Vor- und Nachname?"},
	{"What is the name of your organizational unit?",
		"Wie lautet der Name Ihrer organisatorischen Einheit?"},
	{"What is the name of your organization?",
		"Wie lautet der Name Ihrer Organisation?"},
	{"What is the name of your City or Locality?",
		"Wie lautet der Name Ihrer Stadt oder Gemeinde?"},
	{"What is the name of your State or Province?",
		"Wie lautet der Name Ihres Bundeslandes oder Ihrer Provinz?"},
	{"What is the two-letter country code for this unit?",
		"Wie lautet der Landescode (zwei Buchstaben) f\u00fcr diese Einheit?"},
	{"Is <name> correct?", "Ist {0} richtig?"},
	{"no", "Nein"},
	{"yes", "Ja"},
	{"y", "J"},
	{"  [defaultValue]:  ", " [{0}]:  "},
	{"Alias <alias> has no key",
		"Alias <{0}> verf\u00fcgt \u00fcber keinen Schl\u00fcssel"},
        {"Alias <alias> references an entry type that is not a private key entry.  The -keyclone command only supports cloning of private key entries",
                 "Alias <{0}> verweist auf einen Eintragstyp, der nicht einem Eintrag f\u00fcr einen privaten Schl\u00fcssel entspricht.  Der Befehl -keyclone unterst\u00fctzt nur das Klonen von privaten Schl\u00fcsseleintr\u00e4gen"},
                        
	{"*****************  WARNING WARNING WARNING  *****************",
	    "*****************  WARNUNG WARNUNG WARNUNG  *****************"},

        // Translators of the following 5 pairs, ATTENTION:
        // the next 5 string pairs are meant to be combined into 2 paragraphs,
        // 1+3+4 and 2+3+5. make sure your translation also does.
        {"* The integrity of the information stored in your keystore  *",
	    "* Die Integrit\u00e4t der in Ihrem Keystore gespeicherten Informationen  *"},
        {"* The integrity of the information stored in the srckeystore*",
            "* Die Integrit\u00e4t der in srckeystore gespeicherten Informationen*"},
	{"* has NOT been verified!  In order to verify its integrity, *",
	    "* ist NICHT verifiziert worden! Damit die Integrit\u00e4t verifiziert werden kann, *"},
	{"* you must provide your keystore password.                  *",
	    "* m\u00fcssen Sie Ihr Keystore-Passwort eingeben. *"},
        {"* you must provide the srckeystore password.                *",
            "* Sie m\u00fcssen das Passwort f\u00fcr srckeystore angeben.                *"},
                    
                    
	{"Certificate reply does not contain public key for <alias>",
		"Zertifikatantwort enth\u00e4lt keinen \u00f6ffentlichen Schl\u00fcssel f\u00fcr <{0}>."},
	{"Incomplete certificate chain in reply",
		"Unvollst\u00e4ndige Zertifikatskette in Antwort"},
	{"Certificate chain in reply does not verify: ",
		"Zertifikatskette in Antwort verifiziert nicht: "},
	{"Top-level certificate in reply:\n",
		"Toplevel-Zertifikat in Antwort:\n"},
	{"... is not trusted. ", "... wird nicht vertraut. "},
	{"Install reply anyway? [no]:  ", "Antwort trotzdem installieren? [Nein]:  "},
	{"NO", "NEIN"},
	{"Public keys in reply and keystore don't match",
		"\u00d6ffentliche Schl\u00fcssel in Antwort und Keystore stimmen nicht \u00fcberein."},
	{"Certificate reply and certificate in keystore are identical",
		"Zertifikatantwort und Zertifikat in Keystore sind identisch."},
	{"Failed to establish chain from reply",
		"Kette konnte nicht aus Antwort entnommen werden."},
	{"n", "N"},
	{"Wrong answer, try again", "Falsche Antwort, versuchen Sie es noch einmal."},
	{"Secret key not generated, alias <alias> already exists",
		"Geheimschl\u00fcssel wurde nicht erstellt, Alias <{0}> bereits vorhanden"},
        {"Please provide -keysize for secret key generation",
                "Geben Sie -keysize zum Erstellen eines Geheimschl\u00fcssels an"},
	{"keytool usage:\n", "Keytool-Syntax:\n"},

        {"Extensions: ", "Erweiterungen: "},
        
	{"-certreq     [-v] [-protected]",
		"-certreq     [-v] [-protected]"},
	{"\t     [-alias <alias>] [-sigalg <sigalg>]",
		"\t     [-alias <Alias>] [-sigalg <Sigalg>]"},
	{"\t     [-file <csr_file>] [-keypass <keypass>]",
		"\t     [-file <csr_Datei>] [-keypass <Keypass>]"},
	{"\t     [-keystore <keystore>] [-storepass <storepass>]",
		"\t     [-keystore <Keystore>] [-storepass <Storepass>]"},
	{"\t     [-storetype <storetype>] [-providername <name>]",
		"\t     [-storetype <Speichertyp>] [-providername <Name>]"},
	{"\t     [-providerclass <provider_class_name> [-providerarg <arg>]] ...",
		"\t     [-providerclass <Name der Providerklasse> [-providerarg <Argument>]] ..."},
        {"\t     [-providerpath <pathlist>]",
                "\t     [-providerpath <Pfadliste>]"},
	{"-delete      [-v] [-protected] -alias <alias>",
		"-delete      [-v] [-protected] -alias <Alias>"},
	/** rest is same as -certreq starting from -keystore **/
       
        //{"-export      [-v] [-rfc] [-protected]",
        //       "-export      [-v] [-rfc] [-protected]"},
	{"-exportcert  [-v] [-rfc] [-protected]",
		"-exportcert  [-v] [-rfc] [-protected]"},
	{"\t     [-alias <alias>] [-file <cert_file>]",
		"\t     [-alias <Alias>] [-file <Zert_datei>]"},
	/** rest is same as -certreq starting from -keystore **/

        //{"-genkey      [-v] [-protected]",
        //        "-genkey      [-v] [-protected]"},
	{"-genkeypair  [-v] [-protected]",
		"-genkeypair  [-v] [-protected]"},
	{"\t     [-alias <alias>]", "\t     [-alias <Alias>]"},
	{"\t     [-keyalg <keyalg>] [-keysize <keysize>]",
                "\t     [-keyalg <Schl\u00fcssel-Alg>] [-keysize <Schl\u00fcsselgr\u00f6\u00dfe>]"},
	{"\t     [-sigalg <sigalg>] [-dname <dname>]",
                "\t     [-sigalg <Signal-Alg>] [-dname <Dname>]"},
	{"\t     [-validity <valDays>] [-keypass <keypass>]",
                "\t     [-validity <G\u00fcltigkeitTage>] [-keypass <Schl\u00fcsselpass>]"},
	/** rest is same as -certreq starting from -keystore **/

	{"-genseckey   [-v] [-protected]",
		"-genseckey   [-v] [-protected]"},
	/** rest is same as -certreq starting from -keystore **/

	{"-help", "-help"},
	//{"-identitydb  [-v] [-protected]",
	//	"-identitydb  [-v] [-protected]"},
	//{"\t     [-file <idb_file>]", "\t     [-file <idb_file>]"},
	/** rest is same as -certreq starting from -keystore **/

        //{"-import      [-v] [-noprompt] [-trustcacerts] [-protected]",
        //       "-import      [-v] [-noprompt] [-trustcacerts] [-protected]"},
	{"-importcert  [-v] [-noprompt] [-trustcacerts] [-protected]",
		"-importcert  [-v] [-noprompt] [-trustcacerts] [-protected]"},
	{"\t     [-alias <alias>]", "\t     [-alias <Alias>]"},
        {"\t     [-alias <alias>] [-keypass <keypass>]",
            "\t     [-alias <Alias>] [-keypass <Keypass>]"},
	{"\t     [-file <cert_file>] [-keypass <keypass>]",
		"\t     [-file <Zert_Datei>] [-keypass <Schl\u00fcsselpass>]"},
	/** rest is same as -certreq starting from -keystore **/

	{"-importkeystore [-v] ",
                "-importkeystore [-v] "},
	{"\t     [-srckeystore <srckeystore>] [-destkeystore <destkeystore>]",
                "\t     [-srckeystore <Quell-Keystore>] [-destkeystore <Ziel-Keystore>]"},
	{"\t     [-srcstoretype <srcstoretype>] [-deststoretype <deststoretype>]",
                "\t     [-srcstoretype <Typ des Quell-Keystore>] [-deststoretype <Typ des Ziel-Keystore>]"},
	{"\t     [-srcprotected] [-destprotected]",
                "\t     [-srcprotected] [-destprotected]"},
        {"\t     [-srcstorepass <srcstorepass>] [-deststorepass <deststorepass>]",
                "\t     [-srcstorepass <Passwort f\u00fcr Quell-Keystore>] [-deststorepass <Passwort f\u00fcr Ziel-Keystore>]"},
        {"\t     [-srcprovidername <srcprovidername>]\n\t     [-destprovidername <destprovidername>]",  // Zeile zu lang, zwei Zeilen verwenden
                 "\t     [-srcprovidername <Name des Quell-Providers>]\n\t     [-destprovidername <Name des Ziel-Providers>]"},
	{"\t     [-srcalias <srcalias> [-destalias <destalias>]",
                "\t     [-srcalias <Quell-Alias> [-destalias <Ziel-Alias>]"},
	{"\t       [-srckeypass <srckeypass>] [-destkeypass <destkeypass>]]",
                "\t       [-srckeypass <Passwort f\u00fcr Quell-Keystore>] [-destkeypass <Passwort f\u00fcr Ziel-Keystore>]]"},
        {"\t     [-noprompt]", "\t     [-noprompt]"},
	/** rest is same as -certreq starting from -keystore **/
                
        {"-changealias [-v] [-protected] -alias <alias> -destalias <destalias>",
                "-changealias [-v] [-protected] -alias <Alias> -destalias <Ziel-Alias>"},
        {"\t     [-keypass <keypass>]", "\t     [-keypass <Keypass>]"},
        
	//{"-keyclone    [-v] [-protected]",
	//	"-keyclone    [-v] [-protected]"},
	//{"\t     [-alias <alias>] -dest <dest_alias>",
	//	"\t     [-alias <alias>] -dest <dest_alias>"},
	//{"\t     [-keypass <keypass>] [-new <new_keypass>]",
	//	"\t     [-keypass <keypass>] [-new <new_keypass>]"},
	/** rest is same as -certreq starting from -keystore **/

	{"-keypasswd   [-v] [-alias <alias>]",
		"-keypasswd   [-v] [-alias <Alias>]"},
	{"\t     [-keypass <old_keypass>] [-new <new_keypass>]",
		"\t     [-keypass <alt_Schl\u00fcsselpass>] [-new <neu_Schl\u00fcsselpass>]"},
	/** rest is same as -certreq starting from -keystore **/

	{"-list        [-v | -rfc] [-protected]",
			"-list        [-v | -rfc] [-protected]"},
	{"\t     [-alias <alias>]", "\t     [-alias <Alias>]"},
	/** rest is same as -certreq starting from -keystore **/

	{"-printcert   [-v] [-file <cert_file>]",
		"-printcert   [-v] [-file <Zert_Datei>]"},

	//{"-selfcert    [-v] [-protected]",
	//	"-selfcert    [-v] [-protected]"},
	{"\t     [-alias <alias>]", "\t     [-alias <Alias>]"},
	//{"\t     [-dname <dname>] [-validity <valDays>]",
	//	"\t     [-dname <dname>] [-validity <valDays>]"},
	//{"\t     [-keypass <keypass>] [-sigalg <sigalg>]",
	//	"\t     [-keypass <keypass>] [-sigalg <sigalg>]"},
	/** rest is same as -certreq starting from -keystore **/

	{"-storepasswd [-v] [-new <new_storepass>]",
		"-storepasswd [-v] [-new <neu_Storepass>]"},
	/** rest is same as -certreq starting from -keystore **/
                        
	// policytool
	{"Warning: A public key for alias 'signers[i]' does not exist.  Make sure a KeyStore is properly configured.",
		"Warnung: Kein \u00f6ffentlicher Schl\u00fcssel f\u00fcr Alias {0} vorhanden.  Vergewissern Sie sich, dass der KeyStore ordnungsgem\u00e4\u00df konfiguriert ist."},
	{"Warning: Class not found: class", "Warnung: Klasse nicht gefunden: {0}"},
	{"Warning: Invalid argument(s) for constructor: arg",
		"Warnung: Ung\u00fcltige(s) Argument(e) f\u00fcr Konstruktor: {0}"},
	{"Illegal Principal Type: type", "Unzul\u00e4ssiger Principal-Typ: {0}"},
	{"Illegal option: option", "Unzul\u00e4ssige Option: {0}"},
	{"Usage: policytool [options]", "Syntax: policytool [Optionen]"},
	{"  [-file <file>]    policy file location",
		" [-file <Datei>]    Verzeichnis der Richtliniendatei"},
	{"New", "Neu"},
	{"Open", "\u00d6ffnen"},
	{"Save", "Speichern"},
	{"Save As", "Speichern unter"},
	{"View Warning Log", "Warnungsprotokoll anzeigen"},
	{"Exit", "Beenden"},
	{"Add Policy Entry", "Richtlinieneintrag hinzuf\u00fcgen"},
	{"Edit Policy Entry", "Richtlinieneintrag bearbeiten"},
	{"Remove Policy Entry", "Richtlinieneintrag l\u00f6schen"},
	{"Edit", "Bearbeiten"},
        {"Retain", "Beibehalten"},
                
        {"Warning: File name may include escaped backslash characters. " +
                        "It is not necessary to escape backslash characters " +
                        "(the tool escapes characters as necessary when writing " +
                        "the policy contents to the persistent store).\n\n" +
                        "Click on Retain to retain the entered name, or click on " +
                        "Edit to edit the name.",
            "Warning: File name may include escaped backslash characters. " +
                        "It is not necessary to escape backslash characters " +
                        "(the tool escapes characters as necessary when writing " +
                        "the policy contents to the persistent store).\n\n" +
                        "Click on Retain to retain the entered name, or click on " +
                        "Edit to edit the name."},

        {"Add Public Key Alias", "Alias f\u00fcr \u00f6ffentlichen Schl\u00fcssel hinzuf\u00fcgen"},
	{"Remove Public Key Alias", "Alias f\u00fcr \u00f6ffentlichen Schl\u00fcssel l\u00f6schen"},
	{"File", "Datei"},
	{"KeyStore", "KeyStore"},
	{"Policy File:", "Richtliniendatei:"},
	{"Could not open policy file: policyFile: e.toString()",
		"Richtliniendatei konnte nicht ge\u00f6ffnet werden: {0}: {1}"},
	{"Policy Tool", "Richtlinientool"},
	{"Errors have occurred while opening the policy configuration.  View the Warning Log for more information.",
		"Beim \u00d6ffnen der Richtlinienkonfiguration sind Fehler aufgetreten. Weitere Informationen finden Sie im Warnungsprotokoll."},
	{"Error", "Fehler"},
	{"OK", "OK"},
	{"Status", "Status"},
	{"Warning", "Warnung"},
	{"Permission:                                                       ",
		"Berechtigung:                                                       "},
	{"Principal Type:", "Principal-Typ:"},
        {"Principal Name:", "Principal-Name:"},
	{"Target Name:                                                    ",
		"Zielname:                                                    "},
	{"Actions:                                                             ",
		"Aktionen:                                                             "},
	{"OK to overwrite existing file filename?",
		"Vorhandene Datei {0} \u00fcberschreiben?"},
	{"Cancel", "Abbrechen"},
	{"CodeBase:", "Code-Basis:"},
	{"SignedBy:", "Signiert von:"},
	{"Add Principal", "Principal hinzuf\u00fcgen"},
	{"Edit Principal", "Principal bearbeiten"},
	{"Remove Principal", "Principal l\u00f6schen"},
	{"Principals:", "Principals:"},
	{"  Add Permission", " Berechtigung hinzuf\u00fcgen"},
	{"  Edit Permission", " Berechtigung \u00e4ndern"},
	{"Remove Permission", "Berechtigung l\u00f6schen"},
	{"Done", "Fertig"},
	{"KeyStore URL:", "KeyStore-URL:"},
	{"KeyStore Type:", "KeyStore-Typ:"},
	{"KeyStore Provider:", "KeyStore-Anbieter:"},
	{"KeyStore Password URL:", "KeyStore-Passwort-URL:"},
	{"Principals", "Principals"},
	{"  Edit Principal:", " Principal bearbeiten:"},
	{"  Add New Principal:", " Neuen Principal hinzuf\u00fcgen:"},
	{"Permissions", "Berechtigungen"},
	{"  Edit Permission:", " Berechtigung \u00e4ndern:"},
	{"  Add New Permission:", " Neue Berechtigung hinzuf\u00fcgen:"},
	{"Signed By:", "Signiert von:"},
	{"Cannot Specify Principal with a Wildcard Class without a Wildcard Name",
	    "Principal kann nicht mit einer Wildcard-Klasse ohne Wildcard-Namen angegeben werden"},
        {"Cannot Specify Principal without a Name",
            "Principal kann nicht ohne einen Namen angegeben werden"},
	{"Permission and Target Name must have a value",
		"Berechtigung und Zielname m\u00fcssen einen Wert haben"},
	{"Remove this Policy Entry?", "Diesen Richtlinieneintrag l\u00f6schen?"},
	{"Overwrite File", "Datei \u00fcberschreiben"},
	{"Policy successfully written to filename",
		"Richtlinien erfolgreich in {0} geschrieben"},
	{"null filename", "Null als Dateiname"},
	{"Save changes?", "\u00c4nderungen speichern?"},
	{"Yes", "Ja"},
	{"No", "Nein"},
	{"Policy Entry", "Richtlinieneintrag"},
	{"Save Changes", "\u00c4nderungen speichern"},
	{"No Policy Entry selected", "Kein Richtlinieneintrag ausgew\u00e4hlt"},
	{"Unable to open KeyStore: ex.toString()",
		"KeyStore konnte nicht ge\u00f6ffnet werden: {0}"},
	{"No principal selected", "Kein Principal ausgew\u00e4hlt"},
	{"No permission selected", "Keine Berechtigung ausgew\u00e4hlt"},
	{"name", "Name"},
	{"configuration type", "Konfigurationstyp"},
	{"environment variable name", "Name der Umgebungsvariable"},
	{"library name", "Bibliotheksname"},
	{"package name", "Paketname"},
	{"policy type", "Richtlinientyp"},
	{"property name", "Eigenschaftsname"},
	{"provider name", "Providername"},
        {"Principal List", "Principal-Liste"},
        {"Permission List", "Berechtigungsliste"},
        {"Code Base", "Code-Basis"},
        {"KeyStore U R L:", "KeyStore-URL:"},
        {"KeyStore Password U R L:", "KeyStore-Passwort-URL:"},
        

	// javax.security.auth.PrivateCredentialPermission
	{"invalid null input(s)", "Ung\u00fcltige Null-Eingabe(n)"},
	{"actions can only be 'read'", "Aktionen k\u00f6nnen nur 'gelesen' werden"},
	{"permission name [name] syntax invalid: ",
		"Syntax f\u00fcr Berechtigungsnamen [{0}] ung\u00fcltig: "},
	{"Credential Class not followed by a Principal Class and Name",
		"Nach Authentisierungsklasse folgt keine Principal-Klasse und kein Name."},
	{"Principal Class not followed by a Principal Name",
		"Nach Principal-Klasse folgt kein Principal-Name"},
	{"Principal Name must be surrounded by quotes",
		"Principal-Name muss vorn und hinten mit Anf\u00fchrungsstrichen versehen sein"},
	{"Principal Name missing end quote",
		"Abschlie\u00dfendes Anf\u00fchrungszeichen f\u00fcr Principal-Name fehlt"},
	{"PrivateCredentialPermission Principal Class can not be a wildcard (*) value if Principal Name is not a wildcard (*) value",
		"Private Authentisierungsberechtigung Principal-Klasse kann kein Wildcardwert (*) sein, wenn der Principal-Name kein Wildcardwert (*) ist."},
	{"CredOwner:\n\tPrincipal Class = class\n\tPrincipal Name = name",
		"Authentisierungsbesitzer:\n\tPrincipal-Klasse = {0}\n\tPrincipal-Name = {1}"},

	// javax.security.auth.x500
	{"provided null name", "hat Null als Namen geliefert"},
	{"provided null keyword map", "Leere Schl\u00fcsselwort-Map"},
	{"provided null OID map", "Leere OID-Map"},

	// javax.security.auth.Subject
	{"invalid null AccessControlContext provided",
		"Ung\u00fcltige Null als Zugangskontrollkontext geliefert"},
	{"invalid null action provided", "Ung\u00fcltige Null als Aktion geliefert"},
	{"invalid null Class provided", "Ung\u00fcltige Null als Klasse geliefert"},
	{"Subject:\n", "Betreff:\n"},
	{"\tPrincipal: ", "\tPrincipal: "},
	{"\tPublic Credential: ", "\t\u00d6ffentliche Authentisierung: "},
	{"\tPrivate Credentials inaccessible\n",
		"\tKein Zugriff auf private Authentisierungen m\u00f6glich\n"},
	{"\tPrivate Credential: ", "\tPrivate Authentisierung: "},
	{"\tPrivate Credential inaccessible\n",
		"\tKein Zugriff auf private Authentisierung m\u00f6glich\n"},
	{"Subject is read-only", "Betreff ist schreibgesch\u00fctzt"},
	{"attempting to add an object which is not an instance of java.security.Principal to a Subject's Principal Set",
		"Es wird versucht, ein Objekt hinzuzuf\u00fcgen, das keine Instanz von java.security.Principal f\u00fcr eine Principal-Gruppe eines Betreffs ist."},
	{"attempting to add an object which is not an instance of class",
		"Es wird versucht, ein Objekt hinzuzuf\u00fcgen, das keine Instanz von {0} ist."},

	// javax.security.auth.login.AppConfigurationEntry
	{"LoginModuleControlFlag: ", "Anmeldemodul-Steuerflag: "},

	// javax.security.auth.login.LoginContext
	{"Invalid null input: name", "Ung\u00fcltige Nulleingabe: Name"},
	{"No LoginModules configured for name",
	 "F\u00fcr {0} sind keine Anmeldemodule konfiguriert."},
	{"invalid null Subject provided", "Ung\u00fcltige Null als Betreff geliefert"},
	{"invalid null CallbackHandler provided",
		"Ung\u00fcltige Null als Callback-Handler geliefert"},
	{"null subject - logout called before login",
		"Null-Betreff - Abmeldung vor Anmeldung aufgerufen"},
	{"unable to instantiate LoginModule, module, because it does not provide a no-argument constructor",
		"Es kann keine Instanz des Anmeldemoduls {0} erstellt werden, weil es keinen argumentlosen Konstruktor liefert."},
	{"unable to instantiate LoginModule",
		"Es kann keine Instanz des Anmeldemoduls erstellt werden."},
	{"unable to instantiate LoginModule: ",
		"LoginModule konnte nicht instanziiert werden: "},
	{"unable to find LoginModule class: ",
		"Die Anmeldemodulklasse kann nicht gefunden werden: "},
	{"unable to access LoginModule: ",
		"Kein Zugriff auf Anmeldemodul m\u00f6glich: "},
	{"Login Failure: all modules ignored",
		"Anmeldefehler: Alle Module werden ignoriert"},

	// sun.security.provider.PolicyFile

	{"java.security.policy: error parsing policy:\n\tmessage",
		"java.security.policy: Fehler bei Analyse {0}:\n\t{1}"},
	{"java.security.policy: error adding Permission, perm:\n\tmessage",
		"java.security.policy: Fehler beim Hinzuf\u00fcgen der Genehmigung, {0}:\n\t{1}"},
	{"java.security.policy: error adding Entry:\n\tmessage",
		"java.security.policy: Fehler beim Hinzuf\u00fcgen des Eintrags:\n\t{0}"},
	{"alias name not provided (pe.name)", "Aliasname nicht bereitgestellt ({0})"},
	{"unable to perform substitution on alias, suffix",
		"kann Substition von Alias nicht durchf\u00fchren, {0}"},
	{"substitution value, prefix, unsupported",
		"Substitutionswert, {0}, nicht unterst\u00fctzt"},
	{"(", "("},
	{")", ")"},
	{"type can't be null","Typ kann nicht Null sein"},

	// sun.security.provider.PolicyParser
	{"keystorePasswordURL can not be specified without also specifying keystore",
		"keystorePasswordURL kann nicht ohne Keystore angegeben werden"},
	{"expected keystore type", "erwarteter Keystore-Typ"},
	{"expected keystore provider", "erwarteter Keystore-Provider"},
	{"multiple Codebase expressions",
	        "mehrere Codebase-Ausdr\u00fccke"},
        {"multiple SignedBy expressions","mehrere SignedBy-Ausdr\u00fccke"},
	{"SignedBy has empty alias","Leerer Alias in SignedBy"},
	{"can not specify Principal with a wildcard class without a wildcard name",
		"Kann Principal nicht mit einer Wildcard-Klasse ohne Wildcard-Namen angeben."},
	{"expected codeBase or SignedBy or Principal",
		"CodeBase oder SignedBy oder Principal erwartet"},
	{"expected permission entry", "Berechtigungseintrag erwartet"},
	{"number ", "Nummer "},
	{"expected [expect], read [end of file]",
		"erwartet [{0}], gelesen [Dateiende]"},
	{"expected [;], read [end of file]",
		"erwartet [;], gelesen [Dateiende]"},
	{"line number: msg", "Zeile {0}: {1}"},
	{"line number: expected [expect], found [actual]",
		"Zeile {0}: erwartet [{1}], gefunden [{2}]"},
	{"null principalClass or principalName",
		"Principal-Klasse oder Principal-Name Null"},

	// sun.security.pkcs11.SunPKCS11
	{"PKCS11 Token [providerName] Password: ",
			"Passwort f\u00fcr PKCS11-Token [{0}]: "},

	/* --- DEPRECATED --- */
	// javax.security.auth.Policy
	{"unable to instantiate Subject-based policy",
		"auf Subject basierende Richtlinie konnte nicht instanziiert werden"}
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
