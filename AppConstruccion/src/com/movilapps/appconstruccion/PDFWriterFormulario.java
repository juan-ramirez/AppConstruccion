package com.movilapps.appconstruccion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.ArrayList;

import crl.android.pdfwriter.PDFWriter;
import crl.android.pdfwriter.PaperSize;
import crl.android.pdfwriter.StandardFonts;
import crl.android.pdfwriter.Transformation;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class PDFWriterFormulario {

	public static void savePDF(ArrayList<String> datos, String fileName,Context context)
			throws UnsupportedEncodingException {
		String pdfcontent = generatePDF(datos,context);
		try {
			outputToFile(fileName, pdfcontent, "iso-8859-1");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String generatePDF(ArrayList<String> datos,Context context)
			throws UnsupportedEncodingException {
		PDFWriter mPDFWriter = new PDFWriter(PaperSize.LETTER_WIDTH,
				PaperSize.LETTER_HEIGHT);
		
		AssetManager mngr = context.getAssets();
		try {
			Bitmap xoiPNG = BitmapFactory.decodeStream(mngr.open("kyjing.png"));
	        mPDFWriter.addImage(85, 660, xoiPNG, Transformation.DEGREES_0_ROTATION);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.HELVETICA,"ISO-8859-1");
		for (int i = 0; i < datos.size(); i++) {
			mPDFWriter.addText(85, (500 - i * 25), 18, datos.get(i));
		}

		int pageCount = mPDFWriter.getPageCount();
		for (int i = 0; i < pageCount; i++) {
			mPDFWriter.setCurrentPage(i);
			mPDFWriter.addText(10, 10, 8, Integer.toString(i + 1) + " / "
					+ Integer.toString(pageCount));
		}

		mPDFWriter.addLine(85, 650, 400, 650);
		
		//String s = mPDFWriter.asString();
		String s = Normalizer.normalize(mPDFWriter.asString(),
				Normalizer.Form.NFD);
		s = s.replaceAll("[^\\p{ASCII}]", "");
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

				PrintWriter pdfFile = new PrintWriter(newFile, "ISO-8859-1");
				// FileOutputStream pdfFile = new FileOutputStream(newFile);
				// pdfFile.write(pdfContent.getBytes(encoding));
				pdfFile.write(pdfContent);
				pdfFile.close();

			} catch (FileNotFoundException e) {
				Log.d("ERROR", " - IO");
			}
		} catch (IOException e) {
			Log.d("ERROR", " - IO");
		}
	}

}
