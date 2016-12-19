
package com.gdmss.dialog;

import java.util.ArrayList;
import java.util.List;
import com.gdmss.R;
import com.gdmss.adapter.FavoriteAdapter;
import com.gdmss.base.BaseActivity;
import com.gdmss.entity.PlayNode;
import com.utils.Path;
import com.utils.ScreenUtils;
import com.utils.T;
import com.utils.Utils;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;

public class AcAddFavorite extends BaseActivity implements OnItemClickListener, OnClickListener, Runnable
{
	private ListView lv_favorite;

	private FavoriteAdapter adapter;

	private RelativeLayout rl_addgroup;

	private Button btn_cancel;

	private Dialog dialog;

	private EditText et_groupName;

	private Button dbtn_submit, dbtn_cancel;

	private LayoutInflater inflater;

	private PlayNode node;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_addfavorite);
		setWindowSize(this);
		initParameters();
		initViews();
	}

	private void setWindowSize(Context context)
	{
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.width = (int) (ScreenUtils.getScreenWidth(this));
		params.height = (int) (ScreenUtils.getScreenHeight(this) * 0.35);
		params.gravity = Gravity.BOTTOM;
		window.setAttributes(params);
	}

	private void initParameters()
	{
		node = (PlayNode) getIntent().getSerializableExtra("node");
	}

	private void initViews()
	{
		lv_favorite = (ListView) findViewById(R.id.lv_favorite);

		rl_addgroup = (RelativeLayout) findViewById(R.id.rl_addgroup);

		btn_cancel = (Button) findViewById(R.id.btn_cancel);

		inflater = LayoutInflater.from(this);

		adapter = new FavoriteAdapter(context);

		lv_favorite.setAdapter(adapter);
		
		lv_favorite.setOnItemClickListener(this);

		rl_addgroup.setOnClickListener(this);

		btn_cancel.setOnClickListener(this);
	}

	private void showAddDialog()
	{
		if (null == dialog)
		{
			dialog = new Dialog(this, R.style.smsDialog);

			View v = inflater.inflate(R.layout.dl_input_groupname, null);
			dialog.setContentView(v);

			Window window = dialog.getWindow();
			WindowManager.LayoutParams params = window.getAttributes();
			params.width = (int) (ScreenUtils.getScreenWidth(this) * 0.8);
			params.gravity = Gravity.CENTER;
			window.setAttributes(params);
			dialog.setCanceledOnTouchOutside(false);

			dbtn_submit = (Button) v.findViewById(R.id.dbtn_submit);
			dbtn_cancel = (Button) v.findViewById(R.id.dbtn_cancel);
			et_groupName = (EditText) v.findViewById(R.id.et_groupname);

			dbtn_cancel.setOnClickListener(this);
			dbtn_submit.setOnClickListener(this);
		}
		dialog.show();
	}

	void addGroup()
	{
		String group = et_groupName.getText().toString().trim();

		if (TextUtils.isEmpty(group))
		{
			T.showS(R.string.msg_input_cannot_empty);
			return;
		}

		if (app.favorites.containsKey(group))
		{
			T.showS(R.string.name_has_been_exist);
			return;
		}

		app.favorite_group.add(group);
		app.favorites.put(group, new ArrayList<PlayNode>());
		saveFavorite();
		dialog.dismiss();
		new Handler().post(this);
	}

	void saveFavorite()
	{
		Utils.saveList(app.favorite_group, Path.favorite_group);

		Utils.saveMap(app.favorites, Path.favorites);

		// app.favorite_group = (List<String>)
		// Utils.readList(String.class,Path.favorite_group);
		//
		// app.favorites = Utils.readMap(Path.favorites);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_cancel:
				finish();
				break;
			case R.id.rl_addgroup:
				showAddDialog();
				break;
			case R.id.dbtn_submit:
				addGroup();
				break;
			case R.id.dbtn_cancel:
				et_groupName.setText("");
				dialog.dismiss();
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		addCamera(position);
	}

	void addCamera(int index)
	{
		if (null == node)
		{
			return;
		}
		String key = app.favorite_group.get(index);
		List<PlayNode> nodes = app.favorites.get(key);
		if (nodes.contains(node))
		{
			T.showS(R.string.name_has_been_exist);
			return;
		}
		app.favorites.get(key).add(node);
		Utils.saveMap(app.favorites, Path.favorites);
		T.showS("添加成功");
		adapter.notifyDataSetChanged();
	}

	@Override
	public void run()
	{
		adapter.notifyDataSetChanged();
	}
}