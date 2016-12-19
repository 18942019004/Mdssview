package com.gdmss.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.Player.Core.PlayerClient;
import com.Player.web.response.ResponseCommon;
import com.gdmss.R;
import com.gdmss.base.BaseActivity;
import com.gdmss.entity.PlayNode;
import com.utils.T;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 修改通道名称
 */

public class AcModifyName extends BaseActivity
{
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.btn_sure)
    Button btnSure;
    PlayNode node;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_modifyname);
        ButterKnife.bind(this);
        initParam();
        initViews();
    }

    void initParam()
    {
        PlayNode tempNode = (PlayNode) getIntent().getSerializableExtra("node");
        node = app.cameraMap.get(tempNode.getParentId() + "").get(tempNode.getChNo());
    }

    void initViews()
    {
        if (null == node)
        {
            T.showS("获取通道信息失败");
            finish();
        }
        etName.setText(node.getName());
        btnSure.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkInput();
            }
        });
    }

    void checkInput()
    {
        name = etName.getText().toString().trim();
        if (TextUtils.isEmpty(name))
        {
            T.showS("输入不能为空");
            return;
        }
        showProgress();
        startThread();
    }

    @Override
    protected void runInThread()
    {
        app.client.modifyNodeInfo(node.getNodeId() + "",name,node.getInfo().iNodeType,node.node.id_type,node.getInfo().dwVendorId,node.umid,node.getInfo().pAddress,node.getInfo().devport,node.getInfo().pDevUser,node.getInfo().pDevPwd,node.getInfo().iChNo,node.getInfo().streamtype,handler);
    }

    private Handler handler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            dismissProgress();
            ResponseCommon response = (ResponseCommon) msg.obj;
            if (null != response)
            {
                if (null != response.h)
                {
                    if (response.h.e == 200)
                    {
                        T.showS("修改成功");
                        app.cameraMap.get(node.getParentId() + "").get(node.getChNo()).node.sNodeName = name;
                        finish();
                    }
                    else
                    {
                        T.showS("修改失败");
                    }
                }
            }
            return false;
        }
    });
}
