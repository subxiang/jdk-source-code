/*
 * @(#)Generator.java	1.4 10/03/23
 *
 * Copyright (c) 2006, 2009, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.jdesktop.synthdesigner.generator;

import static org.jdesktop.synthdesigner.generator.TemplateWriter.read;
import static org.jdesktop.synthdesigner.generator.TemplateWriter.writeSrcFile;
import org.jdesktop.synthdesigner.synthmodel.SynthModel;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates the various Java artifacts based on a SynthModel.
 * <p/>
 * Generated source files are split up among two different locations. There are those source files that are meant to be
 * edited (generally, only the LookAndFeel class itself) and those that are autogenerated (everything else).
 * <p/>
 * All autogenerated files are placed in "buildPackageRoot" and are package private. A LAF author (one who has access to
 * the generated sources) will be able to access any of the generated classes. Those referencing the library, however,
 * will only be able to access the main LookAndFeel class itself (since everything else is package private).
 *
 * @author  Richard Bair
 * @author  Jasper Potts
 */
public class Generator {
    /** A map of variables that are used for variable substitution in the template files. */
    private Map<String, String> variables;

    private boolean full = false;
    private File buildPackageRoot;
    private File srcPackageRoot;
    private String packageNamePrefix;
    private String lafName;
    private SynthModel model;

    /**
     * MAIN APPLICATION
     * <p/>
     * This is for using the generator as part of the java build process
     *
     * @param args The commandline arguments
     */
    public static void main(String[] args) {
        if (args.length == 0 || (args.length % 2) != 0) {
            System.out.println("Usage: generator [-options]\n" +
                    "    -full <true|false>     True if we should build the whole LAF or false for building just states and painters.\n" +
                    "    -skinFile <value>      Path to the skin.laf file for the LAF to be generated from.\n" +
                    "    -buildDir <value>      The directory beneath which the build-controlled artifacts (such as the Painters) should\n" +
                    "                           be placed. This is the root directory beneath which the necessary packages and source\n" +
                    "                           files will be created.\n" +
                    "    -srcDir <value>        The directory beneath which the normal user-controlled artifacts (such as the core\n" +
                    "                           LookAndFeel file) should be placed. These are one-time generated files. This is the root\n" +
                    "                           directory beneath which the necessary packages and source files will be created.\n" +
                    "    -resourcesDir <value>  The resources directory containing templates and images.\n" +
                    "    -packagePrefix <value> The package name associated with this synth look and feel. For example,\n" +
                    "                           \"org.mypackage.mylaf\"\n" +
                    "    -lafName <value>       The name of the laf, such as \"MyLAF\".\n");
        } else {
            boolean full = false;
            File skinFile = new File(System.getProperty("user.dir"));
            File buildDir = new File(System.getProperty("user.dir"));
            File srcDir = new File(System.getProperty("user.dir"));
            File resourcesDir = new File(System.getProperty("user.dir"));
            String packagePrefix = "org.mypackage.mylaf";
            String lafName = "MyLAF";
            for (int i = 0; i < args.length; i += 2) {
                String key = args[i].trim().toLowerCase();
                String value = args[i + 1].trim();
                if ("-full".equals(key)) {
                    full = Boolean.parseBoolean(value);
                } else if ("-skinfile".equals(key)) {
                    skinFile = new File(value);
                } else if ("-builddir".equals(key)) {
                    buildDir = new File(value);
                } else if ("-srcdir".equals(key)) {
                    srcDir = new File(value);
                } else if ("-resourcesdir".equals(key)) {
                    resourcesDir = new File(value);
                } else if ("-packageprefix".equals(key)) {
                    packagePrefix = value;
                } else if ("-lafname".equals(key)) {
                    lafName = value;
                }
            }
            System.out.println("### GENERATING LAF CODE ################################");
            System.out.println("   full          :" + full);
            System.out.println("   skinFile      :" + skinFile.getAbsolutePath());
            System.out.println("   buildDir      :" + buildDir.getAbsolutePath());
            System.out.println("   srcDir        :" + srcDir.getAbsolutePath());
            System.out.println("   resourcesDir  :" + resourcesDir.getAbsolutePath());
            System.out.println("   packagePrefix :" +packagePrefix);
            System.out.println("   lafName       :" +lafName);
            try {
                // LOAD SKIN MODEL
                IBindingFactory bindingFactory = BindingDirectory.getFactory(SynthModel.class);
                IUnmarshallingContext mctx = bindingFactory.createUnmarshallingContext();
                mctx.setDocument(new FileInputStream(skinFile), "UTF-8");
                // pass resources directory in as user context so it can be used in SynthModel preSet
                mctx.setUserContext(resourcesDir);
                SynthModel model = (SynthModel) mctx.unmarshalElement();
                // create and run generator
                Generator generator = new Generator(full, buildDir, srcDir, packagePrefix, lafName, model);
                generator.generate();
            } catch (Exception e) {
                System.err.println("Error loading skin and generating java src:");
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a new Generator, capable of outputting the source code artifacts related to a given SynthModel. It is
     * capable of generating the one-time artifacts in addition to the regeneration of build-controlled artifacts.
     *
     * @param full              True if we should build the whole LAF or false for building just states and painters.
     * @param buildDir          The directory beneath which the build-controlled artifacts (such as the Painters) should
     *                          be placed. This is the root directory beneath which the necessary packages and source
     *                          files will be created.
     * @param srcDir            The directory beneath which the normal user-controlled artifacts (such as the core
     *                          LookAndFeel file) should be placed. These are one-time generated files. This is the root
     *                          directory beneath which the necessary packages and source files will be created.
     * @param packageNamePrefix The package name associated with this synth look and feel. For example,
     *                          org.mypackage.mylaf
     * @param lafName           The name of the laf, such as MyLAF.
     * @param model             The actual SynthModel to base these generated files on.
     */
    public Generator(boolean full, File buildDir, File srcDir, String packageNamePrefix, String lafName,
                     SynthModel model) {
        this.full = full;
        //validate the input variables
        if (packageNamePrefix == null) {
            throw new IllegalArgumentException("You must specify a package name prefix");
        }
        if (buildDir == null) {
            throw new IllegalArgumentException("You must specify the build directory");
        }
        if (srcDir == null) {
            throw new IllegalArgumentException("You must specify the source directory");
        }
        if (model == null) {
            throw new IllegalArgumentException("You must specify the SynthModel");
        }
        if (lafName == null) {
            throw new IllegalArgumentException("You must specify the name of the look and feel");
        }

        //construct the map which is used to do variable substitution of the template
        //files
        variables = new HashMap<String, String>();
        variables.put("PACKAGE", packageNamePrefix);
        variables.put("LAF_NAME", lafName);

        //generate and save references to the package-root directories.
        //(That is, given the buildDir and srcDir, generate references to the
        //org.mypackage.mylaf subdirectories)
        buildPackageRoot = new File(buildDir, packageNamePrefix.replaceAll("\\.", "\\/"));
        buildPackageRoot.mkdirs();
        srcPackageRoot = new File(srcDir, packageNamePrefix.replaceAll("\\.", "\\/"));
        srcPackageRoot.mkdirs();

        //save the variables
        this.packageNamePrefix = packageNamePrefix;
        this.lafName = lafName;
        this.model = model;
    }

    public void generate() {
        //Generate the one-time files. If these files already exist, skip the
        //ones that exist and create the missing ones. Register warnings for the
        //already existing files.

        //TODO Skip existing files, send warnings, etc.
        if (full) {
            try {
                //create the LookAndFeel file
                String template = read("resources/LookAndFeel.template");
                writeSrcFile(template, variables, new File(srcPackageRoot, lafName + "LookAndFeel.java"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //create the painters and such.
        regenerate();
    }

    public void regenerate() {
        try {
            if (full) {
                //first, create the AbstractRegionPainter.java file.
                String template = read("resources/AbstractRegionPainter.template");
                writeSrcFile(template, variables, new File(buildPackageRoot, "AbstractRegionPainter.java"));

                //write out BlendingMode.java
                template = read("resources/BlendingMode.template");
                writeSrcFile(template, variables, new File(buildPackageRoot, "BlendingMode.java"));

                //create the SynthPainterImpl class
                template = read("resources/SynthPainterImpl.template");
                writeSrcFile(template, variables, new File(buildPackageRoot, "SynthPainterImpl.java"));

                //create the IconImpl class
                template = read("resources/IconImpl.template");
                writeSrcFile(template, variables, new File(buildPackageRoot, lafName + "Icon.java"));

                //create the StyleImpl class
                template = read("resources/StyleImpl.template");
                writeSrcFile(template, variables, new File(buildPackageRoot, lafName + "Style.java"));

                //write out Effect.java
                template = read("resources/Effect.template");
                writeSrcFile(template, variables, new File(buildPackageRoot, "Effect.java"));

                //write out EffectUtils.java
                template = read("resources/EffectUtils.template");
                writeSrcFile(template, variables, new File(buildPackageRoot, "EffectUtils.java"));

                //write out ShadowEffect.java
                template = read("resources/ShadowEffect.template");
                writeSrcFile(template, variables, new File(buildPackageRoot, "ShadowEffect.java"));

                //write out DropShadowEffect.java
                template = read("resources/DropShadowEffect.template");
                writeSrcFile(template, variables, new File(buildPackageRoot, "DropShadowEffect.java"));

                //write out InnerShadowEffect.java
                template = read("resources/InnerShadowEffect.template");
                writeSrcFile(template, variables, new File(buildPackageRoot, "InnerShadowEffect.java"));

                //write out InnerGlowEffect.java
                template = read("resources/InnerGlowEffect.template");
                writeSrcFile(template, variables, new File(buildPackageRoot, "InnerGlowEffect.java"));

                //write out OuterGlowEffect.java
                template = read("resources/OuterGlowEffect.template");
                writeSrcFile(template, variables, new File(buildPackageRoot, "OuterGlowEffect.java"));

                //write out State.java
                template = read("resources/State.template");
                writeSrcFile(template, variables, new File(buildPackageRoot, "State.java"));

                template = read("resources/ImageCache.template");
                writeSrcFile(template, variables, new File(buildPackageRoot, "ImageCache.java"));

                template = read("resources/ImageScalingHelper.template");
                writeSrcFile(template, variables, new File(buildPackageRoot, "ImageScalingHelper.java"));
            }
            //next, populate the first set of ui defaults based on what is in the
            //various palettes of the synth model
            StringBuilder uiDefaultInit = new StringBuilder();
            StringBuilder styleInit = new StringBuilder();
            DefaultsGenerator.generateDefaults(uiDefaultInit, styleInit, model, variables, packageNamePrefix,
                    buildPackageRoot);
            variables.put("UI_DEFAULT_INIT", uiDefaultInit.toString());
            variables.put("STYLE_INIT", styleInit.toString());
            writeSrcFile(read("resources/Defaults.template"), variables,
                    new File(buildPackageRoot, lafName + "Defaults.java"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
