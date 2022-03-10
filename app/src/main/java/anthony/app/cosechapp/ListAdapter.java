package anthony.app.cosechapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Listausu> {
    private List<Listausu> milista;
    private Context mcontext;
    private int  recurso;
    public ListAdapter(@NonNull Context context, int resource, List<Listausu> objects) {
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
            view= LayoutInflater.from(mcontext).inflate(R.layout.lista_items,null);

        Listausu listausu =milista.get(position);

        ImageView image =view.findViewById(R.id.fumigacion);
        image.setImageResource(listausu.getImage());
        TextView nombreusuario =view.findViewById(R.id.nombreusuario);
        nombreusuario.setText(listausu.getNombreusu());
        TextView rolusuario =view.findViewById(R.id.rolusuario);
        rolusuario.setText(listausu.getRolusu());
        TextView id_usuario =view.findViewById(R.id.id_fumigacion);
        id_usuario.setText(listausu.getId_usuario());
        return view;
    }
}
