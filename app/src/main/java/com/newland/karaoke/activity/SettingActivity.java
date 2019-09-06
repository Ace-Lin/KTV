package com.newland.karaoke.activity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.newland.karaoke.R;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.fragment.AddProductFragment;
import com.newland.karaoke.fragment.AddRoomFragment;
import com.newland.karaoke.fragment.BaseFragment;
import com.newland.karaoke.fragment.ProductDetailsFragment;
import com.newland.karaoke.fragment.RoomDetailsFragment;
import com.newland.karaoke.fragment.SettingFragment;
import com.newland.karaoke.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

import static com.newland.karaoke.utils.ToastUtil.showShortText;

public class SettingActivity extends BaseActivity {

    private FragmentManager fManager;
    //是否是编辑内容页面标志位
    private boolean isEditContent;
    //因为是replace是所以添加fragment的类型，如果hide,show可以使用List<BaseFragment>
    private List<Integer> fragmentTypeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        hideStatusBar();
        fManager = getSupportFragmentManager();
        openFragment(KTVType.FragmentType.SETTING,false);


    }


    /**
     * 打开对应的fragment
     * @param fragment_type
     * @param isBack 是否是后退操作，用于判定动画方向
     */
    public void openFragment(int fragment_type,boolean isBack)
    {
        BaseFragment baseFragment = null;
        FragmentTransaction fTransaction = fManager.beginTransaction();

        if (isBack)
            fTransaction.setCustomAnimations(R.anim.slide_left_in,R.anim.slide_right_out);
        else
           fTransaction.setCustomAnimations(R.anim.slide_right_in,R.anim.slide_left_out);

        switch (fragment_type) {
            case KTVType.FragmentType.ROOMDETAIL:
                baseFragment = new RoomDetailsFragment(this);
                break;
            case KTVType.FragmentType.PRODUCTDETAIL:
                baseFragment = new ProductDetailsFragment(this);
                break;
            case KTVType.FragmentType.ADDROOM:
                baseFragment = new AddRoomFragment();
                isEditContent = true;
                break;
            case KTVType.FragmentType.ADDPRODUCT:
                baseFragment = new AddProductFragment(this);
                isEditContent = true;
                break;
            case KTVType.FragmentType.SETTING:
                baseFragment = new SettingFragment();
                break;
            default:
        }

        fragmentTypeList.add(fragment_type);
        fTransaction.replace(R.id.setting_content, baseFragment);
        fTransaction.commit();
    }


    /**
     * 进入编辑页面
     * @param fragment_type
     * @param updateId
     */
    public void openUpdateFragment(int fragment_type , int updateId){
        BaseFragment baseFragment = null;
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.setCustomAnimations(R.anim.slide_right_in,R.anim.slide_left_out);

        switch (fragment_type) {
            case KTVType.FragmentType.ADDROOM:
                baseFragment = new AddRoomFragment(updateId);
                break;
            case KTVType.FragmentType.ADDPRODUCT:
                baseFragment = new AddProductFragment(this,updateId);
                break;
            default:
        }

        isEditContent = true;
        fragmentTypeList.add(fragment_type);
        fTransaction.replace(R.id.setting_content, baseFragment);
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
            if (fragmentTypeList.size() > 1) {
                int temp = fragmentTypeList.get(fragmentTypeList.size()-2);
                //连续去除集合后两位
                fragmentTypeList.remove(fragmentTypeList.size()-1);
                fragmentTypeList.remove(fragmentTypeList.size()-1);
                openFragment(temp,true);
            } else
                finish();
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
