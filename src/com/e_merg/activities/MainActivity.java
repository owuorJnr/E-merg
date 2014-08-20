package com.e_merg.activities;

import com.e_merg.fragments.AboutFragment;
import com.e_merg.fragments.AddCenterFragment;
import com.e_merg.fragments.NavigationDrawerFragment;
import com.e_merg.fragments.NearbyFragment;
import com.e_merg.fragments.NearbyMapFragment;
import com.e_merg.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		if(position == 0){
			//Nearby Centers
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.container, new NearbyFragment(),"Nearby Fragment")
			.commit();
			
		}else if(position == 1){
			//Add a Center
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.container, new AddCenterFragment(),"AddCenter Fragment")
			.commit();
			
		}else if(position == 2){
			//Feedback
			
			Intent mail = new Intent(Intent.ACTION_SEND);
            mail.putExtra(Intent.EXTRA_EMAIL, new String[]{"dickytea@gmail.com","patweru@gmail.com","nenokirui@gmail.com"});
            mail.setType("message/rfc822");
            startActivity(Intent.createChooser(mail,"choose email service"));
					
		}
		
	}


	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.main, menu);
		restoreActionBar();
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_about) {
			
			//about the app
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.container, new AboutFragment(),"About Fragment")
			.commit();
			
			return true;
		}else if(id == R.id.action_map){
			
			//nearby centers in a map
			getSupportFragmentManager().beginTransaction()
			.replace(R.id.container, new NearbyMapFragment(),"Map Fragment")
			.commit();
			
			return true;
		}else if(id == R.id.action_share){
			
			String message = "Download E-merg from Google Play https://play.google.com/store/apps/details/?id=com.e_merg";
			
			//send as text
			Intent shareIntent;
			shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.putExtra(Intent.EXTRA_TEXT, message);
			shareIntent.setType("text/plain");
			startActivity(Intent.createChooser(shareIntent,"choose share service"));
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
