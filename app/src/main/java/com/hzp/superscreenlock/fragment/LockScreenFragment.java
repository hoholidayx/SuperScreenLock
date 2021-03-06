package com.hzp.superscreenlock.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.activity.MainActivity;
import com.hzp.superscreenlock.entity.AppInfo;
import com.hzp.superscreenlock.entity.EnvironmentInfo;
import com.hzp.superscreenlock.locker.LockManager;
import com.hzp.superscreenlock.manager.AppInfoManager;
import com.hzp.superscreenlock.utils.SystemUtil;
import com.hzp.superscreenlock.view.adapter.AppInfoAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class LockScreenFragment extends Fragment implements AppInfoAdapter.AppInfoListener {

    private RecyclerView mainRecyclerView;
    private AppInfoAdapter appInfoAdapter;
    private TextView timeTv,dateTv;

    private TimeReceiver timeReceiver;
    private SimpleDateFormat timeFormat,dateFormat;


    public LockScreenFragment() {
    }

    public static LockScreenFragment newInstance() {
        LockScreenFragment fragment = new LockScreenFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        dateFormat = new SimpleDateFormat("yyyy/MM/dd",Locale.getDefault());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lock_screen_main, container, false);


        mainRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_main);
        timeTv = (TextView) view.findViewById(R.id.lock_screen_time);
        timeTv.setText(getFormatCurrentTime());
        dateTv = (TextView) view.findViewById(R.id.lock_screen_date);
        dateTv.setText(getFormatCurrentDate());

        setupMainRecyclerView();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        registerTimeReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(timeReceiver);
    }

    private void setupMainRecyclerView() {

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),
                AppInfoManager.getInstance().getBottomIconNumber());
        mainRecyclerView.setLayoutManager(layoutManager);
        appInfoAdapter = new AppInfoAdapter(AppInfoManager.getInstance()
                .getListToDisplay(AppInfo.SCREEN_SHOW_TYPE_BOTTOM));
        appInfoAdapter.setListener(this);
        mainRecyclerView.setAdapter(appInfoAdapter);

    }


    private void registerTimeReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK); //每分钟变化的action
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED); //设置了系统时间的action

        timeReceiver = new TimeReceiver();
        getActivity().registerReceiver(timeReceiver,intentFilter);
    }

    private String getFormatCurrentTime(){
       return  timeFormat.format(Calendar.getInstance().getTimeInMillis());
    }

    private String getFormatCurrentDate() {
        String date = dateFormat.format(Calendar.getInstance().getTimeInMillis());
        String week = SystemUtil.getWeek(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        return date +" "+week;
    }

    @Override
    public void onItemClick(AppInfo appInfo, int position) {
        LockManager.getInstance().startUnlockView(getActivity(),
                getActivity().getSupportFragmentManager(),
                appInfo.getIntent());
    }

    @Override
    public void onItemLongClick(AppInfo appInfo, int position) {

    }

    private class TimeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            timeTv.setText(getFormatCurrentTime());
        }
    }
}
