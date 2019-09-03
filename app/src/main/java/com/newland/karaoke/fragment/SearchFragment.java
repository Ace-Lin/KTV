package com.newland.karaoke.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.newland.karaoke.R;
import com.newland.karaoke.activity.TransactionActivity;
import com.newland.karaoke.adapter.HistoryOrderAdapter;
import com.newland.karaoke.database.KTVOrderInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import static com.newland.karaoke.utils.Utility.closeSoftKeybord;

/**
 *搜索fragment
 */
public class SearchFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, TextView.OnEditorActionListener {


    private  HistoryOrderAdapter searchAdapter;
    private  List<KTVOrderInfo> searchList = new ArrayList<>();

    //region UI变量
    private  EditText edit_OrderNumber;
    private  TextView txt_feedback;
    private  Button btn_Search;
    private  ListView list_search;
    //endregion


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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUIData(view);
        initShowUIData();
    }


    /**
     * 初始化获取UI数据
     */
    private void initUIData(View view)
    {
        txt_feedback = (TextView)view.findViewById(R.id.search_feedback);
        edit_OrderNumber =(EditText) view.findViewById(R.id.search_txt);
        btn_Search = (Button)view.findViewById(R.id.search_btn);
        list_search = (ListView)view.findViewById(R.id.search_listview);
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
        btn_Search.setOnClickListener(this);
        edit_OrderNumber.setOnEditorActionListener(this);
    }


    /**
     * 搜索更新数据
     */
    private  void showSearchInfo(String number)
    {
        //先重置
        searchList.clear();
        txt_feedback.setVisibility(View.GONE);

        searchList.addAll(LitePal.where("order_number=?",number).find(KTVOrderInfo.class));
        if (searchList.size()<=0)
            txt_feedback.setVisibility(View.VISIBLE);
        searchAdapter.notifyDataSetChanged();
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        ((TransactionActivity)getActivity()).openDetailFragment(searchList.get(i));
    }




    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.search_btn) {
            //判断是否输入订单号码
            if (TextUtils.isEmpty(edit_OrderNumber.getText().toString()))
                return;

            closeSoftKeybord(edit_OrderNumber,getContext());
            showSearchInfo(edit_OrderNumber.getText().toString());
        }
    }


    /**
     * 监听搜索按钮
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (TextUtils.isEmpty(edit_OrderNumber.getText().toString()))
            return false;

        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            showSearchInfo(v.getText().toString());
            return true;
        }
        return false;
    }
}
