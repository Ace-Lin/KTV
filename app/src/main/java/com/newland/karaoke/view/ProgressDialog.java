package com.newland.karaoke.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.newland.karaoke.R;

public class ProgressDialog extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ProgressBarDialog);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().getWindow().setWindowAnimations(R.style.ProgressBarDialog);
        getDialog().getWindow().getAttributes().alpha =  0.7f;
        //实现点击外部不消失,
        getDialog().setCanceledOnTouchOutside(false);
        //禁用返回键
        getDialog().setOnKeyListener((dialogInterface, keyCode, keyEvent) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0)  return true;   else   return false;  });

        return inflater.inflate(R.layout.progressbar_connect, container);
    }
}
