package com.newland.karaoke.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.newland.karaoke.R;
import com.newland.karaoke.activity.TransactionActivity;
import com.newland.karaoke.adapter.HistoryOrderAdapter;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.database.KTVOrderInfo;

import org.litepal.LitePal;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import static com.newland.karaoke.utils.DateUtil.getCurrentDayBegin;
import static com.newland.karaoke.utils.DateUtil.getCurrentDayEnd;

/**
 * A simple
 */
public class HistoryOrderlistFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {


    private List<KTVOrderInfo> ktvOrderInfoList;
    private  HistoryOrderAdapter historyAdapter;

    private  Calendar currentDate;//当前日期
    private int currentYear;
    private int currentMonth;
    private int currentDay;

    //region Description
    private TextView txt_turnover;//总营业额
    private ListView list_history;
    private Button btn_searchTime;

    //endregion

    public HistoryOrderlistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_orderlist, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        initBaseView(view,R.id.setting_toolbar,getString(R.string.order_history));
        initUIData(view);
        initShowUIData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_toolbar, menu);
        super.onCreateOptionsMenu(menu,inflater);

    }

    /**
     * 初始化获取UI数据
     */
    private void initUIData(View view)
    {
        btn_searchTime = (Button)view.findViewById(R.id.btn_order_time);
        btn_searchTime.setOnClickListener(this );
        txt_turnover = (TextView)view.findViewById(R.id.txt_turnover);
        list_history = (ListView)view.findViewById(R.id.history_listview);
        currentDate = Calendar.getInstance();
        currentYear = currentDate.get(Calendar.YEAR);
        currentMonth = currentDate.get(Calendar.MONTH) + 1;//数从0开始,需要加1
        currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
    }


    /**
     * 初始化UI数据
     */
    private  void initShowUIData()
    {
        ktvOrderInfoList = LitePal.where("order_status= ? and (order_end_time>? and order_end_time<?) ",
                String.valueOf(KTVType.OrderStatus.PAID),  getCurrentDayBegin(currentDate),String.valueOf(currentDate.getTimeInMillis())).find(KTVOrderInfo.class);

        historyAdapter = new HistoryOrderAdapter(ktvOrderInfoList, getContext());
        list_history.setAdapter(historyAdapter);
        list_history.setOnItemClickListener(this);

        btn_searchTime.setText(String.format("%d-%d-%d",currentDay-1,currentMonth,currentYear));
        txt_turnover.setText(getCurrTurnover(ktvOrderInfoList));
    }


    /**
     *更换时间更新显示数据
     */
    private void updateUIdata(){

        ktvOrderInfoList.clear();
        ktvOrderInfoList.addAll(LitePal.where("order_status= ? and (order_end_time>? and order_end_time<?) ",
                String.valueOf(KTVType.OrderStatus.PAID), getCurrentDayBegin(currentDate),getCurrentDayEnd(currentDate)).find(KTVOrderInfo.class));
        historyAdapter.notifyDataSetChanged();

        txt_turnover.setText(getCurrTurnover(ktvOrderInfoList));
        btn_searchTime.setText(currentDay+"-"+currentMonth+"-"+currentYear);
    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        ((TransactionActivity)getActivity()).switchFragment(ktvOrderInfoList.get(i));
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_order_time)
            setSearchDate();
    }


    //获取查询时间
    private void setSearchDate(){

        DatePickerDialog dp = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                setCurrentDate(dayOfMonth,monthOfYear,year);
                updateUIdata();
            }
        },currentYear,currentMonth,currentDay);
        dp.show();
    }

    /**
     * 更改时间
     */
    private  void setCurrentDate(int day,int month, int year)
    {
        currentDate.set(Calendar. YEAR, year);
        currentDate.set(Calendar. MONTH,  month-1);
        currentDate.set(Calendar. DAY_OF_MONTH , day);
        currentYear = year;
        currentMonth = month;
        currentDay = day;
    }

    /**
     * 返回当前营业额
     */
    private String getCurrTurnover(List<KTVOrderInfo> list){
        DecimalFormat df = new DecimalFormat("0.00");
        double turnover = 0;

        for (KTVOrderInfo orderInfo:list)
            turnover += orderInfo.getPay_amount();

        return  String.valueOf(df.format(turnover));
    }




}
