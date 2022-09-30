package anthony.app.cosechapp.ui.home;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import anthony.app.cosechapp.R;

public class editarfumigacion extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener{

    Button bfecha,bhora,agregarfumigacion,atrasfumigacion;
    EditText tfecha,thora,invernadero,tratamiento,encargado;
    int dia,mes,anio,hora,minuto;
    String id,horaedi,fechaedi,encargadoedi,invernaderoedi,tratamientoedi;
    ProgressDialog progressDialog;
    RequestQueue rq,rqs;
    JsonRequest jrq;
    private editarfumigacionViewModel editarfumigacionl;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        editarfumigacionl =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(editarfumigacionViewModel.class);

        Bundle datosRecuperados = getArguments();
        id= datosRecuperados.getString("id");
        Toast.makeText(getContext(), "Pantalla edición de fumigación.", Toast.LENGTH_SHORT).show();
        String urls="https://cosecha.tech/cosechaap_api_service/selectfumigacioneditar.php?id="+id;
        View v;
        v=inflater.inflate(R.layout.editarfumigacion, container, false);
        bfecha=(Button) v.findViewById(R.id.bfechaeditar);
        bhora=(Button) v.findViewById(R.id.bhoraeditar);
        agregarfumigacion=(Button) v.findViewById(R.id.guardareditarfumigacion);
        atrasfumigacion=(Button) v.findViewById(R.id.atrasfumigacioneditar);
        tfecha=(EditText) v.findViewById(R.id.fechafumigacioneditar);
        thora=(EditText) v.findViewById(R.id.horafumigacioneditar);
        invernadero=(EditText) v.findViewById(R.id.editarinvernadero);
        tratamiento=(EditText) v.findViewById(R.id.fumi_tratamientoeditar);
        encargado=(EditText) v.findViewById(R.id.encargadofumieditar);

        rqs = Volley.newRequestQueue(getContext());

        //******************************************************************************************

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(urls, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        fechaedi = jsonObject.getString("fecha");
                        tfecha.setText(fechaedi);
                        horaedi = jsonObject.getString("hora");
                        thora.setText(horaedi);
                        encargadoedi=jsonObject.getString("encargado");
                        encargado.setText(encargadoedi);
                        invernaderoedi=jsonObject.getString("invernadero");
                        invernadero.setText(invernaderoedi);
                        tratamientoedi=jsonObject.getString("tratamiento");
                        tratamiento.setText(tratamientoedi);
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError errore) {
                Toast.makeText(getContext(), "Error de Conexión", Toast.LENGTH_SHORT).show();
            }
        }
        );
        rq= Volley.newRequestQueue(getContext());
        rq.add(jsonArrayRequest);
        //****************************************************************************************

        //****************************************************************************************

        tfecha.setKeyListener(null);
        thora.setKeyListener(null);
        thora.setClickable(true);
        tfecha.setClickable(true);

        tfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Campo solo lectura. ", Toast.LENGTH_SHORT).show();

            }
        });
        thora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Campo solo lectura. ", Toast.LENGTH_SHORT).show();

            }
        });
        //*******************************************************************************


        bfecha.setOnClickListener(new View.OnClickListener() {//Método para darle función al botón

            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance(Locale.getDefault());

                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                anio = c.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if(month<10)
                            tfecha.setText(year+"-"+0+(month+1)+"-"+dayOfMonth);
                        if (dayOfMonth<10)
                            tfecha.setText(year+"-"+(month+1)+"-"+0+dayOfMonth);
                        if(month<10 && dayOfMonth<10)
                            tfecha.setText(year+"-"+0+(month+1)+"-"+0+dayOfMonth);
                        if(month>9 && dayOfMonth>9)
                            tfecha.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                    }
                }, anio,mes,dia);
                datePickerDialog.show();
            }

        });

        bhora.setOnClickListener(new View.OnClickListener() {//Método para darle función al botón

            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                hora =   c.get(Calendar.HOUR_OF_DAY);
                minuto = c.get(Calendar.MINUTE);

                SimpleDateFormat simpDate1;
                simpDate1 = new SimpleDateFormat("HH");
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String hora= "0"+hourOfDay;
                        String minuto= "0"+minute;
                        if(hourOfDay<10 )
                            thora.setText(hora + ":" + minute);
                        if(minute<10 )
                            thora.setText(hourOfDay + ":" + minuto);
                        if(hourOfDay<10 && minute<10)
                            thora.setText(hora + ":" + 0+minute);
                        if(hourOfDay>9 && minute>9)
                            thora.setText(hourOfDay + ":" + minute);

                        // Incrementing call count.

                    }
                },hora,minuto,true);
                timePickerDialog.show();
            }


        });

        //******************************************************************************************

        atrasfumigacion.setOnClickListener(new View.OnClickListener() {//Método para darle función al botón

            @Override
            public void onClick(View v) {
                Fragment fragmento = new HomeFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction =  getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_menuprincipal, fragmento);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }


        });




        //******************************************************************************************

        agregarfumigacion.setOnClickListener(new View.OnClickListener() {//Método para darle función al botón

            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);
                progressDialog.setMessage("Por favor espera...");//Método del Progress Dialog
                progressDialog.setCancelable(false);
                progressDialog.show();
                editar();


            }


        });





        return v;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.dismiss();
        Toast.makeText(getContext(),"Error al ingresar datos",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialog.dismiss();
        Fragment fragmento = new HomeFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_menuprincipal, fragmento);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Toast.makeText(getContext(),"Ingreso de datos correcto",Toast.LENGTH_SHORT).show();


    }


    private void editar(){

        String url="https://cosecha.tech/cosechaap_api_service/editarfumigacion.php?fecha="+tfecha.getText().toString()+"&hora="+thora.getText().toString()+"&invernadero="+invernadero.getText().toString()+"&tratamiento="+tratamiento.getText().toString()+"&encargado="+encargado.getText().toString()+"&id="+id;
        jrq= new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        rqs.add(jrq);//Envió y recepción de datos
    }
}