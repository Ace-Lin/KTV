package com.newland.karaoke.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.newland.karaoke.R;
import com.newland.karaoke.constant.KTVType;

import static com.newland.karaoke.utils.DensityUtil.df_two;

public class PayDialog extends DialogFragment implements View.OnClickListener {

    private double amount;
    private NoticeDialogListener listener;

    public PayDialog(double amount, NoticeDialogListener listener) {
        this.amount = amount;
        this.listener = listener;
    }


    public interface NoticeDialogListener {
         void onDialogBtnClick(int payType);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.PayDialog);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

                getDialog().getWindow().setWindowAnimations(R.style.PayDialog);
                View view = inflater.inflate(R.layout.dialog_pay, container);

                TextView textView = view.findViewById(R.id.txt_pay_amount);
                ImageButton btn_cash = view.findViewById(R.id.btn_pay_cash);
                ImageButton btn_bank_card = view.findViewById(R.id.btn_pay_bank_card);
                ImageButton btn_scan_code = view.findViewById(R.id.btn_pay_scan_code);
                textView.setText(getString(R.string.dollar) + df_two.format(amount));
                btn_cash.setOnClickListener(this);
                btn_bank_card.setOnClickListener(this);
                btn_scan_code.setOnClickListener(this);

            return view;
    }


    @Override
    public void onClick(View view) {
           switch (view.getId()){
               case R.id.btn_pay_cash:
                   listener.onDialogBtnClick(KTVType.PayType.CASH);
                   break;
               case R.id.btn_pay_bank_card:
                   listener.onDialogBtnClick(KTVType.PayType.CARD);
                   break;
               case R.id.btn_pay_scan_code:
                   listener.onDialogBtnClick(KTVType.PayType.QRCODE);
                   break;
           }
    }

}
