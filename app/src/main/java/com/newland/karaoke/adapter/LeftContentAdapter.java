package com.newland.karaoke.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.newland.karaoke.R;
import com.newland.karaoke.model.LeftContentModel;

import java.util.List;
import java.util.zip.Inflater;

public class LeftContentAdapter extends ArrayAdapter<LeftContentModel> {
    private int resourceId;
    //private View.OnClickListener onClickListener;

    private List<LeftContentModel> leftContentModels;

    public LeftContentAdapter(Context context, int resource, List<LeftContentModel> objects) {
        super(context, resource, objects);
        this.resourceId=resource;
        this.leftContentModels=objects;
    }

    @Override
    public LeftContentModel getItem(int position) {
        return leftContentModels.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        LeftContentModel model=leftContentModels.get(position);
        ViewHold holder=new ViewHold();
        holder.tv_main_left=(TextView)view.findViewById(R.id.tv_main_left);
        holder.iv_main_left=(ImageView) view.findViewById(R.id.iv_main_left);

        holder.tv_main_left.setText(model.getText());
        holder.iv_main_left.setImageResource(model.getImageId());


        return view;



    }

    class ViewHold {
        public ImageView iv_main_left;
        public TextView tv_main_left;
    }

}
