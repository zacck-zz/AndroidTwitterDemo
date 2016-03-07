/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.zacck.twatter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity {

	EditText etUserName, etPassword;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ParseAnalytics.trackAppOpenedInBackground(getIntent());
		etUserName = (EditText) findViewById(R.id.etUserName);
		etPassword = (EditText) findViewById(R.id.etUserPassWord);

		if(ParseUser.getCurrentUser()!= null && ParseUser.getCurrentUser().isAuthenticated())
		{
			goToList();
		}




	}

	public void goToList()
	{
		Intent UserListIntent =  new Intent(this, UserFeed.class);
		startActivity(UserListIntent);

	}

	public void loginOrSignUp(View view) {
		final String UserName, Password;

		if (!etUserName.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty())
		{
			UserName = etUserName.getText().toString();
			Password = etPassword.getText().toString();
			ParseUser.logInInBackground(UserName, Password, new LogInCallback() {
				@Override
				public void done(ParseUser user, ParseException e) {
					if (user != null) {
						goToList();

					} else {
						//lets sign the user up with same credentials
						ParseUser mUser = new ParseUser();
						mUser.setUsername(UserName);
						mUser.setPassword(Password);
						mUser.signUpInBackground(new SignUpCallback() {
							@Override
							public void done(ParseException e) {
								if (e == null) {
									//there were no issues
									Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_LONG).show();
								} else {
									//show user error
									Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

								}

							}
						});


					}
				}
			});

		}
		else
		{
			Toast.makeText(getApplicationContext(), "Please Enter Both UserName and Password!", Toast.LENGTH_LONG).show();
		}


	}

}
