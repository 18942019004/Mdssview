package com.gdmss.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.Player.Core.PlayerClient;
import com.Player.Source.TDevNodeInfor;
import com.Player.Source.TSearchDev;
import com.Player.web.response.ResponseCommon;
import com.gdmss.R;
import com.gdmss.base.BaseActivity;
import com.gdmss.entity.PlayNode;
import com.utils.L;
import com.utils.T;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AcAddIP extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener
{
    @BindView(R.id.leftBtn)
    Button leftBtn;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.et_deviceName)
    EditText etDeviceName;
    @BindView(R.id.et_ipaddress)
    EditText etIpaddress;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_port)
    EditText etPort;
    @BindView(R.id.btn_search)
    Button btnSearch;

    private String sDevName, sIpAddress, sPort, sUserName, sPwd;

    private AppCompatDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_addip);
        ButterKnife.bind(this);
        btnSave.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        initParam();
        setViews();
    }

    void setViews()
    {
        if (!isModify)
        {
            return;
        }
        etDeviceName.setText(node.getName());
        etIpaddress.setText(info.pAddress);
        etPort.setText(info.devport + "");
        etUsername.setText(info.pDevUser);
        etPwd.setText(info.pDevPwd);
    }

    void getInput()
    {
        sDevName = etDeviceName.getText().toString();
        sIpAddress = etIpaddress.getText().toString();
        sPort = etPort.getText().toString();
        sUserName = etUsername.getText().toString();
        sPwd = etPwd.getText().toString();
        if (TextUtils.isEmpty(sPwd))
        {
            sPwd = "";
        }
    }

    boolean checkInput()
    {
        if (!TextUtils.isEmpty(sDevName))
        {
            if (!TextUtils.isEmpty(sIpAddress))
            {
                if (!TextUtils.isEmpty(sPort))
                {
                    if (!TextUtils.isEmpty(sUserName))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    void addIPDevice()
    {
        getInput();
        if (checkInput())
        {
            if (isModify)
            {
                app.client.modifyNodeInfo(node.getNodeId() + "",sDevName,info.iNodeType,info.devicetype,node.node.usVendorId,info.pDevId,sIpAddress,Integer.valueOf(sPort),sUserName,sPwd,info.iChNo,info.streamtype,addHandler);
            }
            else
            {
                getChannel();
            }
        }
    }

    void getChannel()
    {
        progress.show();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                PlayerClient client = new PlayerClient();
//                int result = client.CameraGetDevChNum(sIpAddress,sUserName,sPwd);
                int result = client.CameraGetDevChNum(2060,sIpAddress,Integer.valueOf(sPort),sUserName,sPwd);
                chHandler.sendEmptyMessage(result);
            }
        }).start();
    }

    /**
     * 获取通道失败后弹出选择通道对话框
     */
    void showChannelDialog()
    {
        if (dialog == null)
        {
            View v = LayoutInflater.from(this).inflate(R.layout.layout_choose_channel_no,null);
            ListView listView = (ListView) v.findViewById(R.id.lv_channelnumber);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.channels));
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
            dialog = new AppCompatDialog(this,R.style.chooseChannelDialog);
            dialog.setContentView(v);
        }
        dialog.show();
    }

    private Handler chHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            boolean islocal = app.client.isLocalList();
            L.e("islocal"+islocal);
            if (msg.what >= 1)
            {
                int chNo = msg.what;
                app.client.addNodeInfo(sDevName,0,1,0,sPort.equals("34567") ? 2060 : 1009,"",sIpAddress,Integer.valueOf(sPort),sUserName,sPwd,chNo,chNo,1,addHandler);
            }
            else
            {
                progress.dismiss();
                showChannelDialog();
                T.showS(R.string.addip_getchannelfail);
            }
            return false;
        }
    });

    private Handler addHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            progress.dismiss();
            ResponseCommon responseCommon = (ResponseCommon) msg.obj;
            if (responseCommon != null && responseCommon.h != null)
            {
                if (responseCommon.h.e == 200)
                {
                    app.getListFromServer();
                    T.showS(R.string.addip_add_success);
//                    Intent it = new Intent(AcAddIP.this,AcMain.class);
//                    it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(it);
                    finish();
                }
            }
            return false;
        }
    });

    PlayNode node;

    TDevNodeInfor info;

    boolean isModify;

    void initParam()
    {
        node = (PlayNode) getIntent().getSerializableExtra("node");
        if (null != node)
        {
            isModify = true;
            info = node.getInfo();
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if (requestCode == 999 && resultCode == RESULT_OK)
        {
            TSearchDev info = (TSearchDev) data.getSerializableExtra("device");
            if (null == info)
            {
                return;
            }
            etDeviceName.setText(info.sDevName);
            etIpaddress.setText(info.sIpaddr_1);
            etPort.setText(info.iDevPort + "");
            etUsername.setText(info.sDevUserName);
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.btn_search)
        {

            Intent it = new Intent(this,AcSearchLocalDevice.class);
            it.putExtra("isIp",true);
            startActivityForResult(it,999);
        }
        else
        {
            addIPDevice();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView,View view,int i,long l)
    {
        String[] channels = getResources().getStringArray(R.array.channels);
        dialog.dismiss();
        showProgress();
        chHandler.sendEmptyMessage(Integer.valueOf(channels[i]));
    }
}
