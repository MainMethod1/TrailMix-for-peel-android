package com.mainmethod.trailmix1.sqlite.helper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.google.android.gms.maps.model.LatLng;
import com.mainmethod.trailmix1.kmlparsing.NavigationSaxHandler;
import com.mainmethod.trailmix1.kmlparsing.PlacemarkObj;
import com.mainmethod.trailmix1.kmlparsing.TrailObj;
import com.mainmethod.trailmix1.sqlite.model.Event;
import com.mainmethod.trailmix1.sqlite.model.GeoPoint;
import com.mainmethod.trailmix1.sqlite.model.Placemark;
import com.mainmethod.trailmix1.sqlite.model.Session;
import com.mainmethod.trailmix1.sqlite.model.SessionGeopoint;
import com.mainmethod.trailmix1.sqlite.model.Trail;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;
import android.util.Log;

/***
 * <h1> TrailMix for Android Capstone Project </h1>
 * <h2> Database class to create tables and run queries on Android device </h2>
 * <p> Client: Erica Duque </p>
 * <p> Oganization: Region of Peel </p>
 * @author jonathan zarate, parth sondarva, shivam sharma, garrett may
 * @version 1.0
 */
public class DatabaseHelper extends SQLiteOpenHelper {


	private static final int DATABASE_VERSION = 5;
	private static final String DATABASE_NAME = "final.db";

	private static String DB_PATH = "/data/data/com.mainmethod.trailmix1/databases/";
	private SQLiteDatabase myDatabase;
	private final Context myContext;


	// Logcat tag
	private static final String LOG = "DatabaseHelper";
	// table names
	private static final String TRAIL_TABLE = "trails";
	private static final String GEOPOINT_TABLE = "geoPoints";
	private static final String EVENT_TABLE = "events";
	private static final String PLACEMARK_TABLE = "placemarks";
  private static final String SESSION_TABLE = "sessions";
	private static final String SESSION_GEOPOINT_TABLE = "sessionGeoPoints";

	// Common column names
	private static final String KEY_ID = "_id";
	private static final String KEY_CREATED_AT = "created_at";

	// TRAILS Table - column names

	private static final String KEY_NAME = "name";
	private static final String KEY_TRAIL_CLASS = "class";
	private static final String KEY_LENGTH = "length";
	private static final String KEY_SURFACE = "surfaceType";
	private static final String KEY_TYPE = "type";
	private static final String KEY_AMENITIES = "amenities";
	private static final String KEY_PARKING = "parking";
	private static final String KEY_SEASON_HOURS = "seasonHours";
	private static final String KEY_LIGHTING = "lighting";
	private static final String KEY_WINTER_MAINTENANCE = "winterMaintenance";
	private static final String KEY_PETS = "pets";
	private static final String KEY_NOTES = "notes";
	private static final String KEY_CITY = "city";

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

	// SESSION Table - column names
	private static final String KEY_DISTANCE = "distance";
	private static final String KEY_SPEED = "speed";
	private static final String KEY_TIME = "time";

	// SESSION_GEOPOINT Table - column names
	private static final String KEY_SESSION_ID = "session_id";


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
			+ KEY_AMENITIES
			+ " TEXT,"
			+ KEY_PARKING
			+ " TEXT,"
			+ KEY_SEASON_HOURS
			+ " TEXT,"
			+ KEY_LIGHTING
			+ " TEXT,"
			+ KEY_WINTER_MAINTENANCE
			+ " TEXT,"
			+ KEY_PETS
			+ " TEXT,"
			+ KEY_NOTES
			+ " TEXT,"
			+ KEY_CITY
			+ " TEXT,"
			+ KEY_CREATED_AT
			+ " DATETIME DEFAULT CURRENT_TIMESTAMP" + ");";

	// Placemark table create statement
	private static final String CREATE_TABLE_PLACEMARK = "CREATE TABLE IF NOT EXISTS "
			+ PLACEMARK_TABLE
			+ "("
			+ KEY_ID
			+ " INTEGER PRIMARY KEY,"
			+ KEY_TRAIL_CLASS
			+ " TEXT,"
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

	// Session table create statement
	private static final String CREATE_TABLE_SESSION = "CREATE TABLE IF NOT EXISTS " + SESSION_TABLE + "(" + KEY_ID
			+ " INTEGER PRIMARY KEY," + KEY_DISTANCE + " TEXT," + KEY_SPEED + " TEXT," + KEY_TIME + " TEXT,"
			+ KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ");";
	// Session_Geopoint create statement
	private static final String CREATE_TABLE_SESSION_GEOPOINT = "CREATE TABLE IF NOT EXISTS " + SESSION_GEOPOINT_TABLE
			+ "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LAT + " REAL NOT NULL," + KEY_LNG + " REAL NOT NULL,"
			+ KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP," + KEY_SESSION_ID + " INTEGER," + "FOREIGN KEY ("
			+ KEY_SESSION_ID + ") REFERENCES " + SESSION_TABLE + " (" + KEY_ID + ")" + ");";


	// Event table create statement
	private static final String CREATE_TABLE_EVENT = "CREATE TABLE IF NOT EXISTS "
			+ EVENT_TABLE
			+ "("
			+ KEY_ID
			+ " INTEGER PRIMARY KEY,"
			+ KEY_TITLE
			+ " TEXT NOT NULL,"
			+ KEY_DESC
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
		this.myContext = context;
	}

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.myContext = context;
	}



	@Override
	public void onCreate(SQLiteDatabase db) {

		// creating required tables
		db.execSQL(CREATE_TABLE_TRAIL);
		db.execSQL(CREATE_TABLE_PLACEMARK);
		db.execSQL(CREATE_TABLE_GEOPOINT);
		db.execSQL(CREATE_TABLE_EVENT);
		db.execSQL(CREATE_TABLE_SESSION);
		db.execSQL(CREATE_TABLE_SESSION_GEOPOINT);

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
		db.execSQL("DROP TABLE IF EXISTS " + SESSION_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + SESSION_GEOPOINT_TABLE);

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
		values.put(KEY_AMENITIES, trail.getAmenities());
		values.put(KEY_PARKING, trail.getParking());
		values.put(KEY_SEASON_HOURS, trail.getSeasonHours());
		values.put(KEY_LIGHTING, trail.getLighting());
		values.put(KEY_WINTER_MAINTENANCE, trail.getWinterMaintenance());
		values.put(KEY_PETS, trail.getPets());
		values.put(KEY_NOTES, trail.getNotes());
		values.put(KEY_CITY, trail.getCity());

		// insert row
		long trail_id = db.insert(TRAIL_TABLE, null, values);

		return trail_id;
	}

	/*
	 * get single trail by id
	 */
	public Trail getTrailByName(String trail_name) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TRAIL_TABLE + " WHERE "
				+ KEY_NAME + " = '" + trail_name +"';";

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
		trail.setAmenities(c.getString(c.getColumnIndex(KEY_AMENITIES)));
		trail.setParking(c.getString(c.getColumnIndex(KEY_PARKING)));
		trail.setSeasonHours(c.getString(c.getColumnIndex(KEY_SEASON_HOURS)));
		trail.setLighting(c.getString(c.getColumnIndex(KEY_LIGHTING)));
		trail.setWinterMaintenance(c.getString(c.getColumnIndex(KEY_WINTER_MAINTENANCE)));
		trail.setPets(c.getString(c.getColumnIndex(KEY_PETS)));
		trail.setNotes(c.getString(c.getColumnIndex(KEY_NOTES)));
		trail.setCity(c.getString(c.getColumnIndex(KEY_CITY)));


		return trail;
	}


	public HashMap<String,Trail> getAllTrailsWithInfo(){
		HashMap<String, Trail> namedTrails = new HashMap<String, Trail>();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor c = db.query(TRAIL_TABLE, null, KEY_AMENITIES + " IS NOT NULL", null,
				null, null, null);
		Trail trail;
		if (c.moveToFirst()) {
			do {
				trail = new Trail();
				trail.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				trail.setTrailName((c.getString(c.getColumnIndex(KEY_NAME))));
				trail.setTrailClass(c.getString(c.getColumnIndex(KEY_TRAIL_CLASS)));
				trail.setLength(c.getDouble(c.getColumnIndex(KEY_LENGTH)));
				trail.setSurface(c.getString(c.getColumnIndex(KEY_SURFACE)));
				trail.setAmenities(c.getString(c.getColumnIndex(KEY_AMENITIES)));
				trail.setParking(c.getString(c.getColumnIndex(KEY_PARKING)));
				trail.setSeasonHours(c.getString(c.getColumnIndex(KEY_SEASON_HOURS)));
				trail.setLighting(c.getString(c.getColumnIndex(KEY_LIGHTING)));
				trail.setWinterMaintenance(c.getString(c.getColumnIndex(KEY_WINTER_MAINTENANCE)));
				trail.setPets(c.getString(c.getColumnIndex(KEY_PETS)));
				trail.setNotes(c.getString(c.getColumnIndex(KEY_NOTES)));
				trail.setCity(c.getString(c.getColumnIndex(KEY_CITY)));

				// adding to hashmap
				namedTrails.put(trail.getTrailName(), trail);
			} while (c.moveToNext());
		}
		return namedTrails;
	}
	/*
	 * get single trail by name
	 */
	public HashMap<String, Trail> getAllTrails() {
		HashMap<String, Trail> namedTrails = new HashMap<String, Trail>();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor c = db.query(TRAIL_TABLE, null, null, null,
				null, null, null);
		Trail trail;
		if (c.moveToFirst()) {
			do {
				trail = new Trail();
				trail.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				trail.setTrailName((c.getString(c.getColumnIndex(KEY_NAME))));
				trail.setTrailClass(c.getString(c.getColumnIndex(KEY_TRAIL_CLASS)));
				trail.setLength(c.getDouble(c.getColumnIndex(KEY_LENGTH)));
				trail.setSurface(c.getString(c.getColumnIndex(KEY_SURFACE)));
				trail.setAmenities(c.getString(c.getColumnIndex(KEY_AMENITIES)));
				trail.setParking(c.getString(c.getColumnIndex(KEY_PARKING)));
				trail.setSeasonHours(c.getString(c.getColumnIndex(KEY_SEASON_HOURS)));
				trail.setLighting(c.getString(c.getColumnIndex(KEY_LIGHTING)));
				trail.setWinterMaintenance(c.getString(c.getColumnIndex(KEY_WINTER_MAINTENANCE)));
				trail.setPets(c.getString(c.getColumnIndex(KEY_PETS)));
				trail.setNotes(c.getString(c.getColumnIndex(KEY_NOTES)));
				trail.setCity(c.getString(c.getColumnIndex(KEY_CITY)));

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
		values.put(KEY_LENGTH, trail.getLength());
		values.put(KEY_SURFACE, trail.getSurface());
		values.put(KEY_AMENITIES, trail.getAmenities());
		values.put(KEY_PARKING, trail.getParking());
		values.put(KEY_SEASON_HOURS, trail.getSeasonHours());
		values.put(KEY_LIGHTING, trail.getLighting());
		values.put(KEY_WINTER_MAINTENANCE, trail.getWinterMaintenance());
		values.put(KEY_PETS, trail.getPets());
		values.put(KEY_NOTES, trail.getNotes());
		values.put(KEY_CITY, trail.getCity());

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

	// delete geopoints for placemark
	public void deletePlacemarkGeoPoints(long placemark_id) {

		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(GEOPOINT_TABLE, KEY_PLACEMARK_ID + " = ?",
				new String[] { String.valueOf(placemark_id) });
	}

	public ArrayList<ArrayList<GeoPoint>> getPlacemarks(){
		ArrayList<ArrayList<GeoPoint>> placemarks = new ArrayList<ArrayList<GeoPoint>>();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM " +GEOPOINT_TABLE + ";";
		Cursor c = db.rawQuery(selectQuery,null);

		ArrayList<GeoPoint> allPoints = new ArrayList<GeoPoint>();
		if(c.moveToFirst()){
			do{
				GeoPoint point = new GeoPoint();
				point.setPlacemark_id(c.getInt(c.getColumnIndex(KEY_PLACEMARK_ID)));
				point.setLat(c.getDouble(c.getColumnIndex(KEY_LAT)));
				point.setLng(c.getDouble(c.getColumnIndex(KEY_LNG)));

				allPoints.add(point);

			}while (c.moveToNext());
		}

		int currPId = 1;
		ArrayList<GeoPoint> points = new ArrayList<GeoPoint>();

		for (GeoPoint g: allPoints)
		{
			if(g.getPlacemark_id()==currPId)
			{
				points.add(g);
			}
			else
			{
				currPId++;
				placemarks.add(points);
				points = new ArrayList<GeoPoint>();
				points.add(g);


			}
		}
		placemarks.add(points);
		return placemarks;
	}

	// get geopoints for placemark
	public ArrayList<GeoPoint> getPlacemarkGeoPoints(int placemarkID) {
		ArrayList<GeoPoint> placemarkGeoPoints = new ArrayList<GeoPoint>();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + PLACEMARK_TABLE
				+ " INNER JOIN " + GEOPOINT_TABLE + " ON " + PLACEMARK_TABLE
				+ "." + KEY_ID + " = " + GEOPOINT_TABLE + "."
				+ KEY_PLACEMARK_ID + " WHERE " + PLACEMARK_TABLE + "." + KEY_ID
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

	// get placemarks for trail
	public ArrayList<Placemark> getTrailPlacemarks(String trailName) {
		ArrayList<Placemark> trailPlacemarks = new ArrayList<Placemark>();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TRAIL_TABLE + " INNER JOIN "
				+ PLACEMARK_TABLE + " ON " + TRAIL_TABLE + "." + KEY_ID + " = "
				+ PLACEMARK_TABLE + "." + KEY_TRAIL_ID + " WHERE "
				+ TRAIL_TABLE + "." + KEY_NAME + " = " + "'" + trailName + "';";

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		if (c.moveToFirst()) {
			do {
				Placemark p = new Placemark();
				p.setId(c.getInt(c.getColumnIndex(PLACEMARK_TABLE +"."+KEY_ID)));
				p.setTrail_id(c.getInt(c.getColumnIndex(KEY_TRAIL_ID)));


				trailPlacemarks.add(p);

			} while (c.moveToNext());
		}
		return trailPlacemarks;
	}

	public ArrayList<Placemark> getTrailPlacemarksByClass(String trailClass) {
		ArrayList<Placemark> trailPlacemarks = new ArrayList<Placemark>();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TRAIL_TABLE + " INNER JOIN "
				+ PLACEMARK_TABLE + " ON " + TRAIL_TABLE + "." + KEY_ID + " = "
				+ PLACEMARK_TABLE + "." + KEY_TRAIL_ID + " WHERE "
				+ PLACEMARK_TABLE + "." + KEY_TRAIL_CLASS + " = " + "'" + trailClass + "';";

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		if (c.moveToFirst()) {
			do {
				Placemark p = new Placemark();
				p.setId(c.getInt(c.getColumnIndex(PLACEMARK_TABLE +"."+KEY_ID)));
				p.setTrail_id(c.getInt(c.getColumnIndex(KEY_TRAIL_ID)));


				trailPlacemarks.add(p);

			} while (c.moveToNext());
		}
		return trailPlacemarks;
	}





	// CRUD for Placemark
	public long createPlacemark(Placemark p) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TRAIL_ID,p.getTrail_id());
		values.put(KEY_TRAIL_CLASS , p.getTrail_class());

		long column_id = db.insert(PLACEMARK_TABLE, null, values);

		return column_id;

	}


	// CRUD for EVENT table

	//create a new event
	public long createEvent(Event e) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, e.getTitle());
		values.put(KEY_DESC,e.getDesc());
		values.put(KEY_URL, e.getUrl());
		values.put(KEY_URL_TEXT, e.getUrl_text());
		values.put(KEY_DATE, e.getDate());
		values.put(KEY_START_TIME, e.getStartTime());

		values.put(KEY_END_TIME, e.getEndTime());
		values.put(KEY_IS_ALL_DAY_LONG, e.isAllDayEvent() ? 1 : 0);
		values.put(KEY_LOCATION, e.getLocation());
		values.put(KEY_CONTACT_NAME, e.getContactName());
		values.put(KEY_CONTACT_EMAIL, e.getContactEmail());
		values.put(KEY_EVENT_POSTER_URL, e.getPoster_url());

		long column_id = db.insert(EVENT_TABLE, null, values);

		return column_id;

	}

	//get all events from db
	public ArrayList<Event> getAllEvents() {
		ArrayList<Event> eventCollection = new ArrayList<Event>();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor c = db.query(EVENT_TABLE, null, null, null,
				null, null, null);
		if (c.moveToFirst()) {
			do {
				Event event = new Event();
				event.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				event.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
				event.setDesc(c.getString(c
						.getColumnIndex(KEY_DESC)));
				event.setUrl(c.getString(c.getColumnIndex(KEY_URL)));
				event.setUrl_text(c.getString(c.getColumnIndex(KEY_URL_TEXT)));
				event.setDate(c.getString(c.getColumnIndex(KEY_DATE)));
				event.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));
				event.setStartTime(c.getString(c.getColumnIndex(KEY_START_TIME)));
				event.setEndTime(c.getString(c.getColumnIndex(KEY_END_TIME)));
				event.setAllDayEvent((c.getInt(c.getColumnIndex(KEY_IS_ALL_DAY_LONG)) !=0));
				event.setContactName(c.getString(c.getColumnIndex(KEY_CONTACT_NAME)));
				event.setContactEmail(c.getString(c.getColumnIndex(KEY_CONTACT_EMAIL)));
				if(c.getString(c.getColumnIndex(KEY_EVENT_POSTER_URL)) != null){
					event.setPoster_url(c.getString(c.getColumnIndex(KEY_EVENT_POSTER_URL)));
				} else{
					event.setPoster_url("");
				}
				// adding to hashmap
				eventCollection.add(event);
			} while (c.moveToNext());
		}
		return eventCollection;
	}

	//gets event by name
	public Event getEventByName(String name){
		Event event = null;
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM " + EVENT_TABLE + " WHERE "
			 + KEY_TITLE + " = " + "'" + name + "';";

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst()) {
			do {
			    event = new Event();
				event.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				event.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
				event.setDesc(c.getString(c
						.getColumnIndex(KEY_DESC)));
				event.setUrl(c.getString(c.getColumnIndex(KEY_URL)));
				event.setUrl_text(c.getString(c.getColumnIndex(KEY_URL_TEXT)));
				event.setDate(c.getString(c.getColumnIndex(KEY_DATE)));
				event.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));
				event.setStartTime(c.getString(c.getColumnIndex(KEY_START_TIME)));
				event.setEndTime(c.getString(c.getColumnIndex(KEY_END_TIME)));
				event.setAllDayEvent((c.getInt(c.getColumnIndex(KEY_IS_ALL_DAY_LONG)) !=0));
				event.setContactName(c.getString(c.getColumnIndex(KEY_CONTACT_NAME)));
				event.setContactEmail(c.getString(c.getColumnIndex(KEY_CONTACT_EMAIL)));
				if(c.getString(c.getColumnIndex(KEY_EVENT_POSTER_URL)) != null){
					event.setPoster_url(c.getString(c.getColumnIndex(KEY_EVENT_POSTER_URL)));
				} else{
					event.setPoster_url("");
				}




			} while (c.moveToNext());
		}

		return event;
	}
	// delete event
		public void deleteEvent(long event_id) {

			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(EVENT_TABLE, KEY_ID + " = ?",
					new String[] { String.valueOf(event_id) });
		}

		//delete all events
		public void deleteAllEvent() {

			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(EVENT_TABLE, null,
					null);
			closeDB();
		}


		// SESSION Table
			/*
			* Creating a session
			*/
			public long createSession(Session session) {
				SQLiteDatabase db = this.getWritableDatabase();

				ContentValues values = new ContentValues();
				values.put(KEY_DISTANCE, session.getDistance());
				values.put(KEY_SPEED, session.getSpeed());
				values.put(KEY_TIME, session.getTime());

				// insert row
				long session_id = db.insert(SESSION_TABLE, null, values);

				return session_id;
			}
			/*
			 * Get all Sessions
			 */
			public ArrayList<Session> getAllSessions(){
				ArrayList<Session> sessions = new ArrayList<Session>();
				Session session = null;
				SQLiteDatabase db = this.getReadableDatabase();
				String selectQuery = "SELECT * FROM " + SESSION_TABLE + ";";

				Log.e(LOG, selectQuery);

				Cursor c = db.rawQuery(selectQuery, null);
				if (c.moveToFirst()) {
					do {
					    session = new Session();
						session.setId(c.getInt(c.getColumnIndex(KEY_ID)));
						session.setDistance(Double.parseDouble(c.getString(c.getColumnIndex(KEY_DISTANCE))));
						session.setTime(Double.parseDouble(c.getString(c
								.getColumnIndex(KEY_TIME))));
						session.setSpeed(Double.parseDouble(c.getString(c.getColumnIndex(KEY_SPEED))));
						session.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
						sessions.add(session);
					} while (c.moveToNext());
				}

				return sessions;
			}
			/*
			 * Get Session by id
			 */
			public Session getSessionById(int id){
				Session session = null;
				SQLiteDatabase db = this.getReadableDatabase();
				String selectQuery = "SELECT * FROM " + SESSION_TABLE + " WHERE "
					 + KEY_ID + " = " + id + ";";

				Log.e(LOG, selectQuery);

				Cursor c = db.rawQuery(selectQuery, null);
				if (c.moveToFirst()) {
					do {
					    session = new Session();
						session.setId(c.getInt(c.getColumnIndex(KEY_ID)));
						session.setDistance(Double.parseDouble(c.getString(c.getColumnIndex(KEY_DISTANCE))));
						session.setTime(Double.parseDouble(c.getString(c
								.getColumnIndex(KEY_TIME))));
						session.setSpeed(Double.parseDouble(c.getString(c.getColumnIndex(KEY_SPEED))));
						session.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
					} while (c.moveToNext());
				}

				return session;
			}
       //SESSION_GEOPOINT Table
			/*
			* Creating a session_geopoint
			*/
			public long createSessionGeopoint(SessionGeopoint geopoint) {

				SQLiteDatabase db = this.getWritableDatabase();

				ContentValues values = new ContentValues();
				values.put(KEY_LAT, geopoint.getLat());
				values.put(KEY_LNG, geopoint.getLng());
				values.put(KEY_SESSION_ID, geopoint.getSession_id());

				// insert row
				long geopoint_id = db.insert(SESSION_GEOPOINT_TABLE, null, values);

				return geopoint_id;
			}
       
			/*
			 * get session geopoints by session id
			 */
			public ArrayList<SessionGeopoint> getSessionGeopoints(int session_id) {
				ArrayList<SessionGeopoint> sessionGeoPoints = new ArrayList<SessionGeopoint>();
				SQLiteDatabase db = this.getReadableDatabase();
				String selectQuery = "SELECT  * FROM " + SESSION_TABLE
						+ " INNER JOIN " + SESSION_GEOPOINT_TABLE + " ON " + SESSION_TABLE
						+ "." + KEY_ID + " = " + GEOPOINT_TABLE + "."
						+ KEY_SESSION_ID + " WHERE " + SESSION_TABLE + "." + KEY_ID
						+ " = " + session_id + ";";

				Log.e(LOG, selectQuery);

				Cursor c = db.rawQuery(selectQuery, null);

				if (c.moveToFirst()) {
					do {
						SessionGeopoint point = new SessionGeopoint();
						point.setLat(c.getDouble(c.getColumnIndex(KEY_LAT)));
						point.setLng(c.getDouble(c.getColumnIndex(KEY_LNG)));
                        
						sessionGeoPoints.add(point);

					} while (c.moveToNext());
				}
				return sessionGeoPoints;
			}
			
			/*
			 * delete all sessions data
			 */
			public void deleteAllSessionsData() {

				SQLiteDatabase db = this.getWritableDatabase();
				db.delete(SESSION_TABLE, null,
						null);
				db.delete(SESSION_GEOPOINT_TABLE, null, null);
				closeDB();
			}
  
			
			/*
			 * Get row count for a table
			 * 
			 */
			
			public long getRowCount(String table){
				long count = 0;
				SQLiteDatabase db = this.getReadableDatabase();
				String query = "SELECT COUNT(*) FROM "+table +";";
				count = db.compileStatement(query).simpleQueryForLong();
				return count;
			}
	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 *
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DATABASE_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DB_PATH + DATABASE_NAME;
		myDatabase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);

	}

	@Override
	public synchronized void close() {

		if (myDatabase != null)
			myDatabase.close();

		super.close();

	}

}
