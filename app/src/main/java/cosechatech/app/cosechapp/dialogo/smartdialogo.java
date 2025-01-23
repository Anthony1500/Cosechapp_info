package cosechatech.app.cosechapp.dialogo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cosechatech.app.cosechapp.R;
import cosechatech.app.cosechapp.validation.validateToken;


public class smartdialogo extends DialogFragment {
    Context context= getActivity();
    validateToken validate;
    LottieAnimationView btnsalir,imagen;
    Activity actividad;
    RequestQueue rq;

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
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 500));


        View v=inflater.inflate(R.layout.fragment_smartdialogo,layout);


        imagen =   v.findViewById(R.id.imagenrol);
        btnsalir = v.findViewById(R.id.btnSalir);

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

       Bundle mArgs = getArguments();
       String valor= getArguments().getString("id");
       String url=getResources().getString(R.string.ip)+"selectusuarios";

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++}
        btnsalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url,validate.getusuarios(valor),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonObject = null;
                        try {
                             if(response.getString("privilegio").equals("desarrollador"))
                                if (getActivity()!=null)
                                    imagen.setAnimation(R.raw.farmer);
                                    imagen.playAnimation();

                            if(response.getString("privilegio").equals("admin"))
                                if (getActivity()!=null)
                                    imagen.setAnimation(R.raw.farmer);
                                    imagen.playAnimation();
                            if(response.getString("privilegio").equals("basico"))
                                if (getActivity()!=null)
                                    imagen.setAnimation(R.raw.closebutton);
                                    imagen.playAnimation();//IF que determina en base al privilegio el gif correspondiente
                            if(response.getString("privilegio").equals("dueño"))
                                if (getActivity()!=null)
                                    imagen.setAnimation(R.raw.farmer);
                                    imagen.playAnimation();
                            if(response.getString("privilegio").equals("tecnico"))
                                if (getActivity()!=null)
                                    imagen.setAnimation(R.raw.farmer);
                                    imagen.playAnimation();

                        } catch (JSONException e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error  de Conexion", Toast.LENGTH_SHORT).show();
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



        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        builder.setView(v);

        btnsalir=  v.findViewById(R.id.btnSalir);
        return builder.create();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null) {
            validate = new validateToken(context);
        }

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
