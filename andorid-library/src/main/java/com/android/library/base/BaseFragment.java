package com.android.library.base;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.qmuiteam.qmui.arch.QMUIFragment;

public abstract class BaseFragment extends QMUIFragment {

    protected FragmentActivity activity;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

}
