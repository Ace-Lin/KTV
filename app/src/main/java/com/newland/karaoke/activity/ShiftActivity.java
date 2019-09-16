package com.newland.karaoke.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.newland.karaoke.KTVApplication;
import com.newland.karaoke.R;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.database.KTVUserInfo;
import com.newland.karaoke.database.KTVUserLogin;

import org.litepal.LitePal;

public class ShiftActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_login_back;
    private Button btn_login;
    private EditText et_username;
    private EditText et_password;
    private LinearLayout linear_layout_btn_register;
    private TextView btn_login_find_pwd;
    private String username;
    private String password;
    private Handler shiftHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shift);
        initData();
        initUI();
        initEvent();

    }

    private void initUI(){
        iv_login_back=(ImageView) findViewById(R.id.iv_login_back);
        btn_login=(Button) findViewById(R.id.btn_login);
        et_username=(EditText) findViewById(R.id.et_username);
        et_password=(EditText) findViewById(R.id.et_pwd);
        linear_layout_btn_register=(LinearLayout)findViewById(R.id.linear_layout_btn_register);
        btn_login_find_pwd=(TextView) findViewById(R.id.btn_login_find_pwd);
    }
    private void initData(){

    }

    private void initEvent(){
        iv_login_back.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        linear_layout_btn_register.setOnClickListener(this);
        btn_login_find_pwd.setOnClickListener(this);

        shiftHandler=KTVApplication.getmHandler();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_login_back:
                startActivity(new Intent(ShiftActivity.this,MainActivity.class));
                finish();
                break;
            case R.id.btn_login:
                LoginConfirm();
                break;
            case R.id.linear_layout_btn_register:
                startActivity(new Intent(ShiftActivity.this,RegisterActivity.class));
                //finish();
                break;
            case R.id.btn_login_find_pwd:
                startActivity(new Intent(ShiftActivity.this,ChangePwdActivity.class));
                //finish();
                break;
            default:
                break;
        }
    }
    private void LoginConfirm(){
        username=et_username.getText().toString();
        password=et_password.getText().toString();
        if(username.length()<1||password.length()<1){
            Toast.makeText(KTVApplication.getContext(),"账号或者密码不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        //用户认证

        KTVUserLogin user= LitePal.where("user_account=? and user_password=?",username,password)
                            .findFirst(KTVUserLogin.class,true);
        if(user==null){
            Toast.makeText(this,"Username Or Password Error!",Toast.LENGTH_SHORT).show();
            return;
        }
        KTVUserInfo userInfo=user.getUser_info();
        KTVApplication.setCurrentUserByUser(userInfo,user.getId());
        KTVApplication.setIsLogin(true);
        Message msg=new Message();
        msg.what= KTVType.MsgType.UPDATE_IMG;
        KTVApplication.setIsLogin(true);
        shiftHandler.sendMessage(msg);
        startActivity(new Intent(ShiftActivity.this,MainActivity.class));
        finish();
    }
}
