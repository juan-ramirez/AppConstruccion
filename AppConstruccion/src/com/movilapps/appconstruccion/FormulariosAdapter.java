package com.movilapps.appconstruccion;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class FormulariosAdapter extends ArrayAdapter<String> {

	private Context context;
	private ArrayList<String> values;

	public FormulariosAdapter(Context context, ArrayList<String> values) {
		super(context, R.layout.row_layout_adapter_formulario,
				values);
		this.values = values;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(
				R.layout.row_layout_adapter_formulario, parent,
				false);
		
		
		TextView textViewFormularioElemento = (TextView) rowView
				.findViewById(R.id.textViewFormularioElemento);

		textViewFormularioElemento.setText(values.get(position));
		
		

		ArrayAdapter<String> spinnerArrayAdapter;

		ArrayList<String> spinnerArray = new ArrayList<String>();
		
		spinnerArray.add("1");
		spinnerArray.add("2");
		spinnerArray.add("3");

		spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spinnerArray);

		Spinner spinnerFormulario = (Spinner) rowView
				.findViewById(R.id.spinnerFormulario);
		
		spinnerFormulario.setAdapter(spinnerArrayAdapter);
		

		return rowView;
	}
}
