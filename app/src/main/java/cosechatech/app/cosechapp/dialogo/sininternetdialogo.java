package cosechatech.app.cosechapp.dialogo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.RequestQueue;

import cosechatech.app.cosechapp.R;


public class sininternetdialogo extends DialogFragment {
    ImageButton btnsalir;
    Activity actividad;
    RequestQueue rq;
ImageView imagen;
    TextView botoncerrar;
String rol="";
    ProgressDialog progressDialog;

public sininternetdialogo(){

}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        return creardialogo();
    }
    public static sininternetdialogo newInstance(String valor) {
        sininternetdialogo fragment = new sininternetdialogo();

        Bundle bundle = new Bundle();
        bundle.putString("estado", valor);//Se obtiene el ID enviado del login
        fragment.setArguments(bundle);

        return fragment;
    }

    private AlertDialog creardialogo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater =getActivity().getLayoutInflater();
        View v=inflater.inflate(R.layout.fragment_sininternet,null);

        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

     //  Bundle mArgs = getArguments();
  //     String valor= getArguments().getString("id");
    //   String url="https://cosecha.tech/cosechaap_api_service/selectusuarios.php?id_usuario="+valor;
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++}



        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        builder.setView(v);


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



}
