package com.zacck.twatter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class UserList extends AppCompatActivity {

	ArrayList twatterUsers;
	ListView UserListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);
		UserListView = (ListView)findViewById(R.id.mUserListView);

		twatterUsers = new ArrayList();
		twatterUsers.add("Mattew");
		twatterUsers.add("Zacck");

		ArrayAdapter mAdp = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, twatterUsers);

		UserListView.setAdapter(mAdp);






	}
}
