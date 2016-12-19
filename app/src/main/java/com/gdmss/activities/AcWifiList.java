package com.gdmss.activities;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.net.wifi.WifiInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.Player.Core.PlayerClient;
import com.Player.Source.TDevWifiInfor;
import com.Player.Source.TVideoFile;
import com.Player.Source.TWifiApInfor;
import com.gdmss.R;
import com.gdmss.adapter.WifiAdapter;
import com.gdmss.base.BaseActivity;
import com.gdmss.entity.PlayNode;
import com.utils.T;
import com.utils.WifiAdmin;
import com.utils.WifiSetThread;
import com.widget.ToggleButton;

public class AcWifiList extends BaseActivity implements OnClickListener, OnItemClickListener
{
    WifiAdapter adapter;
    ListView listView;
    String title;
    public static TVideoFile videoFile;
    public String deviceId;
    private EditText etSsid, etWifiPass;
    private Button btnAsyncSure, btnAsyncCancel;
    private CheckBox ckShowPass;
    private PlayNode node;
    private Dialog asyncDialog;
    private TDevWifiInfor devWifiInfo;
    private ArrayList<TWifiApInfor> list;
    ToggleButton toggleButton;
    TextView tvSSID;// ��ǰ���ӵ�wifi
    WifiAdmin wifiAdmin;
    private Spinner spChannel;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            dismissProgress();
            switch (msg.what)
            {
                case WifiSetThread.SET_OK:
                    T.showS(R.string.msg_set_params_success);
                    if (asyncDialog != null)
                    {
                        asyncDialog.dismiss();
                    }
                    if (devWifiInfo != null)
                    {
                        if (devWifiInfo.bEnable == 0)
                        {
                            finish();
                        }
                        else
                        {

                            tvSSID.setText(devWifiInfo.sWifiSSID + "");
                            if (!isSearch)
                            {
                                new ThreadSearchDevice().execute();
                            }

                        }

                    }

                    break;
                case WifiSetThread.SET_FALL:
                    T.showS(R.string.msg_set_params_fail);
                    break;
                default:
                    break;
            }

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_wifi_list);
        int nodeID = getIntent().getIntExtra("currentId",0);
        // PlayNode parentNode = CommonData.GetCurrentParent(nodeID,
        // app.getNodeList());
        // List<PlayNode> childList = CommonData.GetChildListById(nodeID,
        // app.getNodeList());
        // initeSpiner(childList);
        node = (PlayNode) getIntent().getSerializableExtra("node");// CommonData.getPlayNode(app.getNodeList(),nodeID);
        listView = (ListView) findViewById(R.id.lvLive);
        listView.setOnItemClickListener(this);
        adapter = new WifiAdapter(this);
        listView.setAdapter(adapter);
        findViewById(R.id.menu_btn1).setOnClickListener(this);
        toggleButton = (ToggleButton) findViewById(R.id.toggle_wifi);
        toggleButton.setOnClickListener(this);
        tvSSID = (TextView) findViewById(R.id.current_ssid);
        new WifiThread().execute();
    }

    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.toggle_wifi:
                toggleButton = (ToggleButton) findViewById(R.id.toggle_wifi);
                initeview(toggleButton.toggleOn);
                if (devWifiInfo != null)
                {
                    devWifiInfo.bEnable = toggleButton.toggleOn ? 1 : 0;
                    showProgress();
                    new WifiSetThread(new PlayerClient(),node.getDeviceId(),devWifiInfo,handler).start();
                }

                break;
            case R.id.menu_btn1:
                if (!isSearch)
                {
                    new ThreadSearchDevice().execute();
                }
                break;
            case R.id.btn_async_cancel:
                if (asyncDialog != null)
                {
                    asyncDialog.dismiss();
                }
                break;
            case R.id.btn_async_sure:
                // devWifiInfo = AcWifiSettings.devWifiInfo;
                if (devWifiInfo != null)
                {
                    String ssid = etSsid.getText().toString();
                    String pass = etWifiPass.getText().toString();
                    if (TextUtils.isEmpty(ssid) || TextUtils.isEmpty(pass))
                    {
                        T.showS(R.string.NPC_D_MPI_MON_ERROR_USER_PWD_ERROR);
                        break;
                    }
                    devWifiInfo.bEnable = 1;
                    devWifiInfo.sWifiPwd = pass;
                    devWifiInfo.sWifiSSID = ssid;

                    devWifiInfo.bFieldEnable_AuthType = 0;
                    devWifiInfo.bIfSetNetParam = 1;
                    devWifiInfo.bDhcpEnable = 1;
                    showProgress();
                    new WifiSetThread(new PlayerClient(),node.getDeviceId(),devWifiInfo,handler).start();
                }

                break;
            default:
                break;
        }

    }

    public void initeview(boolean isWifiOn)
    {
        if (isWifiOn)
        {
            listView.setVisibility(View.VISIBLE);
            findViewById(R.id.rl_ssid).setVisibility(View.VISIBLE);

        }
        else
        {
            listView.setVisibility(View.GONE);
            findViewById(R.id.rl_ssid).setVisibility(View.GONE);

        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0,View arg1,int arg2,long arg3)
    {
        TWifiApInfor info = list.get(arg2);
        showDialog(info);
        // if (devWifiInfo != null) {
        // if (devWifiInfo.bEnable == 0) {
        // devWifiInfo.bEnable = 1;
        // }
        // String ssid = etSsid.getText().toString();
        // String pass = etWifiPass.getText().toString();
        // if (TextUtils.isEmpty(ssid) && TextUtils.isEmpty(pass)) {
        // Show.toast(this, R.string.NPC_D_MPI_MON_ERROR_USER_PWD_ERROR);
        // return;
        // }
        // devWifiInfo.sWifiPwd = pass;
        // devWifiInfo.sWifiSSID = ssid;
        // showProgressDialog("");
        // new WifiSetThread(app.getPlayerclient(), node.getDeviceId(),
        // devWifiInfo, handler).start();
        //
        // }
    }

    /**
     * ͬ���ֻ�����
     */
    void showDialog(TWifiApInfor info)
    {

        if (asyncDialog == null)
        {
            asyncDialog = new Dialog(this,R.style.smsDialog);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_dialog_wifi_synac_to_device,null);
            view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
            etSsid = (EditText) view.findViewById(R.id.wifi_enter_ssid);
            etWifiPass = (EditText) view.findViewById(R.id.wifi_enter_pass);
            ckShowPass = (CheckBox) view.findViewById(R.id.ck_show_pass);
            btnAsyncSure = (Button) view.findViewById(R.id.btn_async_sure);
            btnAsyncSure.setOnClickListener(this);
            btnAsyncCancel = (Button) view.findViewById(R.id.btn_async_cancel);
            btnAsyncCancel.setOnClickListener(this);
            // ����ɼ�����
            // ����ɼ�����
            ckShowPass.setOnCheckedChangeListener(new OnCheckedChangeListener()
            {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
                {
                    // TODO Auto-generated method stub
                    if (isChecked)
                    {
                        etWifiPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    else
                    {
                        etWifiPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }

                }
            });
            asyncDialog.setContentView(view);
            asyncDialog.setCanceledOnTouchOutside(true);
        }
        etSsid.setText(info.sSSID);
        asyncDialog.show();
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = asyncDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()) - 100; // ���ÿ��
        asyncDialog.getWindow().setAttributes(lp);
    }

    boolean isSearch = false;

    public class ThreadSearchDevice extends AsyncTask<Void, Void, List<TWifiApInfor>>
    {

        @Override
        protected List<TWifiApInfor> doInBackground(Void... params)
        {
            // TODO Auto-generated method stub
            isSearch = true;
            list = new ArrayList<TWifiApInfor>();
            PlayerClient playerclient = new PlayerClient();
            int searchRet = playerclient.CameraSearchWifiApEx(node.getDeviceId());
            // playerclient.CameraSetWIFIConfig(deviceId, DevWifiInforobj);
            for (int i = 0; i < searchRet; i++)
            {
                TWifiApInfor tsearch = playerclient.CLTGetWifiApInfo(i);
                if (tsearch != null)
                {
                    if (!TextUtils.isEmpty(tsearch.sSSID))
                    {
                        Log.w("searchRet","sSSID :" + tsearch.sSSID + " , " + tsearch.iChannel + " , "
                                + tsearch.sEncrypType + "," + tsearch.sAuthType + ",�ź�ǿ�ȣ�" + tsearch.iRSSI);

                        list.add(copyInfo(tsearch));
                    }

                }
                else
                {
                    break;
                }

            }
            isSearch = false;
            return list;
        }

        TWifiApInfor copyInfo(TWifiApInfor info)
        {
            TWifiApInfor i = new TWifiApInfor();
            i.iChannel = info.iChannel;
            i.sAuthType = info.sAuthType;
            i.sEncrypType = info.sEncrypType;
            i.sSSID = info.sSSID;
            i.iRSSI = info.iRSSI;
            return i;

        }

        @Override
        protected void onPostExecute(List<TWifiApInfor> flist)
        {
            // TODO Auto-generated method stub
            dismissProgress();
            if (list.size() > 0)
            {
                listView.setVisibility(View.VISIBLE);
                adapter.setNodeList(list);
                // listView.startLayoutAnimation();
            }
            else
            {
                listView.setVisibility(View.INVISIBLE);
                T.showS(R.string.wifisetting_nodataerro);
            }

            super.onPostExecute(list);
        }

        @Override
        protected void onPreExecute()
        {
            // TODO Auto-generated method stub
            showProgress();
            if (list != null)
            {
                list.clear();
            }

            super.onPreExecute();
        }
    }

    class WifiThread extends AsyncTask<Void, Integer, Void>
    {
        String ssid = "";

        @Override
        protected Void doInBackground(Void... params)
        {
            // TODO Auto-generated method stub
            if (!wifiAdmin.checkNetWorkState())
            {

                publishProgress(0);
            }
            WifiInfo wifiInfo = wifiAdmin.getConnectInfo();
            wifiAdmin.getScanResult();
            if (wifiInfo != null)
            {
                ssid = wifiInfo.getSSID();
            }
            PlayerClient playerclient = new PlayerClient();
            devWifiInfo = playerclient.CameraGetWIFIConfigEx(node.getDeviceId());
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            // TODO Auto-generated method stub
            if (devWifiInfo != null)
            {
                tvSSID.setText(devWifiInfo.sWifiSSID + "");
                Log.d("devWifiInfo",
                        ",devWifiInfo.bDhcpEnable:" + devWifiInfo.bDhcpEnable + ",devWifiInfo.sWifiPwd:"
                                + devWifiInfo.sWifiPwd + ",devWifiInfo.sWifiSSID:" + devWifiInfo.sWifiSSID
                                + ",devWifiInfo.sGateway:" + devWifiInfo.sGateway);
                initeview(devWifiInfo.bEnable == 1);
                toggleButton.setChecked(devWifiInfo.bEnable == 1);
                if (devWifiInfo.bEnable == 1)
                {
                    if (!isSearch)
                    {
                        new ThreadSearchDevice().execute();
                    }
                }
            }
            else
            {
                // Show.toast(AcWifiList.this, R.string.wifi_get_failre);
            }
            dismissProgress();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute()
        {
            // TODO Auto-generated method stub
            showProgress();
            wifiAdmin = new WifiAdmin(AcWifiList.this);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            // TODO Auto-generated method stub
            if (values[0] == 0)
            {
                // Show.toast(AcWifiSettings.this, "���ڿ���WIFI...");
            }
            super.onProgressUpdate(values);
        }

    }

    @Override
    protected void onDestroy()
    {
        // TODO Auto-generated method stub
        new Thread()
        {

            @Override
            public void run()
            {
                PlayerClient playerclient = new PlayerClient();
                playerclient.CameraDisconnect();
            }
        }.start();
        super.onDestroy();
    }

    // public void initeSpiner(final List<PlayNode> childList) {}
}
