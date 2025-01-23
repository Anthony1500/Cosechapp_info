package cosechatech.app.cosechapp;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import static cosechatech.app.cosechapp.DatabaseHelper.COLUMN_accesstoken;
import static cosechatech.app.cosechapp.DatabaseHelper.COLUMN_apptoken;
import static cosechatech.app.cosechapp.DatabaseHelper.COLUMN_email;
import static cosechatech.app.cosechapp.DatabaseHelper.COLUMN_password;
import static cosechatech.app.cosechapp.DatabaseHelper.TABLE_NAME;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.dcastalia.localappupdate.DownloadApk;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;

import cosechatech.app.cosechapp.databinding.FragmentFirstBinding;
import cosechatech.app.cosechapp.dialogo.sininternetdialogo;
import cosechatech.app.cosechapp.validation.validateToken;


public class FirstFragment extends Fragment  implements Response.Listener<JSONObject>,Response.ErrorListener ,OnRequestPermissionsResultCallback {

    private static final int MY_PERMISSIONS_REQUEST_MANAGE_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CODE_WRITE_STORAGE_PERMISSION = 2;
    private static final int REQUEST_CODE_MANAGE_EXTERNAL_STORAGE = 3;

    sininternetdialogo dialogFragment = new sininternetdialogo();

    private FragmentFirstBinding binding;
    RequestQueue rq;

    private Autoupdater updater;
    private RelativeLayout loadingPanel;
    private Context context;
    Button actualizarBTN;

    private TextView textVie;
    ImageSwitcher imageswitcher;
    Integer[] imagenes_banner = {R.drawable.banner_image_1, R.drawable.banner_image_2, R.drawable.banner_image_3, R.drawable.banner_image_4, R.drawable.banner_image_5, R.drawable.banner_image_0};
    JsonRequest jrq;
    EditText cajacorreo, cajacontraseña,cajausuario,cajarepeatcontraseña;//Definimos variables a utilizar
    LottieAnimationView imagen;
    Button botonenviar;
    TextInputLayout complerepeatcontraseña,complecontraseña;
    boolean videoReproduciendose = false;
    boolean imagenesMostrandose = false;
    View snackView;
    Button botoncorreo,botonregistro;
    String nombreusuario = "";
    SurfaceView surfaceView;
    VideoView videoView;
    Boolean trueorfalse;
    String id_usuario = "", valor;
    Handler handler = new Handler();
    ProgressDialog progressDialog;
    private Timer timer;
    private ActivityCompat FragmentCompat;
    validateToken validate = new validateToken(getContext());
    ManejadorGlobal manejador = new ManejadorGlobal();
    String horaInicio = String.format(new Locale("es", "EC"), "%02d:%02d", 19, 00);

    String horaFin = String.format(new Locale("es", "EC"), "%02d:%02d", 05, 59);

   
    String horaActual = manejador.HoraActual();
    int minutoActual = manejador.MinutoActual();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState
    ) {
        View vista = inflater.inflate(R.layout.fragment_first, container, false);


        cajausuario = (EditText) vista.findViewById(R.id.text_usuario);
        cajarepeatcontraseña = (EditText) vista.findViewById(R.id.repeatcontraseña);
        complerepeatcontraseña = (TextInputLayout) vista.findViewById(R.id.complerepeatcontraseña);
        complecontraseña = (TextInputLayout) vista.findViewById(R.id.complecontraseña);
        cajacorreo = (EditText) vista.findViewById(R.id.correo);
        cajacontraseña = (EditText) vista.findViewById(R.id.contraseña);
        botonenviar = (Button) vista.findViewById(R.id.botonlogin);//Instanciamos las variables del XML a variables locales.
        botoncorreo = (Button) vista.findViewById(R.id.botoncorreo);
        botonregistro = (Button) vista.findViewById(R.id.botonregistro);
        imageswitcher = (ImageSwitcher) vista.findViewById(R.id.imageswitcher);
        videoView = (VideoView) vista.findViewById(R.id.videoswitcher);
        //surfaceView = vista.findViewById(R.id.videoswitcher);
        transicion();
        final TextView textView1 = vista.findViewById(R.id.btn_acceder);
        final TextView textView2 = vista.findViewById(R.id.btn_registro);
        setHighlightedBottomBorder(textView1);
        {
            rq = Volley.newRequestQueue(getContext());
        }
        cajausuario.setVisibility(View.GONE);
        cajacorreo.setBackgroundResource(R.drawable.border_top);
        cajacontraseña.setBackgroundResource(R.drawable.border_botton);
        cajarepeatcontraseña.setVisibility(View.GONE);
        botonregistro.setVisibility(View.GONE);
        complerepeatcontraseña.setVisibility(View.GONE);

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar el fondo del TextView 1 cuando se hace clic
                int color = textView1.getCurrentTextColor();
                 manejador.getIp();
                // Comparar el color con el color deseado
                if (color == getResources().getColor(R.color.colorRojo)) {
                    setHighlightedBottomBorder(textView1);
                    // Restaurar el fondo del TextView 2
                    restoreDefaultState(textView2);
                } else {
                    // El color del texto no es el color deseado
                    setHighlightedBottomBorder(textView1);
                    // Restaurar el fondo del TextView 2
                    restoreDefaultState(textView2);
                    terminarAnimacion();
                    cajausuario.setText("");
                    cajarepeatcontraseña.setText("");
                    cajacorreo.setText("");
                    cajacontraseña.setText("");
                }



            }
        });

        // Establecer el listener para el TextView 2
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Cambiar el fondo del TextView 1 cuando se hace clic
                int color = textView2.getCurrentTextColor();

                // Comparar el color con el color deseado
                if (color == getResources().getColor(R.color.colorText)) {

                    // El color del texto no es el color deseado
                    setHighlightedBottomBorder(textView2);
                    // Restaurar el fondo del TextView 2
                    restoreDefaultState(textView1);
                    iniciarAnimacion();
                    cajausuario.setText("");
                    cajarepeatcontraseña.setText("");
                    cajacorreo.setText("");
                    cajacontraseña.setText("");
                } else {
                    setHighlightedBottomBorder(textView2);
                    // Restaurar el fondo del TextView 2
                    restoreDefaultState(textView1);
                }
            }
        });


        // Cambiar el fondo a un estado de resaltado



        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // Inicie el timer.
        Handler handler = new Handler();
        Runnable periodicTask = new Runnable() {
            @Override
            public void run() {
                transicion();
                checkInternetConnection();
                handler.postDelayed(this, 5000); // Programa la próxima ejecución después de 90000 ms
            }
        };

// Iniciar la tarea periódica por primera vez
        handler.postDelayed(periodicTask, 5000); // Inicia después de 90000 ms

        imageswitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        });
        imageswitcher.setImageResource(imagenes_banner[5]);
        Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        imageswitcher.setInAnimation(in);
        imageswitcher.setOutAnimation(out);
        videoView.startAnimation(in);
        videoView.startAnimation(out);
        String video = "android.resource://" + getContext().getPackageName() + "/" + R.raw.videonightwebm;
        videoView.setVideoPath(video);


        //imageSwitcher.setImageResource(R.drawable.cosechabanner2);
        //***************************************************************************************************************
        //requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_STORAGE_PERMISSION);
        requestPermissions(new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_MANAGE_EXTERNAL_STORAGE);
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        try {

            loadingPanel = (RelativeLayout) vista.findViewById(R.id.loadingPanel);
            //Esto sirve si la actualizacion no se realiza al principio.
            loadingPanel.setVisibility(View.GONE);
            //Comentamos esa linea para que no se ejecute al principio.
            //comenzarActualizacion();

            actualizarBTN = (Button) vista.findViewById(R.id.botonActualizar);
            actualizarBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkPer()) {
                        Toast.makeText(getContext(), "Esperando respuesta", Toast.LENGTH_SHORT).show();
                        comenzarActualizar();
                        // Si ya se tiene el permiso, se puede hacer lo que se necesite

                    } else {
                        requestPer();
                    }
                }
            });
        } catch (Exception ex) {
            //Por Cualquier error.
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
//***************************************************************************************
        botonenviar.setOnClickListener(new View.OnClickListener() {//Método para darle función al botón

            @Override
            public void onClick(View v) {
                String caja1 = cajacorreo.getText().toString();
                if (!caja1.isEmpty()) {
                    progressDialog = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);
                    progressDialog.setMessage("Por favor espera...");//Método del Progress Dialog
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    iniciarSesion();
                } else {
                    cajacorreo.setError("Favor de escribir algo");

                }
            }
        });
        botoncorreo.setOnClickListener(new View.OnClickListener() {//Método para darle función al botón

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), sincorreo.class);//Envió hacia otro Activity
                startActivity(intent);
            }
        });
        User usario = new User();

        return vista;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 200 & grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permiso concebido", Toast.LENGTH_SHORT).show();
            } else {

                // Si no se ha concedido el permiso, puedes mostrar un mensaje de error o hacer algo más
                Toast.makeText(getContext(), "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_WRITE_STORAGE_PERMISSION) {
            // Revisa si el usuario concedió o negó el permiso
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permiso concebido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void transicion() {
        // ANIMACION DE CAMBIO DE IMAGENES Y VIDEOS++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        try {
            trueorfalse = manejador.Comprobar(horaActual,horaInicio,horaFin);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (trueorfalse) {

                // Mostrar el video en el VideoView
                //surfaceView.setVisibility(View.VISIBLE);

                if (!videoReproduciendose) {
                    videoReproduciendose = true;

                    videoView.requestFocus();
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            // Start playback cuando el reproductor está listo
                            mp.start();
                            mp.setLooping(true);
                            videoView.start();
                        }
                    });
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            // Marcar como completado cuando el video termine de reproducirse
                            videoReproduciendose = false;
                        }
                    });

            }
        } else {
            if (!imagenesMostrandose) {
                imagenesMostrandose = true;
                imageswitcher.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);

                final Handler handler1 = new Handler(Looper.getMainLooper());
                final Runnable runnable = new Runnable() {
                    int i = 0;

                    @Override
                    public void run() {
                        // Precargar la siguiente imagen en segundo plano
                        int nextImageIndex = i + 1;
                        if (nextImageIndex >= imagenes_banner.length) {
                            nextImageIndex = 0;
                        }
                        final int finalNextImageIndex = nextImageIndex;
                        if (isAdded()) {
                            Glide.with(requireContext())
                                    .load(imagenes_banner[finalNextImageIndex])
                                    .preload();
                        }

                        // Cambiar la imagen mostrada en el ImageSwitcher
                        imageswitcher.setImageResource(imagenes_banner[i]);
                        i++;
                        if (i == imagenes_banner.length) {
                            i = 0;
                        }
                        handler1.postDelayed(this, 5000); // 3000ms = 3s
                    }
                };
                handler1.postDelayed(runnable, 4000);
            }



        }
    // ANIMACION DE CAMBIO DE IMAGENES Y VIDEOS++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

}
    @Override
    public void onErrorResponse(VolleyError error) {//Respuesta fallida
        progressDialog.dismiss();
       // Toast.makeText(getContext(),"Las credenciales ingresadas son incorrectas"+error.toString(),Toast.LENGTH_SHORT).show();



       ManejadorGlobal.mostrarSnackbarfailed("Las credenciales ingresadas son incorrectas.", getView(), getContext());

    }


    public void iniciarAnimacion() {


        final AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(1000);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (cajausuario.getVisibility() == View.GONE) {
                    cajacorreo.setVisibility(View.VISIBLE);
                    cajacontraseña.setVisibility(View.VISIBLE);
                    botonenviar.setVisibility(View.GONE);
                    botoncorreo.setVisibility(View.GONE);
                    cajausuario.setBackgroundResource(R.drawable.border_top);
                    cajausuario.setVisibility(View.VISIBLE);
                    cajacorreo.setBackgroundResource(R.drawable.border_middle);
                    cajacontraseña.setBackgroundResource(R.drawable.border_middle);
                    cajarepeatcontraseña.setVisibility(View.VISIBLE);
                    complerepeatcontraseña.setVisibility(View.VISIBLE);
                    botonregistro.setVisibility(View.VISIBLE);
                    cajarepeatcontraseña.setBackgroundResource(R.drawable.border_botton);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        botonenviar.startAnimation(animation);
        botoncorreo.startAnimation(animation);
        cajarepeatcontraseña.startAnimation(animation);
        cajacorreo.startAnimation(animation);
        complecontraseña.startAnimation(animation);

    }

    public void terminarAnimacion() {


        final AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(1000);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                if (cajausuario.getVisibility() == View.VISIBLE) {
                    cajausuario.setVisibility(View.GONE);
                    cajacorreo.setBackgroundResource(R.drawable.border_top);
                    cajacontraseña.setBackgroundResource(R.drawable.border_botton);
                    cajarepeatcontraseña.setVisibility(View.GONE);
                    complerepeatcontraseña.setVisibility(View.GONE);
                    botonregistro.setVisibility(View.GONE);
                    botonenviar.setVisibility(View.VISIBLE);
                    botoncorreo.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });


        cajausuario.startAnimation(animation);
        cajarepeatcontraseña.startAnimation(animation);
        complerepeatcontraseña.startAnimation(animation);
        botonregistro.startAnimation(animation);
    }

    @Override
    public void onResponse(JSONObject response) {//Respuesta correcta
        Bundle bundle = new Bundle();
        User usario = new User();
        progressDialog.dismiss();
        try {

                        JSONObject json = response.getJSONObject("user");
                        nombreusuario= json.getString("username");//Llenado de datos en base a la consulta
                        id_usuario= json.getString("id_usuario");
                        usario.setEmail(json.getString("email"));
                        usario.setPassword(json.getString("password"));
                        // insert data
            DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_accesstoken,response.getString("access_token"));
            values.put(COLUMN_apptoken,getResources().getString(R.string.API_TOKEN));
            values.put(COLUMN_email,json.getString("email"));
            values.put(COLUMN_password,json.getString("password"));
            db.insert(TABLE_NAME, null, values);
            db.close();


                        } catch (JSONException e) {

                         ManejadorGlobal.mostrarSnackbarfailed("Error al recibir datos.", getView(), getContext());
                        }

        Toast.makeText(getContext(), "¡Bienvenido, " + nombreusuario + "!", Toast.LENGTH_SHORT).show();

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        Intent intent = new Intent(getContext(), menuprincipal.class );
        intent.putExtra("id",id_usuario);//Envió hacia otro Activity
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
         //****************************************************************

    }
    private void iniciarSesion(){

        String url=getResources().getString(R.string.ip)+"login";
        JSONObject data = new JSONObject();
        try {
            data.put("email", cajacorreo.getText().toString());
            data.put("token", getResources().getString(R.string.API_TOKEN));
            data.put("password", cajacontraseña.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jrq= new JsonObjectRequest(Request.Method.POST,url,data,this,this){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept","application/json");
                return headers;
            }
        };
        rq.add(jrq);//Envió y recepción de datos
    }

        private Runnable finishBackgroundDownload = new Runnable() {
        @Override
        public void run() {

            //Volvemos el ProgressBar a invisible.
            loadingPanel.setVisibility(View.GONE);
            //Comprueba que halla nueva versión.
            if(updater.isNewVersionAvailable()){
                //Crea mensaje con datos de versión.
                String msj = "Nueva Versión Disponible" ;
                msj += "\nVersión Actual: " +  updater.getCurrentVersionName() + "(" + updater.getCurrentVersionCode() + ")";
                msj += "\nUltima versión: " + updater.getLatestVersionName() + "(" + updater.getLatestVersionCode() +")";
               // msj += "\nDesea Actualizar?";
                //Crea ventana de alerta.

                AlertDialog.Builder dialog1 = new AlertDialog.Builder(context);
                dialog1.setTitle("Desea Actualizar?");

                dialog1.setIcon(R.drawable.descarga);
                dialog1.setMessage(msj);
                //dialog1.setNegativeButton(R.string.cancel, null);
                //Establece el boton de Aceptar y que hacer si se selecciona.
                dialog1.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Vuelve a poner el ProgressBar mientras se baja e instala.
                        loadingPanel.setVisibility(View.VISIBLE);
                        //Se ejecuta el Autoupdater con la orden de instalar. Se puede poner un listener o no
                       // updater.InstallNewVersion(null);
                      DownloadApk downloadApk = new DownloadApk(getContext());
                      downloadApk.startDownloadingApk(updater.getDownloadURL(),"versión");
                    }
                });

                //Muestra la ventana esperando respuesta.

                dialog1.show();

            }else{
                //No existen Actualizaciones.
                //Log.d("Mensaje","No Hay actualizaciones");
                Toast.makeText(getContext(),"No Hay actualizaciones ",Toast.LENGTH_SHORT).show();
            }
        }
    };
    private void comenzarActualizar(){
        //Para tener el contexto mas a mano.
        context = getContext();
        //Creamos el Autoupdater.
        updater = new Autoupdater(getContext());
        //Ponemos a correr el ProgressBar.
        loadingPanel.setVisibility(View.VISIBLE);
        //Ejecutamos el primer metodo del Autoupdater.
        updater.DownloadData(finishBackgroundDownload);
    }
boolean checkPer(){
 return ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)+
         ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED;
}
void requestPer(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        }
        }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        // Cancela cualquier Runnable o Message enviado al Handler
        handler.removeCallbacksAndMessages(null);
    }

    private void checkInternetConnection() {
        // Compruebe si el dispositivo está conectado a Internet.
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected();
            if (isConnected) {
                // El dispositivo está conectado a Internet.
                if (dialogFragment.isAdded()) {
                    // Oculte el diálogo si está mostrándose.
                    dialogFragment.dismiss();
                }
            } else {
                // El dispositivo no está conectado a Internet.
                // Muestre el diálogo.
                // dialogFragment.show(getParentFragmentManager(), "estado");
                if (!dialogFragment.isAdded()) {
                    dialogFragment.show(getActivity().getSupportFragmentManager(), "estado");
                }
            }

        }





    }






    private void setHighlightedBottomBorder(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView.setTextColor(getResources().getColor(R.color.colorRojo)); // Cambiar al color deseado
    }

    // Restaurar el estado del texto
    private void restoreDefaultState(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        textView.setTextColor(getResources().getColor(R.color.colorText)); // Cambiar al color original
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null) {
            validate = new validateToken(context);
        }
    }


}