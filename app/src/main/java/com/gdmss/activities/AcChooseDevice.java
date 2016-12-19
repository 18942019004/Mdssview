

package com.gdmss.activities;


import com.gdmss.R;
import com.gdmss.adapter.DevicelistAdapter_E;
import com.gdmss.base.APP;
import com.gdmss.base.BaseActivity;
import com.gdmss.entity.PlayNode;
import com.gdmss.fragment.FgPlay;
import com.utils.DataUtils;
import com.utils.ScreenUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import java.util.List;


public class AcChooseDevice extends BaseActivity implements OnChildClickListener, OnGroupClickListener, View.OnClickListener
{
    private ExpandableListView elv_devicelist;

    private DevicelistAdapter_E devicelistAdapter;

    private Button btn_toPlay;

    private APP app;

    private boolean isSelectMode = false;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_choose_device);
        //		setWindowSize();
        getParam();
        initViews();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        devicelistAdapter.getSelectedCount();
    }

    private void setWindowSize()
    {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (ScreenUtils.getScreenWidth(this) * 0.9);
        params.height = (int) (ScreenUtils.getScreenHeight(this) * 0.95);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    void getParam()
    {
        app = (APP) getApplication();

        isSelectMode = getIntent().getBooleanExtra("isSelectMode",false);
    }

    private void initViews()
    {
        btn_toPlay = (Button) findViewById(R.id.btn_addToPlay);

        elv_devicelist = (ExpandableListView) findViewById(R.id.elv_devicelist);

        devicelistAdapter = new DevicelistAdapter_E(this);
        devicelistAdapter.setBtnPlay(btn_toPlay);
        elv_devicelist.setAdapter(devicelistAdapter);
        elv_devicelist.setOnGroupClickListener(this);
        elv_devicelist.setOnChildClickListener(this);
        btn_toPlay.setOnClickListener(this);


        devicelistAdapter.setSelectMode(isSelectMode);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent,View v,int groupPosition,int childPosition,long id)
    {
        PlayNode node = null;
        String key = devicelistAdapter.getKey(groupPosition);
        node = app.cameraMap.get(key).get(childPosition);
        if (!isSelectMode)
        {
            Intent it = new Intent(context,AcMain.class);
            it.putExtra("node",node);
            setResult(RESULT_OK,it);
            finish();
        }
        else
        {
            if (!node.isSelected)
            {
                if (devicelistAdapter.getSelectedCount() >= devicelistAdapter.maxChnnelCount)
                {
                    return false;
                }
            }
            node.isSelected = !node.isSelected;
            referenceParent(node);
            devicelistAdapter.getSelectedCount();
        }
        return false;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent,View v,int groupPosition,long id)
    {
        PlayNode tempNode = (PlayNode) devicelistAdapter.getGroup(groupPosition);
        if (tempNode.isCamera() && !isSelectMode)
        {
            String key = devicelistAdapter.getKey(groupPosition);
            PlayNode result = DataUtils.getCamera(Integer.parseInt(key),app);
            if (null != result)
            {
                Intent it = new Intent(context,AcMain.class);
                it.putExtra("node",result);
                setResult(RESULT_OK,it);
                finish();
            }
        }
        return false;
    }

    void referenceParent(PlayNode node)
    {
        String key = node.getParentId() + "";

        PlayNode parentNode = DataUtils.getNode(app,node.getParentId(),true);

        int count = 0;
        for (int i = 0; i < app.cameraMap.get(key).size(); i++)
        {
            if (app.cameraMap.get(key).get(i).isSelected)
            {
                count++;
            }
        }
        //parentNode.isSelected = count == parentNode.channels.size();
        parentNode.isSelected = count > 0;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v)
    {
        List<PlayNode> tempList = devicelistAdapter.getSelectedNode();
        Intent it = new Intent(AcChooseDevice.this,AcMain.class);
        //        it.putParcelableArrayListExtra(tempList);
        ((FgPlay) app.fragments_live.get(0)).nodeList.clear();
        ((FgPlay) app.fragments_live.get(0)).nodeList.addAll(tempList);
        setResult(RESULT_OK,it);
        finish();
    }
}