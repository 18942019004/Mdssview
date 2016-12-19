package com.gdmss.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.Player.web.response.ResponseCommon;
import com.gdmss.R;
import com.gdmss.base.BaseActivity;
import com.gdmss.entity.PlayNode;
import com.utils.T;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/25.
 */

public class AcDeviceSetting extends BaseActivity implements View.OnClickListener
{


    @BindView(R.id.tv_modifyparam)
    TextView tvModifyparam;
    @BindView(R.id.tv_modifypwd)
    TextView tvModifypwd;
    @BindView(R.id.tv_synctime)
    TextView tvSynctime;

    PlayNode node;
    @BindView(R.id.btn_delete)
    Button btnDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_devicesetting);
        ButterKnife.bind(this);
        initParam();
        setListeners();
    }

    void initParam()
    {
        node = (PlayNode) getIntent().getSerializableExtra("node");
    }

    void setListeners()
    {
        tvModifyparam.setOnClickListener(this);
        tvModifypwd.setOnClickListener(this);
        tvSynctime.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent it = null;
        switch (v.getId())
        {
            case R.id.tv_modifyparam:
                if (node.node.iConnMode == 2)
                {
                    it = new Intent(this,AcAddP2p.class);
                }
                else
                {
                    it = new Intent(this,AcAddIP.class);
                }
                break;
            case R.id.tv_modifypwd:
                it = new Intent(this,AcModifyDevicePassword.class);
                break;
            case R.id.tv_synctime:
                it = new Intent(this,AcModifyDeviceTime.class);
                break;
            case R.id.btn_delete:
                showProgress();
                app.client.deleteNodeInfo(node.getNodeId() + "",node.node.iNodeType,node.node.id_type,deleteHan);
                return;
        }
//        assert it != null;
        it.putExtra("node",node);
        startActivity(it);
    }

    private Handler deleteHan = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            dismissProgress();
            ResponseCommon response = (ResponseCommon) msg.obj;
            if (null != response && null != response.h)
            {
                if (response.h.e == 200)
                {
                    app.getListFromServer();
                    T.showS(R.string.msg_delete_success);
                    finish();
                }
            }
            return false;
        }
    });
}
