package cosechatech.app.cosechapp.ui.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import cosechatech.app.cosechapp.Adapterfumigacion;
import cosechatech.app.cosechapp.Listafumigacion;
import cosechatech.app.cosechapp.R;
import cosechatech.app.cosechapp.databinding.FragmentHomeBinding;
import cosechatech.app.cosechapp.validation.validateToken;

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener , Response.Listener<JSONObject>,Response.ErrorListener{
    Adapterfumigacion miadapter;
    private List<Listafumigacion> milista = new ArrayList<>();
    private boolean isToastShown = false;
    String s = "";
    RequestQueue rq,rq1;
    String id,fecha,hora,fumigacion,encargado,invernadero,tratamiento,privilegio, valor;
    ListView lista;
    JsonRequest jrq;
    Button botonfumigacion;
    ImageView textoermegente;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    String url1,recibido;
    Context context= getContext();
    validateToken validate;
    private Handler handler = new Handler();
    private Runnable runnable;
    public  void recibir (String valor){
        recibido=valor;
    }
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        textoermegente=(ImageView) root.findViewById(R.id.textoemergente);
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        progressBar = root.findViewById(R.id.progressBarslide);
        progressBar.setVisibility(View.VISIBLE);
        TextView navnombre = (TextView) headerView.findViewById(R.id.mostrarnombre);
        TextView navid = (TextView) headerView.findViewById(R.id.idusuario);
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        String url = getResources().getString(R.string.ip)+"selectfumigacionactual";
        url1=getResources().getString(R.string.ip)+"selectusuarios";
        lista = (ListView) root.findViewById(R.id.listatempera);
        lista.setOnItemClickListener(this);
        List<String> names = new ArrayList<String>();
        botonfumigacion=(Button) root.findViewById(R.id.botonlogin);

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


            runnable = new Runnable() {
                @Override
                public void run() {
                    // Hacer la solicitud JSON y actualizar la lista
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST,url,null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            progressBar.setVisibility(View.INVISIBLE);
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
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.INVISIBLE);
                            if (getActivity()!=null) {
                                if (!isToastShown) {
                                    Toast.makeText(getContext(), "Sin datos actuales de fumigación.", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getContext(), "Agrega  una fumigación.", Toast.LENGTH_SHORT).show();
                                    isToastShown = true;
                                }

                            }

                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }
                        @Override
                        public byte[] getBody()  {
                            return validate.getonlyusers().toString().getBytes();
                        }
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();
                            headers.put("Accept", "application/json");
                            validate.llenar();
                            String token = validate.getAccesstoken();
                            if (token != null && !token.isEmpty()) {
                                headers.put("Authorization", "Bearer " +token);
                            }
                            return headers;
                        }
                    };
                    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                    rq = Volley.newRequestQueue(getContext());
                    rq.add(jsonArrayRequest);
                    //*******************************************************************************

                    handler.postDelayed(this, 2000);
                }
            };
            handler.postDelayed(runnable, 1000);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url1,validate.getusuarios(String.valueOf(navid.getText())),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonObject = null;
                        try {




                            if(response.getString("privilegio").equals("usuario")) {
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error  de Conexion", Toast.LENGTH_SHORT).show();
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

        rq= Volley.newRequestQueue(getContext());
        rq.add(jsonRequest);






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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null) {
            validate = new validateToken(context);
        }
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());//Alert dialog cerrar sesión
        alertDialog.setTitle(milista.get(position).getInvernadero());
        alertDialog.setMessage("Está seguro que desea editar la fumigación seleccionada?");
        alertDialog.setIcon( R.drawable.edit);
        alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Bundle datosAEnviar = new Bundle();
                datosAEnviar.putString("id", milista.get(position).getId());
                Fragment fragmento = new editarfumigacion();
                fragmento.setArguments(datosAEnviar);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_menuprincipal, fragmento);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            });
        alertDialog.show();
    }
    private void eliminar(String id){

        String url=getResources().getString(R.string.ip)+"eliminarfumigacion";
         JSONObject data = new JSONObject();
        validate.llenar();
        try {
            data.put("email", validate.getEmail());
            data.put("token", validate.getApptoken());
            data.put("password", validate.getPassword());

            data.put("id",id);


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
        rq1 = Volley.newRequestQueue(getContext());
        rq1.add(jrq);//Envió y recepción de datos

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