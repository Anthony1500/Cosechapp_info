package anthony.app.cosechapp;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.dcastalia.localappupdate.DownloadApk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import anthony.app.cosechapp.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener ,OnRequestPermissionsResultCallback  {

    private static final int MY_PERMISSIONS_REQUEST_MANAGE_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CODE_WRITE_STORAGE_PERMISSION = 2;
    private static final int REQUEST_CODE_MANAGE_EXTERNAL_STORAGE = 3;


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
    String id_usuario="";
    Handler handler = new Handler();
    ProgressDialog progressDialog;
    private ActivityCompat FragmentCompat;

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
        Toast.makeText(getContext(),"las credenciales ingresadas son incorrectas"+error.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {//Respuesta correcta
        Bundle bundle = new Bundle();
        User usario = new User();
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject= null;
        progressDialog.dismiss();
       try {
            jsonObject = jsonArray.getJSONObject(0);
            nombreusuario=jsonObject.optString("username");//Llenado de datos en base a la consulta
            id_usuario=jsonObject.optString("id_usuario");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(getContext(),"Se ha ingresado correctamente "+" "+nombreusuario,Toast.LENGTH_SHORT).show();
        try {
            jsonObject = jsonArray.getJSONObject(0);
            usario.setEmail(jsonObject.optString("email"));//Consulta de datos
            usario.setPassword(jsonObject.optString("password"));
        }catch (JSONException e){
            e.printStackTrace();
        }

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        Intent intent = new Intent(getContext(), menuprincipal.class );
        intent.putExtra("id", id_usuario);//Envió hacia otro Activity
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //****************************************************************

    }
    private void iniciarSesion(){

        String url="https://cosecha.tech/cosechaap_api_service/usuarios.php?email="+cajacorreo.getText().toString()+"&password="+cajacontraseña.getText().toString();
        jrq= new JsonObjectRequest(Request.Method.GET,url,null,this,this);
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
                String msj = "Nueva Version: " + updater.isNewVersionAvailable();
                msj += "\nCurrent Version: " + updater.getCurrentVersionName() + "(" + updater.getCurrentVersionCode() + ")";
                msj += "\nLastest Version: " + updater.getLatestVersionName() + "(" + updater.getLatestVersionCode() +")";
                msj += "\nDesea Actualizar?";
                //Crea ventana de alerta.

                AlertDialog.Builder dialog1 = new AlertDialog.Builder(context);
                dialog1.setMessage(msj);
                dialog1.setNegativeButton(R.string.cancel, null);
                //Establece el boton de Aceptar y que hacer si se selecciona.
                dialog1.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Vuelve a poner el ProgressBar mientras se baja e instala.
                        loadingPanel.setVisibility(View.VISIBLE);
                        //Se ejecuta el Autoupdater con la orden de instalar. Se puede poner un listener o no
                       // updater.InstallNewVersion(null);
                        DownloadApk downloadApk = new DownloadApk(getContext());
                        downloadApk.startDownloadingApk(updater.getDownloadURL(),"update");
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
    }
}