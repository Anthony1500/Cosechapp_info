package anthony.app.cosechapp.validation;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import anthony.app.cosechapp.DatabaseHelper;
import anthony.app.cosechapp.User;
import anthony.app.cosechapp.datos;

public class validateToken extends DatabaseHelper {
    private  String accesstoken,apptoken,email,password;
    ArrayList<datos> datosvalidacion ;

    User user = new User();
    Context context;

    public validateToken(Context context) {
        super(context);
        this.context = context;
    }


    public JSONObject getusuarios(String valor) {

         llenar();
         datos datos = new datos();
        JSONObject  data = new JSONObject();
        try {
            data.put("id_usuario",valor);
            data.put("email",getEmail());
            data.put("token",getApptoken());
            data.put("password",getPassword());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
    public JSONObject getfumigacion(String valor) {

        llenar();
        datos datos = new datos();
        JSONObject  data = new JSONObject();
        try {
            data.put("id",valor);
            data.put("email",getEmail());
            data.put("token",getApptoken());
            data.put("password",getPassword());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
    public JSONObject getonlyusers() {
        llenar();
        JSONObject  data = new JSONObject();
        try {
            data.put("email",getEmail());
            data.put("token",getApptoken());
            data.put("password",getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
    public JSONObject getcomprobar(String comprobar) {
        llenar();
        JSONObject  data = new JSONObject();
        try {
            data.put("username",comprobar);
            data.put("email",getEmail());
            data.put("token",getApptoken());
            data.put("password",getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
    public void borrar(){
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
    public void llenar(){
        llenardatos();
        List<anthony.app.cosechapp.datos> dataList = null;
        dataList = llenardatos();
        for (datos data : dataList) {
            String id = data.getId();
            setAccesstoken(data.getAccesstoken());
            setApptoken(data.getApptoken());
            setEmail(data.getEmail());
            setPassword(data.getPassword());
            // Haz algo con los datos aquí
        }
    }

public List<datos> llenardatos()  {
    DatabaseHelper databaseHelper = new DatabaseHelper(context);
    SQLiteDatabase db = databaseHelper.getWritableDatabase();

    Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    List<datos> data = new ArrayList<>();
    if (cursor.moveToFirst()) {
        do {
            datos datos = new datos();
            datos.setId(cursor.getString(0));
            datos.setAccesstoken(cursor.getString(1));
            datos.setApptoken(cursor.getString(2));
            datos.setEmail(cursor.getString(3));
            datos.setPassword(cursor.getString(4));
            data.add(datos);
            // Haz algo con los datos aquí
        } while (cursor.moveToNext());
    }
    cursor.close();
     return data;
    }
    public void insertuser(String accesstoken, String apptoken, String email, String password){
        try {
            DatabaseHelper databaseHelper = new DatabaseHelper(context.getApplicationContext());
            databaseHelper.addData(accesstoken,apptoken,email,password);
        } catch (Exception e){
            e.toString();
        }

    }

    public String getAccesstoken() {
        llenar();
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public String getApptoken() {
        return apptoken;
    }

    public void setApptoken(String apptoken) {
        this.apptoken = apptoken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}



