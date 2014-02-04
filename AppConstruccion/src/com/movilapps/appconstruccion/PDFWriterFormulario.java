package com.movilapps.appconstruccion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import crl.android.pdfwriter.PDFWriter;
import crl.android.pdfwriter.PaperSize;
import crl.android.pdfwriter.StandardFonts;
import crl.android.pdfwriter.Transformation;

public class PDFWriterFormulario {
	final static int MARGIN_LEFT = 75;
	static PDFWriter mPDFWriter;

	public static void savePDF(ArrayList<String> datos, Bitmap pic1,
			Bitmap pic2, String fileName, Context context)
			throws UnsupportedEncodingException {

		if (pic1 == null) {
			Log.e("PIC1", "NULL ---");
		}
		generatePDF(datos, pic1, pic2, context);
		String pdfcontent = "";
		try {
			outputToFile(fileName, pdfcontent, "iso-8859-1");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void savePDF(ArrayList<ArrayList<String>> datos,
			String fileName, Context context)
			throws UnsupportedEncodingException {
		mPDFWriter = new PDFWriter(PaperSize.LETTER_WIDTH,
				PaperSize.LETTER_HEIGHT);
		Log.e("Formularios", "" + datos.size());
		for (int i = 0; i < datos.size(); i++) {
			Bitmap pic1 = null;
			Bitmap pic2 = null;
			if (i > 0) {
				mPDFWriter.newPage();
			}
			ArrayList<String> formulario = datos.get(i);
			String img1 = formulario.get(formulario.size() - 2);
			String img2 = formulario.get(formulario.size() - 1);
			if (!img1.equals("--1")) {
				pic1 = decodeBase64(img1);
			}
			if (!img2.equals("--2")) {
				pic2 = decodeBase64(img2);
			}

			generatePDF(formulario, pic1, pic2, context);
		}

		// String s = mPDFWriter.asString();
		String s = Normalizer.normalize(mPDFWriter.asString(),
				Normalizer.Form.NFD);
		s = s.replaceAll("[^\\p{ASCII}]", "");
		try {
			outputToFile(fileName, s, "iso-8859-1");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void generatePDF(ArrayList<String> datos, Bitmap pic1,
			Bitmap pic2, Context context) throws UnsupportedEncodingException {

		AssetManager mngr = context.getAssets();
		try {
			// LOGO
			Bitmap xoiPNG = BitmapFactory.decodeStream(mngr
					.open("logo_final_peq.png"));

			mPDFWriter.addImageKeepRatio(MARGIN_LEFT,
					PaperSize.LETTER_HEIGHT - 160, 220, 90, xoiPNG,
					Transformation.DEGREES_0_ROTATION);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Fecha
		mPDFWriter.addText(PaperSize.LETTER_WIDTH - 220,
				PaperSize.LETTER_HEIGHT - 100, 16, now());

		// CUADRICULA

		mPDFWriter.addLine(60, PaperSize.LETTER_HEIGHT - 170,
				PaperSize.LETTER_WIDTH - 60, PaperSize.LETTER_HEIGHT - 170);

		mPDFWriter.addRectangle(60, 60, PaperSize.LETTER_WIDTH - 120,
				PaperSize.LETTER_HEIGHT - 120);

		mPDFWriter.addLine(60, 530, PaperSize.LETTER_WIDTH - 60, 530);
		mPDFWriter.addLine(60, 455, PaperSize.LETTER_WIDTH - 60, 455);

		mPDFWriter.addText(MARGIN_LEFT, 595, 18, "De: Interventoría");
		mPDFWriter.addText(MARGIN_LEFT, 570, 18, "Para: Obra");
		mPDFWriter.addText(MARGIN_LEFT, 545, 18, "Asunto: Observaciones obra");

		mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.HELVETICA_BOLD,
				"ISO-8859-1");

		String[] form = datos.get(0).split(" ");
		String primero = "";
		String segundo = "";

		for (int i = 0; i < form.length; i++) {
			if (i < 6) {
				primero += form[i] + " ";
			} else {
				segundo += form[i] + " ";
			}

		}
		primero = primero.trim();
		segundo = segundo.trim();

		if (form.length > 5) {
			mPDFWriter.addText(MARGIN_LEFT, 435, 15, primero);
			mPDFWriter.addText(MARGIN_LEFT, 415, 15, segundo);
		} else {
			mPDFWriter.addText(MARGIN_LEFT, 435, 15, datos.get(0));
		}

		int top = 410;

		mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.HELVETICA,
				"ISO-8859-1");

		mPDFWriter.addText(MARGIN_LEFT, 515, 15, datos.get(3));
		mPDFWriter.addText(MARGIN_LEFT, 490, 15, datos.get(4));
		mPDFWriter.addText(MARGIN_LEFT, 465, 15, datos.get(5));

		int j = 0;

		for (int i = 6; i < datos.size() - 4; i++) {
			if (i == 18) {
				mPDFWriter.newPage();
				top = PaperSize.LETTER_HEIGHT - 100;
				j = 12;
				mPDFWriter.addRectangle(60, 60, PaperSize.LETTER_WIDTH - 120,
						PaperSize.LETTER_HEIGHT - 120);
			}
			String linea = datos.get(i);
			if (linea.length() > 71) {
				ArrayList<String> lineas = splitLineas(linea);
				datos.remove(i);
				for (int k = 0; k < lineas.size(); k++) {
					String elemento = lineas.get(k);
					datos.add(i + k, elemento);
				}
			}
			mPDFWriter.addText(MARGIN_LEFT, (top - (i - (j + 5)) * 25), 14,
					datos.get(i));

		}

		int pageCount = mPDFWriter.getPageCount();
		for (int i = 0; i < pageCount; i++) {
			mPDFWriter.setCurrentPage(i);
			mPDFWriter.addText(10, 10, 8, Integer.toString(i + 1) + " / "
					+ Integer.toString(pageCount));
		}

		if (datos.get(2).equals("9")) {
			mPDFWriter.addLine(70, 120, 350, 120);
		}

		boolean hasEvidenciaEscrita = false;
		int sup = PaperSize.LETTER_HEIGHT - 100;
		// Imagenes Evidencia Fotografica
		if (pic1 == null) {
			Log.e("PIC1", "NULL ---");
		} else {
			String evidencia = datos.get(datos.size() - 3);
			if (!evidencia.equals("EMPTY")) {
				mPDFWriter.newPage();
				if (evidencia.contains("\n")) {
					String[] lines = datos.get(datos.size() - 3).split("\n");
					for (int i = 0; i < 3; i++) {
						mPDFWriter.addText(MARGIN_LEFT, sup - (i * 25), 14,
								lines[i]);
					}
				} else {
					mPDFWriter.addText(MARGIN_LEFT, sup - 25, 14, evidencia);
				}
				hasEvidenciaEscrita = true;
			} else {
				mPDFWriter.newPage();
			}

			mPDFWriter.addRectangle(60, 60, PaperSize.LETTER_WIDTH - 120,
					PaperSize.LETTER_HEIGHT - 120);
			mPDFWriter
					.addText(MARGIN_LEFT, sup - 80, 14, "Anexo Fotografico 1");
			mPDFWriter.addImageKeepRatio(
					centrar(PaperSize.LETTER_WIDTH, pic1.getWidth()),
					centrar(PaperSize.LETTER_HEIGHT, pic1.getHeight()), 400,
					400, pic1, Transformation.DEGREES_0_ROTATION);
		}
		if (pic2 == null) {
			Log.e("PIC2", "NULL ---");
		} else {
			if (!hasEvidenciaEscrita) {
				String evidencia = datos.get(datos.size() - 3);
				if (!evidencia.equals("EMPTY")) {
					mPDFWriter.newPage();
					if (evidencia.contains("\n")) {
						String[] lines = datos.get(datos.size() - 3)
								.split("\n");
						for (int i = 0; i < 3; i++) {
							mPDFWriter.addText(MARGIN_LEFT, sup - (i * 25), 14,
									lines[i]);
						}
					} else {
						mPDFWriter
								.addText(MARGIN_LEFT, sup - 25, 14, evidencia);
					}
					hasEvidenciaEscrita = true;
				} else {
					mPDFWriter.newPage();
				}
			} else {
				mPDFWriter.newPage();
			}

			mPDFWriter.addRectangle(60, 60, PaperSize.LETTER_WIDTH - 120,
					PaperSize.LETTER_HEIGHT - 120);
			mPDFWriter
					.addText(MARGIN_LEFT, sup - 80, 14, "Anexo Fotografico 2");
			mPDFWriter.addImage(
					centrar(PaperSize.LETTER_WIDTH, pic2.getWidth()),
					centrar(PaperSize.LETTER_HEIGHT, pic2.getHeight()), pic2,
					Transformation.DEGREES_0_ROTATION);
		}

		if (!hasEvidenciaEscrita) {
			String evidencia = datos.get(datos.size() - 3);
			if (!evidencia.equals("EMPTY")) {
				mPDFWriter.newPage();
				mPDFWriter.addRectangle(60, 60, PaperSize.LETTER_WIDTH - 120,
						PaperSize.LETTER_HEIGHT - 120);
				if (evidencia.contains("\n")) {
					String[] lines = datos.get(datos.size() - 3).split("\n");
					for (int i = 0; i < 3; i++) {
						mPDFWriter.addText(MARGIN_LEFT, sup - (i * 25), 14,
								lines[i]);
					}
				} else {
					mPDFWriter.addText(MARGIN_LEFT, sup - 25, 14, evidencia);
				}
				hasEvidenciaEscrita = true;
			}
		}

	}

	public static void savePDF(ArrayList<String> datos, Bitmap pic,
			String fileName, Context context)
			throws UnsupportedEncodingException {

		String pdfcontent = generatePDF(datos, pic, context);
		try {
			outputToFile(fileName, pdfcontent, "iso-8859-1");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static ArrayList<String> splitLineas(String text) {
		ArrayList<String> strings = new ArrayList<String>();
		int index = 0;
		while (index < text.length()) {
			strings.add(text.substring(index,
					Math.min(index + 70, text.length())));
			index += 70;
		}
		return strings;
	}

	private static int centrar(int tamañoPapel, int tamañoImagen) {
		int margen = (tamañoPapel - tamañoImagen) / 2;
		return margen;
	}

	public static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return "Fecha: " + sdf.format(cal.getTime());
	}

	private static void outputToFile(String fileName, String pdfContent,
			String encoding) throws FileNotFoundException {

		File newFile = new File(Environment.getExternalStorageDirectory() + "/"
				+ fileName);

		try {
			newFile.createNewFile();
			try {

				PrintWriter pdfFile = new PrintWriter(newFile, "ISO-8859-1");
				// FileOutputStream pdfFile = new FileOutputStream(newFile);
				// pdfFile.write(pdfContent.getBytes(encoding));
				pdfFile.write(pdfContent);
				pdfFile.close();

			} catch (FileNotFoundException e) {
				Log.e("ERROR", " - IO");
			}
		} catch (IOException e) {
			Log.e("ERROR", " - IO");
		}
	}

	public static Bitmap decodeBase64(String input) {
		byte[] decodedByte = Base64.decode(input, 0);
		return BitmapFactory
				.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

	private static String generatePDF(ArrayList<String> datos, Bitmap pic,
			Context context) {
		PDFWriter mPDFWriter = new PDFWriter(PaperSize.LETTER_WIDTH,
				PaperSize.LETTER_HEIGHT);

		AssetManager mngr = context.getAssets();
		try {
			// LOGO
			Bitmap xoiPNG = BitmapFactory.decodeStream(mngr.open("kyjing.png"));
			mPDFWriter.addImage(MARGIN_LEFT, PaperSize.LETTER_HEIGHT - 160,
					xoiPNG, Transformation.DEGREES_0_ROTATION);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Fecha
		mPDFWriter.addText(PaperSize.LETTER_WIDTH - 220,
				PaperSize.LETTER_HEIGHT - 100, 16, now());
		mPDFWriter.addText(PaperSize.LETTER_WIDTH - 220,
				PaperSize.LETTER_HEIGHT - 120, 16, "MEMO EXPRES");

		// CUADRICULA

		mPDFWriter.addLine(60, PaperSize.LETTER_HEIGHT - 170,
				PaperSize.LETTER_WIDTH - 60, PaperSize.LETTER_HEIGHT - 170);

		mPDFWriter.addRectangle(60, 60, PaperSize.LETTER_WIDTH - 120,
				PaperSize.LETTER_HEIGHT - 120);

		// CUADRICULA

		mPDFWriter.addLine(60, PaperSize.LETTER_HEIGHT - 170,
				PaperSize.LETTER_WIDTH - 60, PaperSize.LETTER_HEIGHT - 170);

		mPDFWriter.addRectangle(60, 60, PaperSize.LETTER_WIDTH - 120,
				PaperSize.LETTER_HEIGHT - 120);

		mPDFWriter.addLine(60, 530, PaperSize.LETTER_WIDTH - 60, 530);

		mPDFWriter.addText(MARGIN_LEFT, 595, 18, "De: " + datos.get(0));
		mPDFWriter.addText(MARGIN_LEFT, 570, 18, "Para: " + datos.get(1));
		mPDFWriter.addText(MARGIN_LEFT, 545, 18, "Asunto: " + datos.get(2));

		int top = 500;
		if (datos.get(3).contains("\n")) {
			String[] lines = datos.get(3).split("\n");
			for (int i = 0; i < lines.length; i++) {
				mPDFWriter.addText(MARGIN_LEFT, top - (i * 25), 18, lines[i]);
			}
		} else {
			mPDFWriter.addText(MARGIN_LEFT, 500, 18, datos.get(3));
		}

		// Imagenes Evidencia Fotografica
		if (pic == null) {
			Log.e("PIC1", "NULL ---");
		} else {
			mPDFWriter.newPage();
			mPDFWriter.addRectangle(60, 60, PaperSize.LETTER_WIDTH - 120,
					PaperSize.LETTER_HEIGHT - 120);
			mPDFWriter.addText(MARGIN_LEFT, PaperSize.LETTER_HEIGHT - 100, 14,
					"Anexo Fotografico 1");
			mPDFWriter.addImage(
					centrar(PaperSize.LETTER_WIDTH, pic.getWidth()),
					centrar(PaperSize.LETTER_HEIGHT, pic.getHeight()), pic,
					Transformation.DEGREES_0_ROTATION);
		}

		String s = Normalizer.normalize(mPDFWriter.asString(),
				Normalizer.Form.NFD);
		s = s.replaceAll("[^\\p{ASCII}]", "");

		return s;
	}

}
