package com.android.library.base;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class BaseEntity implements Serializable {

    public String toJSONString(){
        return JSON.toJSONString(this);
    }

}
