package com.movilapps.appconstruccion;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MemoExpressActivity extends Activity {

	private static final int MAX_IMAGE_DIMENSION = 250;

	private EditText editTextDe;
	private EditText editTextPara;
	private EditText editTextAsunto;
	private EditText editTextContenido;
	private ImageView imageViewAdjunto;
	private View lastContextMenuButton;
	private Bitmap fotoBitmapFinal;
	private Bitmap picEnviar = null;

	private boolean isAdjuntoDefault = true;
	private String campoDe;
	private String campoPara;
	private String campoAsunto;
	private String campoContenido;

	private ArrayList<String> datos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memo_express);
		inicializarObjetos();
		setTitle("Memo Expres");
		registerForContextMenu(imageViewAdjunto);
	}

	private void inicializarObjetos() {
		editTextDe = (EditText) findViewById(R.id.editTextDe);
		editTextPara = (EditText) findViewById(R.id.editTextPara);
		editTextAsunto = (EditText) findViewById(R.id.editTextAsunto);
		editTextContenido = (EditText) findViewById(R.id.editTextContenido);
		imageViewAdjunto = (ImageView) findViewById(R.id.imageViewAdjunto);
	}

	private boolean validarDatos() {
		obtenerDatos();
		if (!verificarVacios()) {
			return true;
		} else {
			Toast.makeText(this,
					"Alguno de los campos est� vac�o, por favor ingr�selo",
					Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	private void obtenerDatos() {

		campoDe = editTextDe.getText().toString().trim();
		campoPara = editTextPara.getText().toString().trim();
		campoAsunto = editTextAsunto.getText().toString().trim();
		campoContenido = editTextContenido.getText().toString().trim();

	}

	private boolean verificarVacios() {
		return campoDe.equals("") || campoPara.equals("")
				|| campoAsunto.equals("") || campoContenido.equals("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_enviar, menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			break;
		case R.id.action_email:
			if (validarDatos()) {
				recogerDatos();
				if (isAdjuntoDefault) {
					enviarMail(null);
				} else {
					enviarMail(picEnviar);
				}

			}
			break;
		}
		return (super.onOptionsItemSelected(item));
	}

	private void recogerDatos() {
		datos = new ArrayList<String>();
		datos.add(campoDe);
		datos.add(campoPara);
		datos.add(campoAsunto);
		datos.add(campoContenido);
	}

	public void enviarMail(Bitmap pic) {

		String date = now();
		String fileName = "MEMO EXPRES " + date + ".pdf";
		try {
			PDFWriterFormulario.savePDF(datos, pic, fileName, this);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		enviarPDfemail(fileName);
	}

	@SuppressLint("SimpleDateFormat")
	public static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy - hh:mm a");
		return sdf.format(cal.getTime());
	}

	private void enviarPDfemail(String fileName) {
		Intent sendIntent;

		sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Cordial saludo");
		sendIntent.putExtra(Intent.EXTRA_TEXT, "Test Text");
		sendIntent.putExtra(Intent.EXTRA_STREAM,
				Uri.parse("file:///mnt/sdcard/" + fileName));
		sendIntent.setType("application/pdf");

		startActivityForResult(Intent.createChooser(sendIntent, "Send Mail"), 1);

	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
		Bundle extras = null;
		if (imageReturnedIntent != null) {
			extras = imageReturnedIntent.getExtras();
		}
		switch (requestCode) {
		case 0:// Toma foto
			if (resultCode == RESULT_OK) {

				byte[] data = extras.getByteArray("pic");

				fotoBitmapFinal = BitmapFactory.decodeByteArray(data, 0,
						data.length);

				picEnviar = redimensionarImagen(fotoBitmapFinal);

				fotoBitmapFinal = Bitmap.createScaledBitmap(fotoBitmapFinal,
						400, 400, false);

				imageViewAdjunto.setImageBitmap(fotoBitmapFinal);
				isAdjuntoDefault = false;

			}

			break;
		case 1:// Sube foto
			if (resultCode == RESULT_OK) {
				Uri selectedImage = imageReturnedIntent.getData();

				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();

				fotoBitmapFinal = BitmapFactory.decodeFile(picturePath);

				picEnviar = redimensionarImagen(fotoBitmapFinal);
				fotoBitmapFinal = Bitmap.createScaledBitmap(fotoBitmapFinal,
						400, 400, false);

				imageViewAdjunto.setImageBitmap(fotoBitmapFinal);
				isAdjuntoDefault = false;
			}
			break;
		}
	}

	private Bitmap redimensionarImagen(Bitmap fotoBitmapFinal) {

		Bitmap result = null;
		int height = fotoBitmapFinal.getHeight();
		int width = fotoBitmapFinal.getWidth();
		if (height > width) {
			int widthFinal = (int) Math.floor((width * 800) / height);
			result = Bitmap.createScaledBitmap(fotoBitmapFinal, widthFinal,
					800, false);
		} else if (width > height) {
			int heightFinal = (int) Math.floor((height * 800) / width);
			result = Bitmap.createScaledBitmap(fotoBitmapFinal, 800,
					heightFinal, false);
		} else {
			result = Bitmap
					.createScaledBitmap(fotoBitmapFinal, 800, 800, false);
		}

		return result;
	}

	// Creating Context Menu
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		lastContextMenuButton = v;
		menu.setHeaderTitle("Opciones de Fotos");
		menu.add(0, 0, 0, "Tomar Foto");
		menu.add(0, 1, 0, "Subir Foto");
		menu.add(0, 2, 0, "Eliminar");

	}

	public boolean onContextItemSelected(MenuItem item) {
		// find out which menu item was pressed

		switch (item.getItemId()) {
		case 0:
			tomarFoto(lastContextMenuButton);
			Log.e("Opcion", "Tomar foto");
			return true;
		case 1:
			subirFoto(lastContextMenuButton);
			Log.e("Opcion", "Subir foto");
			return true;
		case 2:
			eliminarFoto(lastContextMenuButton);
			Log.e("Opcion", "Eliminar");
			return true;
		default:
			return false;
		}
	}

	private void tomarFoto(View v) {
		Intent intent = new Intent(this, CameraActivity.class);
		startActivityForResult(intent, 0);

	}

	private void subirFoto(View v) {
		Intent pickPhoto = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(pickPhoto, 1);// one can be replced with
	}

	private void eliminarFoto(View v) {
		imageViewAdjunto.setImageResource(R.drawable.ic_launcher);
		isAdjuntoDefault = true;
	}

	public static int getOrientation(Context context, Uri photoUri) {
		/* it's on the external media. */
		Cursor cursor = context.getContentResolver().query(photoUri,
				new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
				null, null, null);

		if (cursor.getCount() != 1) {
			return -1;
		}

		cursor.moveToFirst();
		return cursor.getInt(0);
	}

	public static Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri)
			throws IOException {
		InputStream is = context.getContentResolver().openInputStream(photoUri);
		BitmapFactory.Options dbo = new BitmapFactory.Options();
		dbo.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is, null, dbo);
		is.close();

		int rotatedWidth, rotatedHeight;
		int orientation = getOrientation(context, photoUri);

		if (orientation == 90 || orientation == 270) {
			rotatedWidth = dbo.outHeight;
			rotatedHeight = dbo.outWidth;
		} else {
			rotatedWidth = dbo.outWidth;
			rotatedHeight = dbo.outHeight;
		}

		Bitmap srcBitmap;
		is = context.getContentResolver().openInputStream(photoUri);
		if (rotatedWidth > MAX_IMAGE_DIMENSION
				|| rotatedHeight > MAX_IMAGE_DIMENSION) {
			float widthRatio = ((float) rotatedWidth)
					/ ((float) MAX_IMAGE_DIMENSION);
			float heightRatio = ((float) rotatedHeight)
					/ ((float) MAX_IMAGE_DIMENSION);
			float maxRatio = Math.max(widthRatio, heightRatio);

			// Create the bitmap from file
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = (int) maxRatio;
			srcBitmap = BitmapFactory.decodeStream(is, null, options);
		} else {
			srcBitmap = BitmapFactory.decodeStream(is);
		}
		is.close();

		/*
		 * if the orientation is not 0 (or -1, which means we don't know), we
		 * have to do a rotation.
		 */
		if (orientation > 0) {
			Matrix matrix = new Matrix();
			matrix.postRotate(orientation);

			srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
					srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
		}

		return srcBitmap;
	}

}
