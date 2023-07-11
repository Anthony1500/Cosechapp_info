package anthony.app.cosechapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import anthony.app.cosechapp.databinding.ActivityMenuprincipalBinding;
import anthony.app.cosechapp.dialogo.sininternetdialogo;
import anthony.app.cosechapp.dialogo.smartdialogo;
import anthony.app.cosechapp.validation.validateToken;

public class menuprincipal extends AppCompatActivity {


    TextView version;

    RequestQueue rq;
    JsonRequest jrq;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuprincipalBinding binding;
    Button botoncerrarsecion;
    private Timer timer;

    String valor;

    Autoupdater autoupdater = new Autoupdater(this);
    sininternetdialogo dialogFragment = new sininternetdialogo();
    Context context = this;
    validateToken validate = new validateToken(context);


    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        Bundle bundle = getIntent().getExtras();
        valor= getIntent().getStringExtra("id");
        String url=getResources().getString(R.string.ip)+"selectusuarios";
        //validate.llenar();

        //HomeFragment v= new HomeFragment();
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        super.onCreate(savedInstanceState);

        binding = ActivityMenuprincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       setSupportActionBar(binding.appBarMenuprincipal.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mContext = this;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
               R.id.nav_menu_bienvenida,R.id.nav_usuarios, R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_diagnosticar, R.id.nav_calendario)
                .setDrawerLayout(drawer)
                .build();//Función de navegación entre las diferentes pantallas
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menuprincipal);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        botoncerrarsecion=(Button) findViewById(R.id.cerrraesecion);
        version=(TextView) findViewById(R.id.version);
       ImageView infodevelop=(ImageView) findViewById(R.id.infodesarrollador);
       //mostrar la version en el menu
        PackageInfo pckginfo = null;
        try {
            pckginfo = getApplication().getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
       version.setText("v"+pckginfo.versionName);
       //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        NavigationView finalNavigationView = navigationView;

        User user = new User();
        datos datos = new datos();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView navnombre = (TextView) headerView.findViewById(R.id.mostrarnombre);
        TextView navid = (TextView) headerView.findViewById(R.id.idusuario);

//********************************************************************************************




        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url,validate.getusuarios(valor),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                           try {
                                if(response.getString("privilegio").equals("usuario")){
                                    finalNavigationView.getMenu().findItem(R.id.nav_usuarios).setVisible(false);//Mostrar la diferentes funcionalidades en base al privilegio del usuario
                                }
                                if(response.getString("privilegio").equals("admin")) {
                                    finalNavigationView.getMenu().findItem(R.id.nav_usuarios).setVisible(false);
                                }
                               navid.setText(response.getString("id_usuario"));
                               navnombre.setText(Html.fromHtml(response.getString("username")));

                            } catch (JSONException e) {
                                Toast.makeText(menuprincipal.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(menuprincipal.this, "Error de Conexión", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer "+validate.getAccesstoken());
                return headers;
            }
        };

        rq= Volley.newRequestQueue(menuprincipal.this);
        rq.add(jsonRequest);




       //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        Bundle args = new Bundle();
        args.putString("id", valor);
        smartdialogo newFragment = new smartdialogo();
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "id");



      //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        botoncerrarsecion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(menuprincipal.this);//Alert dialog cerrar sesión
                alertDialog.setTitle("Cerrar Sesión...");
                alertDialog.setMessage("Está seguro que desea cerrar sesión?");
                alertDialog.setIcon(R.drawable.cerrar_sesion);
                alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        logout();
                        validate.borrar();
                        Intent intent = new Intent(menuprincipal.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(menuprincipal.this,"Se cerro sesión correctamente." ,Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.show();



            }

        });
//******************************************************************************************************
        infodevelop.setOnClickListener(new View.OnClickListener() {//Método para darle función al botón
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(menuprincipal.this);//Alert dialog cerrar sesión
                alertDialog.setTitle("Ing. Ciencias de la Computación.");
                alertDialog.setMessage("Nombre: Anthony Arteaga").setMessage("Desarrollador de la aplicación móvil.");
                alertDialog.setIcon(R.drawable.anthony);
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menuprincipal);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void checkInternetConnection() {
        // Compruebe si el dispositivo está conectado a Internet.

        ConnectivityManager cm = (ConnectivityManager)getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
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
            if (!dialogFragment.isAdded()) {
                // Muestre el diálogo.
                dialogFragment.show(getSupportFragmentManager(), "estado");

            }
        }








    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    // Inicie el timer.
    @Override
    protected void onResume() {
        super.onResume();
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    checkInternetConnection();
                }
            }, 0, 5000);// Ejecute el método cada 5 segundos.
        }
    }
    private void logout() {
        String url=getResources().getString(R.string.ip)+"logout";
        JSONObject data = new JSONObject();
        validate.llenar();
        try {
            data.put("email", validate.getEmail());
            data.put("token", validate.getApptoken());
            data.put("password", validate.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jrq= new JsonObjectRequest(Request.Method.POST,url,data,null,null){
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
