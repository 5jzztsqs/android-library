package com.android.library.base;

import android.content.Context;
import androidx.annotation.NonNull;
import com.qmuiteam.qmui.arch.QMUIFragment;

public abstract class BaseFragment extends QMUIFragment {

    protected Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        context = getActivity();
    }

}
