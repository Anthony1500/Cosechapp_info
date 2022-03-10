package anthony.app.cosechapp.ui.home;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

import anthony.app.cosechapp.Adapterfumigacion;
import anthony.app.cosechapp.Listafumigacion;
import anthony.app.cosechapp.R;
import anthony.app.cosechapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {
    Adapterfumigacion miadapter;
    private List<Listafumigacion> milista = new ArrayList<>();
    String s = "";
    RequestQueue rq;
    String id,fecha,hora,fumigacion;
    ListView lista;
    Button botonfumigacion;
    String url = "https://apps.indoamerica.edu.ec/selectfumigacion.php";
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        lista = (ListView) root.findViewById(R.id.listafumigacion);
        lista.setOnItemClickListener(this);
        List<String> names = new ArrayList<String>();
        botonfumigacion=(Button) root.findViewById(R.id.programarfumigacion);
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

            JsonArrayRequest jsonArrayrequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {


                    JSONObject jsonObject = null;

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);
                            id = jsonObject.getString("id");
                            fecha = jsonObject.getString("fecha");
                            hora = jsonObject.getString("hora");
                            fumigacion = "Fumigación ";
                            milista.add(new Listafumigacion(fumigacion, "Hora:  " + hora, "Fecha: " + fecha, "Id: " + id));
                            miadapter = new Adapterfumigacion(getContext(), R.layout.lista_items_fumigacion, milista);
                            lista.setAdapter(miadapter);

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
        botonfumigacion.setOnClickListener(new View.OnClickListener() {//Método para darle función al botón

            @Override
            public void onClick(View v) {

            }
        });
        //   textView.setText(s);
        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}