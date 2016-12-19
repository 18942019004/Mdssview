package com.gdmss.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;

import com.Player.Core.PlayerSearchCore;
import com.Player.Source.Date_Time;
import com.Player.Source.TDateTime;
import com.Player.Source.TVideoFile;
import com.gdmss.R;
import com.gdmss.adapter.DevicelistAdapter_E;
import com.gdmss.base.APP;
import com.gdmss.base.BaseActivity;
import com.gdmss.entity.PlayNode;
import com.gdmss.entity.VideoListResult;
import com.gdmss.fragment.FgPlayBack;
import com.utils.DataUtils;
import com.utils.T;
import com.widget.TimePickerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/1.
 */

public class AcChoosePlayBackDevice extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, ExpandableListView.OnChildClickListener
{
    DevicelistAdapter_E adapter;
    @BindView(R.id.btn_addToPlay)
    Button btnAddToPlay;
    @BindView(R.id.elv_devicelist)
    ExpandableListView elvDevicelist;
    @BindView(R.id.cbox_time_start)
    CheckBox cboxTimeStart;
    @BindView(R.id.cbox_time_end)
    CheckBox cboxTimeEnd;
    @BindView(R.id.time_picker)
    TimePickerView timePicker;

    public static TDateTime startTime, endTime;
    private boolean isSelectMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_chooseplaybackdevice);
        ButterKnife.bind(this);
        getParam();
        initViews();
        setViews();
        initTimePicker();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        adapter.getSelectedCount();
    }

    void getParam()
    {
        app = (APP) getApplication();

        isSelectMode = getIntent().getBooleanExtra("isSelectMode",false);
    }

    void initViews()
    {
        btnAddToPlay.setOnClickListener(this);
        adapter = new DevicelistAdapter_E(this);
        adapter.setSelectMode(isSelectMode);
        elvDevicelist.setAdapter(adapter);
        adapter.setBtnPlay(btnAddToPlay);
        elvDevicelist.setOnChildClickListener(this);
        adapter.maxChnnelCount = 4;
    }

    void setViews()
    {
        startTime = new TDateTime();
        endTime = new TDateTime();

        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        startTime.iYear = calendar.get(Calendar.YEAR);
        startTime.iMonth = calendar.get(Calendar.MONTH) + 1;
        startTime.iDay = calendar.get(Calendar.DAY_OF_MONTH);
        startTime.iHour = 0;//calendar.get(Calendar.HOUR_OF_DAY);
        startTime.iMinute = 0;//calendar.get(Calendar.MINUTE);

        endTime.iYear = calendar.get(Calendar.YEAR);
        endTime.iMonth = calendar.get(Calendar.MONTH) + 1;
        endTime.iDay = calendar.get(Calendar.DAY_OF_MONTH);
        endTime.iHour = calendar.get(Calendar.HOUR_OF_DAY);
        endTime.iMinute = calendar.get(Calendar.MINUTE);

        String year = calendar.get(Calendar.YEAR) + "";
        String month = String.format("%02d",(calendar.get(Calendar.MONTH) + 1));
        String day = String.format("%02d",calendar.get(Calendar.DAY_OF_MONTH));

        String hour = String.format("%02d",calendar.get(Calendar.HOUR_OF_DAY));

        String minute = String.format("%02d",calendar.get(Calendar.MINUTE));

        cboxTimeStart.setText(getString(R.string.chooseplayback_starttime) + year + "-" + month + "-" + day + "    00:00");
        cboxTimeEnd.setText(getString(R.string.chooseplayback_endtime) + year + "-" + month + "-" + day + "    " + hour + ":" + minute);

        cboxTimeStart.setOnCheckedChangeListener(this);
        cboxTimeEnd.setOnCheckedChangeListener(this);
    }

    void initTimePicker()
    {
        timePicker.setmCallBack(new TimePickerView.OnDateTimeSetListener()
        {
            @Override
            public void onDateTimeSet(int year,int monthOfYear,int dayOfMonth,int hour,int minute)
            {
                String text = year + "-" + String.format("%02d",monthOfYear) + "-"
                        + String.format("%02d",dayOfMonth) + "    " + String.format("%02d",hour) + ":"
                        + String.format("%02d",minute);
                if (cboxTimeEnd.isChecked())
                {
                    cboxTimeEnd.setText("结束时间:" + text);
                    endTime.iYear = (short) year;
                    endTime.iMonth = (short) monthOfYear;
                    endTime.iDay = (byte) dayOfMonth;
                    endTime.iHour = (byte) hour;
                    endTime.iMinute = (byte) minute;
                    endTime.iSecond = 0;
                }
                else
                {
                    cboxTimeStart.setText("开始时间:" + text);
                    startTime.iYear = (short) year;
                    startTime.iMonth = (short) monthOfYear;
                    startTime.iDay = (byte) dayOfMonth;
                    startTime.iHour = (byte) hour;
                    startTime.iMinute = (byte) minute;
                    startTime.iSecond = 0;
                }
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        List<PlayNode> tempList = adapter.getSelectedNode();
        ((FgPlayBack) app.fragments_live.get(1)).nodeList.clear();
        ((FgPlayBack) app.fragments_live.get(1)).nodeList.addAll(tempList);
        multiSearch(tempList);
//        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
    {
        if (buttonView.getId() == R.id.cbox_time_end)
        {
            if (isChecked)
            {
                cboxTimeStart.setChecked(false);
            }
        }
        else if (buttonView.getId() == R.id.cbox_time_start)
        {
            if (isChecked)
            {
                cboxTimeEnd.setChecked(false);
            }
        }
        if (isChecked)
        {
            if (timePicker.getVisibility() == View.GONE)
            {
                timePicker.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            if (!cboxTimeStart.isChecked() && !cboxTimeEnd.isChecked())
            {
                timePicker.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent,View v,int groupPosition,int childPosition,long id)
    {

        PlayNode node;
        String key = adapter.getKey(groupPosition);
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
            node.isSelected = !node.isSelected;
            referenceParent(node);
            adapter.getSelectedCount();
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
        parentNode.isSelected = count > 0;
//        parentNode.isSelected = count == parentNode.channels.size();
    }

    private List<VideoListResult> multiData;

    public void multiSearch(List<PlayNode> nodeList)
    {
        if (multiData == null)
        {
            multiData = new ArrayList<>();
        }
        else
        {
            multiData.clear();
        }
        if (nodeList.size() > 4)
        {
            T.showS("最多同时播放四个通道");
            return;
        }
        for (int i = 0; i < nodeList.size(); i++)
        {
            new StartSearchRecorde(i,nodeList).start();
        }
    }

    class StartSearchRecorde extends Thread
    {
        int index;
        List<PlayNode> nodeList;

        public StartSearchRecorde(int index,List<PlayNode> nodeList)
        {
            this.nodeList = nodeList;
            this.index = index;
        }

        @Override
        public void run()
        {
            // TODO Auto-generated method stub

            PlayerSearchCore searchCore = new PlayerSearchCore(AcChoosePlayBackDevice.this);
            List<TVideoFile> list = new ArrayList<TVideoFile>();
            Date_Time sTime = changevalue(startTime);
            Date_Time eTime = changevalue(endTime);
            int ret = searchCore.SearchRecFileEx(nodeList.get(index).getDeviceId(),sTime,eTime,
                    PlayerSearchCore.NPC_D_MON_REC_FILE_TYPE_ALL);// 18100000锟斤拷锟斤拷1锟斤拷-1
            System.out.println("锟斤拷锟斤拷锟借备锟脚ｏ拷" + nodeList.get(index).getDeviceId() + "(" + sTime.hour + ":"
                    + sTime.minute + "--" + eTime.hour + ":" + eTime.minute + ")" + ",ret=" + ret);
            if (ret > 0)
            {
                while (true)
                {
                    TVideoFile videofile = searchCore.GetNextRecFile(); // 锟斤拷取录锟斤拷锟侥硷拷
                    if (videofile == null)
                    {
                        System.out.println("锟斤拷锟揭斤拷锟斤拷锟斤拷");
                        break;
                    }
                    else
                    {
                        list.add(videofile);
                        String starttime = videofile.shour + ":" + videofile.sminute + ":" + videofile.ssecond;
                        String endtime = videofile.ehour + ":" + videofile.eminute + ":" + videofile.esecond;
                    }
                }

                if (list.size() == 0)
                {
                    handler.sendEmptyMessage(SEARCH_NODATA);
                }
                else
                {
                    handler.sendEmptyMessage(SEARCH_FINISH);
                }
            }
            else
            {
                handler.sendEmptyMessage(SEARCH_FAILED);
                searchCore.Release();
            }
            VideoListResult vedioListResult = new VideoListResult();
            vedioListResult.multiData = list;
            multiData.add(vedioListResult);

            if (multiData.size() == nodeList.size())
            {// 锟斤拷锟揭伙拷锟�
                if (multiData.size() > 0)
                {
                    Intent intent = new Intent(AcChoosePlayBackDevice.this,AcMain.class);

                    setResult(RESULT_OK,
                            intent.putExtra("vedioList",(Serializable) multiData));
                    //putExtra("playNode",(Serializable) nodeList).putExtra("startDateTime",startTime).putExtra("endTDateTime",endTime).

//                    Intent it = new Intent(AcChoosePlayBackDevice.this,AcMain.class);
//                    //        it.putParcelableArrayListExtra(tempList);
//                    setResult(RESULT_OK,it);

                    finish();
                }
            }
            super.run();
        }
    }

    private static final int SEARCH_FINISH = 2;
    private static final int SEARCH_FAILED = 3;
    private static final int SEARCH_NODATA = 4;

    Handler handler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case SEARCH_FAILED:
                    T.showS("没有找到回放文件");
                    break;
                case SEARCH_FINISH:
                    // startActivity(new Intent(AcAddToPlay.this,
                    // AcSearchRecordResult.class).putExtra("deviceName", ""));

                    break;
                case SEARCH_NODATA:
                    T.showS("没有找到回放文件");
                    break;
                default:
                    break;
            }
        }
    };

    public Date_Time changevalue(TDateTime startTime)
    {
        Date_Time sTime = new Date_Time();
        sTime.year = (short) startTime.iYear;
        sTime.month = (short) startTime.iMonth;
        sTime.day = (byte) startTime.iDay;
        sTime.hour = (byte) startTime.iHour;
        sTime.minute = (byte) startTime.iMinute;
        sTime.second = (byte) startTime.iSecond;
        return sTime;
    }
}
