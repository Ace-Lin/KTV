package com.newland.karaoke.activity;

import android.app.AlertDialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.newland.karaoke.R;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.fragment.AddProductFragment;
import com.newland.karaoke.fragment.AddRoomFragment;
import com.newland.karaoke.fragment.ProductDetailsFragment;
import com.newland.karaoke.fragment.RoomDetailsFragment;
import com.newland.karaoke.fragment.SettingFragment;
import com.newland.karaoke.utils.FileUtils;

import static com.newland.karaoke.utils.ToastUtil.showShortText;

public class SettingActivity extends BaseActivity {

    private FragmentManager fManager;
    private boolean isEditContent;//是否是编辑内容页面标志位
    private SettingFragment settingFragment;
    private RoomDetailsFragment roomDetailsFragment;
    private ProductDetailsFragment productDetailsFragment;
    private AddRoomFragment addRoomFragment;
    private AddProductFragment addProductFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        FileUtils.createDir(getExternalFilesDir("/Picture").getPath());
        initFragment();
    }

    /**
     * 初始化加载fragment页面
     */
    private void initFragment(){
        fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        settingFragment = new SettingFragment();
        fTransaction.add(R.id.setting_content, settingFragment);
        fTransaction.commit();
    }


    /**
     * 打开对应的fragment
     * @param fragment_type
     */
    public void openFragment(int fragment_type)
    {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.setCustomAnimations(R.anim.slide_right_in,R.anim.slide_left_out,R.anim.slide_left_in,R.anim.slide_right_out);

        switch (fragment_type) {
            case KTVType.FragmentType.ROOMDETAIL:
               if (roomDetailsFragment == null)
                   roomDetailsFragment = new RoomDetailsFragment(this);

                 fTransaction.replace(R.id.setting_content, roomDetailsFragment);
                break;
            case KTVType.FragmentType.PRODUCTDETAIL:
                if (productDetailsFragment == null)
                    productDetailsFragment = new ProductDetailsFragment(this);

                fTransaction.replace(R.id.setting_content, productDetailsFragment);
                break;
            case KTVType.FragmentType.ADDROOM:
                if (addRoomFragment == null)
                    addRoomFragment = new AddRoomFragment();

                fTransaction.replace(R.id.setting_content, addRoomFragment);
                isEditContent = true;
                break;
            case KTVType.FragmentType.ADDPRODUCT:
                if (addProductFragment == null)
                    addProductFragment = new AddProductFragment(this);

                fTransaction.replace(R.id.setting_content, addProductFragment);
                isEditContent = true;
                break;
            default:
        }

        fTransaction.addToBackStack(null);
        fTransaction.commit();
    }


    /**
     * 进入编辑页面
     * @param fragment_type
     * @param updateId
     */
    public void openUpdateFragment(int fragment_type , int updateId){
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.setCustomAnimations(R.anim.slide_right_in,R.anim.slide_left_out,R.anim.slide_left_in,R.anim.slide_right_out);
        switch (fragment_type) {
            case KTVType.FragmentType.ADDROOM:
                if (addRoomFragment == null)
                    addRoomFragment = new AddRoomFragment();

                addRoomFragment.updateRoom(updateId);
                fTransaction.replace(R.id.setting_content,addRoomFragment);
                break;
            case KTVType.FragmentType.ADDPRODUCT:
                if (addProductFragment == null)
                    addProductFragment = new AddProductFragment(this);

                addProductFragment.updateProduct(updateId);
                fTransaction.replace(R.id.setting_content,addProductFragment);
                break;
            default:
        }

        isEditContent = true;
        fTransaction.addToBackStack(null);
        fTransaction.commit();
    }

    /**
     * 添加编辑数据存储的反馈
     * @param isSuccess
     */
    public void editDataCallBack(boolean isSuccess){
        showShortText(this,getString(R.string.tips_add_success));
        isEditContent = !isSuccess;
        basefinish();
    }


    @Override
    public void basefinish() {
        if(isEditContent)
            showBackDialog();
        else {
            if (getSupportFragmentManager().getBackStackEntryCount() <= 0)//这里是取出我们返回栈存在Fragment的个数
                finish();
            else
                getSupportFragmentManager().popBackStack();
       }
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
        dialogText.setText(getString(R.string.dialog_title_edit));
        dialogBtnConfirm .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditContent=false;
                basefinish();
                dialog.dismiss();
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
