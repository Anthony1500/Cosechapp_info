package anthony.app.cosechapp.ui.home;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import anthony.app.cosechapp.Listausu;
import anthony.app.cosechapp.R;
import anthony.app.cosechapp.databinding.FragmentHomeBinding;

public class editar_usuarios extends Fragment {

    private List<Listausu> milista = new ArrayList<>();
   private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);
        Bundle datosRecuperados = getArguments();
        String valor= datosRecuperados.getString("nombre");

        View v;
        v=inflater.inflate(R.layout.editar_usuarios, container, false);
        TextView nombre = (TextView) v.findViewById(R.id.textousuario);
        Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
        String[] valores = {"Usuario","Técnico","Administrador"};
        spinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, valores));
         nombre.setText(valor);


        return v;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}