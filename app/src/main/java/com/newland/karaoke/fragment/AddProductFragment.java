package com.newland.karaoke.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.PermissionChecker;

import com.newland.karaoke.R;
import com.newland.karaoke.activity.SettingActivity;
import com.newland.karaoke.database.KTVProduct;
import com.newland.karaoke.utils.FileUtils;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import static com.newland.karaoke.utils.DensityUtil.dp2px;
import static com.newland.karaoke.utils.ToastUtil.showShortText;
import static com.newland.karaoke.utils.Utility.getPirBitMap;

/**
 * 用来显示添加商品信息的Fragment
 */
public class AddProductFragment extends BaseFragment implements View.OnClickListener {

    private  Context context;

    private Dialog bottomDialog;
    private TextView txt_productName;
    private TextView txt_productCount;
    private TextView txt_productPrice;
    private ImageView productPic;//展示图片
    private Button btn_save;
    private String pathPicture;//获取图片路径
    private  boolean isHave; //是否获取了图片
    private  boolean isUpdate;//判断是否是更新数据
    private  int productId;//需要更改的商品id
    private  KTVProduct ktvProduct;//需要更新的room
    
    public AddProductFragment(Context context) {
        this.context = context;
    }

    /**
     * 获取需要修改的商品id
     * @param productId id
     */
    public  void updateProduct(int productId)
    {
        isUpdate = true;
        isHave=true;//修改代表已经添加了照片
        this.productId = productId;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        initToolbar(view,getString(R.string.setting_AddProduct));
        initUI(view);
        return view;
    }

    /**
     * 初始化UI信息
     */
    private void initUI(View view)
    {
        productPic = (ImageView)view.findViewById(R.id.add_product_picture);
        productPic.setOnClickListener(this);
        txt_productName = (TextView) view.findViewById(R.id.add_product_name);
        txt_productCount = (TextView) view.findViewById(R.id.add_product_count);
        txt_productPrice = (TextView) view.findViewById(R.id.add_product_price);
        btn_save = (Button) view.findViewById(R.id.btn_save_product);
        btn_save.setOnClickListener(this);
        txt_productPrice.addTextChangedListener(textWatcher);
        if (isUpdate)
            showUpdateInfo();
    }

    /**
     * 显示原有的数据
     */
    private  void showUpdateInfo(){
        ktvProduct = LitePal.find(KTVProduct.class,productId);
        txt_productName.setText(ktvProduct.getProduct_name());
        txt_productCount.setText(String.valueOf(ktvProduct.getProduct_count()));
        txt_productPrice.setText(String.valueOf(ktvProduct.getProduct_price()));
        productPic.setImageBitmap(getPirBitMap(context,ktvProduct.getProduct_picture()));
    }

    /**
     * 显示选择图片的窗口
     */
    public void showSelectDialog() {
        bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_picture, null);
        bottomDialog.setContentView(contentView);
        Button choosePhoto = (Button) contentView.findViewById(R.id.picture_gllary);
        Button takePhoto = (Button) contentView.findViewById(R.id.picture_camera);
        Button cancel = (Button) contentView.findViewById(R.id.picture_cancle);
        choosePhoto.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        cancel.setOnClickListener(this);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - dp2px(context, 16f);
        params.bottomMargin = dp2px(context, 8f);
        contentView.setLayoutParams(params);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }

    //region 拍照选择照片相关逻辑
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
                this.productPic.setImageBitmap(bitmap);
                saveImage(bitmap);
                isHave = true;
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
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        pathPicture = file.getPath();//获取图片保存路径
    }
    //endregion

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.picture_gllary:
                openGallery();
                bottomDialog.dismiss();
                break;
            case R.id.picture_camera:
                openCamera();
                bottomDialog.dismiss();
                break;
            case R.id.picture_cancle:
                bottomDialog.dismiss();
                break;
            case R.id.add_product_picture:
                showSelectDialog();
                break;
            case R.id.btn_save_product:
                saveProduct();
                break;
           default:
        }
    }

    /**
     * 保存商品信息
     */
    private  void saveProduct()
    {
        if (TextUtils.isEmpty(txt_productName.getText()))
            showShortText(context,getString(R.string.tips_product_name));
        else if (TextUtils.isEmpty(txt_productCount.getText()))
            showShortText(context,getString(R.string.tips_product_count));
        else if (TextUtils.isEmpty(txt_productPrice.getText()))
            showShortText(context,getString(R.string.tips_product_price));
        else if (!isHave)
        showShortText(context,getString(R.string.tips_product_picture));
        else {
            KTVProduct ktvProduct = new KTVProduct();
            if (isUpdate)
                ktvProduct = this.ktvProduct;
            ktvProduct.setProduct_name(txt_productName.getText().toString());
            ktvProduct.setProduct_count(Integer.valueOf(txt_productCount.getText().toString()));
            ktvProduct.setProduct_price(Double.valueOf(txt_productPrice.getText().toString()));
            ktvProduct.setProduct_picture(pathPicture);
            ktvProduct.save();

            ((SettingActivity)getActivity()).editDataCallBack(ktvProduct.isSaved());
        }
    }

    /**
     * 限制两位小数
     */
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String editStr = editable.toString().trim();

            int posDot = editStr.indexOf(".");
            //不允许输入3位小数,超过三位就删掉
            if (posDot < 0) {
                return;
            }
            if (editStr.length() - posDot - 1 > 2) {
                editable.delete(posDot + 3, posDot + 4);
            } else {
                //TODO...这里写逻辑
            }
        }
    };

}
