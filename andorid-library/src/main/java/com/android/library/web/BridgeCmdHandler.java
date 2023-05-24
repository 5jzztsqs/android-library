package com.android.library.web;

import android.net.Uri;
import android.os.Build;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.library.glide.GlideEngine;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.webview.QMUIWebViewBridgeHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BridgeCmdHandler {
    private static final String CMD_STATUES_BAR = "statusBar";
    private static final String CMD_TITLE_BAR = "titleBar";
    private static final String CMD_DEVICE_INFO = "deviceInfo";
    private static final String CMD_WINDOW_INFO = "windowInfo";
    private static final String CMD_PICK_FILE = "pickFile";
    private static final String CMD_APP_INFO = "appInfo";
    private static final String CMD_NEW_WINDOW = "newWindow";
    private static String V_CONSOLE;
    private AppCompatActivity activity;
    public BridgeCmdHandler(AppCompatActivity activity) {
        this.activity = activity;
        this.V_CONSOLE = ResourceUtils.readAssets2String("vconsole.min.js");
    }


    public List<String> getBridgeCmdList() {
        List<String> cmdList = new ArrayList<>();
        cmdList.add(CMD_STATUES_BAR);
        cmdList.add(CMD_TITLE_BAR);
        cmdList.add(CMD_DEVICE_INFO);
        cmdList.add(CMD_NEW_WINDOW);
        return cmdList;
    }


    public void handleMessage(String message, QMUIWebViewBridgeHandler.MessageFinishCallback callback) {
        JSONObject jsonObject = JSON.parseObject(message);
        if (jsonObject != null && jsonObject.containsKey("cmd")) {
            String cmd = jsonObject.getString("cmd");
            if (CMD_STATUES_BAR.equals(cmd)) {
                handlerStateBar(jsonObject, callback);
            } else if (CMD_TITLE_BAR.equals(cmd)) {
                handlerTitleBar(jsonObject, callback);
            } else if (CMD_DEVICE_INFO.equals(cmd)) {
                handlerDeviceInfo(jsonObject, callback);
            } else if (CMD_NEW_WINDOW.equals(cmd)) {
                handlerNewWindow(jsonObject, callback);
            } else if (CMD_WINDOW_INFO.equals(cmd)) {
                handlerWindowInfo(jsonObject, callback);
            } else if (CMD_PICK_FILE.equals(cmd)) {
                handlerPickFile(jsonObject, callback);
            } else {
                callback.finish(null);
            }
        } else {
            callback.finish(null);
        }
    }


    private void handlerStateBar(JSONObject jsonObject, QMUIWebViewBridgeHandler.MessageFinishCallback callback) {

    }


    private void handlerTitleBar(JSONObject jsonObject, QMUIWebViewBridgeHandler.MessageFinishCallback callback) {

    }

    private void handlerDeviceInfo(JSONObject jsonObject, QMUIWebViewBridgeHandler.MessageFinishCallback callback) {

    }

    private void handlerWindowInfo(JSONObject jsonObject, QMUIWebViewBridgeHandler.MessageFinishCallback callback) {
        callback.finish(getWindowInfo(activity));
    }

    private void handlerNewWindow(JSONObject jsonObject, QMUIWebViewBridgeHandler.MessageFinishCallback callback) {
        BridgeWebViewActivity.start(activity, JSON.toJSONString(jsonObject));
    }

    private void handlerPickFile(JSONObject jsonObject, QMUIWebViewBridgeHandler.MessageFinishCallback callback) {
        PictureSelector.create(activity)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.createGlideEngine())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        if (result == null || result.size() == 0) {
                            callback.finish(null);
                        } else {
                            List<String> paths = new ArrayList<>();
                            for(LocalMedia localMedia:result){
                                paths.add(localMedia.getPath());
                            }
                            callback.finish(JSON.toJSONString(paths));
//                            String path = result.get(0).getRealPath();
//                            File file = new File(path);
//                            Uri uri;
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".FileProvider", file);
//                            } else {
//                                uri = Uri.fromFile(file);
//                            }
//                            for (int i = 0; i < result.size(); i++) {
//                                String path = result.get(i).getRealPath();
//                                File file = new File(path);
//                                WebLog.i("provider:" + activity.getPackageName() + ".FileProvider");
//                                Uri uri;
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                    uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".FileProvider", file);
//                                } else {
//                                    uri = Uri.fromFile(file);
//                                }
//                                WebLog.i(path+":"+uri.getPath());
//                                uris.add(uri.toString());
//                            }
//                            try {
//                                Bitmap bitmap = ImageUtils.getBitmap(file);
//                               String base64 = EncodeUtils.base64Encode2String(ImageUtils.bitmap2Bytes(bitmap));
//
//                                org.json.JSONObject metadata = new org.json.JSONObject();
//                                BasicFileAttributes attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
//                                metadata.put("size", file.length());
//                                metadata.put("type", MimeTypeMap.getSingleton().getMimeTypeFromExtension(""));
//                                metadata.put("name", file.getName());
//                                metadata.put("data", base64);
//                                metadata.put("fullPath", "file://localhost"+file.getAbsolutePath());
//                                metadata.put("lastModifiedDate", LocalDateTime.ofInstant(attributes.lastModifiedTime().toInstant(), ZoneId.systemDefault()).toInstant(ZoneOffset.of("+8")).toEpochMilli());
//                                callback.finish(metadata);
//                            } catch (Exception e) {
//                                callback.finish(null);
//                            }

                        }
                    }

                    @Override
                    public void onCancel() {

                        callback.finish(null);
                    }
                });
    }

    public static JSONObject getWindowInfo(AppCompatActivity activity) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("statusBarHeight", QMUIStatusBarHelper.getStatusbarHeight(activity));
        jsonObject.put("isFullScreen", QMUIStatusBarHelper.isFullScreen(activity));
        jsonObject.put("screenWidth", QMUIDisplayHelper.getScreenWidth(activity));
        jsonObject.put("screenHeight", QMUIDisplayHelper.getScreenHeight(activity));
        return jsonObject;
    }

    public static void injectJS(AppCompatActivity activity, WebView webView) {
        String windowInfoJSON = "'" + getWindowInfo(activity).toJSONString() + "'";
        WebLog.json(windowInfoJSON);
        String jsCode = "localStorage.setItem(\"windowInfo\", " + windowInfoJSON + ");new VConsole();";
        executeJS(webView, jsCode);
    }


    public static void executeJS(WebView webView, String js) {
        String injectJS = "(function(){" + js + "})()";
        WebLog.i(injectJS);
        webView.evaluateJavascript(injectJS, null);
    }
}
