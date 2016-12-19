

package com.gdmss.fragment;


import java.util.ArrayList;
import java.util.List;

import com.Player.web.response.AlarmInfo;
import com.Player.web.response.ResponseQueryAlarm;
import com.gdmss.R;
import com.gdmss.adapter.AlarmListAdapter;
import com.gdmss.adapter.AlarmMessageAdapter;
import com.gdmss.adapter.DividerItemDecoration;
import com.gdmss.adapter.AlarmAdapter;
import com.gdmss.base.BaseFragment;
import com.gdmss.entity.AlarmMessage;
import com.gdmss.entity.PlayNode;
import com.widget.SlidingMenu;
import com.widget.TypeRecyclerView;
import com.widget.TypeRecyclerView.LayoutManagerChangeListener;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class FgAlarmMessage extends BaseFragment implements OnRefreshListener, OnScrollListener, OnItemClickListener, OnClickListener, LayoutManagerChangeListener
{
    private ListView lv_alarmMessage;

    private TypeRecyclerView rv_alarmMessage;

    private AlarmMessageAdapter adapter;

    private AlarmListAdapter adapter_1;

    private AlarmAdapter adapter_2;

    private DividerItemDecoration decoration;

    private List<AlarmMessage> data;

    private SwipeRefreshLayout refreshLayout;

    private LinearLayoutManager linear_manager;

    private GridLayoutManager grid_manager;

    private FloatingActionButton fab;

    private TextView head;

    private Button btn_delete;

    public static FgAlarmMessage getInstance(SlidingMenu menu)
    {
        FgAlarmMessage instance = new FgAlarmMessage();
        instance.menu = menu;
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        if (null == view)
        {
            view = inflater.inflate(R.layout.fg_alarmmessage,container,false);
            initData();
            initView(view);
        }
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        refresh();
    }

    void initView(View v)
    {
        //列表d
        head = (TextView) v.findViewById(R.id.tv_date);

        head.setOnClickListener(this);

        lv_alarmMessage = (ListView) v.findViewById(R.id.lv_alarmList);

        adapter = new AlarmMessageAdapter(context);

        lv_alarmMessage.setAdapter(adapter);

        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshListener(this);

        lv_alarmMessage.setOnScrollListener(this);

//        lv_alarmMessage.setOnItemClickListener(this);


        //其他控件
        btn_delete = (Button) v.findViewById(R.id.btn_delete);

        btn_delete.setOnClickListener(this);

        // -------------------------------

        fab = (FloatingActionButton) v.findViewById(R.id.fab);

        fab.setOnClickListener(this);

        rv_alarmMessage = (TypeRecyclerView) v.findViewById(R.id.rv_alarmList);

        grid_manager = new GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false);

        linear_manager = new LinearLayoutManager(context,LinearLayout.VERTICAL,false);

        rv_alarmMessage.setLayoutManager(linear_manager);

        adapter_1 = new AlarmListAdapter(context,data);

        adapter_2 = new AlarmAdapter(getActivity(),data);

        decoration = new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST);

        // rv_alarmMessage.setAdapter(adapter_1);

        rv_alarmMessage.addItemDecoration(decoration);

        rv_alarmMessage.setAdapter(adapter_2);

        rv_alarmMessage.setOnLayoutManagerChangeListener(this);
    }

    void initData()
    {
        data = new ArrayList<>();
        TAG = getClass().getName();
    }

    void refresh()
    {
        data.clear();
        refreshLayout.setRefreshing(true);
        app.client.queryAlarmList(handler);
        //app.client.queryAlarm(0, 100, app.currentUser.getsUserName(), null, new int[]{1, 2, 3, 4, 5}, null, null, handler);
    }

    private Handler handler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            ResponseQueryAlarm response = (ResponseQueryAlarm) msg.obj;
            if (null != response && null != response.h)
            {
                if (response.h.e == 200)
                {
                    AlarmInfo[] infos = response.b.alarms;
                    for (int i = 0; i < infos.length; i++)
                    {
                        AlarmMessage message = new AlarmMessage(context,infos[i]);
                        if (!TextUtils.isEmpty(infos[i].dev_id))
                        {
                            PlayNode tempNode = app.parentMap.get(infos[i].dev_id);
                            message.devName = tempNode.getName();
                            data.add(message);
                        }
                    }
                    // adapter.setAlarmInfos(data);
                    // adapter.notifyDataSetChanged();
                    // adapter_1.notifyDataSetChanged();
                    adapter_2.notifyDataSetChanged();
                }
            }
            refreshLayout.setRefreshing(false);
            return false;
        }
    });

    @Override
    public void onRefresh()
    {
        refresh();
    }

    @Override
    public void onScrollStateChanged(AbsListView view,int scrollState)
    {

    }

    @Override
    public void onScroll(AbsListView view,int firstVisibleItem,int visibleItemCount,int totalItemCount)
    {
        if (data.size() <= visibleItemCount)
        {
            return;
        }
        AlarmMessage message = (AlarmMessage) (adapter.getItem(firstVisibleItem));
        head.setText(message.getDate());
    }

    @Override
    public void onItemClick(AdapterView<?> parent,View view,int position,long id)
    {
//        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setMessage("message").setPositiveButton("确认",null).setNegativeButton("取消",null).create();
//        dialog.show();
    }

    @Override
    public void onClick(View v)
    {
        // lv_alarmMessage.setSelection();
        switch (v.getId())
        {
            case R.id.fab:
                changeView();
                break;
            case R.id.btn_delete:
                deleteAllMsg();
                break;
            default:
                jumpToHeader();
                break;
        }
    }

    void changeView()
    {
        LayoutManager tmpManager = rv_alarmMessage.getLayoutManager();
        if (tmpManager instanceof GridLayoutManager)
        {
            rv_alarmMessage.setLayoutManager(linear_manager);
            decoration.setOrientation(DividerItemDecoration.HORIZONTAL_LIST);
        }
        else
        {
            rv_alarmMessage.setLayoutManager(grid_manager);
            decoration.setOrientation(DividerItemDecoration.VERTICAL_LIST);
        }
    }

    /**
     * 跳到顶部
     */
    void jumpToHeader()
    {
        String group = head.getText().toString();
        boolean result = false;
        for (int i = 0; i < data.size(); i++)
        {
            if (i == 0)
            {
                result = data.get(i).getDate().equals(group);
                if (result)
                {
                    lv_alarmMessage.setSelection(0);
                    return;
                }
                continue;
            }
            if (data.get(i).getDate().equals(group))
            {
                lv_alarmMessage.setSelection(i);
                return;
            }
        }
    }

    /**
     * 删除所有报警信息
     */
    void deleteAllMsg()
    {
        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setMessage(R.string.msg_delete_all_alarm).setNegativeButton(R.string.wifisetting_cancel,null).setPositiveButton(R.string.wifisetting_ok,new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface,int i)
            {
                app.client.deleteAllAlarm(new Handler(new Handler.Callback()
                {
                    @Override
                    public boolean handleMessage(Message message)
                    {
                        data.clear();
                        adapter_2.setData(data);
                        adapter_2.notifyDataSetChanged();
                        return false;
                    }
                }));
            }
        }).create();
        dialog.show();
    }

    @Override
    public void onChange()
    {
        // adapter_1.changeData(rv_alarmMessage.getLayoutManager() instanceof
        // LinearLayoutManager ? 0 : 1);
        // adapter_2.changeData(rv_alarmMessage.getLayoutManager() instanceof
        // LinearLayoutManager ? 0 : 1);
        adapter_2.changeData();
    }
}
