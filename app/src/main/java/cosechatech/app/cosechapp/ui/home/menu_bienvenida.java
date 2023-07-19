package cosechatech.app.cosechapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import cosechatech.app.cosechapp.R;

public class menu_bienvenida extends Fragment {
    private menu_bienbenidaViewModel menu_bienbenidaViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v;
       v=inflater.inflate(R.layout.menu_bienvenida, container, false);
        return v;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
