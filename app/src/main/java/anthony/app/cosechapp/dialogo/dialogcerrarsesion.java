package anthony.app.cosechapp.dialogo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import anthony.app.cosechapp.R;

public class dialogcerrarsesion extends DialogFragment {

    Activity actividad;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        return creardialogo();
    }

    public static dialogcerrarsesion newInstance(String valor) {
        dialogcerrarsesion fragment = new dialogcerrarsesion();


            Bundle bundle = new Bundle();
            bundle.putString("id", valor);
            fragment.setArguments(bundle);



        return fragment;
    }
    private AlertDialog creardialogo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View v=inflater.inflate(R.layout.fragment_cerrarsesion,null);

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
}
