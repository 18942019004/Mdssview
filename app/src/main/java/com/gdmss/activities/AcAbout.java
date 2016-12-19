package com.gdmss.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.gdmss.R;
import com.gdmss.base.BaseActivity;
import com.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/9.
 */

public class AcAbout extends BaseActivity
{
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_about);
        ButterKnife.bind(this);
        initView();
    }

    void initView()
    {
        String sVersion = Utils.getVersionName(context);
        if (!TextUtils.isEmpty(sVersion))
        {
            tvVersion.setText(getString(R.string.app_name) + ":" + sVersion);
        }
        tvTitle.setText(getString(R.string.title_about) + getString(R.string.app_name));
    }
}
