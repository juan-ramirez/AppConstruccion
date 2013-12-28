package com.movilapps.appconstruccion;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuPpalAdapter extends ArrayAdapter<String> {

	private Context context;
	private ArrayList<String> values;

	public MenuPpalAdapter(Context context, ArrayList<String> values) {
		super(context, R.layout.row_layout_adapter_proyectos_formularios,
				values);
		this.values = values;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(
				R.layout.row_layout_adapter_proyectos_formularios, parent,
				false);

		final int pos = position;

		Button buttonEliminar = (Button) rowView
				.findViewById(R.id.buttonEliminar);

		TextView txtViewMenuPpalOpcion = (TextView) rowView
				.findViewById(R.id.txtViewMenuPpalOpcion);

		txtViewMenuPpalOpcion.setText(values.get(position));
		buttonEliminar.setText("Eliminar");

		buttonEliminar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// REMOVE THE ACTION FROM THE ADAPTER'S ARRAYLIST
				values.remove(pos);
				notifyDataSetChanged();
				Activity activity = (Activity) context;

				Toast.makeText(context, activity.getTitle(), Toast.LENGTH_SHORT)
						.show();
			}
		});

		return rowView;
	}

}
