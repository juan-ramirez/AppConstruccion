package com.movilapps.appconstruccion;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
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

	private static ImageView foto1;
	private static ImageView foto2;
	private static EditText evidenciaEscrita;
	public static boolean isFoto1Default = true;
	public static boolean isFoto2Default = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		generalIntent = getIntent();

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
			datosPDF = new ArrayList<String>();
			Log.e("Vacíos", String.valueOf(recogerDatos()));

			SharedPreferences mPrefs = getSharedPreferences("my_prefs",
					MODE_PRIVATE);
			Editor prefsEditor = mPrefs.edit();

			Gson gson = new Gson();
			String json = gson.toJson(datosPDF);
			Log.e("JSON: ", json);
			prefsEditor.putString("MyObject", json);
			Log.e("Commited: ", String.valueOf(prefsEditor.commit()));

			return (true);
		case R.id.action_email:
			datosPDF = new ArrayList<String>();
			Log.e("Vacíos", String.valueOf(recogerDatos()));
			if (!recogerDatos()) {
				String date = now();
				String fileName = generalIntent
						.getStringExtra("nombreFormulario")
						+ " "
						+ date
						+ ".pdf";
				try {
					PDFWriterFormulario.savePDF(datosPDF, fileName, this);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				enviarPDfemail(fileName);
			} else {
				Toast.makeText(
						this,
						"Alguno de los campos se encuentra vacío, por favor ingréselo",
						Toast.LENGTH_SHORT).show();
			}
			return (true);
		}

		return (super.onOptionsItemSelected(item));
	}

	public static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy - hh:mm a");
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

		startActivity(Intent.createChooser(sendIntent, "Send Mail"));

	}

	private boolean recogerDatos() {

		for (int i = 0; i < arrayListFormulario.size(); i++) {

			View elemento = arrayListElementosFormulario.get(i);
			String titulo = arrayListFormulario.get(i).getTitulo();

			switch (arrayListFormulario.get(i).getTipo()) {
			case 1:
				EditText editTextFormularios = (EditText) elemento
						.findViewById(R.id.editTextFormularios);

				String texto = editTextFormularios.getText().toString().trim();
				if (texto.equals("")) {
					return true;
				}

				datosPDF.add(titulo + " " + texto);
				break;
			case 2:
				TimePicker timePickerFormularios = (TimePicker) elemento
						.findViewById(R.id.timePickerFormularios);

				datosPDF.add(titulo + " "
						+ timePickerFormularios.getCurrentHour() + ":"
						+ timePickerFormularios.getCurrentMinute());
				break;
			case 3:
				CheckBox checkBoxFormularios = (CheckBox) elemento
						.findViewById(R.id.checkBoxFormularios);

				EditText editTextFormulariosCheckbox = (EditText) elemento
						.findViewById(R.id.editTextFormulariosCheckbox);

				if (checkBoxFormularios.isChecked()) {
					datosPDF.add(titulo + " No aplica");
				} else {
					String textoEdit = editTextFormulariosCheckbox.getText()
							.toString().trim();
					if (textoEdit.equals("")) {
						return true;
					}
					datosPDF.add(titulo + " " + textoEdit);
				}
				break;
			case 4:

				Spinner spinnerFormularios = (Spinner) elemento
						.findViewById(R.id.spinnerFormularios);

				datosPDF.add(titulo + " "
						+ spinnerFormularios.getSelectedItem().toString());
				break;
			case 5:
				DatePicker datePickerFecha = (DatePicker) elemento
						.findViewById(R.id.datePickerFecha);

				datosPDF.add(titulo + " " + datePickerFecha.getYear() + "/"
						+ (datePickerFecha.getMonth() + 1) + "/"
						+ datePickerFecha.getDayOfMonth());
				break;
			default:
				datosPDF.add(titulo);
				break;
			}
		}

		for (int i = 0; i < datosPDF.size(); i++) {
			Log.d("Datos :  ", datosPDF.get(i));
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

			FormularioFactory mFormularioFactory = new FormularioFactory();

			int numeroFormulario = getActivity().getIntent().getIntExtra(
					"numeroFormulario", 1);

			arrayListFormulario = mFormularioFactory
					.getFormulario(numeroFormulario);

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

			Log.e("ERROR: ", "Reinstanciado");

			return rootView;
		}
	}

	public static class ExampleFragmentTwo extends Fragment {

		private static final int MAX_IMAGE_DIMENSION = 250;
		private Bitmap fotoBitmapFinal;
		private Spinner spinnerObservaciones;
		public View lastContextMenuButton;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Inflate the layout for this fragment

			View rootView = inflater.inflate(R.layout.activity_observaciones,
					container, false);
			Log.e("ERROR: ", "Reinstanciado");

			inicializarSpinner(rootView);
			inicializarObjetos(rootView);

			return rootView;
		}

		private void inicializarSpinner(View rootView) {
			ArrayList<String> SpinnerArray = new ArrayList<String>();
			SpinnerArray.add("Evidencia fotográfica");
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

							// TODO Auto-generated method stub
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
					Uri selectedImage = imageReturnedIntent.getData();
					if (selectedImage.equals(null)) {

					}
					try {
						fotoBitmapFinal = getCorrectlyOrientedImage(
								getActivity(), selectedImage);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					foto1.setImageBitmap(fotoBitmapFinal);
					isFoto1Default = false;
				}

				break;
			case 1:// Sube foto
				if (resultCode == RESULT_OK) {
					Uri selectedImage = imageReturnedIntent.getData();
					try {
						fotoBitmapFinal = getCorrectlyOrientedImage(
								getActivity(), selectedImage);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					foto1.setImageBitmap(fotoBitmapFinal);
					isFoto1Default = false;
				}
				break;
			case 2:// Toma foto
				if (resultCode == RESULT_OK) {
					fotoBitmapFinal = (Bitmap) imageReturnedIntent.getExtras()
							.get("data");
					foto2.setImageBitmap(fotoBitmapFinal);
					isFoto2Default = false;
				}

				break;
			case 3:// Sube foto
				if (resultCode == RESULT_OK) {
					Uri selectedImage = imageReturnedIntent.getData();
					try {
						fotoBitmapFinal = getCorrectlyOrientedImage(
								getActivity(), selectedImage);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					foto2.setImageBitmap(fotoBitmapFinal);
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
			// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub
			super.onCreateOptionsMenu(menu, inflater);
			inflater.inflate(R.menu.main, menu);
		}

		private void tomarFoto(View v) {
			if (v == foto1) {
				Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(takePicture, 0);
			} else if (v == foto2) {
				Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(takePicture, 2);
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

}
