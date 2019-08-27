package com.newland.karaoke.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.newland.karaoke.R;
import com.newland.karaoke.activity.AddActivity;
import com.newland.karaoke.database.KTVRoomInfo;

import org.litepal.LitePal;

import static com.newland.karaoke.utils.ToastUtil.showShortText;

/**
 * 用来显示添加房间信息的Fragment
 */
public class AddRoomFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private  Context context;
    private Spinner spinner_roomType;
    private TextView txt_roomNum;
    private TextView txt_roomPrice;
    private Button btn_save;
    private  int roomType;
    private  boolean isUpdate;//判断是否是更新数据
    private  int roomId;//需要更改的商品id
    private  KTVRoomInfo ktvRoomInfo;//需要更新的room

    public AddRoomFragment(Context context) {
        this.context = context;
    }

    /**
     * 获取需要修改的房间id
     * @param roomId 房间id
     */
    public  void updateRoom(int roomId)
    {
        isUpdate = true;
        this.roomId = roomId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_room, container, false);
        initUI(view);
        return view;
    }

    /**
     * 初始化UI信息
     */
    private void initUI(View view)
    {
        txt_roomNum = (TextView) view.findViewById(R.id.add_room_no);
        txt_roomPrice = (TextView) view.findViewById(R.id.add_room_price);
        btn_save = (Button) view.findViewById(R.id.btn_save_room);
        btn_save.setOnClickListener(this);
        spinner_roomType = (Spinner)view.findViewById(R.id.room_type_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,R.array.room_type_array, R.layout.room_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.room_dropdown_item);
        // Apply the adapter to the spinner
        spinner_roomType.setAdapter(adapter);
        spinner_roomType.setOnItemSelectedListener(this);
        if (isUpdate)
            showUpdateInfo();
    }

    /**
     * 显示原有的数据
     */
    private  void showUpdateInfo(){
        ktvRoomInfo = LitePal.find(KTVRoomInfo.class,roomId);
        spinner_roomType.setSelection(ktvRoomInfo.getRoom_type(),true);
        txt_roomNum.setText(ktvRoomInfo.getRoom_name());
        txt_roomPrice.setText(String.valueOf(ktvRoomInfo.getRoom_price()));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

   //下拉框选择
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        roomType = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {

       if (view.getId()==R.id.btn_save_room){

        if (TextUtils.isEmpty(txt_roomNum.getText()))
           showShortText(context,getString(R.string.tips_room_name));
        else if (TextUtils.isEmpty(txt_roomPrice.getText()))
           showShortText(context,getString(R.string.tips_room_price));
        else {
            KTVRoomInfo roomInfo = new KTVRoomInfo();
            if (isUpdate)
                roomInfo = ktvRoomInfo;

            roomInfo.setRoom_type(roomType);
            roomInfo.setRoom_name(txt_roomNum.getText().toString());
            roomInfo.setRoom_price(Double.valueOf( txt_roomPrice.getText().toString()));
            roomInfo.save();

            if (roomInfo.isSaved())
            {
                showShortText(context,getString(R.string.tips_add_success));
                ((AddActivity)context).finish();
            }
        }

       }
    }
}
