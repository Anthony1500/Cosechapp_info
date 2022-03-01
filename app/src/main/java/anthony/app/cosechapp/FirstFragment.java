package anthony.app.cosechapp;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import anthony.app.cosechapp.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {
   private FragmentFirstBinding binding;
    RequestQueue rq;
    private TextView textVie;
    JsonRequest jrq;
    EditText  cajacorreo,cajacontraseña;//Definimos variables a utilizar
    Button botonenviar;
    Button botoncorreo;
    String nombreusuario="";
    String id_usuario="";
    Handler handler = new Handler();
    ProgressDialog progressDialog;
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View vista = inflater.inflate(R.layout.fragment_first,container,false);
        cajacorreo=(EditText) vista.findViewById(R.id.correo);
        cajacontraseña=(EditText) vista.findViewById(R.id.contraseña);
        botonenviar=(Button) vista.findViewById(R.id.enviar);//Instanciamos las variables del XML a variables locales.
        botoncorreo=(Button) vista.findViewById(R.id.botoncorreo);
        rq = Volley.newRequestQueue(getContext());

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
        Intent intent = new Intent(getContext(), menuprincipal.class);
        intent.putExtra("id", id_usuario);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK); //Envió hacia otro Activity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void iniciarSesion(){

        String url="https://apps.indoamerica.edu.ec/usuarios.php?email="+cajacorreo.getText().toString()+"&password="+cajacontraseña.getText().toString();
        jrq= new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jrq);//Envió y recepción de datos
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}