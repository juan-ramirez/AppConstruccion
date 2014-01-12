package com.movilapps.appconstruccion;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ArrayList<String> arrayListMenu;
	private SQLiteDatabase database;
	private ListView listViewMenu;
	MenuPpalAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_proyectos_formularios);

		
		inicializarBD();
		arrayListMenu = new ArrayList<String>();

		ActionBar actionBar = getActionBar();

		setTitle("Formularios");
		inicializarFormularios();

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
				startActivity(generalIntent);
			}
		});

	}

	public void inicializarBD() {

		DataBaseHelper dbHelper = new DataBaseHelper(this);

		try {
			dbHelper.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
		try {
			dbHelper.openDataBase();
		} catch (SQLException sqle) {
			throw sqle;
		}
		database = dbHelper.myDataBase;
	}
	public void inicializarFormularios() {

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
		spinnerArray.add("MEZCLA, TRANSPORTE, COLOCACIÓN Y CURADO DE CONCRETOS");
		spinnerArray.add("CONSTRUCCIÓN Y RETIRO DE FORMALETAS, OBRA FALSA");
		spinnerArray.add("COLOCACIÓN ACERO DE REFUERZO");
		spinnerArray.add("ACEPTACIÓN DE ELEMENTOS VACIADOS");
		spinnerArray.add("REQUISITOS DE EJECUCIÓN - MUROS Y ELEMENTOS DE MAMPOSTERÍA");
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

}
