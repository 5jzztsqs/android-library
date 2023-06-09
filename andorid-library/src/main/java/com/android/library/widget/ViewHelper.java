package com.android.library.widget;

import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class ViewHelper {


    public static FrameLayout.LayoutParams generateMatchParentFrameLayoutLayoutParams(){
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
    }

    public static FrameLayout.LayoutParams generateMatchParentWidthWrapContentHeightFrameLayoutLayoutParams(){
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    public static FrameLayout.LayoutParams generateWrapContentFrameLayoutLayoutParams(){
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
    }


    public static LinearLayout.LayoutParams generateMatchParentLinearLayoutLayoutParams(){
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
    }

    public static LinearLayout.LayoutParams generateMatchParentWidthWrapContentHeightLinearLayoutParams(){
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public static LinearLayout.LayoutParams generateWrapContentLinearLayoutLayoutParams(){
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    }
}
