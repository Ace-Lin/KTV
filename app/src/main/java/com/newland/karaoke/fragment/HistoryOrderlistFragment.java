package com.newland.karaoke.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.newland.karaoke.R;
import com.newland.karaoke.adapter.HistoryOrderAdapter;
import com.newland.karaoke.database.KTVOrderInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import static com.newland.karaoke.utils.ToastUtil.showShortText;

/**
 * A simple
 */
public class HistoryOrderlistFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView list_history;
    private List<KTVOrderInfo> ktvOrderInfoList;


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
        initBaseView(view,R.id.setting_toolbar,getString(R.string.order_detail));
        initOrderList(view);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_toolbar, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    /**
     * 初始化历史订单列表数据
     */
    private  void initOrderList(View view)
    {
        list_history = (ListView)view.findViewById(R.id.history_listview);
        ktvOrderInfoList = new ArrayList<>();
        ktvOrderInfoList = LitePal.findAll(KTVOrderInfo.class);
        HistoryOrderAdapter historyOrderAdapter = new HistoryOrderAdapter(ktvOrderInfoList, getContext());
        list_history.setAdapter(historyOrderAdapter);
        list_history.setOnItemClickListener(this);
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

        FragmentManager fManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();

        HistoryOderdetailFragment historyOderdetailFragment=new HistoryOderdetailFragment(ktvOrderInfoList.get(i));

        fTransaction.replace(R.id.detail_list_content, historyOderdetailFragment);

        fTransaction.commit();
    }
}
