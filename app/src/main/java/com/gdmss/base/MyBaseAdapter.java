package com.gdmss.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.widget.ShowProgress;

public class MyBaseAdapter extends BaseAdapter
{
    public MyBaseAdapter()
    {
        super();
    }

    protected ShowProgress progress;

    @Override
    public int getCount()
    {
        return 0;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return null;
    }


    protected void showProgress()
    {
        progress.show();
    }

    protected void dismissProgress()
    {
        progress.dismiss();
    }

    public void doRefresh()
    {

    }

    //    public class RefreshReceiver extends BroadcastReceiver
    //    {
    //        @Override
    //        public void onReceive(Context context, Intent intent)
    //        {
    //            refresh();
    //        }
    //    }
}
