package com.hzp.superscreenlock.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.entity.EnvironmentInfo;
import com.hzp.superscreenlock.manager.EnvironmentManager;
import com.hzp.superscreenlock.view.adapter.EnvironmentAdapter;

public class MainActivity extends AppCompatActivity {

    public static final int SETTING_MODE_NORMAL = 1;
    public static final int SETTING_MODE_EDIT = 2;

    public static final int REQUEST_CODE = 999;
    public static final int RESULT_SUCCESS = 998;

    private Toolbar toolbar;

    private int settingMode = SETTING_MODE_NORMAL;

    private RecyclerView envList;
    private EnvironmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_clear_black_24dp);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    settingMode = SETTING_MODE_NORMAL;
                    invalidateOptionsMenu();
                }
            });
        }

        envList = (RecyclerView) findViewById(R.id.setting_environment_list);
        envList.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new EnvironmentAdapter(this);
        adapter.addItems(EnvironmentManager.getInstance().getAllItems());
        envList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_toolbar, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        switch (settingMode) {
            case SETTING_MODE_NORMAL:
                setActionBarNormal(menu);
                break;
            case SETTING_MODE_EDIT:
                setActionBarEdit(menu);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_item://新增
                startActivityForResult(new Intent(this,EnvEditActivity.class),REQUEST_CODE);
                break;
            case R.id.action_edit_mode://编辑模式
                settingMode = SETTING_MODE_EDIT;
                invalidateOptionsMenu();
                break;
            case R.id.action_delete_item://删除所选
                EnvironmentManager.getInstance().deleteItems(adapter.getDeleteList());
                adapter.commitEdit();
                break;
            case R.id.action_setting_item://应用设置
                startActivity(new Intent(this,AppSettingActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE){
            if(resultCode==RESULT_SUCCESS){
                //刷新数据
                adapter.clear();
                adapter.addItems(EnvironmentManager.getInstance().getAllItems());
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void setActionBarNormal(Menu menu) {
        MenuItem delete = menu.findItem(R.id.action_delete_item);
        if (delete != null) {
            delete.setVisible(false);
        }
        MenuItem add = menu.findItem(R.id.action_add_item);
        if (add != null) {
            add.setVisible(true);
        }
        MenuItem editMode = menu.findItem(R.id.action_edit_mode);
        if (editMode != null) {
            editMode.setVisible(true);
        }
        MenuItem setting = menu.findItem(R.id.action_setting_item);
        if (setting != null) {
            setting.setVisible(true);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDefaultDisplayHomeAsUpEnabled(false);
            toolbar.setNavigationIcon(null);
        }

        adapter.setType(EnvironmentAdapter.Type.NORMAL);
    }

    private void setActionBarEdit(Menu menu) {
        MenuItem delete = menu.findItem(R.id.action_delete_item);
        if (delete != null) {
            delete.setVisible(true);
        }
        MenuItem add = menu.findItem(R.id.action_add_item);
        if (add != null) {
            add.setVisible(false);
        }
        MenuItem editMode = menu.findItem(R.id.action_edit_mode);
        if (editMode != null) {
            editMode.setVisible(false);
        }
        MenuItem setting = menu.findItem(R.id.action_setting_item);
        if (setting != null) {
            setting.setVisible(false);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_clear_black_24dp);
        }

        adapter.setType(EnvironmentAdapter.Type.EDIT);
    }
}
