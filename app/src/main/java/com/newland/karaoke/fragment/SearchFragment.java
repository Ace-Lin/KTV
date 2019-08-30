package com.newland.karaoke.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newland.karaoke.R;
import com.newland.karaoke.activity.TransactionActivity;
import com.newland.karaoke.adapter.HistoryOrderAdapter;
import com.newland.karaoke.database.KTVOrderInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple
 */
public class SearchFragment extends BaseFragment implements AdapterView.OnItemClickListener {


    private  HistoryOrderAdapter searchAdapter;
    private ListView list_search;
    private List<KTVOrderInfo> searchList = new ArrayList<>();
    private TextView txt_feedback;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        list_search = (ListView)view.findViewById(R.id.search_listview);
        txt_feedback = (TextView)view.findViewById(R.id.search_feedback);
    }


    /**
     * 初始化UI数据
     */
    private  void initShowUIData()
    {
        //待续使用
        searchAdapter = new HistoryOrderAdapter(searchList, getContext());
        list_search.setAdapter(searchAdapter);
        list_search.setOnItemClickListener(this);

    }




    private  void showSearchInfo(String number)
    {
        searchList.clear();
        searchList.addAll(LitePal.where("order_number=?",number).find(KTVOrderInfo.class));
        if (searchList.size()<=0)
            txt_feedback.setVisibility(View.VISIBLE);
        searchAdapter.notifyDataSetChanged();
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        ((TransactionActivity)getActivity()).switchFragment(searchList.get(i));
    }


}
