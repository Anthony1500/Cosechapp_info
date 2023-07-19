package cosechatech.app.cosechapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
       // TextView encargado =view.findViewById(R.id.encargado);
       // encargado.setText(Listafumigacion.getEncargado());
       // TextView tratamiento =view.findViewById(R.id.tratamiento);
      //  tratamiento.setText(Listafumigacion.getTratamiento());
        TextView invernadero =view.findViewById(R.id.humidity);
        invernadero.setText(Listafumigacion.getInvernadero());
        TextView fumigacion =view.findViewById(R.id.fumigacion);
        fumigacion.setText(Listafumigacion.getFumigacion()+pisicion);
        TextView fecha =view.findViewById(R.id.temperatureamb);
        fecha.setText(Listafumigacion.getFecha());
        TextView hora =view.findViewById(R.id.humidityamb);
        hora.setText(Listafumigacion.getHora());
        TextView id =view.findViewById(R.id.id_fumigacion);
        id.setText(Listafumigacion.getId());
        return view;
    }
}