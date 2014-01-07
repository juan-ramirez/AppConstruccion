package com.movilapps.appconstruccion;

import java.util.ArrayList;

import android.content.Context;
import android.sax.RootElement;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class FormularioAdapter extends ArrayAdapter<DatoFormularioFactory> {

	private Context context;
	private ArrayList<DatoFormularioFactory> values;

	public FormularioAdapter(Context context,
			ArrayList<DatoFormularioFactory> values) {
		super(context, R.layout.row_layout_adapter_proyectos_formularios,
				values);
		this.values = values;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = null;

		switch (values.get(position).getTipo()) {
		case 1:
			rowView = inflater.inflate(R.layout.row_layout_formulario_1,
					parent, false);
			EditText editTextFormularios = (EditText) rowView
					.findViewById(R.id.editTextFormularios);
			break;
		case 2:
			rowView = inflater.inflate(R.layout.row_layout_formulario_2,
					parent, false);
			break;
		case 3:
			rowView = inflater.inflate(R.layout.row_layout_formulario_3,
					parent, false);
			break;
		case 4:
			rowView = inflater.inflate(R.layout.row_layout_formulario_4,
					parent, false);
			Spinner spinnerFormularios = (Spinner) rowView
					.findViewById(R.id.spinnerFormularios);
			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
					context, android.R.layout.simple_spinner_dropdown_item,
					values.get(position).getListaSpinner());
			spinnerFormularios.setAdapter(spinnerArrayAdapter);
			
			break;
		case 5:
			rowView = inflater.inflate(R.layout.row_layout_formulario_5,
					parent, false);
			break;

		default:
			rowView = inflater.inflate(R.layout.row_layout_formulario_6,
					parent, false);
			break;
		}

		TextView textViewFormularios = (TextView) rowView
				.findViewById(R.id.textViewFormularios);
		textViewFormularios.setText(values.get(position).getTitulo());

		return rowView;
	}

}
