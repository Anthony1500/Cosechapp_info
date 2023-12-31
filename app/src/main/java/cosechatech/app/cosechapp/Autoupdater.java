package cosechatech.app.cosechapp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Autoupdater extends androidx.core.content.FileProvider {

    /**
     * Objeto contexto para ejecutar el instalador.
     * Se puede buscar otra forma mas "limpia".
     */
    Context context;

    /**
     * Listener que se llamara despues de ejecutar algun AsyncTask.
     */
    Runnable listener;

    /**
     * El enlace al archivo público de información de la versión. Puede ser de
     * Dropbox, un hosting propio o cualquier otro servicio similar.
     */
    private static final String INFO_FILE ="https://cosechapp.cosecha.tech/txt";

    /**
     * El código de versión establecido en el AndroidManifest.xml de la versión
     * instalada de la aplicación. Es el valor numérico que usa Android para
     * diferenciar las versiones.
     */

    private int currentVersionCode;

    /**
     * El nombre de versión establecido en el AndroidManifest.xml de la versión
     * instalada. Es la cadena de texto que se usa para identificar al versión
     * de cara al usuario.
     */
    private String currentVersionName;

    /**
     * El código de versión establecido en el AndroidManifest.xml de la última
     * versión disponible de la aplicación.
     */
    private int latestVersionCode;

    /**
     * El nombre de versión establecido en el AndroidManifest.xml de la última
     * versión disponible.
     */
    private String latestVersionName;

    /**
     * Enlace de descarga directa de la última versión disponible.
     */
    private String downloadURL;

    /**
     * Constructor unico.
     * @param context Contexto sobre el cual se ejecutara el Instalador.
     */
    public Autoupdater(Context context) {
        this.context = context;
    }

    /**
     * Método para inicializar el objeto. Se debe llamar antes que a cualquie
     * otro, y en un hilo propio (o un AsyncTask) para no bloquear al interfaz
     * ya que hace uso de Internet.
     *
     *            El contexto de la aplicación, para obtener la información de
     *            la versión actual.
     */
    private void getData() {
        try{
            // Datos locales
            Log.d("AutoUpdater", "GetData");
            PackageInfo pckginfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            currentVersionCode = pckginfo.versionCode;
            currentVersionName = pckginfo.versionName;

            // Datos remotos
            String data = downloadHttp(new URL(INFO_FILE));
            JSONObject json = new JSONObject(data);
            latestVersionCode = json.getInt("versionCode");
            latestVersionName = json.getString("versionName");
            downloadURL = json.getString("downloadURL");
            Log.d("AutoUpdate", "Datos obtenidos con éxito");
        }catch(JSONException e){
            Log.e("AutoUpdate", "Ha habido un error con el JSON", e);
        }catch(PackageManager.NameNotFoundException e){
            Log.e("AutoUpdate", "Ha habido un error con el packete :S", e);
        }catch(IOException e){
            Log.e("AutoUpdate", "Ha habido un error con la descarga", e);
        }
    }

    /**
     * Método para comparar la versión actual con la última .
     *
     * @return true si hay una versión más nueva disponible que la actual.
     */
    public boolean isNewVersionAvailable() {
        return getLatestVersionCode() > getCurrentVersionCode();
    }

    /**
     * Devuelve el código de versión actual.
     *
     * @return integer con la version actual
     */
    public int getCurrentVersionCode() {
        return currentVersionCode;
    }

    /**
     * Devuelve el nombre de versión actual.
     *
     * @return IDEM
     */
    public String getCurrentVersionName() {
        return currentVersionName;
    }

    /**
     * Devuelve el código de la última versión disponible.
     *
     * @return IDEM
     */
    public int getLatestVersionCode() {
        return latestVersionCode;
    }

    /**
     * Devuelve el nombre de la última versión disponible.
     *
     * @return IDEM
     */
    public String getLatestVersionName() {
        return latestVersionName;
    }

    /**
     * Devuelve el enlace de descarga de la última versión disponible
     *
     * @return string con la URL de descarga
     */
    public String getDownloadURL() {
        return downloadURL;
    }

    /**
     * Método auxiliar usado por getData() para leer el archivo de información.
     * Encargado de conectarse a la red, descargar el archivo y convertirlo a
     * String.
     *
     * @param url
     *            La URL del archivo que se quiere descargar.
     * @return Cadena de texto con el contenido del archivo
     * @throws IOException
     *             Si hay algún problema en la conexión
     */
    private static String downloadHttp(URL url) throws IOException {
        // Codigo de coneccion, Irrelevante al tema.
        HttpURLConnection c = (HttpURLConnection)url.openConnection();
        c.setRequestMethod("GET");
        c.setReadTimeout(15 * 1000);
        c.setUseCaches(false);
        c.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null){
            stringBuilder.append(line + "\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Metodo de interface.
     * Primer metodo a usar. Se encarga de, en un hilo separado,
     * conectarse al servidor y obtener la informacion de la ultima version de la aplicacion.
     * @param OnFinishRunnable Listener ejecutable al finalizar.
     *                         Codigo que se ejecutara una vez terminado el proceso.
     */
    public void DownloadData(Runnable OnFinishRunnable){
        //Guarda el listener.
        this.listener = OnFinishRunnable;
        // Ejecuta tu tarea en segundo plano utilizando un Thread
        new Thread(downloaderData).start();

    }

    /**
     * Metodo de Interface.
     * Segundo Metodo a usar.
     * Se encargara, una vez obtenidos los datos de la version mas reciente, y en un hilo separado,
     * de comprobar que haya efectivamente una version mas reciente, descargarla e instalarla.
     * Preparar la aplicacion para ser cerrada y desinstalada despues de este metodo.
     * @param OnFinishRunnable Codigo que se ejecutara tras llamar al instalador.
     *                         Ultimo en ejecutar.
     */
    public void InstallNewVersion(Runnable OnFinishRunnable){
        if(isNewVersionAvailable()){
            if(getDownloadURL() == "") return;
            listener = OnFinishRunnable;


        }
    }

    /**
     * Objeto de AsyncTask encargado de descargar la informacion del servidor
     * y ejecutar el listener.
     */
    // Crea un Handler para el hilo principal
    Handler handler = new Handler(Looper.getMainLooper());

    // Crea un Runnable para ejecutar tu tarea en segundo plano
    Runnable downloaderData = new Runnable() {
        @Override
        public void run() {
            // Ejecuta el método getData en segundo plano
            getData();

            // Después de completar la tarea en segundo plano, utiliza el Handler para ejecutar el listener en el hilo principal
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (listener != null) listener.run();
                    listener = null;
                }
            });
        }
    };



    /**
     * Objeto de AsyncTask encargado de descargar e instalar la ultima version de la aplicacion.
     * No es cancelable.
     */



}