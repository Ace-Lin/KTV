package com.newland.karaoke.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.newland.karaoke.PayHandler;
import com.newland.karaoke.R;
import com.newland.karaoke.adapter.OrderDetailProductAdapter;
import com.newland.karaoke.adapter.RoomAdapter;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.database.KTVOrderInfo;
import com.newland.karaoke.database.KTVOrderProduct;
import com.newland.karaoke.database.KTVProduct;
import com.newland.karaoke.database.KTVRoomInfo;
import com.newland.karaoke.view.ScollViewListView;

import org.litepal.LitePal;

import java.util.List;

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener{
    private int id=-1;
    private TextView txt_title;
    private Toolbar commonToolBar;
    private TextView tv_order_detail_room_name;
    private TextView tv_order_detail_room_type;
    private TextView tv_order_detail_room_price;
    private TextView tv_order_detail_number;
    private TextView tv_order_detail_date;
    private TextView tv_order_detail_amount;
    private TextView tv_order_detail_food_amount;
    private ListView lv_order_detail_product;
    private Button btn_order_detail_delete;
    private Button btn_order_detail_pay;
    private ImageView order_detail_room_change;
    private ImageView order_detail_food_change;
    private OrderDetailProductAdapter adapter;
    private double foodAmount=0;
    private double roomAmount=0;
    private KTVOrderInfo currentOrder;
    private KTVRoomInfo currentRoom;
    private List<KTVOrderProduct> currentOrderProducts;
    private List<KTVRoomInfo> roomInfos;
    private PayHandler payHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initData();
        initUI();
        initEvent();

    }

    private  void initUI(){
        hideStatusBar();
        commonToolBar = (Toolbar)findViewById(R.id.order_detail_toolbar);
        commonToolBar.setNavigationIcon(R.drawable.icon_back_left);
        commonToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentOrder=new Intent(OrderDetailActivity.this,OrderActivity.class);
                startActivity(intentOrder);
                finish();
            }
        });
        //加载标题
        txt_title = (TextView) findViewById(R.id.order_detail_title);
        txt_title.setText("Order Detail");

        //加载订单号
        tv_order_detail_number=(TextView)findViewById(R.id.tv_order_detail_number);
        tv_order_detail_number.setText(currentOrder.getOrder_number());

        //加载订单时间
        tv_order_detail_date=(TextView)findViewById(R.id.tv_order_detail_time);
        tv_order_detail_date.setText("Today");
        //tv_order_detail_date.setText(currentOrder.getOrder_start_time().toString());

        //加载食物金额
        tv_order_detail_food_amount=(TextView)findViewById(R.id.tv_order_detail_food_amount);
        if(currentOrderProducts!=null){
            for(KTVOrderProduct product:currentOrderProducts){
                foodAmount+=product.getProduct().getProduct_price()*product.getProduct_quantity();
            }
        }
        tv_order_detail_food_amount.setText("Food: $"+foodAmount);


        //加载房间信息
        tv_order_detail_room_name=(TextView)findViewById(R.id.order_detail_room_name);
        tv_order_detail_room_price=(TextView)findViewById(R.id.order_detail_room_price);
        tv_order_detail_room_type=(TextView)findViewById(R.id.order_detail_room_type);
//        if(currentRoom!=null){
//            tv_order_detail_room_name.setText(currentRoom.getRoom_name());
//            roomAmount=currentRoom.getRoom_price();
//            tv_order_detail_room_price.setText("$"+roomAmount);
//            tv_order_detail_room_type.setText(GetRoomType(currentRoom.getRoom_type()));
//        }
        SetRoomText();

        //加载总金额
        tv_order_detail_amount=(TextView)findViewById(R.id.tv_order_detail_amount);
        tv_order_detail_amount.setText("Total Amount: $"+(foodAmount+roomAmount));

        currentOrder.setPay_amount(foodAmount+roomAmount);
        currentOrder.save();
        //加载商品信息
        lv_order_detail_product=(ScollViewListView)findViewById(R.id.sv_order_detail_product);
        adapter=new OrderDetailProductAdapter(currentOrderProducts,OrderDetailActivity.this);
        lv_order_detail_product.setAdapter(adapter);
        //加载删除按钮
        btn_order_detail_delete=(Button)findViewById(R.id.btn_order_detail_delete);
        //加载修改按钮
        order_detail_food_change=(ImageView)findViewById(R.id.iv_order_detail_food_change);
        order_detail_room_change=(ImageView)findViewById(R.id.iv_order_detail_room_change);
        order_detail_food_change.setOnClickListener(this);
        order_detail_room_change.setOnClickListener(this);
        //加载结算按钮
        btn_order_detail_pay=(Button)findViewById(R.id.btn_order_detail_pay);
        btn_order_detail_pay.setOnClickListener(this);
    }

    private void SetRoomText(){
        if(currentRoom!=null){
            tv_order_detail_room_name.setText(currentRoom.getRoom_name());
            roomAmount=currentRoom.getRoom_price();
            tv_order_detail_room_price.setText("$"+roomAmount);
            tv_order_detail_room_type.setText(GetRoomType(currentRoom.getRoom_type()));
        }
    }

    private void initData(){
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            id=bundle.getInt("id");
            currentOrder= LitePal.where("id=?",String.valueOf(id)).findFirst(KTVOrderInfo.class,true);
            currentRoom=currentOrder.getRoom();
            currentOrderProducts=currentOrder.getProductList();
        }

    }

    private void initEvent(){
        btn_order_detail_delete.setOnClickListener(this);

    }

    public String GetRoomType(int type){
        switch(type){
            case KTVType.RoomType.BIG:
                return "Big";
            case KTVType.RoomType.MIDDLE:
                return "Middle";
            case KTVType.RoomType.SMAlL:
                return "Small";
        }
        return "info missing!";
    }
    @Override
    public void basefinish() {
        finish();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.btn_order_detail_delete:  //删除订单
                ShowConfirmDialog();
                break;

            case R.id.iv_order_detail_food_change:   //修改订单食物
                JumpToDetail();
                finish();
                break;
            case R.id.iv_order_detail_room_change:   //修改订单房间
                ShowConfirmRoomDialog();
                break;
            case R.id.btn_order_detail_pay:     //结算
                payHandler = new PayHandler(this,currentOrder,null);
                break;
            default:
                break;
        }
    }
    private void DeleteOrder(){
        if(id==-1) return;
        //重置房间状态为空闲
        if(currentRoom!=null){
            currentRoom.setRoom_status(KTVType.RoomStatus.FREE);
            currentRoom.save();
        }
        //修改商品存货数量
        if(currentOrderProducts!=null){
            for(KTVOrderProduct orderProduct:currentOrderProducts){
                KTVProduct product=orderProduct.getProduct();
                product.setProduct_count(product.getProduct_count()+orderProduct.getProduct_quantity());
                product.save();
                orderProduct.delete();
            }
        }
        //删除订单
        currentOrder.delete();
    }
    private void ShowConfirmDialog(){

        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("Confirm performance")
                .setMessage("Are you sure to delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DeleteOrder();
                        startActivity(new Intent(OrderDetailActivity.this,OrderActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("No",null)
                .create();
        dialog.show();
    }

    private void ShowConfirmRoomDialog(){
        roomInfos= LitePal.where("room_status=?",String.valueOf(KTVType.RoomStatus.FREE)).find(KTVRoomInfo.class,true);
        RoomAdapter roomAdapter=new RoomAdapter(this,R.layout.item_fragment_room,roomInfos);
        final AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("Confirm Room")
                .setSingleChoiceItems(roomAdapter, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int position) {

                        AlertDialog dialogConfirm=new AlertDialog.Builder(OrderDetailActivity.this)
                                .setTitle("Confirm performance")
                                .setMessage("Are you sure to change?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        currentRoom.setRoom_status(KTVType.RoomStatus.FREE);
                                        currentRoom.save();
                                        currentRoom=roomInfos.get(position);
                                        currentRoom.setRoom_status(KTVType.RoomStatus.NO_FREE);
                                        currentRoom.save();
                                        SetRoomText();
                                        currentOrder.setRoom_id(currentRoom);
                                        currentOrder.setPay_amount(roomAmount+foodAmount);
                                        currentOrder.save();

                                    }
                                })
                                .setNegativeButton("No",null)
                                .show();
                        dialogInterface.dismiss();
                    }

                })
                .create();
                dialog.show();
    }

    private void JumpToDetail(){
        Intent intentBack=new Intent(OrderDetailActivity.this,ChangeOrderInfoActivity.class);
        Bundle bundleBack=new Bundle();
        bundleBack.putInt("id",id);
        intentBack.putExtras(bundleBack);
        startActivity(intentBack);

    }

    //刷卡数据的返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        payHandler.onCardPayResult(requestCode, resultCode, data);
    }
}
