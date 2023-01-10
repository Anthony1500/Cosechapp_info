package anthony.app.cosechapp.ui.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import anthony.app.cosechapp.Adapterfumigacion;
import anthony.app.cosechapp.Listafumigacion;
import anthony.app.cosechapp.R;
import anthony.app.cosechapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener , Response.Listener<JSONObject>,Response.ErrorListener{
    Adapterfumigacion miadapter;
    private List<Listafumigacion> milista = new ArrayList<>();

    String s = "";
    RequestQueue rq;
    String id,fecha,hora,fumigacion,encargado,invernadero,tratamiento,privilegio, valor;
    ListView lista;
    JsonRequest jrq;
    Button botonfumigacion;
    ImageView textoermegente;
    ProgressDialog progressDialog;
    String url1,recibido;
    private Handler handler = new Handler();
    private Runnable runnable;
    public  void recibir (String valor){
        recibido=valor;
    }
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        textoermegente=(ImageView) root.findViewById(R.id.textoemergente);
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navnombre = (TextView) headerView.findViewById(R.id.mostrarnombre);
        TextView navid = (TextView) headerView.findViewById(R.id.idusuario);
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        String url = getResources().getString(R.string.ip)+"selectfumigacionactual.php";
        url1=getResources().getString(R.string.ip)+"selectusuarios.php?id_usuario="+navid.getText();
        lista = (ListView) root.findViewById(R.id.listatempera);
        lista.setOnItemClickListener(this);
        List<String> names = new ArrayList<String>();
        botonfumigacion=(Button) root.findViewById(R.id.reportetemp);

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


            runnable = new Runnable() {
                @Override
                public void run() {
                    // Hacer la solicitud JSON y actualizar la lista
                    JsonArrayRequest jsonArrayrequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            JSONObject jsonObject = null;
                            HashSet<Integer> added = new HashSet<>();
                            milista.clear();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    jsonObject = response.getJSONObject(i);
                                    id = jsonObject.getString("id");
                                    if (!added.contains(id)) {
                                        added.add(Integer.valueOf(id));
                                        fecha = jsonObject.getString("fecha");
                                        hora = jsonObject.getString("hora");
                                        encargado = jsonObject.getString("encargado");
                                        invernadero = jsonObject.getString("invernadero");
                                        tratamiento = jsonObject.getString("tratamiento");
                                        fumigacion = "Fumigación ";


                                    if (getActivity() != null) {

                                        milista.add(new Listafumigacion(fumigacion, "Hora:   " + hora, "Fecha: " + fecha, id, "Encargado: " + encargado, "Invernadero: " + invernadero, "Tratamiento: " + tratamiento));
                                        miadapter = new Adapterfumigacion(getContext(), R.layout.lista_items_fumigacion, milista);
                                        miadapter.notifyDataSetChanged();

                                        lista.setAdapter(miadapter);
                                    }
                                }
                                } catch (JSONException e) {
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (getActivity()!=null) {
                               // Toast.makeText(getContext(), "Sin datos actuales de fumigación.", Toast.LENGTH_SHORT).show();
                               // Toast.makeText(getContext(), "Agrega  una fumigación.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    );
                    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                    rq = Volley.newRequestQueue(getContext());
                    rq.add(jsonArrayrequest);
                    //*******************************************************************************

                    handler.postDelayed(this, 2000);
                }
            };
            handler.postDelayed(runnable, 1000);



        JsonArrayRequest jsonArrayrequest1=new JsonArrayRequest(url1, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {

                    try {

                        jsonObject = response.getJSONObject(i);



                        if(jsonObject.get("privilegio").equals("usuario")) {
                            textoermegente.setImageResource(R.drawable.bloqueado);
                            botonfumigacion.setEnabled(false);
                            textoermegente.setOnClickListener(new View.OnClickListener() {//Método para darle función al botón
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());//Alert dialog cerrar sesión
                                    alertDialog.setTitle("Importante.");
                                    alertDialog.setMessage("Tu rol actual te permite: visualizar fumigaciones");
                                    alertDialog.setIcon(R.drawable.importante);
                                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    alertDialog.show();
                                }
                            });
                        }else{
                            textoermegente.setImageResource(R.drawable.desbloqueado);
                            textoermegente.setOnClickListener(new View.OnClickListener() {//Método para darle función al botón
                                @Override
                                public void onClick(View v) {

                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());//Alert dialog cerrar sesión
                                    alertDialog.setTitle("Importante.");
                                    alertDialog.setMessage("Tu rol actual te permite: visualizar, crear, editar, eliminar fumigaciones");
                                    alertDialog.setIcon(R.drawable.importante);
                                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    alertDialog.show();
                                }
                            });
                        }


                    } catch (JSONException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error de Conexión", Toast.LENGTH_SHORT).show();
            }
        }
        );
        rq= Volley.newRequestQueue(getContext());
        rq.add(jsonArrayrequest1);







        //***********************************************************************************
        botonfumigacion.setOnClickListener(new View.OnClickListener() {//Método para darle función al botón
            @Override
            public void onClick(View v) {
                   Fragment nuevoFragmento = new agregarfumigacion();
                   FragmentTransaction transaction = getFragmentManager().beginTransaction();
                   transaction.replace(R.id.nav_host_fragment_content_menuprincipal, nuevoFragmento);
                   transaction.addToBackStack(null);

                // Commit a la transacción
                 transaction.commit();
            }
        });
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id)
            {
                int pisicion=pos+1;
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());//Alert dialog cerrar sesión
                alertDialog.setTitle("Fumigación: #"+pisicion);
                alertDialog.setMessage("Está seguro que desea eliminar la fumigación seleccionada?");
                alertDialog.setIcon( R.drawable.borrar);
                alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        progressDialog = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);
                        progressDialog.setMessage("Por favor espera...");//Método del Progress Dialog
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        eliminar(milista.get(pos).getId());
                    }
                });
                alertDialog.show();
                return true;
            }
        });
        //   textView.setText(s);
        //**********************************************************************************************

        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        handler.removeCallbacks(runnable);
        runnable = null;
        miadapter = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle datosAEnviar = new Bundle();
        datosAEnviar.putString("id",milista.get(position).getId());
        Fragment fragmento = new editarfumigacion();
        fragmento.setArguments(datosAEnviar);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_menuprincipal, fragmento);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void eliminar(String id){

        String url=getResources().getString(R.string.ip)+"eliminarfumigacion.php?id="+id;
        jrq= new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rq.add(jrq);//Envió y recepción de datos
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.dismiss();
        Toast.makeText(getContext(),"Error al eliminar el ítem selecionado",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onResponse(JSONObject response) {
        Fragment fragmento = new HomeFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_menuprincipal, fragmento);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Toast.makeText(getContext(),"ítem selecionado eliminado correctamente",Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }
}