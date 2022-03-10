package anthony.app.cosechapp;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class Adapterfumigacion extends ArrayAdapter<Listafumigacion> {
    private List<Listafumigacion> milista;
    private Context mcontext;
    private int  recurso;
    public Adapterfumigacion(@NonNull Context context, int resource, List<Listafumigacion> objects) {
        super(context, resource, objects);
        this.milista=objects;
        this.mcontext=context;
        this.recurso=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view==null)
            view= LayoutInflater.from(mcontext).inflate(R.layout.lista_items_fumigacion,null);
        int pisicion=position+1;
        Listafumigacion Listafumigacion =milista.get(position);

        TextView fumigacion =view.findViewById(R.id.fumigacion);
        fumigacion.setText(Listafumigacion.getFumigacion()+pisicion);
        TextView fecha =view.findViewById(R.id.fecha);
        fecha.setText(Listafumigacion.getFecha());
        TextView hora =view.findViewById(R.id.hora);
        hora.setText(Listafumigacion.getHora());
        TextView id =view.findViewById(R.id.id_fumigacion);
        id.setText(Listafumigacion.getId());
        return view;
    }
}