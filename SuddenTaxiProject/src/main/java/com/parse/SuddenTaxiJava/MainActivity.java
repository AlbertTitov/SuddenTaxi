/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.SuddenTaxiJava;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class MainActivity extends AppCompatActivity  {

  final String LOG_TAG = "myLogs";

    public void redirectActivity() {

        if (ParseUser.getCurrentUser().getString("riderOrDriver").equals("rider")) {

            Intent intent = new Intent(getApplicationContext(), RiderActivity.class);
            startActivity(intent);

        } else {

            Intent intent = new Intent(getApplicationContext(), ViewRequestsActivity.class);
            startActivity(intent);
        }
    }

    public void getStarted(View v) {

        Switch userTypeSwitch = (Switch) findViewById(R.id.userTypeSwitch);

        Log.i(LOG_TAG, String.valueOf(userTypeSwitch.isChecked()));

        String userType = "rider";

        if (userTypeSwitch.isChecked()) {

            userType = "driver";
        }

        ParseUser.getCurrentUser().put("riderOrDriver", userType);
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                redirectActivity();
            }
        });
    }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

      getSupportActionBar().hide();

      if (ParseUser.getCurrentUser() == null || ParseUser.getCurrentUser().getUsername().length() < 23) {
          //в одной instance хранятся
          // данные для двух приложений и randomly created anonymous username - примерно 25 символов,
          // соответственно в приложении MyWondLife стоит ограничение на количество символов username,
          // до 22-х

          ParseAnonymousUtils.logIn(new LogInCallback() {
              @Override
              public void done(ParseUser user, ParseException e) {

                  if (e == null) {

                      Log.i(LOG_TAG, "Anonymous login successful");

                  } else {

                      Log.i(LOG_TAG, "Anonymous login failed");
                  }
              }
          });

      } else {

          if (ParseUser.getCurrentUser().get("riderOrDriver") != null) {

              Log.i(LOG_TAG, "Redirecting as " + ParseUser.getCurrentUser().get("riderOrDriver"));

              redirectActivity();
          }
      }

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }


}
