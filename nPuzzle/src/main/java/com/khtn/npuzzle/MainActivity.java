package com.khtn.npuzzle;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.ext.SatelliteMenu;
import android.view.ext.SatelliteMenu.SateliteClickedListener;
import android.view.ext.SatelliteMenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session.StatusCallback;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.khtn.npuzzle.model.DataHelper;

public class MainActivity extends Activity {

	// private static final String TAG = null;
	private UiLifecycleHelper uiHelper;
	private LoginButton loginFB;
	private StatusCallback statuscallback;
	//private ImageView imagescreen;
	private TextView userfb;
	//private Bitmap screenshot;
	private String username;
	private String userid;

	private SQLiteDatabase database = null;
	private ProgressDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		SatelliteMenu menu = (SatelliteMenu) findViewById(R.id.menu);
		float distance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				300, getResources().getDisplayMetrics());
		Log.d("distance", distance + "");
		menu.setSatelliteDistance((int) distance);
		menu.setExpandDuration(1000);
		menu.setCloseItemsOnClick(true);
		menu.setTotalSpacingDegree(180);

		final ArrayList<SatelliteMenuItem> listItems = new ArrayList<SatelliteMenuItem>();
		listItems.add(new SatelliteMenuItem(1, R.drawable.info));
		listItems.add(new SatelliteMenuItem(2, R.drawable.setting));
		listItems.add(new SatelliteMenuItem(3, R.drawable.play));
		listItems.add(new SatelliteMenuItem(4, R.drawable.login));
		listItems.add(new SatelliteMenuItem(5, R.drawable.statistics));

		DataHelper helper = new DataHelper(getApplicationContext());
		helper.onCreate(database);
		
		//helper.openDatabase();
		//helper.generateLevel();

		loadingDialog = new ProgressDialog(MainActivity.this);

		loginFB = (LoginButton) findViewById(R.id.login_button);
		uiHelper = new UiLifecycleHelper(this, statuscallback);
		userfb = (TextView) findViewById(R.id.userFB);

		username = "";
		userid = "";
		int i = 0;
		menu.addItems(listItems);

		menu.setOnItemClickedListener(new SateliteClickedListener() {

			@Override
			public void eventOccured(int id) {
				if (id == 1) {
					final Intent intent = new Intent(MainActivity.this,
							InfoActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						public void run() {
							startActivity(intent);
						}
					}, 1100);
				}
				if (id == 2) {
					final Intent intent = new Intent(MainActivity.this,
							SettingActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						public void run() {
							startActivity(intent);
						}
					}, 1100);
				}
				if (id == 3) {
					final Intent intent = new Intent(MainActivity.this,
							GameScreen.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						public void run() {
							Bundle bundle = new Bundle();
							bundle.putString("user", username);
							bundle.putString("userid", userid);
							intent.putExtra("Facebook_info", bundle);
							startActivity(intent);

						}

					}, 1100);
				}
				if (id == 5) {
					final Intent intent = new Intent(MainActivity.this,
							StatisticActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						public void run() {
							startActivity(intent);
						}
					}, 1100);
				}

			}
		});

		loginFB = (LoginButton) findViewById(R.id.login_button);
		uiHelper = new UiLifecycleHelper(this, statuscallback);
		userfb = (TextView) findViewById(R.id.userFB);
		// uiHelper.onCreate(savedInstanceState);

		loginFB.setUserInfoChangedCallback(userChangedCallback);

		if (hasConnectivity()) {
			try {
				database = helper.getReadableDatabase();
				Cursor cursor;
				cursor = database.query("Score", null, "Score.isUploaded = ?", new String[]{"0"}, null, null, null);
				
				cursor.moveToFirst();

				String userID = "";
				int score = 0;
				String time = "";

				do {
					userID = cursor.getString(1);
					time = cursor.getString(2);
					score = cursor.getInt(3);

					UploadScore uploadScore = new UploadScore(userID, score,
							time);
					uploadScore.execute();

				} while (cursor.moveToNext());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private UserInfoChangedCallback userChangedCallback = new UserInfoChangedCallback() {

		@Override
		public void onUserInfoFetched(GraphUser user) {
			// TODO Auto-generated method stub
			if(user != null)
			{
				userfb.setText(getResources().getString(R.string.facebook_login) + user.getName());
				username = user.getName();
				userid = user.getId();

				SignIn signIn = new SignIn(username, userid, loadingDialog);
				signIn.checkUserSignIn();
			} else {
				userfb.setText(getResources().getString(
						R.string.facebook_logout));
				username = "";
				userid = "";
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	public boolean hasConnectivity() {

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();

		return isConnected;
	}

	private class UploadScore extends AsyncTask<Void, Void, Integer> {

		private String userID;
		private int score;
		private String time;

		public UploadScore(String userID, int score, String time) {

			this.userID = userID;
			this.score = score;
			this.time = time;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			loadingDialog.setMessage("Loading...");
			loadingDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... params) {

			try {

				Log.d("sayuri", userID + "@npuzzle.com");
				Log.d("sayuri", score + "");
				Log.d("sayuri", time);

				final String namespace = "http://npuzzle.com/";
				String methodname = "InsertNewUserScore";
				String SOAP_ACTION = namespace + methodname;
				final String URL = "http://npuzzle.somee.com/PuzzleService.asmx?WSDL";

				SoapObject request = new SoapObject(namespace, methodname);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				MarshalFloat marshal = new MarshalFloat();
				marshal.register(envelope);

				request.addProperty("_email", userID + "@npuzzle.com");
				request.addProperty("_score", score);
				request.addProperty("_datetime", time);

				HttpTransportSE transport = new HttpTransportSE(URL);
				transport.call(SOAP_ACTION, envelope);

				Log.d("sayuri", "upload score ");

				SoapPrimitive respone = (SoapPrimitive) envelope.getResponse();
				Log.d("sayuri", "Add score: " + respone.toString());

				return Integer.valueOf(respone.toString());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);

			// Fail to upload to server
			if (result == 1) {

				updateScoreToLocalDB(userID, score, time);
			}

			loadingDialog.dismiss();
		}
	}

	public void updateScoreToLocalDB(String userid, int score, String time) {

		ContentValues values = new ContentValues();
		values.put("UserID", userid);
		values.put("Score", score);
		values.put("Time", time);
		values.put("isUploaded", 1);

		String whereClause = "isUploaded = ?";
		String[] whereArgs = { "0" };

		if (database.update("Score", values, whereClause, whereArgs) == -1) {
			Toast.makeText(getApplicationContext(), "Update Score failed.",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(getApplicationContext(), "Update Score successful.",
					Toast.LENGTH_LONG).show();
		}
	}
}
