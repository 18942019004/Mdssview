package com.gdmss.adapter;

import java.util.List;
import com.gdmss.R;
import com.gdmss.base.APP;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;



public class FavoriteAdapter extends BaseAdapter
{
    private List<String> key;
    
    private LayoutInflater inflater;
    
    private APP app;
    
    public FavoriteAdapter(Context con)
    {
        inflater = LayoutInflater.from(con);
        app = (APP) con.getApplicationContext();
        key = app.favorite_group;
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return key.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return app.favorites.get(key.get(position));
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        ViewHolder holder = null;
        if(null == view)
        {
            view = inflater.inflate(R.layout.item_favorite,parent,false);
            holder = new ViewHolder();
            holder.tv_groupName = (TextView) view.findViewById(R.id.tv_groupname);
            holder.btn_play = (Button) view.findViewById(R.id.btn_playall);
            holder.tv_count = (TextView) view.findViewById(R.id.tv_count);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }
        setView(position,holder);
        return view;
    }
    
    void setView(int index, ViewHolder holder)
    {
        String groupName = key.get(index);
        
        holder.tv_groupName.setText(groupName);
        
        holder.tv_count.setText("(" + app.favorites.get(groupName).size() + ")");
    }
    
    class ViewHolder
    {
        TextView tv_groupName;
        
        Button btn_play;
        
        TextView tv_count;
    }
    
}
