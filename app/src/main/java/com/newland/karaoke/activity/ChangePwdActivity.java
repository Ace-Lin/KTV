package com.newland.karaoke.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.newland.karaoke.KTVApplication;
import com.newland.karaoke.R;
import com.newland.karaoke.database.KTVUserLogin;

import org.litepal.LitePal;

public class ChangePwdActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView iv_change_psw_back;
    private Button btn_change_psw;
    private EditText et_psw_old;
    private EditText et_psw_new;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_change_pwd);
        initData();
        initUI();
        initEvent();
    }

    private void initUI(){
        iv_change_psw_back=(ImageView)findViewById(R.id.iv_change_psw_back);
        btn_change_psw=(Button)findViewById(R.id.btn_change_psw);
        et_psw_new=(EditText)findViewById(R.id.et_psw_new);
        et_psw_old=(EditText)findViewById(R.id.et_psw_old);


    }
    private void initData(){

    }
    private void initEvent(){
        iv_change_psw_back.setOnClickListener(this);
        btn_change_psw.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_change_psw:
                if(ChangePassword()){
                    startActivity(new Intent(ChangePwdActivity.this,MainActivity.class));
                    finish();
                }
                break;
            case R.id.iv_change_psw_back:
                startActivity(new Intent(ChangePwdActivity.this,MainActivity.class));
                finish();
                break;
            default:
                break;

        }
    }

    public boolean ChangePassword(){
        String pswOld=et_psw_old.getText().toString();
        String pswNew=et_psw_new.getText().toString();
        if(pswOld.length()<1||pswNew.length()<1){
            Toast.makeText(KTVApplication.getContext(),"Can't be empty!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pswNew==pswOld){
            Toast.makeText(KTVApplication.getContext(),
                    "The same to the old one.",Toast.LENGTH_SHORT).show();
            return false;
        }
        KTVUserLogin tempUser= LitePal.where("id=? and user_password=?",
                String.valueOf(KTVApplication.getCurrentUser().getUser_acount_id()),pswOld)
                .findFirst(KTVUserLogin.class,true);
        if(tempUser==null){
            Toast.makeText(KTVApplication.getContext(),
                    "Error old password!",Toast.LENGTH_SHORT).show();
            return false;
        }
        tempUser.setUser_password(pswNew);
        tempUser.save();
        return true;
    }
}
