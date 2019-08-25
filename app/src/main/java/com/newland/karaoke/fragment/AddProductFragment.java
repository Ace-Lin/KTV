package com.newland.karaoke.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.newland.karaoke.R;
import com.newland.karaoke.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 用来显示添加商品信息的Fragment
 */
public class AddProductFragment extends Fragment implements View.OnClickListener {

    private  Context context;
    private ImageView mFace;

    public AddProductFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        this.mFace = (ImageView)view.findViewById(R.id.iv_image);
        Button btnGallery = (Button)view.findViewById(R.id.gallery);
        Button btnCamera= (Button)view.findViewById(R.id.camera);
        btnCamera.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    //获取照片和存储照片
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    public static final int PERMISSION_REQUEST_CODE=4; //权限请求值

    private static final String PHOTO_FILE_NAME = "temp_photo.jpg"; // 头像名称
    private File tempFile;
    private File cameraSavePath;//拍照照片路径
    private Uri uri;//照片uri

    /*
     * 从相册获取
     */
    public void openGallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        this.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /*
     * 打开相机拍摄
     */
    private void openCamera()
    {
        if (hasPermission(context, new String(Manifest.permission.CAMERA))){
            // 执行拍照的逻辑
            camera();
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);

        }
    }

    /*
     * 打开相机
     */
    public void camera() {

        cameraSavePath = new File(context.getExternalFilesDir(null).getPath() + "/" + System.currentTimeMillis() + ".jpg");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //第二个参数为 包名.fileprovider
            uri = FileProvider.getUriForFile(context, "com.example.hxd.pictest.fileprovider", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }

    //检查权限是否已获取
    public static boolean hasPermission(@NonNull Context context, @NonNull String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
                || PermissionChecker.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }


    //权限请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //通过requestCode来识别是否同一个请求
        if (requestCode == PERMISSION_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //用户同意，执行操作
                //  initScan();
                Log.e("Jeffrey", "onRequestPermissionsResult: ");
                camera();
            }else{
                //如果没有授权的话，可以给用户一个友好提示
                Toast.makeText(context, "用户拒绝了拍照权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (hasSdcard()) {
                tempFile = new File(context.getExternalFilesDir(null), PHOTO_FILE_NAME);
                crop(uri);
            } else {
                //"未找到存储卡，无法存储照片！"
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            try {
                Bitmap bitmap = data.getParcelableExtra("data");
                this.mFace.setImageBitmap(bitmap);
                saveImage(bitmap);
                boolean delete = tempFile.delete();
                System.out.println("delete = " + delete);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
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

    //判断是否有Sd card
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 保存上传的照片
     * @param bitmap
     */
    private void saveImage(Bitmap bitmap)
    {
        File file=null;
        String dir = FileUtils.PICTURE_PATH;
        //int result = FileUtils.CreateFile(dir + "/test.txt");
        try {
            File folder = new File(dir);
            if(!folder.exists()){
                folder.mkdir();
            }
            file = new File(dir + "/summer1" + ".jpg");

            if(file.exists()){
                file.delete();
            }
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //showImage(file.getParent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.camera)
            openCamera();
        else if (view.getId()==R.id.gallery)
            openGallery();
    }
}
