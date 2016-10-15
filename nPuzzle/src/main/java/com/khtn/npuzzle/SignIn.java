package com.khtn.npuzzle;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class SignIn {

	private String username;
	private String userid;
	private ProgressDialog dialog;

	public SignIn(String username, String userid, ProgressDialog dialog) {
		this.username = username;
		this.userid = userid;
		this.dialog = dialog;
	}

	public void checkUserSignIn() {

		SignInAsyncTask signInAsyncTask = new SignInAsyncTask();
		signInAsyncTask.execute();
	}
	
	private class SignInAsyncTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog.setMessage("Logging in...");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				final String namespace = "http://puzzle.com/";
				String methodname = "isExistUser";
				String SOAP_ACTION = namespace + methodname;
				final String URL = "http://www.puzzle.somee.com/PuzzleService.asmx?WSDL";

				SoapObject request = new SoapObject(namespace, methodname);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				request.addProperty("_email", userid + "@npuzzle.com");

				HttpTransportSE transport = new HttpTransportSE(URL);
				transport.call(SOAP_ACTION, envelope);

				SoapPrimitive respone = (SoapPrimitive) envelope.getResponse();
				Log.d("sayuri", "User exists: " + respone.toString());
				
				if(Integer.valueOf(respone.toString()) == 0) {
					
					methodname = "InsertNewUser";
					SOAP_ACTION = namespace + methodname;
					
					request = new SoapObject(namespace, methodname);
					envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);
					envelope.dotNet = true;
					envelope.setOutputSoapObject(request);

					request.addProperty("_email", userid + "@npuzzle.com");
					request.addProperty("_nickname", username);

					transport = new HttpTransportSE(URL);
					transport.call(SOAP_ACTION, envelope);

					respone = (SoapPrimitive) envelope.getResponse();
					Log.d("sayuri", "Register success: " + respone.toString());
				}
				
				return null;

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			dialog.dismiss();
		}
	}
}
