package anthony.app.cosechapp;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import anthony.app.cosechapp.databinding.FragmentFirstBinding;
import anthony.app.cosechapp.ui.home.HomeFragment;

public class FirstFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener {
    private FragmentFirstBinding binding;
    RequestQueue rq;
    private TextView textVie;
    JsonRequest jrq;
    String urls = "https://apps.indoamerica.edu.ec/selectusuarios2.php";
    EditText  cajacorreo,cajacontraseña;
    Button botonenviar;
    Button botoncorreo;
    String nombreusuario="";
    String id_usuario="";
    ProgressDialog progressDialog;
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        // return inflater.inflate(R.layout.fragment_first,container,false);
        View vista = inflater.inflate(R.layout.fragment_first,container,false);
        cajacorreo=(EditText) vista.findViewById(R.id.correo);
        cajacontraseña=(EditText) vista.findViewById(R.id.contraseña);
        botonenviar=(Button) vista.findViewById(R.id.enviar);
        botoncorreo=(Button) vista.findViewById(R.id.botoncorreo);
        rq = Volley.newRequestQueue(getContext());

        botonenviar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String caja1 = cajacorreo.getText().toString();


                if(!caja1.isEmpty() )
                {
                    progressDialog = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);
                    progressDialog.setMessage("Por favor espera...");
                    progressDialog.show();
                    iniciarSesion();
                }
                else{
                    cajacorreo.setError("Favor de escribir algo");

                }


            }

        });
        botoncorreo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), sincorreo.class);

                startActivity(intent);

            }

        });
        User usario = new User();

        return vista;
    }



    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.dismiss();
        Toast.makeText(getContext(),"las credenciales ingresadas son incorrectas"+error.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Bundle bundle = new Bundle();
        User usario = new User();
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject= null;
        progressDialog.dismiss();
       // startActivity(new Intent(getContext(),comprovar.class));
        try {
            jsonObject = jsonArray.getJSONObject(0);
            nombreusuario=jsonObject.optString("username");
            id_usuario=jsonObject.optString("id_usuario");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(getContext(),"Se ha ingresado correctamente "+" "+nombreusuario,Toast.LENGTH_SHORT).show();



        try {
            jsonObject = jsonArray.getJSONObject(0);
            usario.setEmail(jsonObject.optString("email"));
            usario.setPassword(jsonObject.optString("password"));
        }catch (JSONException e){
            e.printStackTrace();
        }
        Intent intent = new Intent(getContext(), menuprincipal.class);
        intent.putExtra("id", id_usuario);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);




    }





    private void iniciarSesion(){

        String url="https://apps.indoamerica.edu.ec/usuarios.php?email="+cajacorreo.getText().toString()+"&password="+cajacontraseña.getText().toString();
        jrq= new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jrq);

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}