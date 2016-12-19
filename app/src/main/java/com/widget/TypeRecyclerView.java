

package com.widget;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


public class TypeRecyclerView extends RecyclerView
{
	public interface LayoutManagerChangeListener
	{
		public abstract void onChange();
	}

	public void setOnLayoutManagerChangeListener(LayoutManagerChangeListener listener)
	{
		this.listener = listener;
	}

	public LayoutManagerChangeListener listener;

	public TypeRecyclerView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public TypeRecyclerView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TypeRecyclerView(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setLayoutManager(LayoutManager layout)
	{
		super.setLayoutManager(layout);
		if (null != listener)
		{
			// 在此做数据变换处理
			listener.onChange();
		}
	}
}
