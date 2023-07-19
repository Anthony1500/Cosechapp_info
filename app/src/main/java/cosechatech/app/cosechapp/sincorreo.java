package cosechatech.app.cosechapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cosechatech.app.cosechapp.validation.validateToken;

public class sincorreo extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {

    Button botonatras;
    Button botonconprovar;
    Button botonenviar;
    RequestQueue rq;//Definimos variables a utilizar
    EditText  cajausuario;
    private TextView textVie;
    String id_usuario;
    String email;
    EditText mensaje;
    Context context = this;
    validateToken validate = new validateToken(context);
    JsonRequest jrq;
    ProgressDialog progressDialog;
    String url ="selectusuarios2.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sincorreo);
        cajausuario=(EditText) findViewById(R.id.usuario);
        rq = Volley.newRequestQueue(sincorreo.this);
        botonatras=(Button) findViewById(R.id.atras);
        botonconprovar=(Button) findViewById(R.id.reportetemp);//Instanciamos las variables del XML a variables locales.
       // botonenviar=(Button) findViewById(R.id.botonenviarcorreo);
        botonatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sincorreo.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//Envió hacia otro Activity
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(sincorreo.this,"Volvió al inicio de sesión." ,Toast.LENGTH_SHORT).show();
            }
        });
        botonconprovar.setOnClickListener(new View.OnClickListener() {//Método para darle función al botón

            @Override
            public void onClick(View v) {
                String caja1 = cajausuario.getText().toString();
                if(!caja1.isEmpty() )
                {
                progressDialog = new ProgressDialog(sincorreo.this, R.style.MyAlertDialogStyle);
                progressDialog.setMessage("Por favor espera...");
                progressDialog.setCancelable(false);//Método del Progress Dialog
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
    public void onErrorResponse(VolleyError error) {//Respuesta fallida
        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(),"El usuario ingresado es incorrecto",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onResponse(JSONObject response) {//Respuesta correcta
        progressDialog.dismiss();

        try {
            JSONObject json = response.getJSONObject("user");
            id_usuario =json.optString("id_usuario");//Obtención del id

        } catch (JSONException e) {
            e.printStackTrace();
        }
       Intent intent = new Intent(sincorreo.this, sincorreo2.class);
        intent.putExtra("id", id_usuario);//Envió del id hacia otro Activity
        startActivity(intent);
        Toast.makeText(sincorreo.this,"El usuario ingresado es correcto ",Toast.LENGTH_SHORT).show();
    }

    private void comprovar(){
        String url=getResources().getString(R.string.ip)+"comprobaruser";
        JSONObject data = new JSONObject();
        validate.llenar();
        try {
            data.put("email", validate.getEmail());
            data.put("token", validate.getApptoken());
            data.put("password", validate.getPassword());

            data.put("username", cajausuario.getText().toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        jrq= new JsonObjectRequest(Request.Method.POST,url,data,this,this){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept","application/json");
                headers.put("Authorization", "Bearer "+validate.getAccesstoken());
                return headers;
            }
        };
        rq.add(jrq);//Envió y recepción de datos
    }
}
