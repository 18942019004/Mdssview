

package com.gdmss.activities;


import java.util.ArrayList;
import java.util.List;

import com.gdmss.R;
import com.gdmss.base.BaseActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


public class AcPtzSetting extends BaseActivity implements OnItemClickListener
{
	private ListView listView;

	private List<String> data;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_ptzsetting);
		rightBtn.setVisibility(View.GONE);
		listView = (ListView) findViewById(R.id.lv_ptzLength);
		data = new ArrayList<>();
		for (int i = 0; i < 10; i++)
		{
			data.add(i + 1 + "");
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.simple_textview, data);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Intent it = new Intent(this, AcMain.class);
		it.putExtra("length", position + 1);
		setResult(RESULT_OK, it);
		finish();
	}

}
