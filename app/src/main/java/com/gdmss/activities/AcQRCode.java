

package com.gdmss.activities;


import com.gdmss.R;
import com.gdmss.base.BaseActivity;
import com.utils.L;
import com.utils.QRimage;
import com.utils.T;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;


public class AcQRCode extends BaseActivity
{
	private String deviceInfo = "";

	private ImageView img_qrcode;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_qrcode);
		initData();
	}

	void initData()
	{
		img_qrcode = (ImageView) findViewById(R.id.img_qrcode);
		deviceInfo = getIntent().getStringExtra("devices");
		// String encodeStr = new
		// String(Base64.encode(deviceInfo.getBytes(),Base64.DEFAULT));
		Bitmap bmp = QRimage.createQRImage(deviceInfo);
		if (null != bmp)
		{
			img_qrcode.setImageBitmap(bmp);
		}
		else
		{
			T.showS("生成失败");
			finish();
		}
		L.e(deviceInfo);
		// L.e(encodeStr);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
}
