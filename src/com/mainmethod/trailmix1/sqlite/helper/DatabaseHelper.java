package com.mainmethod.trailmix1.sqlite.helper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.google.android.gms.maps.model.LatLng;
import com.mainmethod.trailmix1.kmlparsing.NavigationSaxHandler;
import com.mainmethod.trailmix1.kmlparsing.Placemark;
import com.mainmethod.trailmix1.kmlparsing.TrailObj;
import com.mainmethod.trailmix1.sqlite.model.GeoPoint;
import com.mainmethod.trailmix1.sqlite.model.Trail;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 10;
	private static final String DATABASE_NAME = "testDB1";
	// Logcat tag
	private static final String LOG = "DatabaseHelper";
	// table names
	private static final String TRAIL_TABLE = "trails";
	private static final String GEOPOINT_TABLE = "geoPoints";
	private static final String EVENT_TABLE = "events";
	private static final String PLACEMARK_TABLE = "placemarks";

	// Common column names
	private static final String KEY_ID = "id";
	private static final String KEY_CREATED_AT = "created_at";

	// TRAILS Table - column names

	private static final String KEY_NAME = "name";
	private static final String KEY_TRAIL_CLASS = "class";
	private static final String KEY_LENGTH = "length";
	private static final String KEY_SURFACE = "surfaceType";

	// GEOPOINTS Table - column names
	private static final String KEY_PLACEMARK_ID = "placemark_id";
	private static final String KEY_LAT = "latitude";
	private static final String KEY_LNG = "longitude";

	// EVENTS Table - column names
	private static final String KEY_TITLE = "title";
	private static final String KEY_DESC = "desc";
	private static final String KEY_EVENT_POSTER_URL = "event_poster_url";
	private static final String KEY_URL = "event_url";
	private static final String KEY_URL_TEXT = "event_url_text";
	private static final String KEY_DATE = "date";
	private static final String KEY_START_TIME = "start_time";
	private static final String KEY_END_TIME = "end_time";
	private static final String KEY_IS_ALL_DAY_LONG = "is_all_day_long";
	private static final String KEY_LOCATION = "location";
	private static final String KEY_CONTACT_NAME = "contact_name";
	private static final String KEY_CONTACT_EMAIL = "contact_email";

	// placemark table columns
	private static final String KEY_TRAIL_ID = "trail_id";

	// Table Create Statements
	// Trail table create statement
	private static final String CREATE_TABLE_TRAIL = "CREATE TABLE IF NOT EXISTS "
			+ TRAIL_TABLE
			+ "("
			+ KEY_ID
			+ " INTEGER PRIMARY KEY,"
			+ KEY_NAME
			+ " TEXT,"
			+ KEY_TRAIL_CLASS
			+ " TEXT,"
			+ KEY_LENGTH
			+ " REAL,"
			+ KEY_SURFACE
			+ " TEXT,"
			+ KEY_CREATED_AT
			+ " DATETIME DEFAULT CURRENT_TIMESTAMP" + ");";

	// Placemark table create statement
	private static final String CREATE_TABLE_PLACEMARK = "CREATE TABLE IF NOT EXISTS "
			+ PLACEMARK_TABLE
			+ "("
			+ KEY_ID
			+ " INTEGER PRIMARY KEY,"
			+ KEY_CREATED_AT
			+ " DATETIME DEFAULT CURRENT_TIMESTAMP,"
			+ KEY_TRAIL_ID
			+ " INTEGER,"
			+ "FOREIGN KEY ("
			+ KEY_TRAIL_ID
			+ ") REFERENCES " + TRAIL_TABLE + " (" + KEY_ID + ")" + ");";

	// GeoPoint table create statement
	private static final String CREATE_TABLE_GEOPOINT = "CREATE TABLE IF NOT EXISTS "
			+ GEOPOINT_TABLE
			+ "("
			+ KEY_ID
			+ " INTEGER PRIMARY KEY,"
			+ KEY_LAT
			+ " REAL NOT NULL,"
			+ KEY_LNG
			+ " REAL NOT NULL,"
			+ KEY_CREATED_AT
			+ " DATETIME DEFAULT CURRENT_TIMESTAMP,"
			+ KEY_PLACEMARK_ID
			+ " INTEGER,"
			+ "FOREIGN KEY ("
			+ KEY_PLACEMARK_ID
			+ ") REFERENCES " + PLACEMARK_TABLE + " (" + KEY_ID + ")" + ");";

	// Event table create statement
	private static final String CREATE_TABLE_EVENT = "CREATE TABLE IF NOT EXISTS "
			+ TRAIL_TABLE
			+ "("
			+ KEY_ID
			+ " INTEGER PRIMARY KEY,"
			+ KEY_TITLE
			+ " TEXT NOT NULL,"
			+ KEY_EVENT_POSTER_URL
			+ " TEXT,"
			+ KEY_URL
			+ " TEXT,"
			+ KEY_URL_TEXT
			+ " TEXT,"
			+ KEY_DATE
			+ " TEXT NOT NULL,"
			+ KEY_START_TIME
			+ " TEXT NOT NULL,"
			+ KEY_END_TIME
			+ " TEXT NOT NULL,"
			+ KEY_IS_ALL_DAY_LONG
			+ " INTEGER NOT NULL,"
			+ KEY_LOCATION
			+ " TEXT NOT NULL,"
			+ KEY_CONTACT_NAME
			+ " TEXT NOT NULL,"
			+ KEY_CONTACT_EMAIL
			+ " TEXT NOT NULL,"
			+ KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ");";

	private InputStream file;

	public DatabaseHelper(Context context, InputStream file) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.file = file;
	}

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// creating required tables
		db.execSQL(CREATE_TABLE_TRAIL);
		db.execSQL(CREATE_TABLE_PLACEMARK);
		db.execSQL(CREATE_TABLE_GEOPOINT);
		db.execSQL(CREATE_TABLE_EVENT);

		// HashMap<String,TrailObj> trailCollection = parser();
		// doInsert(trailCollection);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TRAIL_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + PLACEMARK_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + GEOPOINT_TABLE);

		db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE);

		// create new tables
		onCreate(db);
	}

	/*
	 * Creating a trail
	 */
	public long createTrail(Trail trail) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, trail.getTrailName());
		values.put(KEY_TRAIL_CLASS, trail.getTrailClass());
		values.put(KEY_LENGTH, trail.getLength());
		values.put(KEY_SURFACE, trail.getSurface());

		// insert row
		long trail_id = db.insert(TRAIL_TABLE, null, values);

		return trail_id;
	}

	/*
	 * get single trail by id
	 */
	public Trail getTrailById(long trail_id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TRAIL_TABLE + " WHERE "
				+ KEY_ID + " = " + trail_id;

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Trail trail = new Trail();
		trail.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		trail.setTrailName((c.getString(c.getColumnIndex(KEY_NAME))));
		trail.setTrailClass(c.getString(c.getColumnIndex(KEY_TRAIL_CLASS)));
		trail.setLength(c.getDouble(c.getColumnIndex(KEY_LENGTH)));
		trail.setSurface(c.getString(c.getColumnIndex(KEY_SURFACE)));

		return trail;
	}

	/*
	 * get single trail by name
	 */
	public HashMap<String, Trail> getNamedTrails() {
		HashMap<String, Trail> namedTrails = new HashMap<String, Trail>();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor c = db.query(TRAIL_TABLE, null, KEY_NAME + " IS NOT NULL", null,
				null, null, null);
		if (c.moveToFirst()) {
			do {
				Trail trail = new Trail();
				trail.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				trail.setTrailName((c.getString(c.getColumnIndex(KEY_NAME))));
				trail.setTrailClass(c.getString(c
						.getColumnIndex(KEY_TRAIL_CLASS)));
				trail.setLength(c.getDouble(c.getColumnIndex(KEY_LENGTH)));
				trail.setSurface(c.getString(c.getColumnIndex(KEY_SURFACE)));

				// adding to hashmap
				namedTrails.put(trail.getTrailName(), trail);
			} while (c.moveToNext());
		}
		return namedTrails;
	}

	// Update trail
	public int updateTrail(Trail trail) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, trail.getTrailName());
		values.put(KEY_TRAIL_CLASS, trail.getTrailClass());
		values.put(KEY_SURFACE, trail.getSurface());
		values.put(KEY_LENGTH, trail.getLength());

		// updating row
		return db.update(TRAIL_TABLE, values, KEY_ID + " = ?",
				new String[] { String.valueOf(trail.getId()) });
	}

	// delete trail
	public void deleteTrail(long trail_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TRAIL_TABLE, KEY_ID + " = ?",
				new String[] { String.valueOf(trail_id) });
	}

	// CRUD for Geopoint table

	/*
	 * Creating a geopoint
	 */
	public long createGeoPoint(GeoPoint geopoint) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_LAT, geopoint.getLat());
		values.put(KEY_LNG, geopoint.getLng());
		values.put(KEY_PLACEMARK_ID, geopoint.getPlacemark_id());

		// insert row
		long geopoint_id = db.insert(GEOPOINT_TABLE, null, values);

		return geopoint_id;
	}

	// delete placemark for a trail
	public void deleteTrailPlacemarks(long trail_id) {

		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(PLACEMARK_TABLE, KEY_TRAIL_ID + " = ?",
				new String[] { String.valueOf(trail_id) });
	}
   //delete geopoints for placemark
	public void deletePlacemarkGeoPoints(long placemark_id) {

		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(GEOPOINT_TABLE, KEY_PLACEMARK_ID + " = ?",
				new String[] { String.valueOf(placemark_id) });
	}

	// get geopoints for placemark
	public ArrayList<GeoPoint> getPLacemarkGeoPoints(int placemarkID) {
		ArrayList<GeoPoint> placemarkGeoPoints = new ArrayList<GeoPoint>();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + PLACEMARK_TABLE + " INNER JOIN "
				+ GEOPOINT_TABLE + " ON " + PLACEMARK_TABLE + "." + KEY_ID + " = "
				+ GEOPOINT_TABLE + "." + KEY_PLACEMARK_ID + " WHERE " +PLACEMARK_TABLE +"."+KEY_ID
				+ " = " + placemarkID + ";";

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		if (c.moveToFirst()) {
			do {
				GeoPoint point = new GeoPoint();
				point.setLat(c.getDouble(c.getColumnIndex(KEY_LAT)));
				point.setLng(c.getDouble(c.getColumnIndex(KEY_LNG)));

				placemarkGeoPoints.add(point);

			} while (c.moveToNext());
		}
		return placemarkGeoPoints;
	}

	// TODO: CRUD for EVENT table

	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

}
