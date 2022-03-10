package anthony.app.cosechapp.ui.sistema;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import anthony.app.cosechapp.R;

public class menu_bienvenida  extends Fragment {
    private reglas_sistemaexpertoViewModel mViewModel2;
    Button botonresultado;
    Button botonpasarregla2;
    EditText dato1,dato2,resultado;
    Button botoninfo;
    public static menu_bienvenida newInstance() {
        return new menu_bienvenida();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel2 =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(reglas_sistemaexpertoViewModel.class);
        View v=inflater.inflate(R.layout.menu_bienvenida, container, false);



















        return v ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel2 = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(reglas_sistemaexpertoViewModel.class);
        // TODO: Use the ViewModel
    }

}