package com.example.aceral.gmap;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

//import com.google.android.gms.location.LocationListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    ArrayList<LatLng> arrLatLng = new ArrayList<LatLng>(); //xac+
    private Location mCurrentLocation;
    private LocationManager mManager;



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
            mManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            GoogleMap map =( (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

            LatLng tmpLatLng = new LatLng(location.getLatitude(), location.getLatitude());
            map.addMarker(new MarkerOptions()
                    .position(tmpLatLng)
                    .title("UMark"));

        mManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap map) {

        Toast.makeText(MapsActivity.this, "onMapReady!", Toast.LENGTH_SHORT).show();


        LatLng disneySevenLagoon = new LatLng(28.410067, -81.583699);
        LatLng disneyMagicKingdom = new LatLng(28.418971, -81.581436);


        /*map.addMarker(new MarkerOptions()
                // .position(new LatLng(0,0))
                .position(disneySevenLagoon)
                .title("Marker"));
*/


        LatLngBounds.Builder bounds;

        bounds = new LatLngBounds.Builder();

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


        for(LatLng tmpLatLng : arrLatLng) {
            map.addMarker(new MarkerOptions()
                    .position(tmpLatLng)
                    .title("UMark"));
            bounds.include(tmpLatLng);
        }
        map.moveCamera(CameraUpdateFactory.newLatLng(disneySevenLagoon));
        // CameraUpdate cU = CameraUpdateFactory.newLatLngBounds(bounds.build(), 100, 100, 50);

        //map.moveCamera(cU);

    } // onMapReady


    public void funcButtonMark(View view) {
       Toast.makeText(this, "Marking!", Toast.LENGTH_SHORT).show();
       Log.d("XAC", "Marking");
       mCurrentLocation = mManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
       if (mCurrentLocation != null ) {
           String sMsg =  String.format("Your Location:\n%.2f, %.2f", mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
           Toast.makeText(this, sMsg, Toast.LENGTH_LONG).show();
           LatLng tmpLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
           arrLatLng.add(tmpLatLng);
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
} // MapsActivity