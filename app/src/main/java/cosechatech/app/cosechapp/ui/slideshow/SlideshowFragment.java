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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import cosechatech.app.cosechapp.Adapterreportefumigacion;
import cosechatech.app.cosechapp.Listreportefumigacion;
import cosechatech.app.cosechapp.R;
import cosechatech.app.cosechapp.databinding.FragmentSlideshowBinding;
import cosechatech.app.cosechapp.validation.validateToken;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    validateToken validate;
    Context context= getActivity();
    Adapterreportefumigacion miadapter;
    private List<Listreportefumigacion> milista = new ArrayList<>();
    String s = "";
    ProgressBar progressBar;
    String fecha,hora,encargado,invernadero,tratamiento;
    ListView listafumi;
    Button botonpasaratemperatura,reportefumigacion;
    String url ="selectfumigacion";

    RequestQueue rq;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //TextView linkTextView = root.findViewById(R.id.reportetemperatura);
      //  linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
        listafumi = (ListView) root.findViewById(R.id.listatempera);
        botonpasaratemperatura=(Button) root.findViewById(R.id.botonlogin);
        reportefumigacion=(Button) root.findViewById(R.id.reportefumigacion);
        progressBar = root.findViewById(R.id.progressBarslider);
        progressBar.setVisibility(View.VISIBLE);
        List<String> names = new ArrayList<String>();
        /*final TextView textView = binding.textSlideshow;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        JsonArrayRequest jsonArrayrequest = new JsonArrayRequest(Request.Method.POST,getResources().getString(R.string.ip)+url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar.setVisibility(View.INVISIBLE);

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
        reportefumigacion.setOnClickListener(new View.OnClickListener() {//Método para darle función al botón

            @Override
            public void onClick(View v) {



                String url = getResources().getString(R.string.descarga)+"reportefumigacion";

                final ProgressDialog progressDialog = new ProgressDialog(getContext(),R.style.MyAlertDialogStyle);
                progressDialog.setTitle("Descargando...");
                progressDialog.setMessage("Espere mientras se completa la descarga...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setTitle("Reporte de las Fumigaciones");
                request.setDescription("Descargando...");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.allowScanningByMediaScanner();
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Reporte fumigaciones.xlsx");
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
    }

}