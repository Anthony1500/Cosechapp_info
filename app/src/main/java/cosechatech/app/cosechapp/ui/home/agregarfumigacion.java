package cosechatech.app.cosechapp.ui.home;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cosechatech.app.cosechapp.R;
import cosechatech.app.cosechapp.validation.validateToken;

public class agregarfumigacion extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener{

Button bfecha,bhora,agregarfumigacion,atrasfumigacion;
EditText tfecha,thora,invernadero,tratamiento,encargado;

    validateToken validate;
    Context context= getActivity();
int dia,mes,anio,hora,minuto;
    ProgressDialog progressDialog;
    RequestQueue rq;
    JsonRequest jrq;
    private editarfumigacionViewModel editarfumigacionl;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        editarfumigacionl =
                new ViewModelProvider((ViewModelStoreOwner) this, (ViewModelProvider.Factory) new ViewModelProvider.NewInstanceFactory()).get(editarfumigacionViewModel.class);
        View v;
        v=inflater.inflate(R.layout.agregarfumigacion, container, false);
        bfecha=(Button) v.findViewById(R.id.bfechaeditar);
        bhora=(Button) v.findViewById(R.id.bhoraeditar);
        agregarfumigacion=(Button) v.findViewById(R.id.guardareditarfumigacion);
        atrasfumigacion=(Button) v.findViewById(R.id.atrasfumigacioneditar);
        tfecha=(EditText) v.findViewById(R.id.fechafumigacioneditar);
        thora=(EditText) v.findViewById(R.id.horafumigacioneditar);
        invernadero=(EditText) v.findViewById(R.id.editarinvernadero);
        tratamiento=(EditText) v.findViewById(R.id.fumi_tratamientoeditar);
        encargado=(EditText) v.findViewById(R.id.encargadofumieditar);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        rq = Volley.newRequestQueue(getContext());
        //*******************************************************************************
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hora= "0"+hourOfDay;
                        String minuto= "0"+minute;
                        if(hourOfDay<10 ){
                        thora.setText(hora + ":" + minute);}
                        if(minute<10 ){
                        thora.setText(hourOfDay + ":" + minuto);}
                        if(hourOfDay<10 && minute<10){
                        thora.setText(hora + ":" + 0+minute);}
                        if(hourOfDay>9 && minute>9){
                        thora.setText(hourOfDay + ":" + minute);}
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
                            agregar();
                    }
                });
                return v;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null) {
            validate = new validateToken(context);
        }
    }
    @Override
    public void onErrorResponse(VolleyError error) {
       progressDialog.dismiss();

        Toast.makeText(getContext(),"Error en el ingreso de datos",Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getContext(),"Ingreso exitoso",Toast.LENGTH_SHORT).show();

    }


    private void agregar(){

       // String url=getResources().getString(R.string.ip)+"/agregarfumigacion?fecha="+tfecha.getText().toString()+"&hora="+thora.getText().toString()+"&invernadero="+invernadero.getText().toString()+"&tratamiento="+tratamiento.getText().toString()+"&encargado="+encargado.getText().toString();
        String url=getResources().getString(R.string.ip)+"agregarfumigacion";
        JSONObject data = new JSONObject();
        validate.llenar();
        try {
            data.put("email", validate.getEmail());
            data.put("token", validate.getApptoken());
            data.put("password", validate.getPassword());

            data.put("fecha", tfecha.getText().toString());
            data.put("hora", thora.getText().toString());
            data.put("invernadero", invernadero.getText().toString());
            data.put("tratamiento", tratamiento.getText().toString());
            data.put("encargado", encargado.getText().toString());

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
        rq.add(jrq);//Envió y recepción de datos
    }
}