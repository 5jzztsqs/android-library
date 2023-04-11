package com.android.library.web;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qmuiteam.qmui.widget.webview.QMUIWebViewBridgeHandler;

import java.util.ArrayList;
import java.util.List;

public class BridgeCmdHandler {
    private static final String CMD_STATUES_BAR = "statusBar";
    private static final String CMD_TITLE_BAR = "titleBar";
    private static final String CMD_DEVICE_INFO = "deviceInfo";
    private static final String CMD_APP_INFO = "appInfo";
    private static final String CMD_NEW_WINDOW = "newWindow";
    private AppCompatActivity activity;

    public BridgeCmdHandler(AppCompatActivity activity){
        this.activity = activity;
    }


    public List<String> getBridgeCmdList(){
        List<String> cmdList = new ArrayList<>();
        cmdList.add(CMD_STATUES_BAR);
        cmdList.add(CMD_TITLE_BAR);
        cmdList.add(CMD_DEVICE_INFO);
        cmdList.add(CMD_NEW_WINDOW);
        return cmdList;
    }


    public void handleMessage(String message, QMUIWebViewBridgeHandler.MessageFinishCallback callback){
        JSONObject jsonObject = JSON.parseObject(message);
        if(jsonObject != null && jsonObject.containsKey("cmd")){
            String cmd = jsonObject.getString("cmd");
            if(CMD_STATUES_BAR.equals(cmd)){
                handlerStateBar(jsonObject,callback);
            }else if(CMD_TITLE_BAR.equals(cmd)){
                handlerTitleBar(jsonObject,callback);
            }else if(CMD_DEVICE_INFO.equals(cmd)){
                handlerDeviceInfo(jsonObject,callback);
            }else if(CMD_NEW_WINDOW.equals(cmd)){
                handlerNewWindow(jsonObject,callback);
            }else{
                callback.finish(null);
            }
        }else{
            callback.finish(null);
        }
    }


    private void handlerStateBar(JSONObject jsonObject,QMUIWebViewBridgeHandler.MessageFinishCallback callback){

    }


    private void handlerTitleBar(JSONObject jsonObject,QMUIWebViewBridgeHandler.MessageFinishCallback callback){

    }

    private void handlerDeviceInfo(JSONObject jsonObject,QMUIWebViewBridgeHandler.MessageFinishCallback callback){

    }

    private void handlerNewWindow(JSONObject jsonObject,QMUIWebViewBridgeHandler.MessageFinishCallback callback){
        DefaultWebViewActivity.start(activity,JSON.toJSONString(jsonObject));
    }
}
