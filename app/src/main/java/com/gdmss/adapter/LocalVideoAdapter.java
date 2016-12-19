package com.gdmss.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdmss.activities.AcLocalVideoPlayer;
import com.gdmss.R;
import com.gdmss.fragment.FgFileManage;
import com.utils.BmpUtil;
import com.utils.L;
import com.utils.T;
import com.utils.ThreadPool;
import com.utils.Utils;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/12/5.
 */

public class LocalVideoAdapter extends RecyclerView.Adapter<LocalVideoAdapter.Holder>
{
//    File[] files;

    LinkedList<File> files;

    Context context;

    private boolean isSelectMode = false;

    private boolean[] selectedArray;

    public LinkedList<SoftReference<Bitmap>> bmps;


    public LocalVideoAdapter(Context context,LinkedList<File> files)
    {
        this.context = context;
        this.files = files;
        this.selectedArray = new boolean[files.size()];
        initBmps();
    }

    public void initBmps()
    {
        if (Utils.isEmpty(bmps))
        {
            bmps = new LinkedList<>();
        }
        else
        {
            bmps.clear();
        }
        for (int i = 0; i < files.size(); i++)
        {
            bmps.add(null);
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        final Holder holder = new Holder(LayoutInflater.from(context).inflate(R.layout.item_localvideo,parent,false));
        holder.img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int pos = holder.getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION)
                {
                    return;
                }
                if (pos >= getItemCount())
                {
                    return;
                }
                if (!isSelectMode)
                {
//                    openImg(v,pos);
                    openMp4(holder,pos);
                }
                else
                {
//                    selectedArray[pos] = !selectedArray[pos];
                    selectedArray[pos] = !selectedArray[pos];
                    notifyItemChanged(pos);
                    //                    holder.cb_select.setSelected(!holder.cb_select.isSelected());
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder,int position)
    {
        //根据选择模式设置控件显隐性
        holder.cb_select.setVisibility(isSelectMode ? View.VISIBLE : View.GONE);
        holder.view_overlay.setVisibility(isSelectMode ? View.VISIBLE : View.INVISIBLE);

        //设置控件状态
        holder.cb_select.setChecked(selectedArray[position]);
        holder.view_overlay.setAlpha(selectedArray[position] ? 0.5f : 0.0f);

        holder.tv_totaltime.setText(BmpUtil.getVideoTime(files.get(position).getPath()));

        holder.img.setImageBitmap(null);
        if (!Utils.isEmpty(bmps))
        {
            if (null != bmps.get(position))
            {
                if (null != bmps.get(position).get())
                {
                    holder.img.setImageBitmap(bmps.get(position).get());
                    return;
                }
            }
            ThreadPool.getInstance().submit(new loadImgRunnable(position));
        }
    }

    /**
     * 加载图片runnable（添加到线程池进行加载）
     */
    private class loadImgRunnable implements Runnable
    {
        int position;

        loadImgRunnable(int position)
        {
            this.position = position;
        }

        @Override
        public void run()
        {
            SoftReference<Bitmap> bmp = new SoftReference<>(BmpUtil.getThumbnail(FgFileManage.file_videos.get(position).getPath()));
            bmps.set(position,bmp);
            refreshItemHandler.sendEmptyMessage(position);
        }
    }

    private Handler refreshItemHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message message)
        {
            notifyItemChanged(message.what);
            return false;
        }
    });

    public void refresh(LinkedList<File> files)
    {
        this.files = files;
        selectedArray = new boolean[files.size()];
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return files.size();
    }

    class Holder extends RecyclerView.ViewHolder
    {
        ImageView img;

        CheckBox cb_select;

        TextView tv_totaltime;

        View container;//CardView

        View view_overlay;

        Holder(View itemView)
        {
            super(itemView);
//            if (itemView instanceof CardView)
//            {
            container = itemView;//(CardView)
//            }
            img = (ImageView) itemView.findViewById(R.id.img);

            cb_select = (CheckBox) itemView.findViewById(R.id.cb_select);

            view_overlay = itemView.findViewById(R.id.v_overlay);

            tv_totaltime = (TextView) itemView.findViewById(R.id.tv_totaltime);
        }
    }

    public void setSelectMode(boolean selectMode)
    {
        isSelectMode = selectMode;
        selectedArray = new boolean[files.size()];
        notifyDataSetChanged();
    }

    public void selectAll()
    {
        for (int i = 0; i < selectedArray.length; i++)
            selectedArray[i] = true;
        notifyDataSetChanged();
    }

    private int getSelectedCount()
    {
        int count = 0;
        for (boolean b : selectedArray)
        {
            if (b)
            {
                count++;
            }
        }
        return count;
    }

    public void deleteSelectedFile()
    {
        int count = getSelectedCount();
        if (count <= 0)
        {
            T.showS(R.string.msg_please_select_file);
            return;
        }

        int deletedCount = 0;

        for (int i = 0; i < selectedArray.length; i++)
        {
            if (selectedArray[i])
            {
                L.e("before size:" + files.size());
                boolean success = files.get(i - deletedCount).delete();
                if (success)
                {
                    files.remove(i - deletedCount);
                    bmps.remove(i - deletedCount);
                    L.e("after size:" + files.size());
                    deleteHandler.sendEmptyMessage(i - deletedCount);
                    deletedCount++;
                }
            }
            if (i == selectedArray.length - 1)
            {
                deleteHandler.sendEmptyMessage(FINISH);
            }
        }
    }

    public final int FINISH = -111;

    private Handler deleteHandler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message message)
        {
            if (message.what == FINISH)
            {
                selectedArray = new boolean[files.size()];
//                notifyDataSetChanged();
            }
            else
            {
                notifyItemRemoved(message.what);
            }
            return false;
        }
    });

    void openMp4(Holder holder,int position)
    {
        Intent it = new Intent(context,AcLocalVideoPlayer.class);
        it.putExtra("path",files.get(position).getPath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            context.startActivity(it,ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,holder.img,"img").toBundle());
        }
        else
        {
            context.startActivity(it);
        }
    }
}
