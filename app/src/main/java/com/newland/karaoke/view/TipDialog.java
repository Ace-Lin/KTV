package com.newland.karaoke.view;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.newland.karaoke.R;

public class TipDialog extends DialogFragment {

    private String title;
    private String info;
    private boolean isError;

    public TipDialog(String title, String info, boolean isError) {
        this.title = title;
        this.info = info;
        this.isError = isError;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.PayDialog);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().getWindow().setWindowAnimations(R.style.PayDialog);

        View view = inflater.inflate(R.layout.dialog_tip, container);

        TextView  txt_title = view.findViewById(R.id.txt_title);
        TextView  txt_info = view.findViewById(R.id.txt_info);

        if (isError) {
            txt_title.setTextColor(getResources().getColor(R.color.colorRed));
            txt_info.setTextColor(getResources().getColor(R.color.colorRed));
        }

        txt_title.setText(title);
        txt_info.setText(info);
        return view;
    }
}
