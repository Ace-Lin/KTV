package com.newland.karaoke.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.PermissionChecker;
import androidx.drawerlayout.widget.DrawerLayout;

import com.newland.karaoke.KTVApplication;
import com.newland.karaoke.R;
import com.newland.karaoke.adapter.LeftContentAdapter;
import com.newland.karaoke.constant.Const;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.database.KTVOrderProduct;
import com.newland.karaoke.database.KTVProduct;
import com.newland.karaoke.database.KTVRoomInfo;
import com.newland.karaoke.database.KTVUserInfo;
import com.newland.karaoke.database.KTVUserLogin;
import com.newland.karaoke.model.LeftContentModel;
import com.newland.karaoke.utils.FileUtils;
import com.newland.karaoke.view.SelectDialog;
import com.squareup.picasso.Picasso;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //获取照片和存储照片
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    public static final int PERMISSION_REQUEST_PHOTO_CODE=4; //权限请求值
    public static final int PERMISSION_REQUEST_STORAGE_CODE=5; //权限请求值
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg"; // 头像名称

    private String pathPicture;//图片路径
    private Uri imgUri;
    private File tempFile;
    private  boolean isHave; //是否获取了图片

    //主界面四个按钮
    private ImageButton checkIn;
    private ImageButton order;
    private ImageButton settings;
    private ImageButton shift;
    private TextView tv_main_username;
    private ListView lv_main_left;
    private List<LeftContentModel> leftContentModels;
    private LeftContentAdapter adapter;
    private DrawerLayout mDrawLayout;
    private CircleImageView mHeadImage;//头像
    private Handler handler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消顶部标题
       requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //优化了10%进入速度
        new Handler().postDelayed(()->{
            initData();
            initUI();
            initEvent();
            initDB();
        },200);
    }

    //region 模拟数据创建
    //判断数据库
    private void initDB(){
        if (!getSharedPreferences("data", MODE_PRIVATE).getBoolean("isCreate",false))
            CreateDatabase();
    }

    //创建数据库初始化数据
    public  void CreateDatabase(){

        SQLiteDatabase db= LitePal.getDatabase();
        LitePal.deleteAll(KTVUserLogin.class);
        LitePal.deleteAll(KTVUserInfo.class);

        KTVUserInfo user_info=new KTVUserInfo();
        user_info.setIdentity_card_no("1234");
        user_info.setUser_email("747848014@qq.com");
        user_info.save();

        KTVUserLogin user_login=new KTVUserLogin();
        user_login.setUser_account("15880168030");
        user_login.setUser_password("123456");
        user_login.setUser_info(user_info);
        user_login.save();

        //添加房间信息
        KTVRoomInfo ktvRoomInfo1=new KTVRoomInfo();
        ktvRoomInfo1.setRoom_name("A01");
        ktvRoomInfo1.setRoom_price(100);
        ktvRoomInfo1.setRoom_status(KTVType.RoomStatus.FREE);
        ktvRoomInfo1.setRoom_type(KTVType.RoomType.MIDDLE);
        ktvRoomInfo1.save();

        KTVRoomInfo ktvRoomInfo2=new KTVRoomInfo();
        ktvRoomInfo2.setRoom_name("B02");
        ktvRoomInfo2.setRoom_price(200);
        ktvRoomInfo2.setRoom_type(KTVType.RoomType.BIG);
        ktvRoomInfo2.setRoom_status(KTVType.RoomStatus.FREE);
        ktvRoomInfo2.save();

        KTVRoomInfo ktvRoomInfo3=new KTVRoomInfo();
        ktvRoomInfo3.setRoom_name("C02");
        ktvRoomInfo3.setRoom_price(70);
        ktvRoomInfo3.setRoom_type(KTVType.RoomType.SMAlL);
        ktvRoomInfo3.setRoom_status(KTVType.RoomStatus.FREE);
        ktvRoomInfo3.save();

        //添加商品信息
        KTVProduct ktvProduct1=new KTVProduct();
        ktvProduct1.setProduct_name("Bond Cigar");
        ktvProduct1.setProduct_count(30);
        ktvProduct1.setProduct_price(10);
        ktvProduct1.setProduct_picture(String.valueOf(R.drawable.product_1));
        ktvProduct1.setProduct(new ArrayList<KTVOrderProduct>());
        ktvProduct1.save();

        KTVProduct ktvProduct2=new KTVProduct();
        ktvProduct2.setProduct_name("Extra Beer");
        ktvProduct2.setProduct_count(60);
        ktvProduct2.setProduct_price(10);
        ktvProduct2.setProduct_picture(String.valueOf(R.drawable.product_2));
        ktvProduct2.setProduct(new ArrayList<KTVOrderProduct>());
        ktvProduct2.save();

        KTVProduct ktvProduct3=new KTVProduct();
        ktvProduct3.setProduct_name("BuWei Beer");
        ktvProduct3.setProduct_count(50);
        ktvProduct3.setProduct_price(10);
        ktvProduct3.setProduct_picture(String.valueOf(R.drawable.product_3));
        ktvProduct3.setProduct(new ArrayList<KTVOrderProduct>());
        ktvProduct3.save();

        KTVProduct ktvProduct4=new KTVProduct();
        ktvProduct4.setProduct_name("Fruit tray 1");
        ktvProduct4.setProduct_count(15);
        ktvProduct4.setProduct_price(25);
        ktvProduct4.setProduct_picture(String.valueOf(R.drawable.product_4));
        ktvProduct4.setProduct(new ArrayList<KTVOrderProduct>());
        ktvProduct4.save();

        KTVProduct ktvProduct5=new KTVProduct();
        ktvProduct5.setProduct_name("Fruit tray 2");
        ktvProduct5.setProduct_count(20);
        ktvProduct5.setProduct_price(20);
        ktvProduct5.setProduct_picture(String.valueOf(R.drawable.product_5));
        ktvProduct5.setProduct(new ArrayList<KTVOrderProduct>());
        ktvProduct5.save();

        KTVProduct ktvProduct6=new KTVProduct();
        ktvProduct6.setProduct_name("Cigar");
        ktvProduct6.setProduct_count(50);
        ktvProduct6.setProduct_price(10);
        ktvProduct6.setProduct_picture(String.valueOf(R.drawable.product_6));
        ktvProduct6.setProduct(new ArrayList<KTVOrderProduct>());
        ktvProduct6.save();


        KTVProduct ktvProduct7=new KTVProduct();
        ktvProduct7.setProduct_name("Wiire Beer");
        ktvProduct7.setProduct_count(50);
        ktvProduct7.setProduct_price(12);
        ktvProduct7.setProduct_picture(String.valueOf(R.drawable.product_7));
        ktvProduct7.setProduct(new ArrayList<KTVOrderProduct>());
        ktvProduct7.save();


        //增加用户
        KTVUserInfo userInfo=new KTVUserInfo();
        userInfo.setIdentity_card_no("123432435");
        userInfo.setMobile_phone("15059115150");
        userInfo.setUser_photo("");
        userInfo.setUser_name("yiyi");
        userInfo.save();

        KTVUserLogin userLogin=new KTVUserLogin();
        userLogin.setUser_info(userInfo);
        userLogin.setUser_account("yi");
        userLogin.setUser_password("12345");
        userLogin.save();
        //            //添加商品订单
        //            KTVOrderProduct ktvOrderProduct=new KTVOrderProduct();
        //            ktvOrderProduct.setProduct_quantity(2);
        //            ktvOrderProduct.setProduct(ktvProduct4);
        //            ktvOrderProduct.save();
        //            //添加商品订单
        //            KTVOrderProduct ktvOrderProduct1=new KTVOrderProduct();
        //            ktvOrderProduct1.setProduct_quantity(1);
        //            ktvOrderProduct1.setProduct(ktvProduct3);
        //            ktvOrderProduct1.save();
        //            //添加订单
        //            KTVOrderInfo ktvOrderInfo=new KTVOrderInfo();
        //            ktvOrderInfo.setOrder_start_time(new Date());
        //            ktvOrderInfo.setOrder_end_time(new Date());
        //            ktvOrderInfo.setOrder_number("15472719");
        //            ktvOrderInfo.setRoom_id(ktvRoomInfo3);
        //            ktvOrderInfo.setPay_amount(85);
        //            List<KTVOrderProduct> list=new ArrayList<KTVOrderProduct>();
        //            list.add(ktvOrderProduct);
        //            list.add(ktvOrderProduct1);
        //            ktvOrderInfo.setProductList(list);
        //            ktvOrderInfo.setOrder_status(KTVType.OrderStatus.UNPAID);
        //            ktvOrderInfo.save();
        //            //更新商品订单
        //            ktvOrderProduct.setKtvOrderInfo(ktvOrderInfo);
        //            ktvOrderProduct.save();
        SharedPreferences.Editor sp = getSharedPreferences("data", MODE_PRIVATE).edit();
        sp.putBoolean("isCreate", true);
        sp.apply();
    }
    //endregion

    void initData(){
        leftContentModels=new ArrayList<>();
        leftContentModels.add(new LeftContentModel(R.drawable.main_login,"Login",KTVType.MineType.LOGIN));
        leftContentModels.add(new LeftContentModel(R.drawable.main_person_info,"Personal Info",KTVType.MineType.PERSONAL_INFO));
        leftContentModels.add(new LeftContentModel(R.drawable.main_psw,"Set Password",KTVType.MineType.SET_PWD));
        leftContentModels.add(new LeftContentModel(R.drawable.main_logout,"Log out",KTVType.MineType.LOG_OUT));
    }
    void initUI(){
        //初始化UI
        mDrawLayout=(DrawerLayout)findViewById(R.id.dl_main);

        checkIn=(ImageButton)findViewById(R.id.bt_checkin);
        order=(ImageButton)findViewById(R.id.bt_order);
        settings=(ImageButton)findViewById(R.id.bt_settings);
        shift=(ImageButton)findViewById(R.id.bt_transactions);
        tv_main_username=(TextView)findViewById(R.id.tv_main_username);
        if(KTVApplication.isLogin()){
            tv_main_username.setVisibility(View.VISIBLE);
            tv_main_username.setText(KTVApplication.getCurrentUser().getUser_name());
        }

        //初始化list
        lv_main_left=(ListView)findViewById(R.id.lv_main_left);
        adapter=new LeftContentAdapter(this,R.layout.item_left_content,leftContentModels);
        lv_main_left.setAdapter(adapter);
        //头像
        mHeadImage = (CircleImageView) findViewById(R.id.ci_main_photo);
        //设置圆形头像
        SetPhotoByUser();

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
            Picasso.get().load(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .into(mHeadImage);
            return;
        }

        Picasso.get().load("file://"+url)
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .into(mHeadImage);
    }
    private void initEvent(){
        //设置监听
        checkIn.setOnClickListener(this);
        order.setOnClickListener(this);
        settings.setOnClickListener(this);
        shift.setOnClickListener(this);

        lv_main_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                if(id == -1) {
                    // 点击的是headerView或者footerView
                    return;
                }

                int realPosition=(int)id;
                LeftContentModel item=adapter.getItem(realPosition);
                // 响应代码
                switch(item.getId()){
                    case KTVType.MineType.LOGIN:
                        Intent login=new Intent(MainActivity.this,ShiftActivity.class);
                        startActivity(login);
                        break;
                    case KTVType.MineType.PERSONAL_INFO:
                        if(KTVApplication.getCurrentUser()==null){
                            Toast.makeText(KTVApplication.getContext(),"Please Login!",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        startActivity(new Intent(MainActivity.this, PersonInfoActivity.class));
                        break;
                    case KTVType.MineType.SET_PWD:
                        if(KTVApplication.getCurrentUser()==null){
                            Toast.makeText(KTVApplication.getContext(),"Please Login!",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        startActivity(new Intent(MainActivity.this,ChangePwdActivity.class));
                        break;
                    case KTVType.MineType.LOG_OUT:
                        if(KTVApplication.getCurrentUser()==null){
                            Toast.makeText(KTVApplication.getContext(),"Please Login!",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        KTVApplication.setCurrentUser(null);
                        KTVApplication.setIsLogin(false);
                        tv_main_username.setVisibility(View.GONE);
                        SetPhotoByUser();
                        break;
                    default:
                        break;
                }
                mDrawLayout.closeDrawer(Gravity.LEFT);

            }
        });

        //监听侧边菜单栏打开或关闭的状态
        mDrawLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float v) {

            }
            //打开菜单栏后触发的方法
            @Override
            public void onDrawerOpened(View view) {
               // Toast.makeText(MainActivity.this, "打开了侧边栏" , Toast.LENGTH_SHORT).show();
            }
            //关闭菜单栏后触发的方法
            @Override
            public void onDrawerClosed(View view) {
                //Toast.makeText(MainActivity.this, "关闭了侧边栏" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch(msg.what){
                    case KTVType.MsgType.UPDATE_IMG:
                        SetPhotoByUser();
                        tv_main_username.setVisibility(View.VISIBLE);
                        tv_main_username.setText(KTVApplication.getCurrentUser().getUser_name());
                        break;

                }
            }
        };
        KTVApplication.setmHandler(handler);
        mHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });
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
                        openCamera(MainActivity.this);
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

//    private void SetImageByURL(String url) {
//        if(!KTVApplication.isLogin()){
//            return;
//        }
//        //设置圆形头像
//        Uri uri=Uri.parse(url);
//        Picasso.with(this).load(uri)
//                .placeholder(R.drawable.default_avatar)
//                .error(R.drawable.default_avatar)
//                .memoryPolicy(MemoryPolicy.NO_CACHE)
//                .networkPolicy(NetworkPolicy.NO_CACHE)
//                .into(mHeadImage);
//    }


    @Override
    public void onClick(View view) {
        //处理监听
        switch(view.getId()){
            case R.id.bt_checkin:

                Intent intent_check=new Intent(MainActivity.this,CheckInActivity.class);
                startActivity(intent_check);
                break;
            case R.id.bt_order:
                Intent intent_order=new Intent(MainActivity.this,OrderActivity.class);
                startActivity(intent_order);
                break;
            case R.id.bt_settings:
                Intent intent_settings=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent_settings);
                break;
            case R.id.bt_transactions:
                Intent intent_tansaction=new Intent(MainActivity.this,TransactionActivity.class);
                startActivity(intent_tansaction);
                break;
            default:
                break;
        }
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
                Toast.makeText(MainActivity.this, "User Refused! ", Toast.LENGTH_SHORT).show();
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
                //tempFile = new File(getExternalFilesDir(null), PHOTO_FILE_NAME);
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
                //SetImageByURL(pathPicture);
                isHave = true;
                //boolean delete = tempFile.delete();
                //System.out.println("delete = " + delete);
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

    private long firstTime = 0;
    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            System.exit(0);
        }
    }


}
