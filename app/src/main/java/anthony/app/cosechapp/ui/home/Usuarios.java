package anthony.app.cosechapp.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import anthony.app.cosechapp.ListAdapter;
import anthony.app.cosechapp.Listausu;
import anthony.app.cosechapp.R;
import anthony.app.cosechapp.databinding.FragmentHomeBinding;

public class Usuarios extends Fragment implements AdapterView.OnItemClickListener {
    ListAdapter miadapter;
    private List<Listausu> milista = new ArrayList<>();
    String s = "";
    RequestQueue rq;
    ListView lista;
    String valor,nombreusuario;
    String url = "https://cosecha.tech/cosechaap_api_service/selectusuarios2.php";
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        View v;
        v=inflater.inflate(R.layout.usuarios, container, false);

        lista = (ListView) v.findViewById(R.id.lista_usuarios);
        lista.setOnItemClickListener(this);
        List<String> names = new ArrayList<String>();


        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        JsonArrayRequest jsonArrayrequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        String id_usuario = jsonObject.getString("id_usuario");
                        nombreusuario = jsonObject.getString("username");
                        String rolusuario = jsonObject.getString("privilegio");
                        if (getActivity()!=null) {
                            milista.add(new Listausu(nombreusuario, "Rol: " + rolusuario, R.drawable.sinfoto, id_usuario));
                            miadapter = new ListAdapter(getContext(), R.layout.lista_items, milista);
                            lista.setAdapter(miadapter);
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
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        rq = Volley.newRequestQueue(getContext());
        rq.add(jsonArrayrequest);

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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}