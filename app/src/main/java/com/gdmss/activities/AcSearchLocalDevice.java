package com.gdmss.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.Player.Core.PlayerClient;
import com.Player.Source.TSearchDev;
import com.gdmss.R;
import com.gdmss.adapter.LocalDeviceAdapter;
import com.gdmss.base.BaseActivity;
import com.utils.ThreadPool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 搜索本地设备
 */

public class AcSearchLocalDevice extends BaseActivity implements Runnable, AdapterView.OnItemClickListener
{
    List<TSearchDev> list;
    @BindView(R.id.lv_localdevices)
    ListView lvLocaldevices;

    boolean isIp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_searchlocaldevice);
        ButterKnife.bind(this);
        lvLocaldevices.setOnItemClickListener(this);
        initParam();
        startSearch();
    }

    void initParam()
    {
        isIp = getIntent().getBooleanExtra("isIp",false);
        list = new ArrayList<>();
    }

    void startSearch()
    {
        showProgress();
        ThreadPool.getInstance().submit(this);
    }

    @Override
    public void run()
    {
        super.run();
        PlayerClient client = new PlayerClient();
        int result = client.StartSearchDev(10);
        for (int i = 0; i < result; i++)
        {
            TSearchDev searchRet = client.SearchDevByIndex(i);
            list.add(searchRet);
        }
        client.StopSearchDev();
        han.sendEmptyMessage(1);
    }

    private Handler han = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message message)
        {
            dismissProgress();
            LocalDeviceAdapter adapter = new LocalDeviceAdapter(AcSearchLocalDevice.this,list);
            adapter.isIp = isIp;
            lvLocaldevices.setAdapter(adapter);
            return false;
        }
    });

    @Override
    public void onItemClick(AdapterView<?> adapterView,View view,int i,long l)
    {
        Intent it;
        if (isIp)
        {
            it = new Intent(this,AcAddIP.class);
        }
        else
        {
            it = new Intent(this,AcAddP2p.class);
        }
        it.putExtra("device",list.get(i));
        setResult(RESULT_OK,it);
        finish();
    }
}
