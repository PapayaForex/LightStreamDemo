package com.allen.modifyvalue

import groovy.json.JsonSlurper
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

public class BetaPlugin implements Plugin<Project>{
    private Project project =null;

    private static final String APK_UPLOAD_URL = "https://api.bugly.qq.com/beta/apiv1/exp?app_key=";
    private static final String APK_UPLOAD_URL2 = "https://api.bugly.qq.com/beta2/apiv1/exp?app_key=";

    @Override
    void apply(Project project) {
        this.project = project;
        project.extensions.create("Modify",BetaExtension)
        if (project.android.hasProperty("applicationVariants")){
            project.android.applicationVariants.all{
                variant ->String variantName =variant.name.capitalize();
                    if (false ==project.beta.enable){
                        project.logger.error("Bugly: beta gradle enable is false, if you want to auto upload apk file, you should set the execute = true")
                        return;
                    }
                    Task beatTask = creatUploadTask(variant);
            }
        }
    }
    private Task creatUploadTask(Object variant){
        String variantName = variant.name.capitalize();
        Task uploadTask = project.task.creat("upload${variantName}BetaApkFile") <<{
            if (variantName.contains("Debug")&&!project.beta.debugOn){
                println("Bugly: the option debugOn is closed, if you want to upload apk file on debug model, you can set debugOn = true to open it");
                return ;
            }
            uploadApk(generateUploadInfo(variant));
        }
        println("Bugly:create upload${variantName}BetaApkFile task")
        return
    }

    public boolean uploadApk(UploadInfo uploadInfo){
        String url = APK_UPLOAD_URL+uploadInfo.appKey;
        if (project.beta.expId!=null){
            url =APK_UPLOAD_URL2+uploadInfo.appKey;
        }
        println("Bugly: Apk start uploading....")
        if (uploadInfo.appId == null) {
            project.logger.error("Please set the app id, eg: appId = \"900037672\"")
            return false
        }

        if (uploadInfo.appKey == null) {
            project.logger.error("Please set app key, eg: appKey = \"bQvYLRrBNiqUctfi\"")
            return false
        }

        if (uploadInfo.secret == Constants.OPEN_FOR_PASSWORD) {
            if (uploadInfo.password == null) {
                project.logger.error("your apk download open for password, you must set the password")
                return false
            }
        } else if (uploadInfo.secret == Constants.OPEN_FOR_QQGROUP) {
            if (uploadInfo.users == null) {
                project.logger.error("your apk download open for qq group, you must set the users")
                return false
            }
        } else if (uploadInfo.secret == Constants.OPEN_FOR_WHITELIST) {
            if (uploadInfo.users == null) {
                project.logger.error("your apk download open for white list, you must set the users")
                return false
            }
        }
        println("Bugly:" + uploadInfo.toString())

        if (!post(url, uploadInfo.sourceFile, uploadInfo)) {
            project.logger.error("Bugly: Failed to upload!")
            return false
        } else {
            println("Bugly: upload apk success !!!")
            return true
        }

    }

    /**
     * 上传apk
     * @param url 地址
     * @param filePath 文件路径
     * @param uploadInfo 更新信息
     * @return
     */
    public boolean post(String url, String filePath, UploadInfo uploadInfo) {
        HttpURLConnectionUtil connectionUtil = new HttpURLConnectionUtil(url, Constants.HTTPMETHOD_POST);
        if (uploadInfo.expId != null) {
            connectionUtil.addTextParameter(Constants.EXP_ID, uploadInfo.expId);
            connectionUtil.setHttpMethod(Constants.HTTPMETHOD_PUT)
        } else {
            connectionUtil.addTextParameter(Constants.APP_ID, uploadInfo.appId);
        }
        connectionUtil.addTextParameter(Constants.PLATFORM_ID, uploadInfo.pid);
        connectionUtil.addTextParameter(Constants.TITLE, uploadInfo.title);
        connectionUtil.addTextParameter(Constants.DESCRIPTION, uploadInfo.description);
        connectionUtil.addTextParameter(Constants.SECRET, String.valueOf(uploadInfo.secret));
        connectionUtil.addTextParameter(Constants.USERS, uploadInfo.users);
        connectionUtil.addTextParameter(Constants.PASSWORD, uploadInfo.password);
        connectionUtil.addTextParameter(Constants.DOWNLOAD_LIMIT, String.valueOf(uploadInfo.download_limit));

        connectionUtil.addFileParameter(Constants.FILE, new File(filePath));

        String result = new String(connectionUtil.post(), "UTF-8");
        def data = new JsonSlurper().parseText(result)
        if (data.rtcode == 0) {
            println("Bugly --->share url: " + data.data.url)
            return true
        }
        return false;
    }
    private static class UploadInfo {
        // App ID of Bugly platform.
        public String appId = null
        // App Key of Bugly platform.
        public String appKey = null
        // platform id
        public String pid = "1"
        // Name of apk file to upload.
        public String sourceFile = null
        // app version title
        public String title = null
        // app version description [option]
        public String description = null
        // app secret level
        public int secret = 0
        // if open range was qq group set users to qq group num separate by ';' eg: 13244;23456;43843
        // if open range was qq num set users to qq num separate by ';' eg: 1000136; 10000148;1888432
        public String users = null
        // if open range was password you must set password
        public String password = null
        // download limit [option] default 10000
        public int download_limit = 10000
        // exp id
        public String expId = null


        @Override
        public String toString() {
            return "UploadInfo{" +
                    "appId='" + appId + '\'' +
                    ", appKey='" + appKey + '\'' +
                    ", pid='" + pid + '\'' +
                    ", apkFile='" + sourceFile + '\'' +
                    ", title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", secret=" + secret +
                    ", users='" + users + '\'' +
                    ", password='" + password + '\'' +
                    ", download_limit=" + download_limit +
                    ", expId='" + expId + '\'' +
                    '}';
        }
    }
}
