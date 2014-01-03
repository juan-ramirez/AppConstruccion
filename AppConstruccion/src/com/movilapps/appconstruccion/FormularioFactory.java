package com.movilapps.appconstruccion;

import java.util.ArrayList;

public class FormularioFactory {

	private ArrayList<DatoFormularioFactory> formulario;

	public FormularioFactory() {
		super();
	}

	public ArrayList<DatoFormularioFactory> getFormulario(int tipo) {
		formulario = new ArrayList<DatoFormularioFactory>();
		switch (tipo) {
		case 1:
			return getf1();
		case 2:
			return getf2();
		case 3:
			return getf3();
		case 4:
			return getf4();
		case 5:
			return getf5();
		case 6:
			return getf6();
		case 7:
			return getf7();
		case 8:
			return getf8();
		case 9:
			return getf9();
			
		default:
			return null;
			break;
		}
		
	}

	private ArrayList<DatoFormularioFactory> getf1() {
		
		formulario.add(new DatoFormularioFactory(5,"Fecha"));
		formulario.add(new DatoFormularioFactory(5,"Fecha"));
		formulario.add(new DatoFormularioFactory(5,"Fecha"));
		formulario.add(new DatoFormularioFactory(5,"Fecha"));
		formulario.add(new DatoFormularioFactory(5,"Fecha"));
		formulario.add(new DatoFormularioFactory(5,"Fecha"));
		formulario.add(new DatoFormularioFactory(5,"Fecha"));
		formulario.add(new DatoFormularioFactory(5,"Fecha"));
		
		return formulario;
	}

}
