package anthony.app.cosechapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class Adapterreportefumigacion extends ArrayAdapter<Listreportefumigacion> {
    private List<Listreportefumigacion> milista;
    private Context mcontext;
    private int recurso;

    public Adapterreportefumigacion(@NonNull Context context, int resource, List<Listreportefumigacion> objects) {
        super(context, resource, objects);
        this.milista = objects;
        this.mcontext = context;
        this.recurso = resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view==null)
            view= LayoutInflater.from(mcontext).inflate(R.layout.lista_items_fumigacionreporte,null);
        int pisicion=position+1;
        Listreportefumigacion Listafumigacion =milista.get(position);
        TextView encargado =view.findViewById(R.id.hora);
         encargado.setText(Listafumigacion.getEncargado());
        TextView tratamiento =view.findViewById(R.id.fecha);
        tratamiento.setText(Listafumigacion.getTratamiento());
        TextView invernadero =view.findViewById(R.id.humidity);
        invernadero.setText(Listafumigacion.getInvernadero());
        TextView fecha =view.findViewById(R.id.temperatureamb);
        fecha.setText(Listafumigacion.getFecha());
        TextView hora =view.findViewById(R.id.humidityamb);
        hora.setText(Listafumigacion.getHora());

        return view;
    }
}