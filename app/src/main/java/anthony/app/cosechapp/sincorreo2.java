package anthony.app.cosechapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class sincorreo2 extends AppCompatActivity {
    EditText correo;
    RequestQueue rq;
    Button botonenviar2;
    Session session;
    String correoelectronico="corpcosechapp@gmail.com";
    String contraseña="cosecha123";
    String usuario,contrasñausuario,emailusuario,privilegio;
    ProgressDialog progressDialog;
    Handler handler = new Handler();
    public static int esperar = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        Bundle bundle = getIntent().getExtras();
        String valor = getIntent().getStringExtra("id");
        String url = "https://apps.indoamerica.edu.ec/selectusuarios.php?id_usuario=" + valor;
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sincorreo2);
        botonenviar2 = (Button) findViewById(R.id.botonenviar2);
        correo = (EditText) findViewById(R.id.correoenvio2);
        correo.setKeyListener(null);

        correo.setClickable(true);

        correo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(sincorreo2.this, "Campo solo lectura. ", Toast.LENGTH_SHORT).show();

            }
        });

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {

                        jsonObject = response.getJSONObject(i);
                        correo.setText(jsonObject.getString("email"));
                        usuario = jsonObject.getString("username");
                        contrasñausuario = jsonObject.getString("password");
                        emailusuario = jsonObject.getString("email");
                        privilegio = jsonObject.getString("privilegio");


                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de Conexión", Toast.LENGTH_SHORT).show();
            }
        }
        );

        rq = Volley.newRequestQueue(this);
        rq.add(jsonArrayRequest);

        botonenviar2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Properties properties = new Properties();
                properties.put("mail.smtp.host", "smtp.googlemail.com");
                properties.put("mail.smtp.socketFactory.port", "465");
                properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.port", "465");


                try {
                    session = javax.mail.Session.getDefaultInstance(properties, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(correoelectronico, contraseña);
                        }
                    });



                    if (session != null) {
                        setProgressDialog();
                        javax.mail.Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(correoelectronico));
                        message.setSubject("Consulta de Datos");
                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correo.getText().toString()));
                        message.setContent("<center><h2>Credenciales de Acceso</h2></center><br>" + "<center><img  src=\"https://apps.indoamerica.edu.ec/img/cosecha.png\"></center>" + "<center>" + usuario.toUpperCase() + "<br>" + "</center>" + "<br>" + "Email : "
                                + emailusuario + "<br>" + "Contraseña:" + contrasñausuario + "<br>" + "Privilegio:" + privilegio + "<center><p>Recuerda no compartir esta información. </p></center>", "text/html; charset=utf-8");

                        Transport.send(message);
                     setProgressDialog();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                handler.postDelayed(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(sincorreo2.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Se ha enviado las credenciales al correo.",Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(),"Revisa tu correo.",Toast.LENGTH_SHORT).show();


                    }
                }, 3000);
            }

        });




}
    public void setProgressDialog() {

        int llPadding = 20;
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        ll.setLayoutParams(llParam);

        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(false);

        progressBar.setPadding(0, 0, llPadding, 0);
        progressBar.setLayoutParams(llParam);

        progressBar.setDrawingCacheBackgroundColor(Color.rgb(248,99,0));


        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        TextView tvText = new TextView(this);
        tvText.setText("Enviando correo...");
        tvText.setTextColor(Color.parseColor("#000000"));
        tvText.setTextSize(20);
        tvText.setLayoutParams(llParam);

        ll.addView(progressBar);
        ll.addView(tvText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setView(ll);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();

        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);

            handler.postDelayed(new Runnable() {
                public void run() {
                    dialog.dismiss();


                }
            }, 5000);


        }

    }



}
