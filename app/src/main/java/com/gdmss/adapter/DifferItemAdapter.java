

package com.gdmss.adapter;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;


public abstract class DifferItemAdapter<LinearHolder extends ViewHolder, GridHolder extends ViewHolder> extends Adapter<RecyclerView.ViewHolder>
{
	public int displayMode = 0;
	
	public int getDisplayMode()
	{
		return displayMode;
	}

	@Override
	public int getItemViewType(int position)
	{
		return displayMode;
	}

	/**
	 * 创建viewholder
	 * <p>
	 * 实现自己的LinearItme布局
	 * </p>
	 */
	public abstract LinearHolder onCreateLinearHolder(ViewGroup parentView);

	/**
	 * 创建viewholder
	 * <p>
	 * 实现自己的GridItme布局
	 * </p>
	 */
	public abstract GridHolder onCreateGridHolder(ViewGroup parentView);

	public abstract void onBindLinearHolder(LinearHolder holder, int position);

	public abstract void onBindGridHolder(GridHolder holder, int position);

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
	{
		ViewHolder holder = null;
		if (displayMode == 0)
		{
			holder = onCreateLinearHolder(paramViewGroup);
		}
		else
		{
			holder = onCreateGridHolder(paramViewGroup);
		}
		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder paramVH, int paramInt)
	{
		if (displayMode == 0)
		{
			onBindLinearHolder((LinearHolder) paramVH, paramInt);
		}
		else
		{
			onBindGridHolder((GridHolder) paramVH, paramInt);
		}
	}
}
