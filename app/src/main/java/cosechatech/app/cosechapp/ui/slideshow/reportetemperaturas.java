package cosechatech.app.cosechapp.ui.slideshow;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cosechatech.app.cosechapp.Adaptertemperaturas;
import cosechatech.app.cosechapp.Listtemperaturas;
import cosechatech.app.cosechapp.R;
import cosechatech.app.cosechapp.databinding.FragmentSlideshowBinding;
import cosechatech.app.cosechapp.validation.validateToken;

public class reportetemperaturas extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    Adaptertemperaturas miadapter;
    private List<Listtemperaturas> milista = new ArrayList<Listtemperaturas>();
    String s = "";
    validateToken validate;
    Context context= getActivity();
    String temperaturaaire,humedadaire,humedad,fecha,hora;
    ListView listatempera;
    Button botonregresar,reportetemperatura;
    String url ="selecttemperaturas";
    ProgressBar progressBar;
    RequestQueue rq;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v;
        v=inflater.inflate(R.layout.reportetemperaturas, container, false);
        TextView linkTextView = v.findViewById(R.id.botonlogin);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
        listatempera = (ListView) v.findViewById(R.id.listatempera);
        botonregresar=(Button) v.findViewById(R.id.regresar);
        reportetemperatura=(Button) v.findViewById(R.id.botonlogin);
        progressBar = v.findViewById(R.id.progressbartemp);
        progressBar.setVisibility(View.VISIBLE);
        List<String> names = new ArrayList<String>();
        /*final TextView textView = binding.textSlideshow;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.POST, getResources().getString(R.string.ip)+url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar.setVisibility(View.INVISIBLE);

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
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
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
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        rq = Volley.newRequestQueue(getContext());
        rq.add(jsonRequest);
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
        reportetemperatura.setOnClickListener(new View.OnClickListener() {//Método para darle función al botón

            @Override
            public void onClick(View v) {
                String url = getResources().getString(R.string.descarga)+"reportedatosplanta";
                final ProgressDialog progressDialog = new ProgressDialog(getContext(),R.style.MyAlertDialogStyle);
                progressDialog.setTitle("Descargando...");
                progressDialog.setMessage("Espere mientras se completa la descarga...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setTitle("Reporte de las temperaturas");
                request.setDescription("Descargando...");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.allowScanningByMediaScanner();
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Reporte temperaturas.xlsx");
                // agregar la cabecera
                validate.llenar();
                String token = validate.getAccesstoken();
                request.addRequestHeader("Authorization","Bearer "+token);
                request.setVisibleInDownloadsUi(true);
                DownloadManager downloadManager = (DownloadManager) ContextCompat.getSystemService(getContext(), DownloadManager.class);
                downloadManager.enqueue(request);

                // registrar un BroadcastReceiver para recibir actualizaciones sobre la descarga
                BroadcastReceiver onComplete = new BroadcastReceiver() {
                    public void onReceive(Context ctxt, Intent intent) {
                        // Eliminar el BroadcastReceiver
                        getContext().unregisterReceiver(this);
                        progressDialog.dismiss();
                    }
                };
                getContext().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


            }
        });


        return v;
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