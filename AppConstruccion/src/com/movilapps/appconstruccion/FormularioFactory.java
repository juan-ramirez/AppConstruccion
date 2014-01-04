package com.movilapps.appconstruccion;

import java.util.ArrayList;

public class FormularioFactory {

	private ArrayList<DatoFormularioFactory> formulario;
	private ArrayList<String> cumple_nocumple;
	private ArrayList<String> cumple_nocumple_noaplica;
	private ArrayList<String> mixer_concretadora;
	private ArrayList<String> torregrua_sistemabombeo_coche;
	private ArrayList<String> aplica_noaplica;
	private ArrayList<String> bomba_noaplica;
	private ArrayList<String> tolva_tuberia_coche;
	private ArrayList<String> agua_aditivo;
	private ArrayList<String> bueno_regular_malo;
	private ArrayList<String> requiere_norequiere;
	private ArrayList<String> si_no;
	private ArrayList<String> I_II_III_IV;
	private ArrayList<String> cemex_holcim_argos;

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
		
		mixer_concretadora= new ArrayList<String>();
		mixer_concretadora.add("Mixer");
		mixer_concretadora.add("Concretadora");
		
		torregrua_sistemabombeo_coche= new ArrayList<String>();
		torregrua_sistemabombeo_coche.add("Torre Grúa");
		torregrua_sistemabombeo_coche.add("Sistema Bombeo");
		torregrua_sistemabombeo_coche.add("Coche");
		
		aplica_noaplica = new ArrayList<String>();
		aplica_noaplica.add("Aplica");
		aplica_noaplica.add("No Aplica");
		
		bomba_noaplica = new ArrayList<String>();
		bomba_noaplica.add("Bomba");
		bomba_noaplica.add("No Aplica");
		
		tolva_tuberia_coche = new ArrayList<String>();
		tolva_tuberia_coche.add("Tolva");
		tolva_tuberia_coche.add("Tubería");
		tolva_tuberia_coche.add("Coche");
		
		agua_aditivo = new ArrayList<String>();;
		agua_aditivo.add("Agua");
		agua_aditivo.add("Aditivo");
		
		bueno_regular_malo = new ArrayList<String>();
		bueno_regular_malo.add("Bueno");
		bueno_regular_malo.add("Regular");
		bueno_regular_malo.add("Malo");
		
		requiere_norequiere = new ArrayList<String>();
		requiere_norequiere.add("Requiere");
		requiere_norequiere.add("No Requiere");
		
		si_no = new ArrayList<String>();
		si_no.add("Si");
		si_no.add("No");
		
		I_II_III_IV = new ArrayList<String>();
		I_II_III_IV.add("I");
		I_II_III_IV.add("II");
		I_II_III_IV.add("III");
		I_II_III_IV.add("IV");
		
		cemex_holcim_argos = new ArrayList<String>();
		cemex_holcim_argos.add("Cemex");
		cemex_holcim_argos.add("Holcim");
		cemex_holcim_argos.add("Argos");
		
		

		
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
		
		formulario.add(new DatoFormularioFactory(TIPO_5,"Fecha revisión"));
		formulario.add(new DatoFormularioFactory(TIPO_1,"Lote",ALFANUMERICO));
		formulario.add(new DatoFormularioFactory(TIPO_1,"Cantidad Total",NUMERICO));
		formulario.add(new DatoFormularioFactory(TITULO,"VARIABLES A CONTROLAR NTC-121 Numeral 7"));
		formulario.add(new DatoFormularioFactory(TIPO_4,"Tipo", I_II_III_IV));
		
		formulario.add(new DatoFormularioFactory(TIPO_4,"Marcas Legibles",cumple_nocumple));
		formulario.add(new DatoFormularioFactory(TIPO_4,"Palabra Cemento Portland",cumple_nocumple));
		formulario.add(new DatoFormularioFactory(TIPO_4,"Marca",cemex_holcim_argos));
		formulario.add(new DatoFormularioFactory(TIPO_1,"Lugar de fabricación",ALFABETICO));
		formulario.add(new DatoFormularioFactory(TIPO_1,"Masa del bulto (Kg)",DECIMAL));
		formulario.add(new DatoFormularioFactory(TIPO_4,"Almacenamiento (libre de humedad y de agentes contaminantes)",cumple_nocumple));
		
		return formulario;
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
	
private ArrayList<DatoFormularioFactory> getf3() {
		
		formulario.add(new DatoFormularioFactory(TIPO_5,"Fecha revisión"));
		formulario.add(new DatoFormularioFactory(TIPO_1,"Elemento",ALFANUMERICO));
		formulario.add(new DatoFormularioFactory(TIPO_1,"Ubicación",ALFANUMERICO));
		formulario.add(new DatoFormularioFactory(TIPO_1,"Plano",ALFANUMERICO));
		formulario.add(new DatoFormularioFactory(TITULO,"Replanteo Geométrico"));
		formulario.add(new DatoFormularioFactory(TIPO_1,"Eje Num",NUMERICO));
		formulario.add(new DatoFormularioFactory(TIPO_1,"Eje Lit",ALFABETICO));
		formulario.add(new DatoFormularioFactory(TITULO,"Sección Típica Excavación"));
		formulario.add(new DatoFormularioFactory(TIPO_1,"Profundidad",DECIMAL));
		formulario.add(new DatoFormularioFactory(TIPO_1,"Ancho",DECIMAL));
		formulario.add(new DatoFormularioFactory(TITULO,"Sección Modificada"));
		formulario.add(new DatoFormularioFactory(TIPO_1,"Sobre excav",DECIMAL));
		formulario.add(new DatoFormularioFactory(TIPO_1,"Sobre ancho",DECIMAL));
		formulario.add(new DatoFormularioFactory(TITULO,"Estrato portante"));
		formulario.add(new DatoFormularioFactory(TIPO_4,"Condicion Estrato",cumple_nocumple));
		formulario.add(new DatoFormularioFactory(TIPO_1,"Profundidad",NUMERICO));
		formulario.add(new DatoFormularioFactory(TITULO,"Condición de las Excavaciones"));
		formulario.add(new DatoFormularioFactory(TIPO_4,"Limpieza de fondo",cumple_nocumple));
		formulario.add(new DatoFormularioFactory(TIPO_4,"Protección ",cumple_nocumple));
		formulario.add(new DatoFormularioFactory(TIPO_4,"Sistema de drenaje",bomba_noaplica));
		formulario.add(new DatoFormularioFactory(TIPO_4,"Solado de Protección",cumple_nocumple_noaplica));
		
		
		
		
		return formulario;
	}


	
	
}
