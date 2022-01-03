package anthony.app.cosechapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import anthony.app.cosechapp.databinding.ActivityMenuprincipalBinding;
import anthony.app.cosechapp.dialogo.smartdialogo;

public class menuprincipal extends AppCompatActivity {


    TextView someText;

    RequestQueue rq;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuprincipalBinding binding;
    Button botoncerrarsecion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        Bundle bundle = getIntent().getExtras();
        String valor= getIntent().getStringExtra("id");
        String url="https://apps.indoamerica.edu.ec/selectusuarios.php?id_usuario="+valor;
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        super.onCreate(savedInstanceState);

        binding = ActivityMenuprincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       setSupportActionBar(binding.appBarMenuprincipal.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
               R.id.nav_menu_bienvenida, R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_diagnosticar, R.id.nav_calendario)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menuprincipal);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        botoncerrarsecion=(Button) findViewById(R.id.cerrraesecion);


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
                Intent intent = new Intent(menuprincipal.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(menuprincipal.this,"Se cerro sesión correctamente." ,Toast.LENGTH_SHORT).show();

            }

        });


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView navnombre = (TextView) headerView.findViewById(R.id.mostrarnombre);
        TextView navcorreo = (TextView) headerView.findViewById(R.id.mostrarcorreo);



        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        navnombre.setText(Html.fromHtml(jsonObject.getString("username")));

                        navcorreo.setText(jsonObject.getString("email"));


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
        rq= Volley.newRequestQueue(this);
        rq.add(jsonArrayRequest);


    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menuprincipal);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public void buscarUsuarios(String URL){


    }
}