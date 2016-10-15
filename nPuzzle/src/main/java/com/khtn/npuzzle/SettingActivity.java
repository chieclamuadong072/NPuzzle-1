package com.khtn.npuzzle;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

public class SettingActivity extends Activity {

	ImageView imgBackM;
	Spinner spinner;
	ImageView imgVN, imgEN, imgJP;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_setting);
		
		imgBackM = (ImageView) findViewById(R.id.imgBackM);
		imgBackM.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		spinner = (Spinner) findViewById(R.id.spinDiffcultLevel);
		ArrayList<String> diff = new ArrayList<String>();
		diff.add(new String("Easy"));
		diff.add(new String("Medium"));
		diff.add(new String("Hard"));
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,diff);
		spinner.setAdapter(adapter);

		//Ngon ngu
		imgVN =  (ImageView) findViewById(R.id.imgVietNam);
		imgEN = (ImageView) findViewById(R.id.imgUnitedKingdom);
		imgJP =  (ImageView) findViewById(R.id.imgJapan);
		imgVN.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setLocale("vi");
			}
		});
		imgEN.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setLocale("en");
			}
		});
		imgJP.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setLocale("jp");
			}
		});
	}
	public void setLocale(String Lang)
	{
		Locale myLocale = new Locale(Lang);
		Resources res = getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = myLocale;
		res.updateConfiguration(conf, dm);
		Intent intent = new Intent(this, SettingActivity.class);
		startActivity(intent);
		finish();
	}
}
