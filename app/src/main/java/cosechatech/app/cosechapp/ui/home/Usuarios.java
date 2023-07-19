package cosechatech.app.cosechapp.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cosechatech.app.cosechapp.ListAdapter;
import cosechatech.app.cosechapp.Listausu;
import cosechatech.app.cosechapp.R;
import cosechatech.app.cosechapp.databinding.FragmentHomeBinding;
import cosechatech.app.cosechapp.validation.validateToken;

public class Usuarios extends Fragment implements AdapterView.OnItemClickListener {
    ListAdapter miadapter;
    validateToken validate;
    Context context= getActivity();
    RequestQueue rq;
    private List<Listausu> milista = new ArrayList<>();
    String s = "";

    JsonRequest jrq;
    ListView lista;
    String valor,nombreusuario;

     ProgressBar progressBar ;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View v;
        v=inflater.inflate(R.layout.usuarios, container, false);
        String url =getResources().getString(R.string.ip);
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        lista = (ListView) v.findViewById(R.id.lista_usuarios);
        lista.setOnItemClickListener(this);
        List<String> names = new ArrayList<String>();

//********************************************************************************************

        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.POST, url+"selectusuarios2", null,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        JSONObject jsonObject = null;
                        for (int i = 0; i < response.length(); i++) {

                            try {
                                jsonObject =response.getJSONObject(i);
                                String id_usuario = jsonObject.getString("id_usuario");
                                nombreusuario = jsonObject.getString("username");
                                String rolusuario = jsonObject.getString("privilegio");
                                if (getActivity() != null) {
                                    milista.add(new Listausu(nombreusuario, "Rol: " + rolusuario, R.drawable.sinfoto, id_usuario));
                                    miadapter = new ListAdapter(getContext(), R.layout.lista_items, milista);
                                    lista.setAdapter(miadapter);
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "Error de Conexión", Toast.LENGTH_SHORT).show();
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
        rq= Volley.newRequestQueue(getContext());
        rq.add(jsonRequest);
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //   textView.setText(s);


        return v;

    }
    @Override
    public void onItemClick(AdapterView<?> Adapterview, View view, int position, long id) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());//Alert dialog cerrar sesión
        alertDialog.setTitle("Usuario: "+milista.get(position).getNombreusu());
        alertDialog.setMessage("Está seguro que desea editar el rol del usuario seleccionado?");
        alertDialog.setIcon( R.drawable.sinfoto);
        alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Bundle datosAEnviar = new Bundle();
                datosAEnviar.putString("nombre", milista.get(position).getNombreusu());
                datosAEnviar.putInt("imagen", milista.get(position).getImage());
                datosAEnviar.putString("id_usuario", milista.get(position).getId_usuario());
                Fragment fragmento = new editar_usuarios();
                fragmento.setArguments(datosAEnviar);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction =  getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_menuprincipal, fragmento);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                Toast.makeText(getContext(),"Pantalla de edición de rol." ,Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
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
    }
}