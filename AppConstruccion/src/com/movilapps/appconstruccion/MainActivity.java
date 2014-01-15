package com.movilapps.appconstruccion;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainActivity extends Activity {

	private ArrayList<String> arrayListMenu;
	private ListView listViewMenu;
	MenuPpalAdapter adapter;
	private ArrayList<ArrayList<String>> formularios = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_proyectos_formularios);

		arrayListMenu = new ArrayList<String>();
		ActionBar actionBar = getActionBar();

		setTitle("Formularios");

		actionBar.setDisplayHomeAsUpEnabled(true);

		adapter = new MenuPpalAdapter(this, arrayListMenu, "Formularios");

		listViewMenu = (ListView) findViewById(R.id.listViewProyectos_Formularios);

		listViewMenu.setAdapter(adapter);

		listViewMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent generalIntent = new Intent(getApplicationContext(),
						FormularioActivity.class);
				generalIntent.putExtra("esCargar", true);
				generalIntent.putStringArrayListExtra("formulario",
						formularios.get(position));

				int numeroFormulario = Integer.valueOf(formularios
						.get(position).get(1));
				generalIntent.putExtra("numeroFormulario", numeroFormulario);

				String form = formularios.get(position).get(0);
				String nombreFormulario = form.substring(0, form.length() - 23);
				generalIntent.putExtra("nombreFormulario", nombreFormulario);

				startActivity(generalIntent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			String result = bundle.getString("perfil", "empty");
			if (!result.equals("administrador")) {
				menu.findItem(R.id.action_settings).setVisible(false);
			}
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(getApplicationContext(),
					AdministrarUsuariosActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_new:

			showMessageFormularios();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showMessageFormularios() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Nuevo formulario");
		alert.setMessage("Por favor escoja uno de los formularios");

		final Spinner input = new Spinner(this);
		ArrayAdapter<String> spinnerArrayAdapter;
		ArrayList<String> spinnerArray = new ArrayList<String>();

		spinnerArray.add("EMPAQUE Y ROTULADO DE CEMENTO");
		spinnerArray.add("EVALUACIÓN CONCRETO");
		spinnerArray.add("VERIFICACIÓN CONDICIONES DE CIMENTACIÓN");
		spinnerArray
				.add("MEZCLA, TRANSPORTE, COLOCACIÓN Y CURADO DE CONCRETOS");
		spinnerArray.add("CONSTRUCCIÓN Y RETIRO DE FORMALETAS, OBRA FALSA");
		spinnerArray.add("COLOCACIÓN ACERO DE REFUERZO");
		spinnerArray.add("ACEPTACIÓN DE ELEMENTOS VACIADOS");
		spinnerArray
				.add("REQUISITOS DE EJECUCIÓN - MUROS Y ELEMENTOS DE MAMPOSTERÍA");
		spinnerArray.add("LIBERACIÓN DE ELEMENTOS");

		spinnerArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, spinnerArray);
		input.setAdapter(spinnerArrayAdapter);

		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				Intent generalIntent = new Intent(getApplicationContext(),
						FormularioActivity.class);
				generalIntent.putExtra("numeroFormulario",
						(input.getSelectedItemPosition() + 1));
				generalIntent.putExtra("nombreFormulario", input
						.getSelectedItem().toString());
				startActivity(generalIntent);

			}
		});
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});
		alert.show();
	}

	public void popMessage(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
				.show();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		SharedPreferences mPrefs = getSharedPreferences("my_prefs",
				MODE_PRIVATE);
		Gson gson = new Gson();
		String jsonFormularios = mPrefs.getString("Formularios", "");

		if (jsonFormularios.equals("")) {
			Toast.makeText(this, "No encontrado", Toast.LENGTH_SHORT).show();
			formularios = new ArrayList<ArrayList<String>>();
		} else {
			formularios = gson.fromJson(jsonFormularios, ArrayList.class);
		}

		for (int i = 0; i < formularios.size(); i++) {
			String element = formularios.get(i).get(0);
			if (!arrayListMenu.contains(element)) {
				arrayListMenu.add(element);
			}

		}
		adapter.notifyDataSetChanged();

	}

}
