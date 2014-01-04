package com.movilapps.appconstruccion;

import java.io.IOException;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ArrayList<String> arrayListMenu;
	private SQLiteDatabase database;
	private boolean esProyecto;
	private ListView listViewMenu;
	MenuPpalAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_proyectos_formularios);

		Intent intent = getIntent();
		esProyecto = intent.getBooleanExtra("esProyecto", false);
		inicializarBD();
		arrayListMenu = new ArrayList<String>();

		String tag = "";
		ActionBar actionBar = getActionBar();
		
		if (esProyecto) {
			String proyecto = intent.getStringExtra("nombreProyecto");
			tag = "Formularios - " + proyecto;
			setTitle(tag);
			inicializarFormularios();

			actionBar.setDisplayHomeAsUpEnabled(true);

			adapter = new MenuPpalAdapter(this, arrayListMenu, "Formularios");

		} else {
			tag = "Proyectos";
			inicializarProyectos();
			actionBar.setDisplayHomeAsUpEnabled(false);

			adapter = new MenuPpalAdapter(this, arrayListMenu, "Proyectos");
			
		}

		listViewMenu = (ListView) findViewById(R.id.listViewProyectos_Formularios);

		listViewMenu.setAdapter(adapter);

		if (!esProyecto) {
			listViewMenu.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent generalIntent = new Intent(getApplicationContext(),
							MainActivity.class);
					generalIntent.putExtra("esProyecto", true);
					generalIntent.putExtra("nombreProyecto", arrayListMenu.get(position));
					startActivity(generalIntent);
				}
			});
		} else {
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

	public void inicializarProyectos() {

		String query = "select * from proyectos";
		Cursor c = database.rawQuery(query, null);
		c.moveToFirst();
		if (c.getCount() > 0) {
			while (c.isAfterLast() == false) {
				arrayListMenu.add(c.getString(c
						.getColumnIndex("nombre_proyecto")));
				c.moveToNext();
			}

		}	
		

	}

	public void inicializarFormularios() {

		String query = "select * from proyectos";
		Cursor c = database.rawQuery(query, null);
		c.moveToFirst();
		if (c.getCount() > 0) {
			while (c.isAfterLast() == false) {
				arrayListMenu.add(c.getString(c
						.getColumnIndex("nombre_proyecto")));
				c.moveToNext();
			}

		}

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
			if (esProyecto) {
				showMessageFormularios();
			}else{
				showMessageProyectos();
			}
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
		
		spinnerArray.add("1");
		spinnerArray.add("2");
		spinnerArray.add("3");
		spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
		input.setAdapter(spinnerArrayAdapter);		
		
		alert.setView(input);
		
		alert.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						popMessage("Formulario creado exitosamente");
					}
				});
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {

					}
				});
		alert.show();
	}

	private void showMessageProyectos() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Nuevo proyecto");
		alert.setMessage("Por favor ingrese el nombre del proyecto");

		final EditText input = new EditText(this);
		alert.setView(input);
		
		alert.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						String text = input.getEditableText().toString();
						almacenarDatos(text);
						arrayListMenu.add(text);
						adapter.notifyDataSetChanged();
						popMessage("Proyecto creado exitosamente");
					}
				});
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {

					}
				});
		alert.show();
	}
	
	private void almacenarDatos(String proyecto) {

		String query = "insert into proyectos values ('" + proyecto + "')";
		database.execSQL(query);
	}
	
	public void popMessage(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
				.show();
	}
	
	@Override
	protected void onStop() {
		database.close();
		super.onStop();
	}

}
