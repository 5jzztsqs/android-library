package com.android.library.base;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import com.qmuiteam.qmui.arch.QMUIFragment;

public abstract class BaseFragment extends QMUIFragment {

    protected Activity activity;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

}
