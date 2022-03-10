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
import android.widget.Toast;

import anthony.app.cosechapp.R;

public class reglas_sistemaexperto extends Fragment {
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
        View v=inflater.inflate(R.layout.sistemaexpertoreglas_fragment, container, false);
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

                String caja1 = dato1.getText().toString();
                String caja2 = dato2.getText().toString();


                if(!caja1.isEmpty() &&  !caja2.isEmpty() )
                {
                    resultado.setText("");
                    int a = Integer.parseInt(dato1.getText().toString());
                    int b = Integer.parseInt(dato2.getText().toString());

                    if(a==b){
                        Toast.makeText(getContext(),"Son iguales.",Toast.LENGTH_SHORT).show();
                        resultado.setText(" Son iguales.");

                    }else{
                        Toast.makeText(getContext(),"No son iguales.",Toast.LENGTH_SHORT).show();
                        resultado.setText(" No son iguales.");
                    }


                }
                else{
                    dato1.setError("Favor de ingresar algo");
                    dato2.setError("Favor de ingresar algo");

                }


            }

        });

        resultado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Campo solo lectura. ", Toast.LENGTH_SHORT).show();

            }
        });




        botonpasarregla2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Fragment nuevoFragmento = new sistema_experto_regla();
               // FragmentTransaction transaction = getFragmentManager().beginTransaction();
              //  transaction.replace(R.id.nav_host_fragment_content_menuprincipal, nuevoFragmento);
              //  transaction.addToBackStack(null);

                // Commit a la transacción
              //  transaction.commit();
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