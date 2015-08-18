package com.example.aceral.gmap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;


public class MarkerCP extends ContentProvider{

    // xac1 - ContentProvider contains a SQL db (and other db such as media) under the hood
    // db is just a handle; will use the helper get its instance of db (helper has the db)
    // via getWritebleDatabase
    private SQLiteDatabase db;  // just a handle

    // xac3 - a SQL db has db name, table name, and db version, defined here then later passed to
    // SQL db creation by the SQLHelper (that gives us a methodC, methodR() interface rather than
    // db.execSQL(somt long command)
    static final String sDATABASE_NAME = "DbMarker";
    static final String sTABLE_NAME = "TblMarker";
    static final int iDATABASE_VERSION = 1;
    // xac4 - bulding up the long ass SQL command to pass to SQ.execSQL that will create
    // the db table : DbMarker
    // db table name : TblMarker
    // column #1 _id
    // column #2 lat (string)
    // column #3 lng (string)
    static final String sCREATE_DB_TABLE =
            " CREATE TABLE " + sTABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " lat TEXT NOT NULL, " +
                    " lng TEXT NOT NULL);";

    // xac7 - an URI is needed for content provider USERS to access the content provider
    static final String PROVIDER_NAME = "com.example.aceral.gmap"; // authority?
    static final String URL = "content://" + PROVIDER_NAME + "/D  bMarker"; // db name
    static final Uri uriCONTENT_URI = Uri.parse(URL); // creates the Uri from authority+db name

    //xac2 - although the SQL db has// methods to C.R.U.D., they are implemented via "execSQL".
    // An object method interface methodC, methodR, etc is more friendly than db.execSQL(super long string to create db)
    private static class DatabaseHelper extends SQLiteOpenHelper {

        // BUILT IN! SQLiteDatabase db; !!

        DatabaseHelper(Context context){
            super(context, sDATABASE_NAME, null, iDATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(sCREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  sTABLE_NAME);
            onCreate(db);
        }
    }



    // xac10 - temporary places to hold SQL row values
    // populated by the CP caller

    static final String _ID = "_id";
    static final String sLAT = "constLatitude";
    static final String sLNG = "constLongitude";
    // xac5 - the helper (an object in the CP) needs to create the SQL db embedded in the CP
    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    // xac12 - oh boy - projection is part of the Android built-in API data passed
    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {

        /*
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(sTABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case STUDENTS:
                qb.setProjectionMap(STUDENTS_PROJECTION_MAP);
                break;

            case STUDENT_ID:
                qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder == ""){

            sortOrder = NAME;
        }
        Cursor c = qb.query(db,	projection,	selection, selectionArgs,null, null, sortOrder);


        //register to watch a content URI for changes

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    */

        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    // xac8 - CP users will call this helper insert method
    // the CP user will pass 1) uri - which/where db
    // 2) the value to pass in

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long rowID = db.insert(	sTABLE_NAME, "", values); // get the next free row pointer

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(uriCONTENT_URI, rowID); // uriOcn
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        } else {
            throw new SQLException("Failed to add a record into " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    // xac12 - uriMatch is an instance of a static class (?)
    // it converts URI (content://com.example.aceral.gmap/sTABLE_NAME to an integer
    // the integer will be used in a switch to decode what to do
    //
    /*
    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "students", STUDENTS);
        uriMatcher.addURI(PROVIDER_NAME, "students/#", STUDENT_ID);
    }
    */
}
