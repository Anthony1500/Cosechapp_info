package anthony.app.cosechapp.ui.sistema;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import anthony.app.cosechapp.R;

public class sistemaexperto extends Fragment {

    private SistemaexpertoViewModel mViewModel;

    public static sistemaexperto newInstance() {
        return new sistemaexperto();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(SistemaexpertoViewModel.class);


        return inflater.inflate(R.layout.sistemaexperto, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(SistemaexpertoViewModel.class);
        // TODO: Use the ViewModel
    }

}