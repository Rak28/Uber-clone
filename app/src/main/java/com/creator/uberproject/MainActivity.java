package com.creator.uberproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

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
    }
}