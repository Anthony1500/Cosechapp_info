package anthony.app.cosechapp.ui.home;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.List;

import anthony.app.cosechapp.ListAdapter;
import anthony.app.cosechapp.Listausu;
import anthony.app.cosechapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    ListAdapter miadapter;
    private List<Listausu> milista = new ArrayList<>();
    String s = "";
    RequestQueue rq;
    ListView lista;
    String url = "https://apps.indoamerica.edu.ec/selectusuarios2.php";
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();






        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}