

package com.gdmss.fragment;


import com.gdmss.activities.AcAddDevice;
import com.gdmss.activities.AcQRCode;
import com.gdmss.R;
import com.gdmss.adapter.DeviceManageAdapter;
import com.gdmss.adapter.DevicelistAdapter;
import com.gdmss.base.APP;
import com.gdmss.base.BaseFragment;
import com.utils.L;
import com.widget.SlidingMenu;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class FgDeviceManage extends BaseFragment implements OnClickListener, OnItemClickListener, OnCheckedChangeListener
{
    private Button btn_add;

    private Button btn_share;

    private Button btn_cancel;

    private CheckBox cb_selectall;

    private ListView lv_device;

    private DevicelistAdapter adapter;

    private ExpandableListView elv_devicelist;

    private DeviceManageAdapter adp;

    public static FgDeviceManage getInstance(SlidingMenu menu)
    {
        FgDeviceManage instance = new FgDeviceManage();
        instance.menu = menu;
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        if (null == view)
        {
            app = (APP) getActivity().getApplication();
            context = app.getApplicationContext();
            view = inflater.inflate(R.layout.fg_devicemanager,container,false);
            initViews();
            initData();
        }
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        adp.notifyDataSetChanged();
    }

    private void initViews()
    {
        btn_add = (Button) view.findViewById(R.id.btn_add);

        lv_device = (ListView) view.findViewById(R.id.lv_device);

        cb_selectall = (CheckBox) view.findViewById(R.id.cb_selectall);

        btn_share = (Button) view.findViewById(R.id.btn_share);

        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        btn_add.setOnClickListener(this);

        btn_share.setOnClickListener(this);

        btn_cancel.setOnClickListener(this);

        cb_selectall.setOnCheckedChangeListener(this);
        //-----------------------------------------------
        elv_devicelist = (ExpandableListView) view.findViewById(R.id.elv_devicelist);

        adp = new DeviceManageAdapter(getActivity(),app);

        elv_devicelist.setAdapter(adp);
    }

    private void initData()
    {
        adapter = new DevicelistAdapter(getActivity(),app.parentList);
        lv_device.setAdapter(adapter);
        lv_device.setOnItemClickListener(this);
    }

    void toEditMode()
    {
        adapter.isEditMode = !adapter.isEditMode;
        requiresOnkeyDown = adapter.isEditMode;
        if (adapter.isEditMode)
        {
            btn_add.setVisibility(View.GONE);
            view.findViewById(R.id.btn_menu).setVisibility(View.GONE);
            btn_cancel.setVisibility(View.VISIBLE);
            cb_selectall.setVisibility(View.VISIBLE);
        }
        else
        {
            btn_add.setVisibility(View.VISIBLE);
            view.findViewById(R.id.btn_menu).setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.GONE);
            cb_selectall.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View paramView)
    {
        switch (paramView.getId())
        {
            case R.id.btn_add:
                Intent it = new Intent(context,AcAddDevice.class);
                startActivity(it);
                break;
            case R.id.btn_share:
                if (!adapter.isEditMode)
                {
                    toEditMode();
                }
                else if (adapter.count > 0)
                {
                    getSelectedInfo();
                }
                break;
            case R.id.btn_cancel:
                toEditMode();
                break;
        }
    }

    private void getSelectedInfo()
    {
        Intent it = new Intent(context,AcQRCode.class);
        it.putExtra("devices",adapter.getSelectedList());
        startActivity(it);
        toEditMode();
    }

    @Override
    public void onItemClick(AdapterView<?> parent,View view,int position,long id)
    {
        if (adapter.isEditMode)
        {
            adapter.selected[position] = !adapter.selected[position];
            if (adapter.selected[position])
            {
                adapter.count++;
            }
            else
            {
                adapter.count--;
            }
            L.e("selectCount:" + adapter.count);
            adapter.notifyDataSetChanged();
        }
        else
        {
            //            showEditDialog(position);
            adapter.setEditItem(position);
            //            showEditView(position);
        }
    }

    private void showEditDialog(int index)
    {
        Dialog dlg = new Dialog(getActivity(),R.style.myProgress);
        View v = adapter.getView(index,null,lv_device);
        v.measure(0,0);
        dlg.setContentView(R.layout.title_back);
        //        dlg.setContentView(v);
        dlg.show();
        Window win = dlg.getWindow();
        WindowManager.LayoutParams param = win.getAttributes();
        param.width = lv_device.getMeasuredWidth();
        param.height = v.getMeasuredHeight();
        param.x = (int) v.getX();
        param.y = 0;//(int) v.getTop();
        win.setAttributes(param);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
    {
        for (int i = 0; i < adapter.selected.length; i++)
        {
            adapter.selected[i] = isChecked;
        }
        adapter.count = isChecked ? adapter.getCount() : 0;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onKeyDown(int keyCode,KeyEvent event)
    {
        toEditMode();
        requiresOnkeyDown = false;
        super.onKeyDown(keyCode,event);
    }
}
