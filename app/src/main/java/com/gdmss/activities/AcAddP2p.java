package com.gdmss.activities;

import com.Player.Core.PlayerClient;
import com.Player.Source.TDevNodeInfor;
import com.Player.Source.TSearchDev;
import com.Player.web.response.ResponseCommon;
import com.gdmss.R;
import com.gdmss.base.BaseActivity;
import com.gdmss.entity.PlayNode;
import com.utils.T;
import com.widget.ShowProgress;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;


public class AcAddP2p extends BaseActivity implements OnClickListener, AdapterView.OnItemClickListener
{
    private ImageView img;

    private EditText et_name, et_seria, et_user, et_password;

    private String name, seria, user, password;

    private ShowProgress progress;

    private AppCompatDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_addp2p);
        initParam();
        initView();
        setViews();
    }

    void initView()
    {
        progress = new ShowProgress(this);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        img = (ImageView) findViewById(R.id.img_code);

        et_name = (EditText) findViewById(R.id.et_devicename);

        et_seria = (EditText) findViewById(R.id.et_seria);

        et_user = (EditText) findViewById(R.id.et_username);

        et_password = (EditText) findViewById(R.id.et_pwd);

        findViewById(R.id.btn_save).setOnClickListener(this);

        findViewById(R.id.btn_search).setOnClickListener(this);

        img.setOnClickListener(this);
    }

    void setViews()
    {
        if (!isModify)
        {
            return;
        }
        img.setVisibility(View.GONE);
        et_name.setText(node.getName());
        et_seria.setText(node.umid);
        et_user.setText(node.getUserName());
        et_password.setText(node.getPassWord());
    }

    void getInput()
    {
        name = et_name.getText().toString();

        seria = et_seria.getText().toString();

        user = et_user.getText().toString();

        password = et_password.getText().toString();
    }

    void checkInput()
    {
        if (!TextUtils.isEmpty(name))
        {
            if (!TextUtils.isEmpty(seria))
            {
                if (!TextUtils.isEmpty(user))
                {
                    getChannel();
                    return;
                }
            }
        }
        T.showS(R.string.msg_input_cannot_empty);
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
                int result = client.CameraGetDevChNum(seria,user,password);
                chHandler.sendEmptyMessage(result);
            }
        }).start();
    }

    private Handler chHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            if (msg.what > 1)
            {
                int chNo = msg.what;
                app.client.addNodeInfo(name,0,1,2,vendorID(seria),seria,"",0,user,password,chNo,chNo,1,addHandler);
            }
            else
            {
                progress.dismiss();
                showChannelDialog();
                T.showS(R.string.addp2p_getchannelfail);
            }
            return false;
        }
    });

    /**
     * 获取通道失败后弹出选择通道对话框
     */
    void showChannelDialog()
    {
        if (null == dialog)
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
                    //                    client.getNodeList(false, 0, 0, 0, getDevHandler);
                    // handler.sendEmptyMessage(AddThread.OK);
                    app.getListFromServer();
                    if (isModify)
                    {
                        T.showS(R.string.addp2p_modify_success);
                        finish();
                    }
                    else
                    {
                        T.showS(R.string.addp2p_add_success);
//                        Intent it = new Intent(AcAddP2p.this,AcMain.class);
//                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(it);
                        finish();
                    }
                }
            }
            return false;
        }
    });

    int vendorID(String umid)
    {
        if (umid.length() == 16)
        {
            return PlayerClient.NPC_D_MON_VENDOR_ID_HZXM;
        }
        else if (umid.substring(0,2).contains("xm") || umid.substring(0,2).contains("Xm") || umid.substring(0,2).contains("xM"))
        {
            return PlayerClient.NPC_D_MON_VENDOR_ID_HZXM;
        }
        else
        {
            return PlayerClient.NPC_D_MON_VENDOR_ID_UMSP;
        }
    }

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
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_save:
//                showChannelDialog();
                getInput();
                if (isModify)
                {
                    progress.show();
                    app.client.modifyNodeInfo(node.getNodeId() + "",name,info.iNodeType,info.devicetype,node.node.usVendorId,seria,info.pAddress,info.devport,user,password,info.iChNo,info.streamtype,addHandler);
                }
                else
                {
                    checkInput();
                }
                break;
            case R.id.img_code:
                Intent it = new Intent(this,AcCode.class);
                startActivityForResult(it,888);
                break;
            case R.id.btn_search:
                startActivityForResult(new Intent(this,AcSearchLocalDevice.class),999);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if (requestCode == 888 && resultCode == RESULT_OK)
        {
            et_seria.setText(data.getStringExtra("serianumber"));
        }
        else if (requestCode == 999 && resultCode == RESULT_OK)
        {
            TSearchDev info = (TSearchDev) data.getSerializableExtra("device");
            if (null == info)
            {
                return;
            }
            et_name.setText(info.sDevName);
            et_seria.setText(info.sDevId);
            et_user.setText(info.sDevUserName);
        }
        super.onActivityResult(requestCode,resultCode,data);
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
