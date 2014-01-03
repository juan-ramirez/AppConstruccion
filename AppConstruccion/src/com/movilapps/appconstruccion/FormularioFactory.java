package com.movilapps.appconstruccion;

import java.util.ArrayList;

public class FormularioFactory {

	private ArrayList<Dato> formulario;

	public FormularioFactory() {
		super();
	}

	public ArrayList<Dato> getFormulario(int tipo) {
		formulario = new ArrayList<Dato>();
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

	private ArrayList<Dato> getf1() {
		
		formulario.add(new Dato(5,"Fecha"));
		formulario.add(new Dato(5,"Fecha"));
		formulario.add(new Dato(5,"Fecha"));
		formulario.add(new Dato(5,"Fecha"));
		formulario.add(new Dato(5,"Fecha"));
		formulario.add(new Dato(5,"Fecha"));
		formulario.add(new Dato(5,"Fecha"));
		formulario.add(new Dato(5,"Fecha"));
		
		return formulario;
	}

	static class Dato {
		public int tipo;
		public String titulo;

		public Dato(int tipo, String titulo) {
			super();
			this.tipo = tipo;
			this.titulo = titulo;
		}

		public int getTipo() {
			return tipo;
		}

		public String getTitulo() {
			return titulo;
		}

	}
}
