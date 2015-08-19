package com.example.aceral.gmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.aceral.gmap.Constants.LAT;
import static com.example.aceral.gmap.Constants.LNG;
import static com.example.aceral.gmap.Constants.TABLE_NAME;

//import com.google.android.gms.location.LocationListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    ArrayList<LatLng> arrLatLng = new ArrayList<LatLng>(); //xac+
    private Location mCurrentLocation;
    private LocationManager mManager;
    static int markerInt = 0;

    // private static String[] FROM = { _ID, TIME, TITLE, };
    private static String[] FROM = { _ID, LAT, LNG, };
    // private static String ORDER_BY = TIME + " DESC";
    private static String ORDER_BY = LAT + " DESC";
    private MarkerSQL events;



    private LocationListener mListener = new LocationListener() {
            //New location event
            @Override
            public void onLocationChanged(Location location) {
                mCurrentLocation = location;
                // GPS updates quite often, sending a Toast every 5 seconds or so
                Toast.makeText(MapsActivity.this, "GPS invoked onLocationChanged!", Toast.LENGTH_SHORT).show();
                updateDisplay(location);

            }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }
        };


    void updateDisplay(Location location) {

        LatLngBounds.Builder bounds;

        bounds = new LatLngBounds.Builder();

        mManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        GoogleMap map =( (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        LatLng tmpLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        // this works over onMapReady
        /*
        map.addMarker(new MarkerOptions()
                    .position(tmpLatLng)
                    .title("YMark" + String.valueOf(markerInt++)));
        */
        // should be in funcMarkButton arrLatLng.add(tmpLatLng);


        for(LatLng iLatLng : arrLatLng) {
            map.addMarker(new MarkerOptions()
                    .position(iLatLng)
                    .title("UMark" + String.valueOf(markerInt++)));
            bounds.include(iLatLng);
        }

        if (arrLatLng.size() > 0) {
            // map.moveCamera( CameraUpdateFactory.newLatLng(arrLatLng.get((arrLatLng.size() - 1)), 2.0)  );
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(arrLatLng.get(arrLatLng.size() - 1), 17));
        }

        mapFragment.getMapAsync(this); // needed??
    }

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.setRetainInstance(true); // otherwise will crash during orietation change

        mapFragment.getMapAsync(this); // needed?

        events = new MarkerSQL(this);
        // a quick test #1 of MarkerSQL
        /*
        try {
            LatLng llDis = new LatLng(28.418971, -81.581436);
            Double dLat = llDis.latitude;
            Double dLng = llDis.longitude;
            String sLat = dLat.toString();
            String sLng = dLng.toString();
            addMarkerSql(sLat, sLng);
            Cursor cursor = getEvents();
            showMarkers(cursor);
        } finally {
            events.close();
        */


        // a quick test #2 of loading from MarkerSQL

        try {
            Cursor cursor = getEvents();
            loadMarkers(cursor);
        } finally {
            events.close();


    }


    }

    @Override
    public void onMapReady(GoogleMap map) {

        Toast.makeText(MapsActivity.this, "onMapReady!", Toast.LENGTH_SHORT).show();



        // LatLng disneyMagicKingdom = new LatLng(28.418971, -81.581436);


        /*map.addMarker(new MarkerOptions()
                // .position(new LatLng(0,0))
                .position(disneySevenLagoon)
                .title("Marker"));
*/


        LatLngBounds.Builder bounds;

        bounds = new LatLngBounds.Builder();
/*
        map.addMarker(new MarkerOptions()
                // .position(new LatLng(0,0))
                .position(disneySevenLagoon)
                .title("Lagoon"));
        bounds.include(new LatLng(28.410067, -81.583699));

        map.addMarker(new MarkerOptions()
                // .position(new LatLng(0,0))
                .position(disneyMagicKingdom)
                .title("Kingdom"));
        bounds.include(new LatLng(28.418971, -81.581436));
*/
        /*
        for(LatLng tmpLatLng : arrLatLng) {
            map.addMarker(new MarkerOptions()
                    .position(tmpLatLng)
                    .title("UMark" + String.valueOf(markerInt++)));
            bounds.include(tmpLatLng);
        }
*/
        // move map to last position
        // moved to update //if (arrLatLng.size() > 0)
        //    map.moveCamera(CameraUpdateFactory.newLatLng(arrLatLng.get((arrLatLng.size()-1))));
        // map.moveCamera(CameraUpdateFactory.newLatLng(disneySevenLagoon));
        // CameraUpdate cU = CameraUpdateFactory.newLatLngBounds(bounds.build(), 100, 100, 50);

        //map.moveCamera(cU);

    } // onMapReady

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("keyMarkers", arrLatLng);


    }

    public void funcButtonMark(View view) {

       Toast.makeText(this, "Marking!", Toast.LENGTH_SHORT).show();
       Log.d("XAC", "Marking");

       mCurrentLocation = mManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

       if (mCurrentLocation != null ) {
           String sMsg =  String.format("Your Location:\n%.2f, %.2f", mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
           Toast.makeText(this, sMsg, Toast.LENGTH_LONG).show();
           LatLng tmpLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
           arrLatLng.add(tmpLatLng); // add to  listarray to be marked on the map

           // LatLng llDis = new LatLng(28.418971, -81.581436);
           Double dLat = tmpLatLng.latitude;
           Double dLng = tmpLatLng.longitude;
           String sLat = dLat.toString();
           String sLng = dLng.toString();
           addMarkerSql(sLat, sLng);
           Cursor cursor = getEvents();
           showMarkers(cursor);
       } else {
           Toast.makeText(this, "NO GPS location found?!", Toast.LENGTH_LONG).show();
       }


   }


   public void funcButtonPic(View view) {
       Toast.makeText(this, "Click!", Toast.LENGTH_SHORT).show();
       Log.d("XAC", "Click!");

   }


    // Add a marker in Sydney, Australia, and move the camera.
        // LatLng sydney = new LatLng(-34, 151);
        // map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // map.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // another marker from the book
        /*
        LatLng disneyMagicKingdom = new LatLng(28.418971, -81.581436);
        LatLng disneySevenLagoon = new LatLng(28.410067, -81.583699);
        */
        /*
        MarkerOptions markerOptions = new MarkerOptions()
                .draggable(false)
                .flat(false)
                .position(disneyMagicKingdom)
                .title("Magic Kingdom")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        map.addMarker(markerOptions);
        */
        /*
        Marker marker1 = map.addMarker(new MarkerOptions() .position(new LatLng(10,10)) .title("Hello World"));
        Marker marker2 = map.addMarker(new MarkerOptions() .position(new LatLng(28.410067, -81.583699)) .title("Black Lagoon was filed here"));

        LatLngBounds.Builder builder = new LatLngBounds.Builder(); // multi mark
        //for (Marker marker : map) {// multi mar
        builder.include(marker1.getPosition());// multi mar
        builder.include(marker2.getPosition());// multi mar

        //builder.include(disneyMagicKingdom);// multi mar

       // }// multi mark
        LatLngBounds bounds = builder.build();
        int padding = 0;

        // CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        //map.moveCamera(cu);
        map.moveCamera(CameraUpdateFactory.newLatLng(disneyMagicKingdom));





    }
*/

    @Override
    protected void onResume() {
        super.onResume();
        int minTime = 5000;
        float minDistance = 0;
        mManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Add a new student record
        /*
        ContentValues values = new ContentValues();

        for(LatLng ll : arrLatLng) {
            Double dLatTmp = ll.latitude;
            Double dLngTmp = ll.longitude;
            String sLatTmp = dLatTmp.toString();
            String sLngTmp = dLngTmp.toString();

            values.put(MarkerCP.sLAT, sLatTmp);
            values.put(MarkerCP.sLNG, sLngTmp);

            // values.put(StudentsProvider.GRADE, ((EditText)findViewById(R.id.editText3)).getText().toString());

            Uri uri = getContentResolver().insert(MarkerCP.uriCONTENT_URI, values);

            Toast.makeText(getBaseContext(),
                    uri.toString(), Toast.LENGTH_LONG).show();

        }
        */

    }



    private void addMarkerSql(String sLat, String sLng) {
        // Insert a new record into the Events data source.
        // You would do something similar for delete and update.
        SQLiteDatabase db = events.getWritableDatabase();
        ContentValues values = new ContentValues();
        // values.put(TIME, System.currentTimeMillis());
        // values.put(TITLE, string);
        values.put(LAT, sLat);
        values.put(LNG, sLng);
        db.insertOrThrow(TABLE_NAME, null, values);
    }

    private Cursor getEvents() {
        // Perform a managed query. The Activity will handle closing
        // and re-querying the cursor when needed.
        SQLiteDatabase db = events.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, FROM, null, null, null,
                null, ORDER_BY);
        startManagingCursor(cursor);
        return cursor;
    }

    private void showMarkers(Cursor cursor) {
        // Stuff them all into a big string
        StringBuilder builder = new StringBuilder(
                "Saved events:\n");
        while (cursor.moveToNext()) {
            // Could use getColumnIndexOrThrow() to get indexes
            // long id = cursor.getLong(0);
            // long time = cursor.getLong(1);
            // String title = cursor.getString(2);
            long id = cursor.getLong(0);
            String lat  = cursor.getString(1);
            String lng  = cursor.getString(2);
            builder.append(id).append(": ");
            //builder.append(time).append(": ");
            builder.append(lat).append(": ");
            builder.append(lng).append("\n");
        }
        // Display on the screen
        TextView text = (TextView) findViewById(R.id.text);
        text.setText(builder);
    }


    private void loadMarkers(Cursor cursor) {
        // Stuff SQL markers into local ArrayList arrLatLng
        // StringBuilder builder = new StringBuilder("Saved events:\n");
        while (cursor.moveToNext()) {

            long id = cursor.getLong(0);
            String sLat  = cursor.getString(1);
            String sLng  = cursor.getString(2);
            Double dLat = Double.parseDouble(sLat);
            Double dLng = Double.parseDouble(sLng);
            arrLatLng.add(new LatLng(dLat, dLng));
        }
        // Display on the screen
        TextView text = (TextView) findViewById(R.id.text);
    }


} // MapsActivity