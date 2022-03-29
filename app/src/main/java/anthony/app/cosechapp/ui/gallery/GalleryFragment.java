package anthony.app.cosechapp.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import anthony.app.cosechapp.R;
import anthony.app.cosechapp.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    RequestQueue rq;
    JsonRequest jrq;
    TextView humedad,temperatura,humedadcultivo;
    Timer timer = new Timer();
    String url="https://apps.indoamerica.edu.ec/selectsensado.php";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        humedad = (TextView) root.findViewById(R.id.datohumedad);
        temperatura = (TextView) root.findViewById(R.id.datotemperatura);
        humedadcultivo = (TextView) root.findViewById(R.id.datohumedadcultivo);
        humedad.setText("cargando..");
        temperatura.setText("cargando..");
        humedadcultivo.setText("cargando..");

        //Creamos el Timer

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                FuncionParaEsteHilo();
            }
        }, 0, 5000);
//******************************************************************************************

 //******************************************************************************************





        //final TextView textView = binding.textGallery;
      //  galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
         //   @Override
         //   public void onChanged(@Nullable String s) {
             //   textView.setText(s);
          //  }
      //  });



        return root;
    }
    private void FuncionParaEsteHilo()
    {
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        humedad.setText(jsonObject.getString("humidityamb"));
                        temperatura.setText(jsonObject.getString("temperatureamb"));
                        humedadcultivo.setText(jsonObject.getString("humidity"));
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError errore) {
                humedad.setText("Sin datos..");
                temperatura.setText("Sin datos..");
                humedadcultivo.setText("Sin datos..");
            }
        }
        );


        rq= Volley.newRequestQueue(getContext());
        rq.add(jsonArrayRequest);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (timer!=null){
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }
}