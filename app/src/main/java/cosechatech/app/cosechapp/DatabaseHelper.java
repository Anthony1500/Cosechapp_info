package cosechatech.app.cosechapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "mydatabase.db";
    public static final String TABLE_NAME = "mytable";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_accesstoken = "accesstoken";
    public static final String COLUMN_apptoken = "apptoken";
    public static final String COLUMN_email = "email";
    public static final String COLUMN_password = "password";
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + COLUMN_accesstoken + "   TEXT , "
                + COLUMN_apptoken + "    TEXT , "
                + COLUMN_email + "     TEXT , "
                + COLUMN_password + "    TEXT) ";
        db.execSQL(query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void addData(String accesstoken, String apptoken, String email, String password) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_accesstoken, accesstoken);
        values.put(COLUMN_apptoken, apptoken);
        values.put(COLUMN_email, email);
        values.put(COLUMN_password, password);
        db.insert(TABLE_NAME, null, values);
        db.close();

    }
    public void getData() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                datos datos = new datos();
                datos.setId(cursor.getString(0));
                datos.setAccesstoken(cursor.getString(1));
                datos.setApptoken(cursor.getString(2));
                datos.setEmail(cursor.getString(3));
                datos.setPassword(cursor.getString(4));

                // Haz algo con los datos aquí
            } while (cursor.moveToNext());
        }
        cursor.close();
    }





}
