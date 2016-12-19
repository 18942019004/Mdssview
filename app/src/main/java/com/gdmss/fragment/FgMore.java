package com.gdmss.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.Player.web.response.ResponseCommon;
import com.gdmss.activities.AcAbout;
import com.gdmss.activities.AcAccountManage;
import com.gdmss.activities.AcLocalSetting;
import com.gdmss.activities.AcLogin;
import com.gdmss.R;
import com.gdmss.base.BaseFragment;
import com.utils.L;
import com.widget.SlidingMenu;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Lee
 */

public class FgMore extends BaseFragment implements View.OnClickListener
{
    @BindView(R.id.tv_usermanage)
    TextView tvUsermanage;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @BindView(R.id.btn_logout)
    Button btnLogout;

    public FgMore()
    {
        super();
    }

    public static FgMore getInstance(SlidingMenu menu)
    {
        FgMore instance = new FgMore();
        instance.menu = menu;
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (null == view)//mdssview_Android_V1.2_Build20161213_0958_signed.apk
        {
            view = inflater.inflate(R.layout.fg_more, container, false);
            ButterKnife.bind(this, view);
            setViews();
        }
        return view;
    }

    void setViews()
    {
        tvUsermanage.setOnClickListener(this);
        tvSetting.setOnClickListener(this);
        tvAbout.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        if (app.client.isLocalList())
            tvUsermanage.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_usermanage:
                startActivity(new Intent(getActivity(), AcAccountManage.class));
                break;
            case R.id.tv_setting:
                startActivity(new Intent(getActivity(), AcLocalSetting.class));
                break;
            case R.id.tv_about:
                startActivity(new Intent(getActivity(), AcAbout.class));
                break;
            case R.id.btn_logout:
                progress.show();
                app.client.logoutServer(1, logoutHan);
                break;
        }
    }

    private Handler logoutHan = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            progress.dismiss();
            ResponseCommon response = (ResponseCommon) msg.obj;
            if (null != response && null != response.h)
            {
                L.e("成功注销");
                getActivity().finish();
                Intent it = new Intent(getActivity(), AcLogin.class);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP & Intent.FLAG_ACTIVITY_NEW_TASK);
                app.clearData();
                it.putExtra("logout", true);
                startActivity(it);
            }
            return false;
        }
    });
}
