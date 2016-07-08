package com.nick.plugin

import com.android.builder.AndroidBuilder
import com.android.builder.BuildTools
import com.android.builder.LoggerWrapper
import org.apache.tools.ant.taskdefs.optional.depend.DirectoryIterator
import org.gradle.api.Plugin
import org.gradle.api.Project

class AssembleApkPlugin implements Plugin<Project> {

    protected Project project;

    @Override
    void apply(Project project) {
        this.project = project;

        project.gradle.addListener(new TimeListener())

        if (!project.android) {
            throw new IllegalStateException('Must apply \'com.android.application\' or \'com.android.library\' first!')
        }
        project.extensions.create('pluginArgs', AssembleApkExtension);

        createExtension()

        project.afterEvaluate {
            println "#####################"
            println "${project.android.getSdkDirectory().getAbsolutePath()}"
            println "#####################"
        }

        project.android.applicationVariants.all { variant ->
            variant.outputs.each { output ->
                File file = output.outputFile
//                output.outputFile = new File(destDir, nameMap(file.getName()))
                getAppTools().outputFile = file;
                println "%%%%%%%%%%%%%: "+getAppTools().outputFile.getAbsolutePath();
            }
        }

        project.task('testPlugin') << {

            project.logger.info("ssssssssssssss")

            println project.pluginArgs.message;
            File manifestFile = new File(BuildTools.manifestFile);
            File resFolder = new File(BuildTools.resFolder)
            File assetsDir = null;
            AndroidBuilder builder = new AndroidBuilder(new LoggerWrapper(project.logger),true)
            builder.processResources(manifestFile,resFolder,assetsDir,null,BuildTools.sourceOutputDir,null,BuildTools.resPackageOutput,null,true,new ArrayList<String>())

            String bootclasspath = BuildTools.ANDROID_JAR_Path;
            ArrayList<String> javaSource = new ArrayList<>()
            javaSource.add("G:\\Android\\workspace\\demo\\nickStudyDemo\\network\\src\\main\\java\\app\\study\\nick\\network\\*.java")
            javaSource.add("G:\\Android\\workspace\\demo\\nickStudyDemo\\network\\build2\\generated\\resources\\app\\study\\nick\\network\\R.java")
            builder.processJavaSourceCode("UTF-8",bootclasspath,BuildTools.outputClassDir,javaSource)

            ArrayList<File> files = new ArrayList<>();
            File classDir = new File(BuildTools.inputClassDir);
            if(classDir.isDirectory()){
                files.addAll(classDir.listFiles())
            }
            ArrayList<File> libs = new ArrayList<>();
            builder.convertByteCode(files,libs,new File(BuildTools.outputDex),true);
        }



    }

    protected void createExtension() {
        // Add the 'mAppTools' extension object
        project.extensions.create('mAppTools', AppExtension.class, project)
    }

    protected <T extends AppExtension> T getAppTools() {
        return (T) project.mAppTools
    }
}
