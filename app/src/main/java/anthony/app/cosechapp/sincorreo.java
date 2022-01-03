package anthony.app.cosechapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

public class sincorreo extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {

    Button botonatras;
    Button botonconprovar;
    Button botonenviar;
    RequestQueue rq;
    EditText  cajausuario;
    private TextView textVie;
    String id_usuario;
    String email;
    EditText mensaje;


    JsonRequest jrq;
    ProgressDialog progressDialog;
    String url = "https://apps.indoamerica.edu.ec/selectusuarios2.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sincorreo);

        cajausuario=(EditText) findViewById(R.id.usuario);


        rq = Volley.newRequestQueue(sincorreo.this);

        botonatras=(Button) findViewById(R.id.atras);
        botonconprovar=(Button) findViewById(R.id.enviar);
        botonenviar=(Button) findViewById(R.id.botonenviar2);

        botonatras.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sincorreo.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(sincorreo.this,"Volvió al inicio de sesión." ,Toast.LENGTH_SHORT).show();

            }

        });


        botonconprovar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String caja1 = cajausuario.getText().toString();


                if(!caja1.isEmpty() )
                {
                progressDialog = new ProgressDialog(sincorreo.this, R.style.MyAlertDialogStyle);

                progressDialog.setMessage("Por favor espera...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                comprovar();

                }
                else{
                    cajausuario.setError("Favor de escribir algo");

                }

            }

        });



    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(),"las credenciales ingresadas son incorrectas"+error.toString(),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialog.dismiss();
        JSONArray jsonArray = response.optJSONArray("probar");
        JSONObject jsonObject= null;
        try {
            jsonObject = jsonArray.getJSONObject(0);
            id_usuario =jsonObject.optString("id_usuario");

        } catch (JSONException e) {
            e.printStackTrace();
        }
       Intent intent = new Intent(sincorreo.this, sincorreo2.class);
        intent.putExtra("id", id_usuario);
        startActivity(intent);
        Toast.makeText(sincorreo.this,"El usuario ingresado es correcto ",Toast.LENGTH_SHORT).show();








    }
    private void comprovar(){

        String urls="https://apps.indoamerica.edu.ec/comprobar.php?username="+cajausuario.getText().toString();
        jrq= new JsonObjectRequest(Request.Method.GET,urls,null,this,this);
        rq.add(jrq);

    }

}
