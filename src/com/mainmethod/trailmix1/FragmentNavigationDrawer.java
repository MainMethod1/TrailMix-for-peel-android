package com.mainmethod.trailmix1;

/*
** FragmentNavigationDrawer object for use with support-v4 library using compatibility fragments
*/

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/***
 * <h1> TrailMix for Android Capstone Project </h1>
 * <h2> This is a custom class for the Left Navigation Menu which extends 
 * DrawerLayout </h2>
 * <p> Client: Erica Duque </p>
 * <p> Oganization: Region of Peel </p>
 * @author jonathan zarate, parth sondarva, shivam sharma, garrett may
 * @version 1.0
 */

public class FragmentNavigationDrawer extends DrawerLayout {
	// define private global variables
	private ActionBarDrawerToggle drawerToggle;
	private ListView lvDrawer;
	private NavDrawerListAdapter drawerAdapter;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private ArrayList<FragmentNavItem> drawerNavItems;    
	private int drawerContainerRes;

	/***
	 * Define the constructors for this class.  However, they are currently not in use
	 * and are reserved for future testing purposes.
	 * @param context - Defines what context/"activity" you are currently in
	 * @param attrs - Defines which attributes you wish to pass to each fragment
	 * @param defStyle -
	 */
	public FragmentNavigationDrawer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FragmentNavigationDrawer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FragmentNavigationDrawer(Context context) {
		super(context);
	}
    
	// setupDrawerConfiguration((ListView) findViewById(R.id.lvDrawer), R.layout.drawer_list_item, R.id.flContent);
	/***
	 * 
	 * @param drawerListView
	 * @param drawerItemRes
	 * @param drawerContainerRes
	 */
    public void setupDrawerConfiguration(ListView drawerListView, int drawerItemRes, int drawerContainerRes) {
        // Setup navigation items array
        drawerNavItems = new ArrayList<FragmentNavigationDrawer.FragmentNavItem>();         
        navDrawerItems = new ArrayList<NavDrawerItem>();                
        this.drawerContainerRes = drawerContainerRes;  
        // Setup drawer list view
        lvDrawer = drawerListView; 
        // Setup item listener
        lvDrawer.setOnItemClickListener(new FragmentDrawerItemListener());
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        drawerToggle = setupDrawerToggle();
        setDrawerListener(drawerToggle);
        // set a custom shadow that overlays the main content when the drawer
        setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // Setup action buttons
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    /***
     * <h1> Adding Navigation Items and Create Fragments/"Views" from each item </h1>
     * @param navTitle - Defines the name values for each nav item
     * @param icon - Sets the icon associated with each nav item
     * @param windowTitle - Defines the window title for each fragment activity
     * @param fragmentClass - Creates a new fragment for each nav item on click
     */
    public void addNavItem(String navTitle, int icon, String windowTitle, Class<? extends Fragment> fragmentClass) { 
        // adding nav drawer items to array
        navDrawerItems.add(new NavDrawerItem(navTitle, icon)); 
        // Set the adapter for the list view
        drawerAdapter = new NavDrawerListAdapter(getActivity(), navDrawerItems);    
        lvDrawer.setAdapter(drawerAdapter);
        drawerNavItems.add(new FragmentNavItem(windowTitle, fragmentClass));
    }

    /***
     * Swaps fragments in the main content view
     * @param position - Defines the item "position" based on integer value
     */
	public void selectDrawerItem(int position) {
		// Create a new fragment and specify the planet to show based on position
		FragmentNavItem navItem = drawerNavItems.get(position);
		// Initialize fragment variable
		Fragment fragment = null;
		try {
			fragment = navItem.getFragmentClass().newInstance();
			
			Bundle args = navItem.getFragmentArgs();
			if (args != null) { 
			  fragment.setArguments(args);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("MainActivity", "Error in creating fragment");
		}

		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction =  fragmentManager.beginTransaction().replace(drawerContainerRes, fragment);
		if(fragment == HomeFragment.newInstance()){
			//do not add it to back stack 
		}else{
			transaction.addToBackStack(null);
		}
		
		transaction.commit();

		// Highlight the selected item, update the title, and close the drawer
		lvDrawer.setItemChecked(position, true);
		setTitle(navItem.getTitle());
		closeDrawer(lvDrawer);	
	}
	
	public ActionBarDrawerToggle getDrawerToggle() {
		return drawerToggle;
	}

	private FragmentActivity getActivity() {
		return (FragmentActivity) getContext();
	}

	private ActionBar getActionBar() {
		return getActivity().getActionBar();
	}

	private void setTitle(CharSequence title) {
		getActionBar().setTitle(title);
	}
	
	private class FragmentDrawerItemListener implements ListView.OnItemClickListener {		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectDrawerItem(position);
		}	
	}
	
	private class FragmentNavItem {
		// Define private global variables
		private Class<? extends Fragment> fragmentClass;
		private String title;
		private Bundle fragmentArgs;
		
		public FragmentNavItem(String title, Class<? extends Fragment> fragmentClass) {
			this(title, fragmentClass, null);
		}
		
		public FragmentNavItem(String title, Class<? extends Fragment> fragmentClass, Bundle args) {
			this.fragmentClass = fragmentClass;
			this.fragmentArgs = args;
			this.title = title;
		}
		
		public Class<? extends Fragment> getFragmentClass() {
			return fragmentClass;
		}
		
		public String getTitle() {
			return title;
		}
		
		public Bundle getFragmentArgs() {
			return fragmentArgs;
		}
	}
	
	private ActionBarDrawerToggle setupDrawerToggle() {
		return new ActionBarDrawerToggle(getActivity(), /* host Activity */
		this, /* DrawerLayout object */
		R.drawable.ic_navigation_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				// setTitle(getCurrentTitle());
				getActivity().invalidateOptionsMenu(); // call onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				// setTitle("Navigate");
				getActivity().invalidateOptionsMenu(); // call onPrepareOptionsMenu()
			}
		};
	}

	public boolean isDrawerOpen() {
		return isDrawerOpen(lvDrawer);
	}
}
