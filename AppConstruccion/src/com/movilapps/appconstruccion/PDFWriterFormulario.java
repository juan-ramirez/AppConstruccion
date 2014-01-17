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
import android.util.Log;
import crl.android.pdfwriter.PDFWriter;
import crl.android.pdfwriter.PaperSize;
import crl.android.pdfwriter.StandardFonts;
import crl.android.pdfwriter.Transformation;

public class PDFWriterFormulario {
	final static int MARGIN_LEFT = 75;

	public static void savePDF(ArrayList<String> datos, Bitmap pic1,
			Bitmap pic2, String fileName, Context context)
			throws UnsupportedEncodingException {

		if (pic1 == null) {
			Log.e("PIC1", "NULL ---");
		}
		String pdfcontent = generatePDF(datos, pic1, pic2, context);
		try {
			outputToFile(fileName, pdfcontent, "iso-8859-1");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

		return mPDFWriter.asString();
	}

	private static String generatePDF(ArrayList<String> datos, Bitmap pic1,
			Bitmap pic2, Context context) throws UnsupportedEncodingException {
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

		mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.HELVETICA,
				"ISO-8859-1");

		int top = 410;
		mPDFWriter.addText(MARGIN_LEFT, 515, 15, datos.get(1));
		mPDFWriter.addText(MARGIN_LEFT, 490, 15, datos.get(2));
		mPDFWriter.addText(MARGIN_LEFT, 465, 15, datos.get(3));

		int j = 0;

		for (int i = 4; i < datos.size(); i++) {
			if (i == 16) {
				mPDFWriter.newPage();
				top = PaperSize.LETTER_HEIGHT - 100;
				j = 12;
				mPDFWriter.addRectangle(60, 60, PaperSize.LETTER_WIDTH - 120,
						PaperSize.LETTER_HEIGHT - 120);
			}
			Log.e("TOP", "" + (top - (i - (j + 4)) * 25));
			mPDFWriter.addText(MARGIN_LEFT, (top - (i - (j + 3)) * 25), 14,
					datos.get(i));
		}

		int pageCount = mPDFWriter.getPageCount();
		for (int i = 0; i < pageCount; i++) {
			mPDFWriter.setCurrentPage(i);
			mPDFWriter.addText(10, 10, 8, Integer.toString(i + 1) + " / "
					+ Integer.toString(pageCount));
		}

		// Imagenes Evidencia Fotografica
		if (pic1 == null) {
			Log.e("PIC1", "NULL ---");
		} else {
			mPDFWriter.newPage();
			mPDFWriter.addRectangle(60, 60, PaperSize.LETTER_WIDTH - 120,
					PaperSize.LETTER_HEIGHT - 120);
			mPDFWriter.addText(MARGIN_LEFT, PaperSize.LETTER_HEIGHT - 100, 14,
					"Anexo Fotografico 1");
			mPDFWriter.addImage(
					centrar(PaperSize.LETTER_WIDTH, pic1.getWidth()),
					centrar(PaperSize.LETTER_HEIGHT, pic1.getHeight()), pic1,
					Transformation.DEGREES_0_ROTATION);
		}
		if (pic2 == null) {
			Log.e("PIC2", "NULL ---");
		} else {
			mPDFWriter.newPage();
			mPDFWriter.addRectangle(60, 60, PaperSize.LETTER_WIDTH - 120,
					PaperSize.LETTER_HEIGHT - 120);
			mPDFWriter.addText(MARGIN_LEFT, PaperSize.LETTER_HEIGHT - 100, 14,
					"Anexo Fotografico 2");
			mPDFWriter.addImage(
					centrar(PaperSize.LETTER_WIDTH, pic2.getWidth()),
					centrar(PaperSize.LETTER_HEIGHT, pic2.getHeight()), pic2,
					Transformation.DEGREES_0_ROTATION);
		}

		// String s = mPDFWriter.asString();
		String s = Normalizer.normalize(mPDFWriter.asString(),
				Normalizer.Form.NFD);
		s = s.replaceAll("[^\\p{ASCII}]", "");
		return s;

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

}
