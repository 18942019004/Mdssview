package com.gdmss.adapter;

import java.util.ArrayList;
import java.util.List;

import com.Player.web.response.ResponseCommon;
import com.alibaba.fastjson.JSON;
import com.daasuu.bl.BubbleLayout;
import com.gdmss.R;
import com.gdmss.base.APP;
import com.gdmss.base.MyBaseAdapter;
import com.gdmss.entity.PlayNode;
import com.utils.RefreshReceiver;
import com.utils.T;
import com.widget.ShowProgress;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class DevicelistAdapter extends MyBaseAdapter
{
    private List<PlayNode> list;

    private LayoutInflater inflater;

    public boolean[] selected;

    public boolean isEditMode = false;

    public int count = 0;

    private APP app;

    private Context context;

    private RefreshReceiver receiver;

    public DevicelistAdapter(Context context,List<PlayNode> list)
    {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        selected = new boolean[list.size()];
        app = APP.getInstance(context);
        progress = new ShowProgress(context);
    }

    private void registerReceiver()
    {
        receiver = new RefreshReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("refresh");
        context.registerReceiver(receiver,filter);
    }


    @Override
    public int getCount()
    {
//        registerReceiver();
        return list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent)
    {
        ViewHolder holder;
        PlayNode node = list.get(position);
        if (null == convertView)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_devicelist,parent,false);
            // holder.coantainer = (RelativeLayout)
            // convertView.findViewById(R.id.container);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_devicename);
            holder.cb_selected = (CheckBox) convertView.findViewById(R.id.cb_select);
            holder.bl = (BubbleLayout) convertView.findViewById(R.id.bl);
            holder.ibtn_edit = (ImageButton) convertView.findViewById(R.id.ibtn_edit);
            holder.ibtn_alarm = (ImageButton) convertView.findViewById(R.id.ibtn_alarm);
            holder.ibtn_delete = (ImageButton) convertView.findViewById(R.id.ibtn_delete);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        setView(position,holder,node);
        return convertView;
    }

    private void setView(int index,ViewHolder holder,PlayNode node)
    {
        holder.tv_name.setText(node.getName());

        if (isEditMode)
        {
            holder.cb_selected.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.cb_selected.setVisibility(View.GONE);
        }

        holder.cb_selected.setChecked(selected[index]);

        holder.bl.setVisibility(editItem == index ? View.VISIBLE : View.GONE);

        holder.ibtn_edit.setOnClickListener(new onClick(node));

        holder.ibtn_alarm.setOnClickListener(new onClick(node));

        holder.ibtn_delete.setOnClickListener(new onClick(node));
    }

    private int editItem = -1;

    public void setEditItem(int editItem)
    {
        if (this.editItem == editItem)
        {
            this.editItem = -1;
        }
        else
        {
            this.editItem = editItem;
        }
        notifyDataSetChanged();
    }

    public int getEditItem()
    {
        return editItem;
    }

    class onCheck implements OnCheckedChangeListener
    {
        int index = -1;

        public onCheck(int index)
        {
            this.index = index;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
        {
            selected[index] = isChecked;
            int i = isChecked ? 1 : -1;
            count += i;
        }
    }

    class onClick implements View.OnClickListener
    {
        PlayNode node;

        public onClick(PlayNode node)
        {
            this.node = node;
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.ibtn_edit:

                    break;
                case R.id.ibtn_alarm:

                    break;
                case R.id.ibtn_delete:
                    showProgress();
                    app.client.deleteNodeInfo(node.getNodeId() + "",node.node.iNodeType,node.node.id_type,deleteHan);
                    break;
            }
        }
    }

    @Override
    public void doRefresh()
    {
        this.list = app.parentList;
        selected = new boolean[list.size()];
        notifyDataSetChanged();
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
                    T.showS("删除成功");
                    editItem = -1;
                }
            }
            app.getListFromServer();
            return false;
        }
    });

    public String getSelectedList()
    {
        List<PlayNode> result = new ArrayList<PlayNode>();
        for (int i = 0; i < selected.length; i++)
        {
            if (selected[i])
            {
                result.add(list.get(i));
            }
        }
        return JSON.toJSONString(result);
    }

    class ViewHolder
    {
        TextView tv_name;

        CheckBox cb_selected;

        BubbleLayout bl;

        ImageButton ibtn_edit, ibtn_alarm, ibtn_delete;
    }
}