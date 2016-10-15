package com.khtn.npuzzle;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoActivity extends Activity {

	TextView txtInfo;
	ImageView imgBackMain;
	Animation mAnim;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_info);
		
		imgBackMain = (ImageView) findViewById(R.id.imgbackmain);
		imgBackMain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		txtInfo = (TextView) findViewById(R.id.txtInfo);
		mAnim = new TranslateAnimation(0,0,1000,-600);
		mAnim.setDuration(8000);    // Set custom duration.
		mAnim.setStartOffset(0);    // Set custom offset.
		mAnim.setRepeatMode(Animation.REVERSE);    // This will animate text back ater it reaches end.
		mAnim.setRepeatCount(Animation.INFINITE);    // Infinite animation.
		txtInfo.startAnimation(mAnim);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
