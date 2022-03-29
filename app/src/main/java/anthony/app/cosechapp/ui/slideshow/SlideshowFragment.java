package anthony.app.cosechapp.ui.slideshow;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

import anthony.app.cosechapp.Adapterreportefumigacion;
import anthony.app.cosechapp.Listreportefumigacion;
import anthony.app.cosechapp.R;
import anthony.app.cosechapp.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    Adapterreportefumigacion miadapter;
    private List<Listreportefumigacion> milista = new ArrayList<>();
    String s = "";

    String fecha,hora,encargado,invernadero,tratamiento;
    ListView listafumi;
    Button botonpasaratemperatura;
    String url = "https://apps.indoamerica.edu.ec/selectfumigacion.php";

    RequestQueue rq;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView linkTextView = root.findViewById(R.id.reportetemperatura);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
        listafumi = (ListView) root.findViewById(R.id.listatempera);
        botonpasaratemperatura=(Button) root.findViewById(R.id.reportetemp);
        List<String> names = new ArrayList<String>();
        /*final TextView textView = binding.textSlideshow;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        JsonArrayRequest jsonArrayrequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        fecha = jsonObject.getString("fecha");
                        hora = jsonObject.getString("hora");
                        encargado=jsonObject.getString("encargado");
                        invernadero=jsonObject.getString("invernadero");
                        tratamiento=jsonObject.getString("tratamiento");

                        if (getActivity()!=null) {
                            milista.add(new Listreportefumigacion("Fecha: " + fecha, "Hora:   " + hora, "Invernadero: " + invernadero, "Tratamiento: " + tratamiento, "Encargado: " + encargado));
                            miadapter = new Adapterreportefumigacion(getContext(), R.layout.lista_items_fumigacionreporte, milista);
                            listafumi.setAdapter(miadapter);
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
/*
        GraphView graph = (GraphView) root.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        series.setColor(Color.RED);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(6);
        for (int i = 0; i < 10; i++) {
            series.appendData(new DataPoint(i, 0), true, 20);
        }
        graph.addSeries(series);
*/
        botonpasaratemperatura.setOnClickListener(new View.OnClickListener() {//Método para darle función al botón

            @Override
            public void onClick(View v) {
                Fragment nuevoFragmento = new reportetemperaturas();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_content_menuprincipal, nuevoFragmento);
                transaction.addToBackStack(null);

                // Commit a la transacción
                transaction.commit();
            }
        });







        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}