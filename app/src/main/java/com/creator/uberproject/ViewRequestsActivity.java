package com.creator.uberproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ViewRequestsActivity extends AppCompatActivity {

    private LocationManager locationManager;

    private LocationListener locationListener;

    private ListView listView;

    private ArrayList<String> request;

    private ArrayAdapter<String> adapter;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                    if (lastKnownLocation != null) {
//                        setMap(lastKnownLocation);
//                    }
                }
            }
        }


    }

    private void updateListView(final Location location) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");
        final ParseGeoPoint parseGeoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
        query.whereNear("Location", parseGeoPoint);
        query.setLimit(10);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {
                    request.clear();
                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
//                            ParseGeoPoint parseGeoPoint = object.getParseGeoPoint("Location");
                            Double distanceInMiles = parseGeoPoint.distanceInMilesTo((ParseGeoPoint) object.get("Location"));
                            Log.i("distanceInMiles", String.valueOf(distanceInMiles));
                            Double distance = (double) Math.round(distanceInMiles * 10) / 10;
                            request.add(distance + "mi");
                            Log.i("Geo_Points", distance.toString() + object.getString("Username"));

                        }
                    } else {
                        request.add("No active requests nearby");
                    }

                    adapter.notifyDataSetChanged();
                }

            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);
        request = new ArrayList<>();
        request.add("Getting location");
        listView = findViewById(R.id.listView);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateListView(location);
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.i("Location", String.valueOf(lastKnownLocation));
            if (lastKnownLocation != null) {
                updateListView(lastKnownLocation);
            }

        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, request);
        Log.i("Request", request.toString());
        listView.setAdapter(adapter);


    }


    public void logout(View view) {
        ParseUser.logOut();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

