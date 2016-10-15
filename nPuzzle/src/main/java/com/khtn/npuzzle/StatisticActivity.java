package com.khtn.npuzzle;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import com.google.gson.Gson;
import com.khtn.npuzzle.adapter.StatisticAdapter;
import com.khtn.npuzzle.model.ListTopUser;
import com.khtn.npuzzle.model.TopUserScore;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class StatisticActivity extends Activity {

	ImageView imgTopOfWeek;
	AsyncTask<Void, String, String> task;
	String jsonText;
	ImageView imgBack;
	ListView lvTopScore;
	ArrayList<TopUserScore> data;
	ArrayAdapter<TopUserScore> adapter;
	StatisticAdapter sAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_statistic);
		imgBack = (ImageView) findViewById(R.id.imgBM);
		imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		lvTopScore = (ListView) findViewById(R.id.listViewTopScore);
		data = new ArrayList<TopUserScore>();
		task = new AsyncTask<Void, String, String>() {

			@Override
			protected String doInBackground(Void... params) {
				String jsonContent ="";
				jsonContent = getJSON();
				return jsonContent;
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if(result.equals("nonjson"))
				{
					return;
				}
				Log.d("json", result);
				String jsontext = "{\"results\":"+result+"}";
				ListTopUser user = new Gson().fromJson(jsontext,ListTopUser.class);
				data = user.toArrayList();
				Log.d("data", data.size()+"");
				if(data.size()>0)
				{
					runOnUiThread(new Runnable(){
						public void run(){
							sAdapter = new StatisticAdapter(StatisticActivity.this, R.layout.topscore_row, data);
							lvTopScore.setAdapter(sAdapter);
						}
					});
				}
			}
		};
		task.execute();
	}
	private String getJSON()
	{
		try {
			final String namespace = "http://puzzle.com/";
			final String methodname = "GetTopScoreAllTime";
			final String SOAP_ACTION = namespace+methodname;
			final String URL = "http://www.puzzle.somee.com/PuzzleService.asmx?WSDL";
			SoapObject request = new SoapObject(namespace, methodname);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet=true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE transport = new HttpTransportSE(URL);
			transport.call(SOAP_ACTION, envelope);
			SoapPrimitive respone = (SoapPrimitive) envelope.getResponse();
			Log.d("JSONTEXT", respone.toString());
			return respone.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "nonjson";
	}
}
