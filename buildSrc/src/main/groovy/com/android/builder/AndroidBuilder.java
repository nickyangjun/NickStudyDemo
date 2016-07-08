package com.android.builder;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//import com.android.builder.dependency.SymbolFileProvider;
//import com.android.builder.internal.SymbolLoader;
//import com.android.builder.internal.SymbolWriter;
//import com.android.builder.model.AaptOptions;
import com.android.ide.common.internal.CommandLineRunner;
import com.android.ide.common.internal.LoggedErrorException;
//import com.android.sdklib.BuildToolInfo;
import com.android.utils.ILogger;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.android.utils.Preconditions.checkArgument;
import static com.android.utils.Preconditions.checkNotNull;

public class AndroidBuilder {

//    private final SdkParser mSdkParser;
    private final ILogger mLogger;
    private final CommandLineRunner mCmdLineRunner;
    private final boolean mVerboseExec;
//    @NonNull
//    private final IAndroidTarget mTarget;
//    @NonNull
//    private final BuildToolInfo mBuildTools;


    AndroidBuilder(@NonNull ILogger logger,  boolean verboseExec){
//        mSdkParser = checkNotNull(sdkParser);
        mLogger = checkNotNull(logger);
        mCmdLineRunner = new CommandLineRunner(mLogger);
//        mBuildTools = mSdkParser.getBuildTools();
//        mTarget = mSdkParser.getTarget();
        mVerboseExec = verboseExec;
    }
    /**
     * Process the resources and generate R.java and/or the packaged resources.
     *
     * @param manifestFile the location of the manifest file
     * @param resFolder the merged res folder
     * @param assetsDir the merged asset folder
     * @param libraries the flat list of libraries
     * @param packageForR Package override to generate the R class in a different package.
     * @param sourceOutputDir optional source folder to generate R.java
     * @param resPackageOutput optional filepath for packaged resources
     * @param proguardOutput optional filepath for proguard file to generate
     * @param type the type of the variant being built
     * @param debuggable whether the app is debuggable
     * @param options the {@link com.android.builder.model.AaptOptions}
     *
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws LoggedErrorException
     */
    public void processResources(
            @NonNull File manifestFile,
            @NonNull  File resFolder,
            @Nullable File assetsDir,
//            @NonNull List<? extends SymbolFileProvider> libraries,
            @Nullable String packageForR,
            @Nullable String sourceOutputDir,
            @Nullable String symbolOutputDir,
            @Nullable String resPackageOutput,
            @Nullable String proguardOutput,
//            VariantConfiguration.Type type,
            boolean debuggable,
//            @NonNull AaptOptions options,
            @NonNull Collection<String> resourceConfigs)
            throws IOException, InterruptedException, LoggedErrorException {

        checkNotNull(manifestFile, "manifestFile cannot be null.");
        checkNotNull(resFolder, "resFolder cannot be null.");
//        checkNotNull(libraries, "libraries cannot be null.");
//        checkNotNull(options, "options cannot be null.");
        // if both output types are empty, then there's nothing to do and this is an error
        checkArgument(sourceOutputDir != null || resPackageOutput != null,
                "No output provided for aapt task");

        // launch aapt: create the command line
//        ArrayList<String> command = Lists.newArrayList();
        ArrayList<String> command = new ArrayList<>();

        String aapt = BuildTools.aapt_Path;
        if (aapt == null || !new File(aapt).isFile()) {
            throw new IllegalStateException("aapt is missing");
        }

        command.add(aapt);
        command.add("package");

        if (mVerboseExec) {
            command.add("-v");
        }

        command.add("-f");

        command.add("--no-crunch");

        // inputs
        command.add("-I");

        command.add(BuildTools.ANDROID_JAR_Path);

        command.add("-M");
        command.add(manifestFile.getAbsolutePath());

        if (resFolder.isDirectory()) {
            command.add("-S");
            command.add(resFolder.getAbsolutePath());
        }

        if (assetsDir != null && assetsDir.isDirectory()) {
            command.add("-A");
            command.add(assetsDir.getAbsolutePath());
        }

        // outputs

        if (sourceOutputDir != null) {
            command.add("-m");
            command.add("-J");
            command.add(sourceOutputDir);
        }

        if (resPackageOutput != null) {
            command.add("-F");
            command.add(resPackageOutput);
        }

        if (proguardOutput != null) {
            command.add("-G");
            command.add(proguardOutput);
        }

        // options controlled by build variants

        if (debuggable) {
            command.add("--debug-mode");
        }

//        if (type == VariantConfiguration.Type.DEFAULT) {
            if (packageForR != null) {
                command.add("--custom-package");
                command.add(packageForR);
                mLogger.verbose("Custom package for R class: '%s'", packageForR);
            }
//        }

        // library specific options
//        if (type == VariantConfiguration.Type.LIBRARY) {
//            command.add("--non-constant-id");
//        }

        // AAPT options
//        String ignoreAssets = options.getIgnoreAssets();
//        if (ignoreAssets != null) {
//            command.add("--ignore-assets");
//            command.add(ignoreAssets);
//        }
//
//        Collection<String> noCompressList = options.getNoCompress();
//        if (noCompressList != null) {
//            for (String noCompress : noCompressList) {
//                command.add("-0");
//                command.add(noCompress);
//            }
//        }

        if (!resourceConfigs.isEmpty()) {
            command.add("-c");

            Joiner joiner = Joiner.on(',');
            command.add(joiner.join(resourceConfigs));
        }

//        if (symbolOutputDir != null &&
//                (type == VariantConfiguration.Type.LIBRARY || !libraries.isEmpty())) {
//            command.add("--output-text-symbols");
//            command.add(symbolOutputDir);
//        }

        mCmdLineRunner.runCmdLine(command);

        // now if the project has libraries, R needs to be created for each libraries,
        // but only if the current project is not a library.
//        if (type != VariantConfiguration.Type.LIBRARY && !libraries.isEmpty()) {
//            SymbolLoader fullSymbolValues = null;
//
//            // First pass processing the libraries, collecting them by packageName,
//            // and ignoring the ones that have the same package name as the application
//            // (since that R class was already created).
//            String appPackageName = packageForR;
//            if (appPackageName == null) {
//                appPackageName = VariantConfiguration.getManifestPackage(manifestFile);
//            }
//
//            // list of all the symbol loaders per package names.
//            Multimap<String, SymbolLoader> libMap = ArrayListMultimap.create();
//
//            for (SymbolFileProvider lib : libraries) {
//                File rFile = lib.getSymbolFile();
//                // if the library has no resource, this file won't exist.
//                if (rFile.isFile()) {
//
//                    String packageName = VariantConfiguration.getManifestPackage(lib.getManifest());
//                    if (appPackageName.equals(packageName)) {
//                        // ignore libraries that have the same package name as the app
//                        continue;
//                    }
//
//                    // load the full values if that's not already been done.
//                    // Doing it lazily allow us to support the case where there's no
//                    // resources anywhere.
//                    if (fullSymbolValues == null) {
//                        fullSymbolValues = new SymbolLoader(new File(symbolOutputDir, "R.txt"),
//                                mLogger);
//                        fullSymbolValues.load();
//                    }
//
//                    SymbolLoader libSymbols = new SymbolLoader(rFile, mLogger);
//                    libSymbols.load();
//
//
//                    // store these symbols by associating them with the package name.
//                    libMap.put(packageName, libSymbols);
//                }
//            }
//
//            // now loop on all the package name, merge all the symbols to write, and write them
//            for (String packageName : libMap.keySet()) {
//                Collection<SymbolLoader> symbols = libMap.get(packageName);
//
//                SymbolWriter writer = new SymbolWriter(sourceOutputDir, packageName,
//                        fullSymbolValues);
//                for (SymbolLoader symbolLoader : symbols) {
//                    writer.addSymbolsToWrite(symbolLoader);
//                }
//                writer.write();
//            }
//        }
    }

    public void processJavaSourceCode(
            @NonNull String sourceEncode,
            @NonNull String bootclasspath ,
            @NonNull String outputDir,
            @NonNull List<String> sources ) throws IOException, InterruptedException {

        ArrayList<String> command = new ArrayList<>();

        String javac = BuildTools.javac_path;
        if (javac == null || !new File(javac).isFile()) {
            throw new IllegalStateException("javac is missing");
        }

        command.add(javac);

        command.add("-encoding");
        command.add(sourceEncode);

        command.add("-target");
        command.add(BuildTools.jdk_version);

        command.add("-bootclasspath");
        command.add(bootclasspath);

        command.add("-d");
        command.add(outputDir);

        for (String s: sources){
            command.add(s);
        }

        mCmdLineRunner.runCmdLine(command);
    }

    /**
     * Converts the bytecode to Dalvik format
     * @param inputs the input files
     * @param preDexedLibraries the list of pre-dexed libraries
     * @param outDexFile the location of the output classes.dex file
     * @param dexOptions dex options
     * @param incremental true if it should attempt incremental dex if applicable
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws LoggedErrorException
     */
    public void convertByteCode(
            @NonNull Iterable<File> inputs,
            @NonNull Iterable<File> preDexedLibraries,
            @NonNull File outDexFile,
            boolean incremental) throws IOException, InterruptedException, LoggedErrorException {
        checkNotNull(inputs, "inputs cannot be null.");
        checkNotNull(outDexFile, "outDexFile cannot be null.");

        // launch dx: create the command line
        ArrayList<String> command =  new ArrayList<>();

        String dx = BuildTools.dx;
        if (dx == null || !new File(dx).isFile()) {
            throw new IllegalStateException("dx is missing");
        }

        command.add(dx);

        command.add("--dex");

        if (mVerboseExec) {
            command.add("--verbose");
        }

        if (incremental) {
            command.add("--incremental");
            command.add("--no-strict");
        }

        command.add("--output");
        command.add(outDexFile.getAbsolutePath());

        // clean up input list
        List<String> inputList = new ArrayList<>();
        for (File f : inputs) {
            if (f != null && f.exists()) {
                inputList.add(f.getAbsolutePath());
            }
        }

        if (!inputList.isEmpty()) {
            mLogger.verbose("Dex inputs: " + inputList);
            command.addAll(inputList);
        }

        // clean up and add library inputs.
        List<String> libraryList = new ArrayList<>();
        for (File f : preDexedLibraries) {
            if (f != null && f.exists()) {
                libraryList.add(f.getAbsolutePath());
            }
        }

        if (!libraryList.isEmpty()) {
            mLogger.verbose("Dex pre-dexed inputs: " + libraryList);
            command.addAll(libraryList);
        }

        mCmdLineRunner.runCmdLine(command);
    }

}