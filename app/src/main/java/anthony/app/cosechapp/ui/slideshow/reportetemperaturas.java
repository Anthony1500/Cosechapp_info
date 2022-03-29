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

import anthony.app.cosechapp.Adaptertemperaturas;
import anthony.app.cosechapp.Listtemperaturas;
import anthony.app.cosechapp.R;
import anthony.app.cosechapp.databinding.FragmentSlideshowBinding;

public class reportetemperaturas extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    Adaptertemperaturas miadapter;
    private List<Listtemperaturas> milista = new ArrayList<Listtemperaturas>();
    String s = "";

    String temperaturaaire,humedadaire,humedad,fecha,hora;
    ListView listatempera;
    Button botonregresar;
    String url = "https://apps.indoamerica.edu.ec/selectsensado.php";

    RequestQueue rq;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(SlideshowViewModel.class);

        View v;
        v=inflater.inflate(R.layout.reportetemperaturas, container, false);
        TextView linkTextView = v.findViewById(R.id.reportetemp);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
        listatempera = (ListView) v.findViewById(R.id.listatempera);
        botonregresar=(Button) v.findViewById(R.id.regresar);
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
                        temperaturaaire=jsonObject.getString("temperatureamb");
                        humedadaire=jsonObject.getString("humidityamb");
                        humedad=jsonObject.getString("humidity");

                        if (getActivity()!=null) {
                            milista.add(new Listtemperaturas("Temperatura Aire: " + temperaturaaire, "Humedad Aire:   " + humedadaire, "Humedad: " + humedad, "Fecha: " + fecha, "Hora: " + hora));
                            miadapter = new Adaptertemperaturas(getContext(), R.layout.lista_items_temperaturareporte, milista);
                            listatempera.setAdapter(miadapter);
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
        botonregresar.setOnClickListener(new View.OnClickListener() {//Método para darle función al botón

            @Override
            public void onClick(View v) {
                Fragment nuevoFragmento = new SlideshowFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment_content_menuprincipal, nuevoFragmento);
                transaction.addToBackStack(null);

                // Commit a la transacción
                transaction.commit();
            }
        });
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}