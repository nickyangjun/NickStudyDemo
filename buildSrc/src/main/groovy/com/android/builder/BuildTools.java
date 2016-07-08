package com.android.builder;

import java.io.File;

/**
 * Created by yangjun1 on 2016/6/30.
 */
public class BuildTools {
    public static final String sdk_path = "G:\\Android\\sdk";
    public static final String aapt_Path = sdk_path + "\\build-tools\\23.0.3\\aapt.exe";
    public static final String ANDROID_JAR_Path = sdk_path + "\\platforms\\android-23\\android.jar";

    public static String manifestFile = "G:\\Android\\workspace\\demo\\nickStudyDemo\\network\\src\\main\\AndroidManifest.xml";
    public static String resFolder = "G:\\Android\\workspace\\demo\\nickStudyDemo\\network\\src\\main\\res";
    public static String assetsDir = "";

    public static String sourceOutputDir = "G:\\Android\\workspace\\demo\\nickStudyDemo\\network\\build2\\generated\\resources";
    public static String resPackageOutput = "G:\\Android\\workspace\\demo\\nickStudyDemo\\network\\build2\\generated\\resources\\resources.ap_";


    public static final String jdk_path = "C:\\Program Files\\Java\\jdk1.8.0_31";
    public static final String javac_path = jdk_path + "\\bin\\javac.exe";
    public static final String jdk_version = "1.8";
    public static String outputClassDir = "G:\\Android\\workspace\\demo\\nickStudyDemo\\network\\build2\\generated";


    public static final String dx = sdk_path +"\\build-tools\\23.0.3\\dx.bat";
    public static final String inputClassDir = "G:\\Android\\workspace\\demo\\nickStudyDemo\\network\\build2\\generated\\app\\study\\nick\\network";
    public static final String outputDex = "G:\\Android\\workspace\\demo\\nickStudyDemo\\network\\build2\\generated\\dex\\classes.dex";
}
