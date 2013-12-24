package com.movilapps.appconstruccion;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
	}
	
	public void doLogin(View v) {
		Intent generalIntent = new Intent(getApplicationContext(), MainActivity.class);		
		startActivity(generalIntent);		
		finish();
	}
	
}
