package com.newland.karaoke.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.newland.karaoke.KTVApplication;
import com.newland.karaoke.R;
import com.newland.karaoke.UI.SelectDialog;
import com.newland.karaoke.constant.Const;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.model.UserModel;
import com.newland.karaoke.utils.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView mHeadImage;
    private TextView tv_person_name;
    private TextView tv_person_idcard;
    private TextView tv_person_phone;
    private TextView tv_person_email;
    private Button btn_person_back;
    private NiftyDialogBuilder dialogBuilder;

    //获取照片和存储照片
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    public static final int PERMISSION_REQUEST_PHOTO_CODE=4; //权限请求值
    public static final int PERMISSION_REQUEST_STORAGE_CODE=5; //权限请求值
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg"; // 头像名称


    private String pathPicture;//图片路径
    private Uri imgUri;
    private Handler personHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_person_info);
        initUI();
        initData();
        initEvent();
    }

    private void initData() {
        //设置个人信息
        ShowPersonInfo();
        personHandler=KTVApplication.getmHandler();

    }

    private void ShowPersonInfo() {
        if (KTVApplication.getCurrentUser() == null) {
            return;
        }
        UserModel user = KTVApplication.getCurrentUser();
        String temp = user.getUser_name();
        tv_person_name.setText(temp!=null ? temp :"No Info" );
        temp = user.getIdentity_card_no();
        tv_person_idcard.setText(temp!=null ? temp :"No Info");
        temp = user.getMobile_phone();
        tv_person_phone.setText(temp!=null ? temp :"No Info");
        temp = user.getUser_email();
        tv_person_email.setText(temp!=null ? temp :"No Info");


    }

    private void initUI() {
        tv_person_name = (TextView) findViewById(R.id.tv_person_name);
        tv_person_idcard = (TextView) findViewById(R.id.tv_person_idcard);
        tv_person_phone = (TextView) findViewById(R.id.tv_person_phone);
        tv_person_email = (TextView) findViewById(R.id.tv_person_email);
        btn_person_back = (Button) findViewById(R.id.btn_person_back);

        mHeadImage=(CircleImageView)findViewById(R.id.ci_person_photo);
        Picasso.get().load(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .into(mHeadImage);
        SetPhotoByUser();

    }

    private void initEvent() {
        tv_person_name.setOnClickListener(this);
        tv_person_idcard.setOnClickListener(this);
        tv_person_phone.setOnClickListener(this);
        tv_person_email.setOnClickListener(this);
        btn_person_back.setOnClickListener(this);
        mHeadImage.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_person_back:
                startActivity(new Intent(PersonInfoActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.tv_person_idcard:
                GetInputEdit("Set IDCard", KTVType.DlgType.DLG_IDCARD);
                break;
            case R.id.tv_person_phone:
                GetInputEdit("Set Phone",KTVType.DlgType.DLG_PHONE);
                break;
            case R.id.tv_person_email:
                GetInputEdit("Set Email",KTVType.DlgType.DLG_EMAIL);
                break;
            case R.id.tv_person_name:
                GetInputEdit("Set NickName",KTVType.DlgType.DLG_NAME);
                break;
            case R.id.ci_person_photo:
                selectPhoto();
                break;
        }
    }

    /**
     * 弹出选择图片或者拍照
     */
    private void selectPhoto() {
        List<String> list = new ArrayList<>();
        list.add("拍照");
        list.add("相册");
        showDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //getIconFromCamera();
                        openCamera(PersonInfoActivity.this);
                        break;
                    case 1:
                        getIconFromPhoto(); // 从系统相册获取
                        break;
                    default:
                        break;
                }
            }
        },list);

    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> list){
        SelectDialog dialog = new SelectDialog(this,
                R.style.transparentFrameWindowStyle,listener,list);
        dialog.show();
        return dialog;
    }

    //判断是否有Sd card
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    //检查权限是否已获取
    public static boolean hasPermission(@NonNull Context context, @NonNull String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
                || PermissionChecker.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    /*
     * 从相册获取
     */
    public void getIconFromPhoto() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }
    private void getIconFromCamera() {
        File file=new File(getExternalFilesDir(null).getPath() + "/" + System.currentTimeMillis() + ".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            imgUri = Uri.fromFile(file);
        }else{

            imgUri = FileProvider.getUriForFile(this, Const.FILE_PROVIDER_AUTHORITY,file);

        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }

    /*
     * 打开相机拍摄
     */
    private void openCamera(Context context)
    {
        if (hasPermission(context, new String(Manifest.permission.CAMERA))){
            // 执行拍照的逻辑
            getIconFromCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_PHOTO_CODE);

        }
    }
    //权限请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //通过requestCode来识别是否同一个请求
        if (requestCode == PERMISSION_REQUEST_PHOTO_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //用户同意，执行操作
                Log.e("Photo", "onRequestPermissionsResult: ");
                //camera();
            }else{
                //如果没有授权的话，可以给用户一个友好提示
                Toast.makeText(PersonInfoActivity.this, "User Refused! ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径
                imgUri = data.getData();
                crop(imgUri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (hasSdcard()) {
                crop(imgUri);
            } else {
                //"未找到存储卡，无法存储照片！"
            }

        }
        else if (requestCode == PHOTO_REQUEST_CUT) {
            try {
                Bitmap bitmap = data.getParcelableExtra("data");
                setCropImg(data);
                saveImage(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private void setCropImg(Intent picData){
        Bundle bundle = picData.getExtras();
        if (bundle != null){
            Bitmap mBitmap = bundle.getParcelable("data");
            mHeadImage.setImageBitmap(mBitmap);
        }
    }

    /**
     * 剪切图片
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1，
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        //华为手机需要这样设置不然为圆形图片
        //intent.putExtra("aspectX", 1);
        // intent.putExtra("aspcircleCrop ectY", 1);

        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        //在7.0系统上还需要添加intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);这样的一句话，
        // 表示对目标应用临时授权该Uri所代表的文件，否则会报无法加载此图片的错误。
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /**
     * 保存上传的照片
     * @param bitmap
     */
    private void saveImage(Bitmap bitmap)
    {
        File file=null;
        String dir = FileUtils.PICTURE_PATH;

        try {
            File folder = new File(dir);
            if(!folder.exists()){
                folder.mkdir();
            }
            UUID uuid = UUID.randomUUID();//生成随机路径名字
            file = new File(dir + "/" +uuid.toString()+ ".jpg");

            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pathPicture = file.getAbsolutePath();//获取图片保存路径
        if(KTVApplication.isLogin()){
            KTVApplication.getCurrentUser().setUser_photo(pathPicture);
            KTVApplication.UpdateUserInfo();
        }


    }
    private PersonInfoActivity getWeakContext() {
        WeakReference<PersonInfoActivity> weakReference = new WeakReference<>(PersonInfoActivity.this);
        return weakReference.get();
    }

    private void GetInputEdit(String title,int tag) {
        View view = LayoutInflater.from(getWeakContext()).inflate(R.layout.view_dialog_edit, null);
        final EditText mEditText = (EditText) view.findViewById(R.id.view_dialog_edit);
        final int tagCopy=tag;
        dialogBuilder = NiftyDialogBuilder.getInstance(getWeakContext());
        dialogBuilder.withTitle(title)                                  //.withTitle(null)  no title
                .withMessage(null)
                .withDialogColor(getResources().getColor(R.color.main_bg_color))           //def  | withDialogColor(int resid)                               //def
                .withIcon(getResources().getDrawable(R.mipmap.karaoke))
                .withEffect(Effectstype.RotateBottom )                                         //def Effectstype.Slidetop
                .withButton1Text("Confirm")                                      //def gone
                .withButton2Text("Cancel")                                  //def gone
                .setCustomView(view, PersonInfoActivity.this)         //.setCustomView(View or ResId,context)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String edt_value= mEditText.getText().toString().trim();
                        if(edt_value==null||edt_value.length()<1){
                            Toast.makeText(PersonInfoActivity.this,"No input!",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        UpdateUserAndView(edt_value,tagCopy);
                        dialogBuilder.dismiss();
                        dialogBuilder = null;
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        dialogBuilder = null;
                    }
                }).show();


    }

    private void SetPhotoByUser(){
        if(KTVApplication.getCurrentUser()==null){
            Picasso.get().load(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .into(mHeadImage);
            return;
        }
        String url=KTVApplication.getCurrentUser().getUser_photo();
        if(url==null||url.length()<1){
            return;
        }

        Picasso.get().load("file://"+url)
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .into(mHeadImage);
    }

    private void UpdateUserAndView(String value,int tag){
        UserModel user=KTVApplication.getCurrentUser();
        switch(tag){
            case KTVType.DlgType.DLG_NAME:
                tv_person_name.setText(value);
                user.setUser_name(value);
                KTVApplication.UpdateUserInfo();
                break;
            case KTVType.DlgType.DLG_PHONE:
                tv_person_phone.setText(value);
                user.setMobile_phone(value);
                KTVApplication.UpdateUserInfo();
                break;
            case KTVType.DlgType.DLG_IDCARD:
                tv_person_idcard.setText(value);
                user.setIdentity_card_no(value);
                KTVApplication.UpdateUserInfo();
                break;
            case KTVType.DlgType.DLG_EMAIL:
                tv_person_email.setText(value);
                user.setUser_email(value);
                KTVApplication.UpdateUserInfo();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Message msg=new Message();
        msg.what= KTVType.MsgType.UPDATE_IMG;
        personHandler.sendMessage(msg);
    }
}
