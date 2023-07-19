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

public class Adaptertemperaturas extends ArrayAdapter<Listtemperaturas> {
    private List<Listtemperaturas> milista;
    private Context mcontext;
    private int recurso;

    public Adaptertemperaturas(@NonNull Context context, int resource, List<Listtemperaturas> objects) {
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
            view= LayoutInflater.from(mcontext).inflate(R.layout.lista_items_temperaturareporte,null);
        int pisicion=position+1;
        Listtemperaturas Listtemperaturas =milista.get(position);
        TextView temperatureamb =view.findViewById(R.id.temperatureamb);
        temperatureamb.setText(Listtemperaturas.getTemperaturaaire());
        TextView humidityamb =view.findViewById(R.id.humidityamb);
        humidityamb.setText(Listtemperaturas.getHumedadaire());
        TextView humidity =view.findViewById(R.id.humidity);
        humidity.setText(Listtemperaturas.getHumedad());
        TextView fecha =view.findViewById(R.id.fecha);
        fecha.setText(Listtemperaturas.getFecha());
        TextView hora =view.findViewById(R.id.hora);
        hora.setText(Listtemperaturas.getHora());

        return view;
    }
}