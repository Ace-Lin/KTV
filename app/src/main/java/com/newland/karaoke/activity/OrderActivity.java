package com.newland.karaoke.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.newland.karaoke.R;
import com.newland.karaoke.adapter.HistoryOrderAdapter;
import com.newland.karaoke.adapter.OrderAdapter;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.database.KTVOrderInfo;
import com.newland.karaoke.database.KTVOrderProduct;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.newland.karaoke.utils.DateUtil.getCurrentDayBegin;
import static com.newland.karaoke.utils.ToastUtil.showShortText;

public class OrderActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener , OrderAdapter.Callback {

    private  Button btn_back;
    private  TextView txt_title;
    private  ListView list_order;
    private  OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initView();
        showListView();
    }

    //初始化数据
    private void initView(){

        btn_back = findViewById(R.id.fragment_back_btn);
        txt_title = findViewById(R.id.setting_title);
        list_order = (ListView)findViewById(R.id.order_listview);

        txt_title.setText(R.string.order_today);
        btn_back.setOnClickListener(this);
    }

    //初始化数据
    private  void showListView(){

        List<KTVOrderInfo>   ktvOrderInfoList = LitePal.where("order_status= ? and (order_start_time>? and order_start_time<?) ",
                String.valueOf(KTVType.OrderStatus.UNPAID),  getCurrentDayBegin(Calendar.getInstance()),String.valueOf(new Date().getTime())).find(KTVOrderInfo.class);


        orderAdapter = new OrderAdapter(ktvOrderInfoList, this,this);
        list_order.setAdapter(orderAdapter);
        list_order.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.fragment_back_btn)
          onBackPressed();
    }

    @Override
    public void listSubClick(View view) {
        int  postions = (Integer)view.getTag();//adapter设置了tag

        if (view.getId()==R.id.order_btn_details) {

              showShortText(this,postions + "order_btn_details");
        }
         else if (view.getId()==R.id.order_btn_pay) {
            showShortText(this,postions + "order_btn_pay");
        }

    }
}
