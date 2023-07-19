package cosechatech.app.cosechapp.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cosechatech.app.cosechapp.Listausu;
import cosechatech.app.cosechapp.R;
import cosechatech.app.cosechapp.databinding.FragmentHomeBinding;
import cosechatech.app.cosechapp.validation.validateToken;

public class editar_usuarios extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener{

    private List<Listausu> milista = new ArrayList<>();
   private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    Context context= getActivity();
    validateToken validate;
    String privilegio,id_usuario;
    RequestQueue rq,rqs;
    JsonRequest jrq;
    Spinner spinner;
    ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Bundle datosRecuperados = getArguments();
        String valor= datosRecuperados.getString("nombre");
        int imagen= datosRecuperados.getInt("imagen");
        id_usuario= datosRecuperados.getString("id_usuario");
        String url=getResources().getString(R.string.ip)+"comprobar";
        View v;
        v=inflater.inflate(R.layout.editar_usuarios, container, false);
        rq = Volley.newRequestQueue(getContext());
        TextView nombre = (TextView) v.findViewById(R.id.textousuario);
        ImageView usuario = (ImageView) v.findViewById(R.id.usuarioimagen);
       spinner = (Spinner) v.findViewById(R.id.spinner);
        Button  atrasrol =(Button) v.findViewById(R.id.atrasrol);
        Button  guardarrol =(Button) v.findViewById(R.id.guardarrol);
        String valores[] = getResources().getStringArray(R.array.meinv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.simple_spinner_item,valores);
        // Establece el estilo que se mostrará al momento que se desplega
        adapter.setDropDownViewResource(R.layout.my_drop_down_item);
        spinner.setAdapter(adapter);
        nombre.setText(valor);
        Glide.with(getActivity()).load(imagen).into(usuario);
        //******************************************************************************************
        atrasrol.setOnClickListener(new View.OnClickListener() {//Método para darle función al botón

            @Override
            public void onClick(View v) {

                Fragment fragmento = new Usuarios();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction =  getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_content_menuprincipal, fragmento);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        //******************************************************************************************
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url,validate.getcomprobar(valor),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            privilegio= response.getString("privilegio");
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error de Conexión", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer "+validate.getAccesstoken());
                return headers;
            }
        };

        rq= Volley.newRequestQueue(getContext());
        rq.add(jsonRequest);
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        guardarrol.setOnClickListener(new View.OnClickListener() {//Método para darle función al botón

            @Override
            public void onClick(View v) {



                String caja1 = spinner.getSelectedItem().toString();
                if(caja1.equals("-------")){
                    Toast.makeText(getContext(), "Seleccione un rol para guardar.", Toast.LENGTH_SHORT).show();
                }else {
                    if (caja1.equals(privilegio)) {
                        Toast.makeText(getContext(), "No se puede guardar si el rol es igual al anterior", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);

                        progressDialog.setMessage("Por favor espera...");
                        progressDialog.setCancelable(false);//Método del Progress Dialog
                        progressDialog.show();
                        comprovar();


                    }
                }
            }
        });
        return v;

    }






    @Override
    public void onResponse(JSONObject response) {
        progressDialog.dismiss();
        Toast.makeText(getContext(), "Se guardo Correctamente", Toast.LENGTH_SHORT).show();
        Fragment fragmento = new Usuarios();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_menuprincipal, fragmento);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.dismiss();
        Toast.makeText(getContext(), "No se  pudo guardar", Toast.LENGTH_SHORT).show();

    }
    private void comprovar(){
// String url=getResources().getString(R.string.ip)+"/agregarfumigacion?fecha="+tfecha.getText().toString()+"&hora="+thora.getText().toString()+"&invernadero="+invernadero.getText().toString()+"&tratamiento="+tratamiento.getText().toString()+"&encargado="+encargado.getText().toString();
        String url=getResources().getString(R.string.ip)+"actualizar";
        JSONObject data = new JSONObject();
        validate.llenar();
        try {
            data.put("email", validate.getEmail());
            data.put("token", validate.getApptoken());
            data.put("password", validate.getPassword());

            data.put("privilegio",spinner.getSelectedItem().toString());
            data.put("id_usuario",id_usuario);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        jrq= new JsonObjectRequest(Request.Method.PUT,url,data,this,this){
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
