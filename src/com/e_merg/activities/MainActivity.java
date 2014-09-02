package com.e_merg.activities;

import java.lang.reflect.Field;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;

import com.e_merg.R;
import com.e_merg.fragments.AboutFragment;
import com.e_merg.fragments.NavigationDrawerFragment;
import com.e_merg.fragments.NearbyFragment;
import com.e_merg.fragments.NearbyMapFragment;
import com.e_merg.fragments.PickLocationMapFragment;
import com.e_merg.interfaces.OnChangeFragmentListener;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks,OnChangeFragmentListener {

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

	public static String currentCenterNo;

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
		
		// Force show overflow menu, if this is left as the default then
		// the overflow menu items will be attached to the hardware button for
		// menu
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			// field is of type java.lang.reflect.Field
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			// you can choose to display the exception
			Log.e("MainActivity: Overflow Not Set", e.toString());
		}

		if (savedInstanceState == null) {
			currentCenterNo = "";
		}
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
			.replace(R.id.container, new PickLocationMapFragment(),"PickLocationMapFragment Fragment")
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

	@Override
	public void onChangeFragment(Fragment fragment) {
		// TODO Auto-generated method stub
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.container, fragment,"New Fragment")
		.commit();
	}


}
