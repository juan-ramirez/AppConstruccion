package com.movilapps.appconstruccion;

import java.util.ArrayList;

public class FormularioFactory {

	private ArrayList<DatoFormularioFactory> formulario;
	private ArrayList<String> cumple_nocumple;
	private ArrayList<String> cumple_nocumple_noaplica;
	
	public final int TIPO_1 = 1;
	public final int TIPO_2 = 2;
	public final int TIPO_3 = 3;
	public final int TIPO_4 = 4;
	public final int TIPO_5 = 5;
	public final int TITULO = 6;
	
	public final int ALFANUMERICO = 1;
	public final int ALFABETICO = 2;
	public final int NUMERICO = 3;
	public final int DECIMAL = 4;
	
	public FormularioFactory() {
		inicializarListas();
	}

	private void inicializarListas() {
		cumple_nocumple = new ArrayList<String>();		
		cumple_nocumple.add("Cumple");
		cumple_nocumple.add("No cumple");
		
		cumple_nocumple_noaplica= new ArrayList<String>();
		cumple_nocumple_noaplica.add("Cumple");
		cumple_nocumple_noaplica.add("No cumple");
		cumple_nocumple_noaplica.add("No Aplica");
		
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

	private ArrayList<DatoFormularioFactory> getf2() {
		
		formulario.add(new DatoFormularioFactory(TIPO_5,"Fecha"));
		formulario.add(new DatoFormularioFactory(TIPO_1,"Tipo de concreto(PSI)",NUMERICO));
		formulario.add(new DatoFormularioFactory(TITULO,"Preparación"));
		formulario.add(new DatoFormularioFactory(TIPO_3,"N° Mixer",ALFANUMERICO));
		formulario.add(new DatoFormularioFactory(TIPO_3,"N° Tanda de Mezclado",NUMERICO));
		formulario.add(new DatoFormularioFactory(TIPO_2,"Hora carga"));
		formulario.add(new DatoFormularioFactory(TIPO_2,"Hora descarga"));
		formulario.add(new DatoFormularioFactory(TITULO,"Asentamiento (CM)"));
		formulario.add(new DatoFormularioFactory(TIPO_1,"N° Mixer",NUMERICO));
		formulario.add(new DatoFormularioFactory(TIPO_1,"N° Mixer",NUMERICO));
		
		
		return formulario;
	}

	
	
}
