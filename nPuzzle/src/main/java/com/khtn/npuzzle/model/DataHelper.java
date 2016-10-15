package com.khtn.npuzzle.model;

import java.io.File;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHelper extends SQLiteOpenHelper {
	
	private static final String DB_NAME = "npuzzle.db";
	private static final int DB_VERSION = 1;
	
	private Context context;
	private SQLiteDatabase database;
	
	public DataHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		//deleteDatabase();
		database = db;
		
		File file = new File(getPathDatabase());
		if (!file.exists()) {
			db = context.openOrCreateDatabase("npuzzle.db", Context.MODE_PRIVATE, null);
			db.setLocale(Locale.getDefault());
			
			String sqllevel = "CREATE TABLE Level (";
			sqllevel += "LevelID INT primary key,";
			sqllevel += "Difficulty INT,";
			sqllevel += "Score INT,";
			sqllevel += "MinimumStep INT,";
			sqllevel += "LevelMax VARCHAR(20))";
			db.execSQL(sqllevel);
			
			//Tao script cho bang Score
			String sqlscore = "CREATE TABLE Score (";
			sqlscore +="ScoreID INT primary key,";
			sqlscore +="UserID VARCHAR(45),";
			sqlscore +="Time DATETIME,";
			sqlscore +="Score INT,";
			sqlscore +="isUploaded INT )";
			db.execSQL(sqlscore);
		}
	}
	
	public boolean openDatabase() {
		try {
			database = SQLiteDatabase.openDatabase(getPathDatabase(),
					null, SQLiteDatabase.OPEN_READWRITE);
			return true;
		} catch (Exception e) {
			e.getStackTrace();
			return false;
		}
	}
	
	@SuppressWarnings("unused")
	private boolean deleteDatabase() {
		File file = new File(getPathDatabase());
		return file.delete();
	}
	
	public String getPathDatabase() {
		return "data/data/" + context.getPackageName() + "/databases/" + DB_NAME;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	public void generateLevel() {
		
		generateMap("1", "10", "1", "1 2 3 4 5 6 7 9 8");
		generateMap("1", "20", "2", "1 2 3 4 9 5 7 8 6");
		generateMap("1", "40", "4", "9 1 2 4 5 3 7 8 6");
		generateMap("1", "70", "7", "5 9 2 1 4 3 7 8 6");
		generateMap("1", "60", "6", "1 6 2 4 9 3 7 5 8");
		generateMap("1", "80", "8", "9 1 3 7 2 6 5 4 8");
		generateMap("1", "90", "9", "1 3 5 8 2 9 4 7 6");
		generateMap("1", "80", "8", "1 8 2 4 9 3 7 6 5");
		generateMap("1", "90", "9", "1 6 2 9 5 3 4 7 8");
		generateMap("1", "80", "8", "4 2 9 5 1 3 7 8 6");

		generateMap("2", "400", "20", "1 4 9 6 3 5 7 2 8");
		generateMap("2", "260", "13", "4 1 2 9 6 8 7 3 5");
		generateMap("2", "360", "18", "4 3 1 2 9 8 6 7 5");
		generateMap("2", "360", "18", "9 2 6 1 4 8 3 7 5");
		generateMap("2", "320", "16", "5 2 3 4 7 6 1 8 9");
		generateMap("2", "320", "16", "7 1 2 8 3 6 4 5 9");
		generateMap("2", "380", "19", "4 9 3 8 7 5 1 2 6");
		generateMap("2", "340", "17", "4 9 6 7 2 1 8 3 5");
		generateMap("2", "280", "14", "3 5 4 2 1 6 7 8 9");
		generateMap("2", "300", "15", "7 1 4 9 3 2 8 6 5");

		generateMap("3", "690", "23", "8 2 4 9 7 1 6 3 5");
		generateMap("3", "750", "25", "4 9 7 3 6 1 2 8 5");
		generateMap("3", "630", "21", "3 6 2 9 4 8 5 1 7");
		generateMap("3", "660", "22", "5 8 3 4 6 2 7 1 9");
		generateMap("3", "810", "27", "1 7 6 2 5 9 8 4 3");
		generateMap("3", "780", "26", "9 7 8 2 6 5 4 1 3");
		generateMap("3", "720", "24", "1 5 8 3 9 4 7 2 6");
		generateMap("3", "720", "24", "8 6 5 4 2 1 9 3 7");
		generateMap("3", "750", "25", "8 7 5 3 6 1 2 9 4");
		generateMap("3", "780", "26", "9 4 3 6 5 2 1 8 7");
	}
	
	private void generateMap(String diff, String score, String minimumStep, String map) {
		
		ContentValues values = new ContentValues();
		values.put("Difficulty", diff);
		values.put("Score", score);
		values.put("MinimumStep", minimumStep);
		values.put("LevelMax", map);
		
		database.insert("Level", null, values);
	}
}
