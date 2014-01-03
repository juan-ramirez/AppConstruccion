package com.movilapps.appconstruccion;

import java.util.ArrayList;

public class DatoFormularioFactory {

	public int tipo;
	public String titulo;
	public String tipoDato;
	public ArrayList<String> listaSpinner;

	public DatoFormularioFactory(int tipo, String titulo) {
		super();
		this.tipo = tipo;
		this.titulo = titulo;
	}

	public DatoFormularioFactory(int tipo, String titulo, String tipoDato) {
		super();
		this.tipo = tipo;
		this.titulo = titulo;
		this.tipoDato = tipoDato;
	}

	public DatoFormularioFactory(int tipo, String titulo,
			ArrayList<String> listaSpinner) {
		super();
		this.tipo = tipo;
		this.titulo = titulo;
		this.listaSpinner = listaSpinner;
	}

	public int getTipo() {
		return tipo;
	}

	public String getTitulo() {
		return titulo;
	}

}
