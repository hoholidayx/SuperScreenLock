package com.hzp.superscreenlock.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.locker.LockManager;
import com.hzp.superscreenlock.manager.AppInfoManager;
import com.hzp.superscreenlock.view.adapter.AppInfoAdapter;


public class LockScreenFragment extends Fragment {

    private RecyclerView mainRecyclerView;
    private AppInfoAdapter appInfoAdapter;

    public LockScreenFragment() {
    }

    public static LockScreenFragment newInstance() {
        LockScreenFragment fragment = new LockScreenFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lock_screen_main, container, false);

        mainRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_main);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LockManager.getInstance().startUnlockView
                        (getActivity(),
                                getActivity().getSupportFragmentManager());
            }
        });

        setupMainRecyclerView();

        return view;
    }

    private void setupMainRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        mainRecyclerView.setLayoutManager(layoutManager);
        // TODO: 2016/8/23 换位置载入数据
        appInfoAdapter = new AppInfoAdapter(AppInfoManager.getInstance().getAppInfoDisplayOnMain());
        mainRecyclerView.setAdapter(appInfoAdapter);

    }

}