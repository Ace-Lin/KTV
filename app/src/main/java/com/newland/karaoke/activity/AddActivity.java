package com.newland.karaoke.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.newland.karaoke.R;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.fragment.AddProductFragment;
import com.newland.karaoke.fragment.AddRoomFragment;

/**
 * 作为添加商品和房间信息的fragmen的载体
 */
public class AddActivity extends BaseActivity {

    private int fragment_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initBaseView(R.id.setting_toolbar);


        intiFragment();
    }

    /**
     * 初始化选择是商品还是房间的fragment
     */
    private  void intiFragment(){

        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();

        AddRoomFragment addRoomFragment=new AddRoomFragment(this);
        AddProductFragment addProductFragment=new AddProductFragment(this);

        fragment_type = getIntent().getIntExtra(getString(R.string.fragment_type),0);

        if (fragment_type == KTVType.FragmentType.ADDROOM) {
            setToolBarTitle(getString(R.string.setting_AddRoom));
            fTransaction.replace(R.id.add_content,addRoomFragment);
        }
        else if(fragment_type == KTVType.FragmentType.ADDPRODUCT){
            setToolBarTitle(getString(R.string.setting_AddProduct));
            fTransaction.replace(R.id.add_content,addProductFragment);
        }
        else if(fragment_type == KTVType.FragmentType.EDITROOM){
            setToolBarTitle(getString(R.string.setting_AddRoom));
            addRoomFragment.updateRoom(getIntent().getIntExtra(getString(R.string.edit_detail_id),0));
            fTransaction.replace(R.id.add_content,addRoomFragment);
        }
        else if(fragment_type == KTVType.FragmentType.EDITRODUCT) {
            setToolBarTitle(getString(R.string.setting_AddProduct));
            addProductFragment.updateProduct(getIntent().getIntExtra(getString(R.string.edit_detail_id),0));
            fTransaction.replace(R.id.add_content, addProductFragment);
        }

        fTransaction.commit();
    }


    @Override
    public void basefinish() {
        showBackDialog();
    }


    /**
     * 增加添加信息退出警告信息
     */
    private void showBackDialog() {
        final AlertDialog.Builder layoutDialog = new AlertDialog.Builder(this);
        final AlertDialog dialog = layoutDialog.create();
        dialog.show();

        //加载布局并初始化组件
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_alert_layout,null);
        TextView dialogText = (TextView) ((View) dialogView).findViewById(R.id.alert_dialog_text);
        Button dialogBtnConfirm = (Button) dialogView.findViewById(R.id.alert_dialog_btn_confirm);
        Button dialogBtnCancel = (Button) dialogView.findViewById(R.id.alert_dialog_btn_cancel);

        //设置组件
        dialogText.setText(getString(R.string.dialog_title_add));
        dialogBtnConfirm .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialogBtnCancel .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //在布局文件中设置了背景为圆角的shape后，发现上边显示的是我们的自定义的圆角的布局文件，
        //底下居然还包含了一个方形的白块，如何去掉这个白块,添加下面这句话
        dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
        dialog.getWindow().setContentView(dialogView);//自定义布局应该在这里添加，要在dialog.show()的后面
    }


}
