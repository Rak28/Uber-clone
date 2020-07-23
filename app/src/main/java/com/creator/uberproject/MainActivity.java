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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        } else if (ParseUser.getCurrentUser().get("riderOrDriver") != null) {
            Log.i("Switch Parse", "Redirecting to " + ParseUser.getCurrentUser().get("riderOrDriver"));
        }

    }

    public void getStarted(View view) {
        Switch userTypeSwitch = findViewById(R.id.userTypeSwitch);
        Log.i("Switch Value", String.valueOf(userTypeSwitch.isChecked()));
        String usertype = "driver";
        if (userTypeSwitch.isChecked()) {
            usertype = "rider";
        }
        ParseUser.getCurrentUser().put("riderOrDriver", usertype);
        Log.i("Switch Parse", "Redirecting to " + ParseUser.getCurrentUser().get("riderOrDriver"));
        Intent intent = new Intent(this, RiderActivity.class);
        startActivity(intent);
    }
}