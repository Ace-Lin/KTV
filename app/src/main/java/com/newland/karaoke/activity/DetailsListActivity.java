package com.newland.karaoke.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.newland.karaoke.R;
import com.newland.karaoke.fragment.RoomDetailsFragment;

/**
 * 作为显示所有商品和房间信息的fragmen的载体
 */
public class DetailsListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_list);

        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();

        RoomDetailsFragment roomDetailsFragment=new RoomDetailsFragment(fManager);
        fTransaction.replace(R.id.detail_list_content,roomDetailsFragment);
        fTransaction.commit();

        setBackArrow(R.id.details_list_toolbar);
    }

    @Override
    public void basefinish() {
        finish();
    }
}
