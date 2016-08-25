package com.hzp.superscreenlock.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.entity.AppInfoManager;


public class LockScreenMainFragment extends Fragment {

    private RecyclerView mainRecyclerView;
    private AppInfoAdapter appInfoAdapter;

    public LockScreenMainFragment() {}

    public static LockScreenMainFragment newInstance() {
        LockScreenMainFragment fragment = new LockScreenMainFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_lock_screen_main, container, false);

        mainRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_main);
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
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