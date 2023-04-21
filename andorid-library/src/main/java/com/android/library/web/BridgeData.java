package com.android.library.web;


import com.blankj.utilcode.util.ResourceUtils;

public class BridgeData {
    private static BridgeData instance;
    private String vConsole;
    public static BridgeData getInstance() {
        if(instance == null){
            synchronized (BridgeData.class) {
                if(instance == null){
                    instance = new BridgeData();
                }
            }
        }
        return instance;
    }

    private void loadResource(){
        vConsole = ResourceUtils.readAssets2String("vconsole.min.js");
    }
    public static void init(){
        BridgeData.getInstance().loadResource();
    }

    public String getVConsole() {
        return vConsole;
    }

}
