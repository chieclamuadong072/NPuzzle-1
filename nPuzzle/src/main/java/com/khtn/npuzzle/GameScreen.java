package com.khtn.npuzzle;

import java.util.Calendar;

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
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.khtn.npuzzle.adapter.GameBoardAdapter;
import com.khtn.npuzzle.myenum.Direction;
import com.khtn.npuzzle.pojo.Matrix;

public class GameScreen extends Activity {

	private GridView gameBoard;
	private ImageView imgBack;
	private ImageView imgRandom;
	private ImageView imgHint;
	private GameBoardAdapter myAdapter;

	private Chronometer txtTime;
	private TextView txtScore;
	private CountDownTimer scoreDecrease;
	// private long score;

	private GameEngine game;

	private Handler handler;
	private boolean isSolverRunning;
	private Toast toast;
	private String userID;
	
	private SQLiteDatabase database;

	private ProgressDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_game_screen);

		loadingDialog = new ProgressDialog(GameScreen.this);

		game = new GameEngine();
		game.setMatrix(new Matrix(3));
		game.setSize(3);

		gameBoard = (GridView) findViewById(R.id.game_board);
		imgBack = (ImageView) findViewById(R.id.imaBack);
		imgRandom = (ImageView) findViewById(R.id.imgRandom);
		imgHint = (ImageView) findViewById(R.id.imgHint);
		txtTime = (Chronometer) findViewById(R.id.txtTime);
		txtScore = (TextView) findViewById(R.id.txtScore);
		myAdapter = new GameBoardAdapter(getApplicationContext(),
				R.layout.board_tile, game.getMatrix());

		isSolverRunning = false;
		xulyluugame();

	}

	private void xulyluugame() {
		Intent callIntentMain = getIntent();
		Bundle callFacebookInfo = callIntentMain
				.getBundleExtra("Facebook_info");
		userID = callFacebookInfo.getString("userid");

	}

	@Override
	protected void onStart() {
		super.onStart();

		imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				finish();
			}
		});

		startTimer();

		gameBoard.setAdapter(myAdapter);
		gameBoard.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (!isSolverRunning) {

					int blankPos = game.getMatrix().getBlankTilePos();
					int gameBoardSize = game.getMatrix().getBoardSize();

					int state = game.Evaluate(game.getMatrix());

					if (state == 0) {
						return;
					}

					// blank tile on top
					if (position - gameBoardSize == blankPos) {
						game.getMatrix().makeMove(Direction.DOWN);
					}

					// blank tile at bottom
					if (position + gameBoardSize == blankPos) {
						game.getMatrix().makeMove(Direction.UP);
					}

					// blank tile on left
					if (position - 1 == blankPos) {
						game.getMatrix().makeMove(Direction.RIGHT);
					}

					// blank tile on right
					if (position + 1 == blankPos) {
						game.getMatrix().makeMove(Direction.LEFT);
					}
					myAdapter.notifyDataSetChanged();
					state = game.Evaluate(game.getMatrix());

					if (state == 0) {
						if (toast != null) {
							toast.cancel();
						}
						toast = Toast.makeText(
								getApplicationContext(),
								getResources().getString(
										R.string.reach_goal_state),
								Toast.LENGTH_SHORT);
						toast.show();

						scoreDecrease.cancel();
						txtTime.stop();

						Log.d("sayuri", "" + txtScore.getText());

						if (!userID.equals("")) {
							Calendar c = Calendar.getInstance();

							database = openOrCreateDatabase(
									"npuzzle.db",
									SQLiteDatabase.CREATE_IF_NECESSARY, null);

							String time = (c.get(Calendar.MONTH) + 1) + "/"
									+ c.get(Calendar.DAY_OF_MONTH) + "/"
									+ c.get(Calendar.YEAR);
							int score = Integer.valueOf(txtScore.getText()
									.toString());

							if (hasConnectivity()) {

								UploadScore uploadScore = new UploadScore(userID,
										score, time);
								uploadScore.execute();

							}
							else {

								insertScoreToLocalDB(userID, score, time, 0);
							}
						}
					}
				} else {
					return;
				}
			}
		});

		imgRandom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				game.Shuffle();
				myAdapter.notifyDataSetChanged();
				startTimer();
				
			}
		});
		imgHint.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (toast != null) {
					toast.cancel();
				}

				if (game.CanSolve()) {

					final long startTime = System.currentTimeMillis();
					game.Solve();
					final long endTime = System.currentTimeMillis();

					// solveAsyncTask = new SolvePuzzleAsyncTask();
					// solveAsyncTask.execute();

					if (game.getSolution().size() == 0) {
						toast = Toast.makeText(getApplicationContext(),
								getResources()
										.getString(R.string.at_goal_state),
								Toast.LENGTH_SHORT);
					} else {

						isSolverRunning = true;
						imgRandom.setEnabled(false);
						imgHint.setEnabled(false);

						// score = 0;
						txtScore.setText("0");
						scoreDecrease.cancel();
						txtTime.stop();

						toast = Toast.makeText(
								getApplicationContext(),
								getResources().getString(R.string.solve_time)
										+ (endTime - startTime)
										+ getResources().getString(
												R.string.solve_time_unit)
										+ ". "
										+ getResources().getString(
												R.string.solve_move)
										+ game.getSolution().size(),
								Toast.LENGTH_SHORT);

						handler = new Handler();
						handler.postDelayed(runnable, 1000);
					}
				} else {
					toast = Toast.makeText(getApplicationContext(),
							getResources().getString(R.string.no_solution),
							Toast.LENGTH_SHORT);
				}
				toast.show();
			}
		});
	}

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			/* do what you need to do */
			if (game.getSolution().size() > 0) {

				isSolverRunning = true;

				game.getMatrix().makeMove(game.getSolution().pop());
				myAdapter.notifyDataSetChanged();
			}

			/* and here comes the "trick" */
			if (handler != null) {

				if (game.getSolution().size() == 0) {

					isSolverRunning = false;
					imgRandom.setEnabled(true);
					imgHint.setEnabled(true);
					return;
				} else {
					handler.postDelayed(this, 1000);
				}
			}
		}
	};

	private void startTimer() {

		// score =
		// Integer.valueOf(getResources().getString(R.string.ScoreDemo));

		if (scoreDecrease != null) {
			scoreDecrease.cancel();
			txtScore.setText(getResources().getString(R.string.ScoreDemo));
		}

		scoreDecrease = new CountDownTimer(10 * 1000, 1000) {

			public void onTick(long millisUntilFinished) {
			}

			public void onFinish() {

				scoreDecrease = new CountDownTimer(
						Integer.valueOf(getResources().getString(
								R.string.ScoreDemo)) * 1000, 1000) {

					public void onTick(long millisUntilFinished) {

						txtScore.setText(String
								.valueOf(millisUntilFinished / 1000));
					}

					public void onFinish() {

					}
				};

				scoreDecrease.start();
			}

		};

		scoreDecrease.start();

		txtTime.setBase(SystemClock.elapsedRealtime());
		txtTime.start();
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

				final String namespace = "http://puzzle.com/";
				String methodname = "InsertNewUserScore";
				String SOAP_ACTION = namespace + methodname;
				final String URL = "http://www.puzzle.somee.com/PuzzleService.asmx?WSDL";

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
			if(result == 0) {
				Toast.makeText(getApplicationContext(),
						"Insert score to server failed.", Toast.LENGTH_LONG)
						.show();
				insertScoreToLocalDB(userID, score, time, 0);
			}
			else {
				Toast.makeText(getApplicationContext(),
						"Insert score to server successful.", Toast.LENGTH_LONG)
						.show();
				insertScoreToLocalDB(userID, score, time, 1);
			}

			loadingDialog.dismiss();
		}

	}
	
	public boolean hasConnectivity() {

		ConnectivityManager cm =
		        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
		                      activeNetwork.isConnectedOrConnecting();
		
		return isConnected;
	}
	
	public void insertScoreToLocalDB(String userid, int score, String time, int isUploadToServer) {

		ContentValues values = new ContentValues();
		values.put("UserID", userid);
		values.put("Score", score);
		values.put("Time", time);
		values.put("isUploaded", isUploadToServer);

		if (database.insert("Score", null, values) == -1) {
			Toast.makeText(getApplicationContext(),
					"Insert score to local failed.", Toast.LENGTH_LONG)
					.show();
		} else {
			Toast.makeText(getApplicationContext(),
					"Insert score to local successful.", Toast.LENGTH_LONG)
					.show();
		}
	}
}
