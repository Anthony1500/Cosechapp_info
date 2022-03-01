package anthony.app.cosechapp.dialogo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import anthony.app.cosechapp.R;


public class smartdialogo extends DialogFragment {
    ImageButton btnsalir;
    Activity actividad;
    RequestQueue rq;
ImageView imagen;
    TextView botoncerrar;
String rol="";
    ProgressDialog progressDialog;
    Handler handler = new Handler();
public smartdialogo (){

}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        return creardialogo();
    }
    public static smartdialogo newInstance(String valor) {
        smartdialogo fragment = new smartdialogo();

        Bundle bundle = new Bundle();
        bundle.putString("id", valor);//Se obtiene el ID enviado del login
        fragment.setArguments(bundle);

        return fragment;
    }

    private AlertDialog creardialogo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View v=inflater.inflate(R.layout.fragment_smartdialogo,null);
        TextView nombre = (TextView) v.findViewById(R.id.textonombre);
        TextView privilegio = (TextView) v.findViewById(R.id.textorol);
        imagen = (ImageView) v.findViewById(R.id.imagenrol);
        btnsalir= (ImageButton) v.findViewById(R.id.btnSalir);
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

       Bundle mArgs = getArguments();
       String valor= getArguments().getString("id");
       String url="https://apps.indoamerica.edu.ec/selectusuarios.php?id_usuario="+valor;
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++}
        btnsalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {

                    try {
                        jsonObject = response.getJSONObject(i);
                        nombre.setText(Html.fromHtml(jsonObject.getString("username").toUpperCase()));
                        privilegio.setText(Html.fromHtml(jsonObject.getString("privilegio").toUpperCase()));
                        rol= String.valueOf(Html.fromHtml(jsonObject.getString("privilegio")));//Llenado en los diferentes campos en base a la consulta realizada

                        if(jsonObject.get("privilegio").equals("desarrollador"))
                            Glide.with(getActivity()).load(R.drawable.desarrollador).into(imagen);
                        if(jsonObject.get("privilegio").equals("admin"))
                            Glide.with(getActivity()).load(R.drawable.administrador).into(imagen);
                        if(jsonObject.get("privilegio").equals("usuario"))
                            Glide.with(getActivity()).load(R.drawable.usuario).into(imagen);//IF que determina en base al privilegio el gif correspondiente
                        if(jsonObject.get("privilegio").equals("dueño"))
                            Glide.with(getActivity()).load(R.drawable.dueno).into(imagen);
                        if(jsonObject.get("privilegio").equals("tecnico"))
                            Glide.with(getActivity()).load(R.drawable.tecnico).into(imagen);
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error de Conexión", Toast.LENGTH_SHORT).show();
            }
        }
        );
        rq= Volley.newRequestQueue(getContext());
        rq.add(jsonArrayRequest);



        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        builder.setView(v);

        btnsalir=  v.findViewById(R.id.btnSalir);
        return builder.create();

    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof Activity){
            this.actividad=(Activity) context;

        }else{
            throw new RuntimeException(context.toString()+
                    "must Implement OnFragmentInteractionListener");
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public void setProgressDialog() {

        int llPadding = 20;
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        ll.setLayoutParams(llParam);

        ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setIndeterminate(false);

        progressBar.setPadding(0, 0, llPadding, 0);
        progressBar.setLayoutParams(llParam);

        progressBar.setDrawingCacheBackgroundColor(Color.rgb(248,99,0));


        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        TextView tvText = new TextView(getContext());
        tvText.setText("Cargando datos...");
        tvText.setTextColor(Color.parseColor("#000000"));
        tvText.setTextSize(20);
        tvText.setLayoutParams(llParam);

        ll.addView(progressBar);
        ll.addView(tvText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setView(ll);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        Window window = dialog.getWindow();

        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);

            handler.postDelayed(new Runnable() {
                public void run() {
                    dialog.dismiss();


                }
            }, 5000);


        }

    }


}
