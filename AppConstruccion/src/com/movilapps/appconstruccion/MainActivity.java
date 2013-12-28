package com.movilapps.appconstruccion;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MainActivity extends Activity {

	ArrayList<String> arrayListMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_proyectos_formularios);

		Intent intent = getIntent();
		boolean esProyecto = intent.getBooleanExtra("esProyecto", false);

		arrayListMenu = new ArrayList<String>();

		String tag = "";
		ActionBar actionBar = getActionBar();
		if (esProyecto) {
			tag = "Formularios";
			setTitle(tag);
			inicializarFormularios();

			actionBar.setDisplayHomeAsUpEnabled(true);

		} else {
			tag = "Proyectos";
			inicializarProyectos();
			actionBar.setDisplayHomeAsUpEnabled(false);
		}

		ListView listViewMenu = (ListView) findViewById(R.id.listViewProyectos_Formularios);
		MenuPpalAdapter adapter = new MenuPpalAdapter(this, arrayListMenu);
		listViewMenu.setAdapter(adapter);

		if (!esProyecto) {
			listViewMenu.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent generalIntent = new Intent(getApplicationContext(),
							MainActivity.class);
					generalIntent.putExtra("esProyecto", true);
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

	public void inicializarProyectos() {

		arrayListMenu.add("Proyecto 1");
		arrayListMenu.add("Proyecto 2");
		arrayListMenu.add("Proyecto 3");
		arrayListMenu.add("Proyecto 4");
		arrayListMenu.add("Proyecto 5");
		arrayListMenu.add("Proyecto 1");
		arrayListMenu.add("Proyecto 2");
		arrayListMenu.add("Proyecto 3");
		arrayListMenu.add("Proyecto 4");
		arrayListMenu.add("Proyecto 5");
		arrayListMenu.add("Proyecto 1");
		arrayListMenu.add("Proyecto 2");
		arrayListMenu.add("Proyecto 3");
		arrayListMenu.add("Proyecto 4");
		arrayListMenu.add("Proyecto 5");

	}

	public void inicializarFormularios() {

		arrayListMenu.add("Formulario 1");
		arrayListMenu.add("Formulario 2");
		arrayListMenu.add("Formulario 3");
		arrayListMenu.add("Formulario 4");
		arrayListMenu.add("Formulario 5");
		arrayListMenu.add("Formulario 1");
		arrayListMenu.add("Formulario 2");
		arrayListMenu.add("Formulario 3");
		arrayListMenu.add("Formulario 4");
		arrayListMenu.add("Formulario 5");
		arrayListMenu.add("Formulario 1");
		arrayListMenu.add("Formulario 2");
		arrayListMenu.add("Formulario 3");
		arrayListMenu.add("Formulario 4");
		arrayListMenu.add("Formulario 5");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		Bundle bundle = getIntent().getExtras();		
			
			if (bundle != null) {
				String result = bundle.getString("perfil","empty");
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
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void popMessage(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
				.show();
	}

}
