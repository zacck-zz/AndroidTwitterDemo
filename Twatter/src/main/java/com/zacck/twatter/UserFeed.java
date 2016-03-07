package com.zacck.twatter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserFeed extends AppCompatActivity {

	ListView mUserFeed;
	List<Map<String, String>> tweetData;
	SimpleAdapter myAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_feed);
		mUserFeed = (ListView)findViewById(R.id.mUseFeedList);


		//lets make a couple of multiline list items
		tweetData = new ArrayList<>();

		//adapter for list
		 myAdapter = new SimpleAdapter(this,
				tweetData,
				android.R.layout.simple_expandable_list_item_2,
				new String[]{"content","username"},
				new int[] {android.R.id.text2, android.R.id.text1});
		mUserFeed.setAdapter(myAdapter);
		getTweets();

	}
	public void alert(String msg)
	{
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	public void getTweets()
	{
		//fetch a user list
		ParseQuery<ParseObject> mTwatterTwatsQuery = ParseQuery.getQuery("Tweet");
		//temporalily add this use to teir own feed
		Collection<String> timeline = ParseUser.getCurrentUser().getList("isFollowing");
		if(timeline != null) {
			timeline.add(ParseUser.getCurrentUser().getUsername());
			mTwatterTwatsQuery.whereContainedIn("username", timeline);
			mTwatterTwatsQuery.orderByDescending("createdAt");
			mTwatterTwatsQuery.findInBackground(new FindCallback<ParseObject>() {
				@Override
				public void done(List<ParseObject> tweets, ParseException e) {
					if (e == null) {
						//lets add each user we get lets add their name to the dataset
						tweetData.clear();
						for (ParseObject currTweet : tweets) {
							Map<String, String> Tweet = new HashMap<String, String>();
							Tweet.put("content", currTweet.getString("content"));
							Tweet.put("username", currTweet.getString("username"));
							tweetData.add(Tweet);
						}

						myAdapter.notifyDataSetChanged();

					} else {
						alert(e.getMessage());
					}

				}
			});
		}
		else
		{
			alert(getResources().getString(R.string.follow));
			Intent mFollowersIntent = new Intent(UserFeed.this, UserList.class);
			startActivity(mFollowersIntent);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_user_feed, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
			case R.id.Followers:
				Intent mFollowersIntent = new Intent(UserFeed.this, UserList.class);
				startActivity(mFollowersIntent);
				return super.onOptionsItemSelected(item);
			case R.id.action_tweet:
				//alertDialog
				AlertDialog.Builder mTweetDialog = new AlertDialog.Builder(UserFeed.this);
				mTweetDialog.setTitle(R.string.send_tweet);
				//and View During Runtime
				final EditText etUserTweet = new EditText(UserFeed.this);

				mTweetDialog.setView(etUserTweet);
				mTweetDialog.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.i(getPackageName(), etUserTweet.getText().toString());

						ParseObject mTweet = new ParseObject("Tweet");
						mTweet.put("content",etUserTweet.getText().toString());
						mTweet.put("username", ParseUser.getCurrentUser().getUsername());
						mTweet.saveInBackground(new SaveCallback() {
							@Override
							public void done(ParseException e) {
								if(e == null)
								{
									alert(getResources().getString(R.string.tweet_success));
									getTweets();
								}
								else
								{
									alert(e.getMessage());
								}

							}
						});


					}
				});

				mTweetDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				mTweetDialog.show();
				return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}
}
