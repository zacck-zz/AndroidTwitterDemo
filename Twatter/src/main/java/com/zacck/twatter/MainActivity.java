/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.zacck.twatter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;


public class MainActivity extends AppCompatActivity {

	EditText etUserName, etPassword;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ParseAnalytics.trackAppOpenedInBackground(getIntent());
		etUserName = (EditText) findViewById(R.id.etUserName);
		etPassword = (EditText) findViewById(R.id.etUserPassWord);


	}

	public void loginOrSignUp(View view) {
		String UserName, Password;

		if (!etUserName.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
			Toast.makeText(getApplicationContext(), "Please Enter Both UserName and Password!", Toast.LENGTH_LONG).show();
		} else {
			UserName = etUserName.getText().toString();
			Password = etPassword.getText().toString();
			ParseUser.logInInBackground(UserName, Password, new LogInCallback() {
				@Override
				public void done(ParseUser user, ParseException e) {
					if (user != null)
					{

					}
					else
					{
						//lets sign the user up with same credentials

					}
				}
			});
		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
