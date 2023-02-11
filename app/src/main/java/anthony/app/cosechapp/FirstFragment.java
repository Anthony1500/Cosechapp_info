package anthony.app.cosechapp;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import static anthony.app.cosechapp.DatabaseHelper.COLUMN_accesstoken;
import static anthony.app.cosechapp.DatabaseHelper.COLUMN_apptoken;
import static anthony.app.cosechapp.DatabaseHelper.COLUMN_email;
import static anthony.app.cosechapp.DatabaseHelper.COLUMN_password;
import static anthony.app.cosechapp.DatabaseHelper.TABLE_NAME;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.dcastalia.localappupdate.DownloadApk;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import anthony.app.cosechapp.databinding.FragmentFirstBinding;
import anthony.app.cosechapp.dialogo.sininternetdialogo;
import anthony.app.cosechapp.validation.validateToken;

public class FirstFragment extends Fragment  implements Response.Listener<JSONObject>,Response.ErrorListener ,OnRequestPermissionsResultCallback  {

    private static final int MY_PERMISSIONS_REQUEST_MANAGE_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CODE_WRITE_STORAGE_PERMISSION = 2;
    private static final int REQUEST_CODE_MANAGE_EXTERNAL_STORAGE = 3;

    sininternetdialogo dialogFragment = new sininternetdialogo();

    private FragmentFirstBinding binding;
    RequestQueue rq;

    private Autoupdater updater;
    private RelativeLayout loadingPanel;
    private Context context;
    Button  actualizarBTN;
    private TextView textVie;
    JsonRequest jrq;
    EditText  cajacorreo,cajacontraseña;//Definimos variables a utilizar
    Button botonenviar;
    Button botoncorreo;
    String nombreusuario="";
    String id_usuario="",valor;
    Handler handler = new Handler();
    ProgressDialog progressDialog;
    private Timer timer;
    private ActivityCompat FragmentCompat;
    validateToken validate = new validateToken(getContext());

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View vista = inflater.inflate(R.layout.fragment_first,container,false);

        cajacorreo=(EditText) vista.findViewById(R.id.correo);
        cajacontraseña=(EditText) vista.findViewById(R.id.contraseña);
        botonenviar=(Button) vista.findViewById(R.id.reportetemp);//Instanciamos las variables del XML a variables locales.
        botoncorreo=(Button) vista.findViewById(R.id.botoncorreo);

        {
            rq = Volley.newRequestQueue(getContext());
        }
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // Inicie el timer.
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkInternetConnection();
            }
        }, 0, 90000);  // Ejecute el método cada 5 segundos.




        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

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
if ( checkPer()){
    Toast.makeText(getContext(),"Esperando respuesta",Toast.LENGTH_SHORT).show();
    comenzarActualizar();
    // Si ya se tiene el permiso, se puede hacer lo que se necesite

}else{
    requestPer();
}
              }
            });
        }catch (Exception ex){
            //Por Cualquier error.
            Toast.makeText(getContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
        }
//***************************************************************************************
        botonenviar.setOnClickListener(new View.OnClickListener() {//Método para darle función al botón

            @Override
            public void onClick(View v) {
                String caja1 = cajacorreo.getText().toString();
                if(!caja1.isEmpty() )
                {
                    progressDialog = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);
                    progressDialog.setMessage("Por favor espera...");//Método del Progress Dialog
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    iniciarSesion();
                }
                else{
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

        if (requestCode == 200 & grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permiso concebido", Toast.LENGTH_SHORT).show();
            } else {

                // Si no se ha concedido el permiso, puedes mostrar un mensaje de error o hacer algo más
                Toast.makeText(getContext(), "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
       else if (requestCode == REQUEST_CODE_WRITE_STORAGE_PERMISSION) {
            // Revisa si el usuario concedió o negó el permiso
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permiso concebido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {//Respuesta fallida
        progressDialog.dismiss();
       // Toast.makeText(getContext(),"Las credenciales ingresadas son incorrectas"+error.toString(),Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(),"Las credenciales ingresadas son incorrectas"+error.getMessage(),Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(),"Error al recibir datos",Toast.LENGTH_SHORT).show();
                        }

        Toast.makeText(getContext(),"Se ha ingresado correctamente "+" "+nombreusuario,Toast.LENGTH_SHORT).show();
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
        timer.cancel();
    }
    private void checkInternetConnection() {
        // Compruebe si el dispositivo está conectado a Internet.

        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
            if (!dialogFragment.isAdded())
                dialogFragment.show(getFragmentManager(), "estado");
        }





    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null) {
            validate = new validateToken(context);
        }
    }


}