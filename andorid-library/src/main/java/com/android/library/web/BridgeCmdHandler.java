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
    private static final String CMD_PICK_IMAGE = "pickImage";
    private static final String CMD_APP_INFO = "appInfo";
    private AppCompatActivity activity;

    public BridgeCmdHandler(AppCompatActivity activity){
        this.activity = activity;
    }


    public List<String> getBridgeCmdList(){
        List<String> cmdList = new ArrayList<>();
        cmdList.add(CMD_STATUES_BAR);
        cmdList.add(CMD_TITLE_BAR);
        cmdList.add(CMD_DEVICE_INFO);
        cmdList.add(CMD_PICK_IMAGE);
        return cmdList;
    }


    public void handleMessage(String message, QMUIWebViewBridgeHandler.MessageFinishCallback callback){
        JSONObject jsonObject = JSON.parseObject(message);
        if(jsonObject != null && jsonObject.containsKey("cmd")){
            String cmd = jsonObject.getString("cmd");
            if(CMD_STATUES_BAR.equals(cmd)){

            }else if(CMD_TITLE_BAR.equals(cmd)){

            }else if(CMD_DEVICE_INFO.equals(cmd)){

            }else if(CMD_PICK_IMAGE.equals(cmd)){

            }else{
                callback.finish(null);
            }
        }else{
            callback.finish(null);
        }
    }
}
