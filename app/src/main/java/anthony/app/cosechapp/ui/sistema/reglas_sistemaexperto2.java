package anthony.app.cosechapp.ui.sistema;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import anthony.app.cosechapp.R;

public class reglas_sistemaexperto2 extends Fragment {
    private reglas_sistemaexpertoViewModel mViewModel2;
    Button botonresultado;
    Button botonpasarregla2;
    EditText dato1,dato2,resultado;

    public static reglas_sistemaexperto newInstance() {
        return new reglas_sistemaexperto();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel2 =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(reglas_sistemaexpertoViewModel.class);
        View v=inflater.inflate(R.layout.sistemaexpertoreglas_fragment2, container, false);
        dato1=(EditText) v.findViewById(R.id.campodato1);
        dato2=(EditText) v.findViewById(R.id.campodato2);
        resultado=(EditText) v.findViewById(R.id.resultado);
        botonresultado=(Button) v.findViewById(R.id.verificar);
        botonresultado=(Button) v.findViewById(R.id.verificar);
        botonpasarregla2=(Button) v.findViewById(R.id.botonregla2);
        resultado.setKeyListener(null);

        resultado.setClickable(true);
        botonresultado.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resultado.setText("");
                int a = Integer.parseInt(dato1.getText().toString());
                int b = Integer.parseInt(dato2.getText().toString());
                //obtenemos las variables de los campos y los guardamos en varibles locales
                if(a>b){
                    Toast.makeText(getContext(),a+" es mayor que "+b,Toast.LENGTH_SHORT).show();
                    resultado.setText(a+" es mayor que "+b);
                    //si la sentecia es correcta se ejecuta este bloque

                }else{
                    Toast.makeText(getContext(),a+" no es  mayor que "+b,Toast.LENGTH_SHORT).show();
                    resultado.setText(a+" no es  mayor que "+b);
                    //si la sentecia es falsa se ejecuta este bloque
                }


            }

        });

        resultado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Campo solo lectura. ", Toast.LENGTH_SHORT).show();

            }
        });













        return v ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel2 = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(reglas_sistemaexpertoViewModel.class);
        // TODO: Use the ViewModel
    }

}