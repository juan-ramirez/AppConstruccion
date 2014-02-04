package com.movilapps.appconstruccion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

public class FormularioActivity extends FragmentActivity {
	Intent generalIntent;
	static ArrayList<View> arrayListElementosFormulario;
	static ArrayList<DatoFormularioFactory> arrayListFormulario;
	ArrayList<String> datosPDF;

	private static Uri contentUri;
	private static Bitmap fotoBitmapFinal;
	private static ImageView foto1;
	private static ImageView foto2;
	private static EditText evidenciaEscrita = null;
	public static boolean isFoto1Default = true;
	public static boolean isFoto2Default = true;
	public static boolean esCargar = false;
	public static Bitmap pic1 = null;
	public static Bitmap pic2 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		generalIntent = getIntent();

		esCargar = generalIntent.getBooleanExtra("esCargar", false);

		setTitle(generalIntent.getStringExtra("nombreFormulario"));

		Tab tab = actionBar
				.newTab()
				.setText("Formulario")
				.setTabListener(
						new TabListener<ExampleFragment>(this, "Formulario",
								ExampleFragment.class));
		actionBar.addTab(tab);

		tab = actionBar
				.newTab()
				.setText("Observaciones")
				.setTabListener(
						new TabListener<ExampleFragmentTwo>(this,
								"Observaciones", ExampleFragmentTwo.class));
		actionBar.addTab(tab);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_formulario, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return (true);
		case R.id.action_save:
			// Recoger Encabezado
			datosPDF = new ArrayList<String>();
			String nombreFormulario = generalIntent
					.getStringExtra("nombreFormulario");

			datosPDF.add(nombreFormulario);
			datosPDF.add(now());

			int numeroFormulario = generalIntent.getIntExtra(
					"numeroFormulario", 1);
			datosPDF.add("" + numeroFormulario);

			// Recoger datos

			if (recogerDatos(false)) {
				showMessageIncompleto();
			} else {
				datosPDF.add("Completo");
				obtenerEvidencia();
			}

			return (true);
		}

		return (super.onOptionsItemSelected(item));
	}

	@SuppressWarnings("unchecked")
	private void guardarFormulario() {

		// Guardar formulario
		ArrayList<ArrayList<String>> formularios = null;
		Gson gson = new Gson();

		SharedPreferences mPrefs = getSharedPreferences("my_prefs",
				MODE_PRIVATE);

		String jsonFormularios = mPrefs.getString("Formularios", "");

		if (jsonFormularios.equals("")) {
			formularios = new ArrayList<ArrayList<String>>();
		} else {
			formularios = gson.fromJson(jsonFormularios, ArrayList.class);
			if (esCargar) {
				ArrayList<String> elemento = generalIntent
						.getStringArrayListExtra("formulario");
				Log.e("ELIMINAR", "" + formularios.remove(elemento));
			}

		}

		formularios.add(datosPDF);

		Editor prefsEditor = mPrefs.edit();
		String json = gson.toJson(formularios);

		// Log.e("JSON: ", json);

		prefsEditor.putString("Formularios", json);
		Log.e("Commited: ", String.valueOf(prefsEditor.commit()));
		finish();

	}

	private void showMessage() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Evidencia");
		alert.setMessage("No ha ingresado ninguna evidencia, �Desea guardarlo sin evidencia? ");

		alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				datosPDF.add("EMPTY");
				datosPDF.add("--1");
				datosPDF.add("--2");
				guardarFormulario();
			}
		});
		alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});
		alert.show();
	}

	private void showMessageIncompleto() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Formulario Incompleto");
		alert.setMessage("El formulario se encuentra incompleto, �Desea guardarlo as�? ");

		alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				datosPDF = new ArrayList<String>(datosPDF.subList(0, 3));
				recogerDatos(true);
				datosPDF.add("Incompleto");
				obtenerEvidencia();

			}
		});
		alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});
		alert.show();
	}

	private void obtenerEvidencia() {

		if (evidenciaEscrita == null) {
			showMessage();
		} else {
			String texto = evidenciaEscrita.getText().toString().trim();
			if (texto.equals("") && (isFoto1Default && isFoto2Default)) {
				showMessage();
			} else {
				String evidencia1 = "--1";
				String evidencia2 = "--2";
				if (!isFoto1Default) {
					evidencia1 = encodeTobase64(pic1);
					Log.e("PIC1", "Existe");
				}
				if (!isFoto2Default) {
					evidencia2 = encodeTobase64(pic2);
					Log.e("PIC2", "Existe");
				}
				if (!texto.equals("")) {
					datosPDF.add("Evidencia escrita: " + texto);
				} else {
					datosPDF.add("EMPTY");
				}
				datosPDF.add(evidencia1);
				datosPDF.add(evidencia2);
				guardarFormulario();

			}
		}

	}

	public static String encodeTobase64(Bitmap image) {
		Bitmap immagex = image;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

		return imageEncoded;
	}

	public static Bitmap decodeBase64(String input) {
		byte[] decodedByte = Base64.decode(input, 0);
		return BitmapFactory
				.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

	public void enviarMail(Bitmap pic1, Bitmap pic2) {

		String date = now();
		String fileName = generalIntent.getStringExtra("nombreFormulario")
				+ " " + date + ".pdf";
		try {
			if (pic1 == null) {
				Log.e("PIC1", "NULL -- ");
			}
			PDFWriterFormulario.savePDF(datosPDF, pic1, pic2, fileName, this);
		} catch (UnsupportedEncodingException e) {
		}
		enviarPDfemail(fileName);
	}

	public static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy - hh:mm:ss a");
		return sdf.format(cal.getTime());
	}

	private void enviarPDfemail(String fileName) {
		Intent sendIntent;

		sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Observaciones de obra");
		sendIntent.putExtra(Intent.EXTRA_TEXT, "Test Text");
		sendIntent.putExtra(Intent.EXTRA_STREAM,
				Uri.parse("file:///mnt/sdcard/" + fileName));
		sendIntent.setType("application/pdf");

		startActivityForResult(Intent.createChooser(sendIntent, "Send Mail"), 1);

	}

	private boolean recogerDatos(boolean isTemporal) {

		for (int i = 0; i < arrayListFormulario.size(); i++) {

			View elemento = arrayListElementosFormulario.get(i);
			String titulo = arrayListFormulario.get(i).getTitulo();

			switch (arrayListFormulario.get(i).getTipo()) {
			case 1:
				EditText editTextFormularios = (EditText) elemento
						.findViewById(R.id.editTextFormularios);

				String texto = editTextFormularios.getText().toString().trim();
				if (texto.equals("")) {
					if (isTemporal) {
						datosPDF.add("EMPTY");
					} else {
						return true;
					}

				} else {
					if (isTemporal) {
						datosPDF.add(texto);
					} else {
						datosPDF.add(titulo + " : " + texto);
					}
				}

				break;
			case 2:
				TimePicker timePickerFormularios = (TimePicker) elemento
						.findViewById(R.id.timePickerFormularios);

				String time = timePickerFormularios.getCurrentHour() + ":"
						+ timePickerFormularios.getCurrentMinute();

				if (isTemporal) {
					datosPDF.add(time);
				} else {
					datosPDF.add(titulo + " : " + time);
				}

				break;
			case 3:
				CheckBox checkBoxFormularios = (CheckBox) elemento
						.findViewById(R.id.checkBoxFormularios);

				EditText editTextFormulariosCheckbox = (EditText) elemento
						.findViewById(R.id.editTextFormulariosCheckbox);

				if (checkBoxFormularios.isChecked()) {

					if (isTemporal) {
						datosPDF.add("No aplica");
					} else {
						datosPDF.add(titulo + ": No aplica");
					}
				} else {
					String textoEdit = editTextFormulariosCheckbox.getText()
							.toString().trim();
					if (textoEdit.equals("")) {
						if (isTemporal) {
							datosPDF.add("EMPTY");
						} else {
							return true;
						}
					} else {
						if (isTemporal) {
							datosPDF.add(textoEdit);
						} else {
							datosPDF.add(titulo + " : " + textoEdit);
						}
					}
				}
				break;
			case 4:

				Spinner spinnerFormularios = (Spinner) elemento
						.findViewById(R.id.spinnerFormularios);

				if (isTemporal) {
					datosPDF.add(spinnerFormularios.getSelectedItem()
							.toString());
				} else {
					datosPDF.add(titulo + " : "
							+ spinnerFormularios.getSelectedItem().toString());
				}

				break;
			case 5:
				DatePicker datePickerFecha = (DatePicker) elemento
						.findViewById(R.id.datePickerFecha);

				int day = datePickerFecha.getDayOfMonth();
				int month = datePickerFecha.getMonth();
				int year = datePickerFecha.getYear();

				Calendar cal = Calendar.getInstance();
				cal.set(year, month, day);

				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				String formatedDate = sdf.format(cal.getTime());

				if (isTemporal) {
					datosPDF.add(formatedDate);
				} else {
					datosPDF.add(titulo + " : " + formatedDate);
				}
				break;
			default:
				datosPDF.add(titulo);

				break;
			}
		}
		for (int i = 0; i < datosPDF.size(); i++) {
			Log.w("Datos :  ", datosPDF.get(i));
		}
		return false;
	}

	public static class TabListener<T extends Fragment> implements
			ActionBar.TabListener {
		private Fragment mFragment;
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;

		/**
		 * Constructor used each time a new tab is created.
		 * 
		 * @param activity
		 *            The host Activity, used to instantiate the fragment
		 * @param tag
		 *            The identifier tag for the fragment
		 * @param clz
		 *            The fragment's Class, used to instantiate the fragment
		 */
		public TabListener(Activity activity, String tag, Class<T> clz) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
		}

		/* The following are each of the ActionBar.TabListener callbacks */

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// Check if the fragment is already initialized
			if (mFragment == null) {
				// If not, instantiate and add it to the activity
				mFragment = Fragment.instantiate(mActivity, mClass.getName());
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				// If it exists, simply attach it in order to show it
				// ft.attach(mFragment);
				ft.show(mFragment);
			}
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				// Detach the fragment, because another one is being attached
				// ft.detach(mFragment);
				ft.hide(mFragment);
			}
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// User selected the already selected tab. Usually do nothing.
		}
	}

	public static class ExampleFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			pic1 = null;
			pic2 = null;
			FormularioFactory mFormularioFactory = new FormularioFactory();

			int numeroFormulario = getActivity().getIntent().getIntExtra(
					"numeroFormulario", 1);

			int reps = getActivity().getIntent().getIntExtra("reps", 1);

			Log.e("REPS", "" + reps);
			arrayListFormulario = mFormularioFactory.getFormulario(
					numeroFormulario, reps);

			// Inflate the layout for this fragment
			View rootView = inflater.inflate(R.layout.activity_formulario,
					container, false);

			LinearLayout formulario = (LinearLayout) rootView
					.findViewById(R.id.linearLayoutFormulario);

			FormularioViewGenerator mFormularioViewGenerator = new FormularioViewGenerator(
					inflater, container, getActivity(), arrayListFormulario);

			arrayListElementosFormulario = mFormularioViewGenerator
					.generarFormulario();

			for (int i = 0; i < arrayListElementosFormulario.size(); i++) {
				formulario.addView(arrayListElementosFormulario.get(i),
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
			}

			if (esCargar) {
				ArrayList<String> form = getActivity().getIntent()
						.getStringArrayListExtra("formulario");
				cargarDatos(form);
			}

			return rootView;
		}

		@SuppressWarnings("unchecked")
		private void cargarDatos(ArrayList<String> form) {
			for (int i = 0; i < arrayListFormulario.size(); i++) {

				View elemento = arrayListElementosFormulario.get(i);

				switch (arrayListFormulario.get(i).getTipo()) {
				case 1:
					EditText editTextFormularios = (EditText) elemento
							.findViewById(R.id.editTextFormularios);

					if (!form.get(i + 3).equals("EMPTY")) {
						editTextFormularios.setText(form.get(i + 3));
					}
					break;
				case 2:
					TimePicker timePickerFormularios = (TimePicker) elemento
							.findViewById(R.id.timePickerFormularios);

					String[] time = form.get(i + 3).split(":");
					timePickerFormularios.setCurrentHour(Integer
							.parseInt(time[0]));
					timePickerFormularios.setCurrentMinute(Integer
							.parseInt(time[1]));

					break;
				case 3:
					CheckBox checkBoxFormularios = (CheckBox) elemento
							.findViewById(R.id.checkBoxFormularios);

					EditText editTextFormulariosCheckbox = (EditText) elemento
							.findViewById(R.id.editTextFormulariosCheckbox);

					if (form.get(i + 3).equals("No aplica")) {
						checkBoxFormularios.setChecked(true);
					} else {
						checkBoxFormularios.setChecked(false);
						if (!form.get(i + 3).equals("EMPTY")) {
							editTextFormulariosCheckbox
									.setText(form.get(i + 3));
						}
					}

					break;
				case 4:

					Spinner spinnerFormularios = (Spinner) elemento
							.findViewById(R.id.spinnerFormularios);

					ArrayAdapter<String> myAdap = (ArrayAdapter<String>) spinnerFormularios
							.getAdapter();
					int spinnerPosition = myAdap.getPosition(form.get(i + 3));

					// set the default according to value
					spinnerFormularios.setSelection(spinnerPosition);

					break;
				case 5:
					DatePicker datePickerFecha = (DatePicker) elemento
							.findViewById(R.id.datePickerFecha);

					String[] date = form.get(i + 3).split("-");

					int day = Integer.parseInt(date[0]);
					int month = Integer.parseInt(date[1]) - 1;
					int year = Integer.parseInt(date[2]);

					datePickerFecha.updateDate(year, month, day);
					break;
				default:
					break;
				}
			}

			String img1 = form.get(form.size() - 2);
			String img2 = form.get(form.size() - 1);
			String evidenciaEscritaForm = form.get(form.size() - 3);
			Log.e("Evidencia 1", img1);
			Log.e("Evidencia 2", img2);
			Log.e("Evidencia 2", evidenciaEscritaForm);
			if (!img1.equals("--1")) {
				fotoBitmapFinal = decodeBase64(img1);
				foto1.setImageBitmap(fotoBitmapFinal);
				isFoto1Default = false;
			}
			if (!img2.equals("--2")) {
				fotoBitmapFinal = decodeBase64(img2);
				foto2.setImageBitmap(fotoBitmapFinal);
				isFoto2Default = false;
			}
			if (!evidenciaEscritaForm.equals("EMPTY")) {
				evidenciaEscrita.setText(evidenciaEscritaForm);
				Log.e("Evidencia 2", evidenciaEscritaForm);
			}
		}
	}

	public static class ExampleFragmentTwo extends Fragment {

		private static final int MAX_IMAGE_DIMENSION = 250;
		private Spinner spinnerObservaciones;
		public View lastContextMenuButton;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Inflate the layout for this fragment

			View rootView = inflater.inflate(R.layout.activity_observaciones,
					container, false);
			inicializarSpinner(rootView);
			inicializarObjetos(rootView);

			return rootView;
		}

		private void inicializarSpinner(View rootView) {
			ArrayList<String> SpinnerArray = new ArrayList<String>();
			SpinnerArray.add("Evidencia fotogr�fica");
			SpinnerArray.add("Evidencia escrita");

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_spinner_item,
					SpinnerArray);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerObservaciones = (Spinner) rootView
					.findViewById(R.id.spinnerObservaciones);
			spinnerObservaciones.setAdapter(adapter);
			spinnerObservaciones.setPrompt("Seleccione opcion");
			spinnerObservaciones
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int posicion, long arg3) {

							switch (posicion) {
							case 0:
								evidenciaEscrita.setVisibility(View.GONE);
								foto1.setVisibility(View.VISIBLE);
								foto2.setVisibility(View.VISIBLE);
								break;

							case 1:
								foto1.setVisibility(View.INVISIBLE);
								foto2.setVisibility(View.INVISIBLE);
								evidenciaEscrita.setVisibility(View.VISIBLE);
								break;

							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {

						}

					});

		}

		private void inicializarObjetos(View rootView) {
			foto1 = (ImageView) rootView
					.findViewById(R.id.imageViewObservaciones1);
			foto2 = (ImageView) rootView
					.findViewById(R.id.imageViewObservaciones2);
			evidenciaEscrita = (EditText) rootView
					.findViewById(R.id.editTextObservaciones);
			registerForContextMenu(foto1);
			registerForContextMenu(foto2);
		}

		public void onActivityResult(int requestCode, int resultCode,
				Intent imageReturnedIntent) {
			super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
			switch (requestCode) {
			case 0:// Toma foto
				if (resultCode == RESULT_OK) {
					// fotoBitmapFinal = (Bitmap)
					// imageReturnedIntent.getExtras().get("data");

					getActivity().getContentResolver().notifyChange(contentUri,
							null);
					ContentResolver cr = getActivity().getContentResolver();

					try {
						fotoBitmapFinal = android.provider.MediaStore.Images.Media
								.getBitmap(cr, contentUri);
					} catch (Exception e) {
						Toast.makeText(getActivity(), "Failed to load",
								Toast.LENGTH_SHORT).show();
					}
					fotoBitmapFinal = Bitmap.createScaledBitmap(
							fotoBitmapFinal, 400, 400, false);

					foto1.setImageBitmap(fotoBitmapFinal);
					pic1 = fotoBitmapFinal;
					isFoto1Default = false;
				}

				break;
			case 1:// Sube foto
				if (resultCode == RESULT_OK) {
					Uri selectedImage = imageReturnedIntent.getData();

					/*
					 * try { fotoBitmapFinal = getCorrectlyOrientedImage(
					 * getActivity(), selectedImage); } catch (IOException e) {
					 * e.printStackTrace(); }
					 */

					String[] filePathColumn = { MediaStore.Images.Media.DATA };
					Cursor cursor = getActivity().getContentResolver().query(
							selectedImage, filePathColumn, null, null, null);
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					String picturePath = cursor.getString(columnIndex);
					cursor.close();
					fotoBitmapFinal = BitmapFactory.decodeFile(picturePath);
					fotoBitmapFinal = Bitmap.createScaledBitmap(
							fotoBitmapFinal, 400, 400, false);

					foto1.setImageBitmap(fotoBitmapFinal);
					pic1 = fotoBitmapFinal;
					isFoto1Default = false;
				}
				break;
			case 2:// Toma foto
				if (resultCode == RESULT_OK) {
					getActivity().getContentResolver().notifyChange(contentUri,
							null);
					ContentResolver cr = getActivity().getContentResolver();

					try {
						fotoBitmapFinal = android.provider.MediaStore.Images.Media
								.getBitmap(cr, contentUri);
					} catch (Exception e) {
						Toast.makeText(getActivity(), "Failed to load",
								Toast.LENGTH_SHORT).show();
					}
					fotoBitmapFinal = Bitmap.createScaledBitmap(
							fotoBitmapFinal, 400, 400, false);

					foto2.setImageBitmap(fotoBitmapFinal);
					pic2 = fotoBitmapFinal;
					isFoto2Default = false;
				}

				break;
			case 3:// Sube foto
				if (resultCode == RESULT_OK) {
					Uri selectedImage = imageReturnedIntent.getData();

					String[] filePathColumn = { MediaStore.Images.Media.DATA };
					Cursor cursor = getActivity().getContentResolver().query(
							selectedImage, filePathColumn, null, null, null);
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					String picturePath = cursor.getString(columnIndex);
					cursor.close();
					fotoBitmapFinal = BitmapFactory.decodeFile(picturePath);
					fotoBitmapFinal = Bitmap.createScaledBitmap(
							fotoBitmapFinal, 400, 400, false);

					foto2.setImageBitmap(fotoBitmapFinal);
					pic2 = fotoBitmapFinal;
					isFoto2Default = false;

				}
				break;
			}
		}

		public static int getOrientation(Context context, Uri photoUri) {
			/* it's on the external media. */
			Cursor cursor = context
					.getContentResolver()
					.query(photoUri,
							new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
							null, null, null);

			if (cursor.getCount() != 1) {
				return -1;
			}

			cursor.moveToFirst();
			return cursor.getInt(0);
		}

		public static Bitmap getCorrectlyOrientedImage(Context context,
				Uri photoUri) throws IOException {
			InputStream is = context.getContentResolver().openInputStream(
					photoUri);
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
			 * if the orientation is not 0 (or -1, which means we don't know),
			 * we have to do a rotation.
			 */
			if (orientation > 0) {
				Matrix matrix = new Matrix();
				matrix.postRotate(orientation);

				srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
						srcBitmap.getWidth(), srcBitmap.getHeight(), matrix,
						true);
			}

			return srcBitmap;
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
				Log.v("Opcion", "Tomar foto");
				return true;
			case 1:
				subirFoto(lastContextMenuButton);
				Log.v("Opcion", "Subir foto");
				return true;
			case 2:
				eliminarFoto(lastContextMenuButton);
				Log.v("Opcion", "Eliminar");
				return true;
			default:
				return false;
			}
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			super.onCreateOptionsMenu(menu, inflater);
			inflater.inflate(R.menu.main, menu);
		}

		private void tomarFoto(View v) {
			if (v == foto1) {
				Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				// Create the File where the photo should go
				File photoFile = null;
				try {
					photoFile = createImageFile();
				} catch (IOException ex) {
					Log.e("ERROR", "NOT CREATED");
				}
				// Continue only if the File was successfully created
				if (photoFile != null) {
					contentUri = Uri.fromFile(photoFile);
					takePicture.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
					startActivityForResult(takePicture, 0);
				}

			} else if (v == foto2) {
				Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				// Create the File where the photo should go
				File photoFile = null;
				try {
					photoFile = createImageFile();
				} catch (IOException ex) {
					Log.e("ERROR", "NOT CREATED");
				}
				// Continue only if the File was successfully created
				if (photoFile != null) {
					contentUri = Uri.fromFile(photoFile);
					takePicture.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
					startActivityForResult(takePicture, 2);
				}
			}
		}

		private void subirFoto(View v) {
			if (v == foto1) {
				Intent pickPhoto = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(pickPhoto, 1);// one can be replced with
														// any action code
			} else if (v == foto2) {
				Intent pickPhoto = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(pickPhoto, 3);// one can be replced with
														// any action code
			}
		}

		private void eliminarFoto(View v) {
			if (v == foto1) {
				foto1.setImageResource(R.drawable.ic_launcher);
				isFoto1Default = true;
			} else if (v == foto2) {
				foto2.setImageResource(R.drawable.ic_launcher);
				isFoto2Default = true;
			}
		}

	}

	static String mCurrentPhotoPath;

	private static File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}
}
