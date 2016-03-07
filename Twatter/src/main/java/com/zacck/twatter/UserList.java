package com.zacck.twatter;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class UserList extends AppCompatActivity {

	ArrayList<String> twatterUsers;
	ListView UserListView;
	ArrayAdapter mAdp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);
		android.support.v7.app.ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);

		UserListView = (ListView) findViewById(R.id.mUserListView);

		if (ParseUser.getCurrentUser().get("isFollowing") == null) {
			List<String> emptyList = new ArrayList<>();
			ParseUser.getCurrentUser().put("isFollowing", emptyList);
		}

		twatterUsers = new ArrayList();

		mAdp = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, twatterUsers);

		UserListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
		UserListView.setAdapter(mAdp);

		UserListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CheckedTextView mUserVIew = (CheckedTextView) view;
				if (mUserVIew.isChecked()) {
					Log.i(getPackageName(), "Row Checked + " + position);
					ParseUser.getCurrentUser().getList("isFollowing").add(twatterUsers.get(position));
					ParseUser.getCurrentUser().saveInBackground();

				} else {
					Log.i(getPackageName(), "Row Not Checked + " + position);
					ParseUser.getCurrentUser().getList("isFollowing").remove(twatterUsers.get(position));
					ParseUser.getCurrentUser().saveInBackground();
				}

			}
		});

		//fetch a user list
		ParseQuery<ParseUser> mTwatterUsersQuery = ParseUser.getQuery();
		mTwatterUsersQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
		mTwatterUsersQuery.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> objects, ParseException e) {
				if (e == null) {
					//lets add each user we get lets add their name to the dataset
					twatterUsers.clear();
					for (ParseUser currUser : objects) {
						twatterUsers.add(currUser.getUsername());
					}

					mAdp.notifyDataSetChanged();

					//chesck which user the user follows show them as ticked
					for (String currUsername : twatterUsers) {
						if (ParseUser.getCurrentUser().getList("isFollowing").contains(currUsername)) {
							UserListView.setItemChecked(twatterUsers.indexOf(currUsername), true);
						}
					}

				} else {
					Log.i(getPackageName(), "There was an error");
				}

			}
		});

	}

	public void alert(String msg)
	{
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	//create menus
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_user_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.menu_tweet) {

			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
