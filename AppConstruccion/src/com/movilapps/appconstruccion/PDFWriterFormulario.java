package com.movilapps.appconstruccion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import crl.android.pdfwriter.PDFWriter;
import crl.android.pdfwriter.PaperSize;
import crl.android.pdfwriter.StandardFonts;

import android.os.Environment;
import android.util.Log;

public class PDFWriterFormulario {

	public static void savePDF(ArrayList<String> datos) {
		String pdfcontent = generatePDF(datos);
		try {
			outputToFile("TestExample2.pdf", pdfcontent, "iso-8859-1");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String generatePDF(ArrayList<String> datos) {
		PDFWriter mPDFWriter = new PDFWriter(PaperSize.LETTER_WIDTH,
				PaperSize.LETTER_HEIGHT);

		mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.HELVETICA);
		for (int i = 0; i < datos.size(); i++) {
			mPDFWriter.addText(85, (750 - i * 25), 18, datos.get(i));
		}

		int pageCount = mPDFWriter.getPageCount();
		for (int i = 0; i < pageCount; i++) {
			mPDFWriter.setCurrentPage(i);
			mPDFWriter.addText(10, 10, 8, Integer.toString(i + 1) + " / "
					+ Integer.toString(pageCount));
		}

		String s = mPDFWriter.asString();
		Log.e("PDF", s);
		return s;
	}

	private static void outputToFile(String fileName, String pdfContent,
			String encoding) throws FileNotFoundException {

		File newFile = new File(Environment.getExternalStorageDirectory() + "/"
				+ fileName);

		try {
			newFile.createNewFile();
			try {
				FileOutputStream pdfFile = new FileOutputStream(newFile);
				pdfFile.write(pdfContent.getBytes(encoding));
				pdfFile.close();
			} catch (FileNotFoundException e) {
				Log.d("ERROR", " - IO");
			}
		} catch (IOException e) {
			Log.d("ERROR", " - IO");
		}
	}

}
