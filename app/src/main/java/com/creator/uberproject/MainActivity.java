package com.creator.uberproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {

    private final static String QUERY = "riderOrDriver";
    private final static String RIDER = "rider";
    private final static String DRIVER = "driver";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Log.i("Parse_User", ParseUser.getCurrentUser().getUsername());

        try {
            Log.i("Current State", (String) ParseUser.getCurrentUser().get(QUERY));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (ParseUser.getCurrentUser() == null) {
            ParseAnonymousUtils.logIn(new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        Log.i("Log in Anonymous", "Success");
                    } else {
                        Log.i("Log in Anonymous", "Failed");
                    }
                }
            });
        } else if (ParseUser.getCurrentUser().get(QUERY) != null) {
            Log.i("Switch Parse", "Redirecting to " + ParseUser.getCurrentUser().get(QUERY));
            redirect();
        }

    }

    public void getStarted(View view) {
        Switch userTypeSwitch = findViewById(R.id.userTypeSwitch);
        Log.i("Switch Value", String.valueOf(userTypeSwitch.isChecked()));
        String userType = DRIVER;
        if (userTypeSwitch.isChecked()) {
            userType = RIDER;
        }
        ParseUser user = ParseUser.getCurrentUser();
        user.put(QUERY, userType);
        final String finalUserType = userType;
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i("Switch Parse Btn", "Redirecting to " + ParseUser.getCurrentUser().get(QUERY));
                redirect();
            }
        });

    }

    private void redirect() {
        Intent intent = new Intent();
        if (ParseUser.getCurrentUser().get(QUERY).equals(RIDER)) {
            intent = new Intent(this, RiderActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.i("Rider", RIDER);
            startActivity(intent);
        } else if (ParseUser.getCurrentUser().get(QUERY).equals(DRIVER)) {
            intent = new Intent(this, ViewRequestsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.i("Driver", DRIVER);
            startActivity(intent);
        }
    }
}