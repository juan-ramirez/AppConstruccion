package com.movilapps.appconstruccion;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FormularioActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		setTitle("Formulario 1");

		Tab tab = actionBar
				.newTab()
				.setText("Formulario")
				.setTabListener(
						new TabListener<ExampleFragment>(this, "Formulario",
								ExampleFragment.class));
		actionBar.addTab(tab);

		tab = actionBar
				.newTab()
				.setText("Observaciones")
				.setTabListener(
						new TabListener<ExampleFragmentTwo>(this,
								"Observaciones", ExampleFragmentTwo.class));
		actionBar.addTab(tab);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_formulario, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return (true);
		}

		return (super.onOptionsItemSelected(item));
	}

	public static class TabListener<T extends Fragment> implements
			ActionBar.TabListener {
		private Fragment mFragment;
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;

		/**
		 * Constructor used each time a new tab is created.
		 * 
		 * @param activity
		 *            The host Activity, used to instantiate the fragment
		 * @param tag
		 *            The identifier tag for the fragment
		 * @param clz
		 *            The fragment's Class, used to instantiate the fragment
		 */
		public TabListener(Activity activity, String tag, Class<T> clz) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
		}

		/* The following are each of the ActionBar.TabListener callbacks */

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// Check if the fragment is already initialized
			if (mFragment == null) {
				// If not, instantiate and add it to the activity
				mFragment = Fragment.instantiate(mActivity, mClass.getName());
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				// If it exists, simply attach it in order to show it
				ft.attach(mFragment);
			}
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				// Detach the fragment, because another one is being attached
				ft.detach(mFragment);
			}
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// User selected the already selected tab. Usually do nothing.
		}
	}

	public static class ExampleFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Inflate the layout for this fragment
			View rootView = inflater.inflate(R.layout.activity_main_proyectos_formularios,
					container, false);
			ArrayList<String> arrayListMenu = new ArrayList<String>();
			
			arrayListMenu.add("Elemento 1");
			arrayListMenu.add("Elemento 2");
			arrayListMenu.add("Elemento 3");
			arrayListMenu.add("Elemento 4");
			arrayListMenu.add("Elemento 5");
			arrayListMenu.add("Elemento 1");
			arrayListMenu.add("Elemento 2");
			arrayListMenu.add("Elemento 3");
			arrayListMenu.add("Elemento 4");
			arrayListMenu.add("Elemento 5");
			arrayListMenu.add("Elemento 1");
			arrayListMenu.add("Elemento 2");
			
			ListView listViewMenu = (ListView) rootView.findViewById(R.id.listViewProyectos_Formularios);
			FormulariosAdapter adapter = new FormulariosAdapter(getActivity(), arrayListMenu);
			listViewMenu.setAdapter(adapter);
			
			return rootView;
		}

	}

	public static class ExampleFragmentTwo extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Inflate the layout for this fragment

			View rootView = inflater.inflate(R.layout.activity_observaciones,
					container, false);
			
			

			return rootView;
		}
	}

}
