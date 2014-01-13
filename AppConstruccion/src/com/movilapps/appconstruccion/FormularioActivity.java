package com.movilapps.appconstruccion;

import java.util.ArrayList;

import com.google.gson.Gson;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

public class FormularioActivity extends FragmentActivity {
	Intent generalIntent;
	static ArrayList<View> arrayListElementosFormulario;
	static ArrayList<DatoFormularioFactory> arrayListFormulario;
	ArrayList<String> datosPDF;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		generalIntent = getIntent();

		setTitle("Formulario "
				+ generalIntent.getIntExtra("numeroFormulario", 1));

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
		ExampleFragment fragment = (ExampleFragment) getFragmentManager()
				.findFragmentByTag("Formulario");

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
		case R.id.action_save:
			datosPDF = new ArrayList<String>();
			Log.e("Vacíos", String.valueOf(recogerDatos()));
			
			SharedPreferences mPrefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
			Editor prefsEditor = mPrefs.edit();
			Gson gson = new Gson();
			String json = gson.toJson(datosPDF);
			Log.e("JSON: ", json);
			prefsEditor.putString("MyObject", json);
			Log.e("Commited: ", String.valueOf(prefsEditor.commit()));
				

			return (true);
		case R.id.action_email:
			datosPDF = new ArrayList<String>();
			Log.e("Vacíos", String.valueOf(recogerDatos()));
			PDFWriterFormulario.savePDF(datosPDF);
			return (true);
		}

		return (super.onOptionsItemSelected(item));
	}

	private boolean recogerDatos() {

		for (int i = 0; i < arrayListFormulario.size(); i++) {

			View elemento = arrayListElementosFormulario.get(i);
			String titulo = arrayListFormulario.get(i).getTitulo();

			switch (arrayListFormulario.get(i).getTipo()) {
			case 1:
				EditText editTextFormularios = (EditText) elemento
						.findViewById(R.id.editTextFormularios);

				String texto = editTextFormularios.getText().toString().trim();
				if (texto.equals("")) {
					return false;
				}

				datosPDF.add(titulo + " " + texto);
				break;
			case 2:
				TimePicker timePickerFormularios = (TimePicker) elemento
						.findViewById(R.id.timePickerFormularios);

				datosPDF.add(titulo + " "
						+ timePickerFormularios.getCurrentHour() + ":"
						+ timePickerFormularios.getCurrentMinute());
				break;
			case 3:
				CheckBox checkBoxFormularios = (CheckBox) elemento
						.findViewById(R.id.checkBoxFormularios);

				EditText editTextFormulariosCheckbox = (EditText) elemento
						.findViewById(R.id.editTextFormulariosCheckbox);

				if (checkBoxFormularios.isChecked()) {
					datosPDF.add(titulo + " No aplica");
				} else {
					String textoEdit = editTextFormulariosCheckbox.getText()
							.toString().trim();
					if (textoEdit.equals("")) {
						return false;
					}
					datosPDF.add(titulo + " " + textoEdit);
				}
				break;
			case 4:

				Spinner spinnerFormularios = (Spinner) elemento
						.findViewById(R.id.spinnerFormularios);

				datosPDF.add(titulo + " "
						+ spinnerFormularios.getSelectedItem().toString());
				break;
			case 5:
				DatePicker datePickerFecha = (DatePicker) elemento
						.findViewById(R.id.datePickerFecha);

				datosPDF.add(titulo + " " + datePickerFecha.getYear() + "/"
						+ (datePickerFecha.getMonth() + 1) + "/"
						+ datePickerFecha.getDayOfMonth());
				break;
			default:
				datosPDF.add(titulo);
				break;
			}
		}

		for (int i = 0; i < datosPDF.size(); i++) {
			Log.d("Datos :  ", datosPDF.get(i));
		}
		return true;
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
				// ft.attach(mFragment);
				ft.show(mFragment);
			}
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				// Detach the fragment, because another one is being attached
				// ft.detach(mFragment);
				ft.hide(mFragment);
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

			FormularioFactory mFormularioFactory = new FormularioFactory();

			int numeroFormulario = getActivity().getIntent().getIntExtra(
					"numeroFormulario", 1);

			arrayListFormulario = mFormularioFactory
					.getFormulario(numeroFormulario);

			// Inflate the layout for this fragment
			View rootView = inflater.inflate(R.layout.activity_formulario,
					container, false);

			LinearLayout formulario = (LinearLayout) rootView
					.findViewById(R.id.linearLayoutFormulario);

			FormularioViewGenerator mFormularioViewGenerator = new FormularioViewGenerator(
					inflater, container, getActivity(), arrayListFormulario);

			arrayListElementosFormulario = mFormularioViewGenerator
					.generarFormulario();

			for (int i = 0; i < arrayListElementosFormulario.size(); i++) {
				formulario.addView(arrayListElementosFormulario.get(i),
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
			}

			Log.e("ERROR: ", "Reinstanciado");

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
			Log.e("ERROR: ", "Reinstanciado");

			return rootView;
		}

	}

}
