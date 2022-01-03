package anthony.app.cosechapp.ui.home;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import anthony.app.cosechapp.R;

public class menu_bienvenida extends Fragment {

    private menu_bienbenidaViewModel menu_bienbenidaViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        menu_bienbenidaViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(menu_bienbenidaViewModel.class);
        View v;
       v=inflater.inflate(R.layout.menu_bienvenida, container, false);


        //   textView.setText(s);


        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
