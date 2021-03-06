package com.hzp.superscreenlock.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.entity.AppInfo;
import com.hzp.superscreenlock.manager.AppInfoManager;
import com.hzp.superscreenlock.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hezhipeng on 2016/8/23.
 */
public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.AppInfoHolder> {
public static final String TAG = AppInfoAdapter.class.getSimpleName();


    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_EDIT =2;

    List<AppInfo> list;
    AppInfoListener listener;

    private int type = TYPE_NORMAL;

    public AppInfoAdapter() {
        this(null);
    }

    public AppInfoAdapter(List<AppInfo> list) {
        if (list != null) {
            this.list = list;
        } else {
            this.list = new ArrayList<>();
        }
        Collections.sort(this.list);
    }

    public void addItem(AppInfo item) {
        if (item == null) {
            return;
        }
        int position = getItemCount();
        list.add(item);
        Collections.sort(list);
        notifyItemInserted(position);
    }

    public void addItems(List<AppInfo> list){
        for (AppInfo i :
                list) {
            addItem(i);
        }
    }

    @Override
    public AppInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (type){
            case TYPE_NORMAL:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_app_info, parent, false);
                break;
            case TYPE_EDIT:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_app_info_edit, parent, false);
                break;
            default:
                throw new IllegalArgumentException("wrong AppInfo list type!");
        }

        return new AppInfoHolder(view);
    }

    @Override
    public void onBindViewHolder(final AppInfoHolder holder, int position) {
        final AppInfo appInfo = list.get(position);
        if (appInfo.getAppIcon() != null) {
            holder.icon.setImageDrawable(appInfo.getAppIcon());
        }
        if (appInfo.getAppLabel() != null) {
            holder.label.setText(appInfo.getAppLabel());
        }

        switch (appInfo.getScreenShowType()){
            case AppInfo.SCREEN_SHOW_TYPE_SLIDE:
                case AppInfo.SCREEN_SHOW_TYPE_SELECT_LIST:
                    holder.label.setVisibility(View.VISIBLE);
                    break;
                    default:
                        holder.label.setVisibility(View.GONE);
        }

        if(getType()!=TYPE_EDIT && appInfo.getPkgName().equals(AppInfoManager.STUB_PACKAGE_NAME)){//非编辑模式下的stub不显示
            holder.itemView.setVisibility(View.INVISIBLE);
        }else{
            holder.itemView.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition();
                LogUtil.i(TAG,"app ={"+appInfo.getPkgName()+"} position = "+position+" is selected");

                if(listener!=null){
                    listener.onItemClick(appInfo,position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getLayoutPosition();
                LogUtil.i(TAG,"app ={"+appInfo.getPkgName()+"} position = "+position+" is long click");

                if(listener!=null){
                    listener.onItemLongClick(appInfo,position);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void removeItem(AppInfo info){
        list.remove(info);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public static class AppInfoHolder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView label;

        public AppInfoHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.item_app_info_icon);
            label = (TextView) itemView.findViewById(R.id.item_app_info_label);
        }
    }

    public AppInfoAdapter setListener(AppInfoListener listener) {
        this.listener = listener;
        return this;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public interface AppInfoListener{
        /**
         * 将点击事件分发出去处理
         * @param appInfo
         */
        void onItemClick(AppInfo appInfo,int position);

        void onItemLongClick(AppInfo appInfo,int position);
    }
}
