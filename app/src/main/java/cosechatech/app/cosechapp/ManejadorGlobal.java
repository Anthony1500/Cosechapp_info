package cosechatech.app.cosechapp;

import static android.content.ContentValues.TAG;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cosechatech.app.cosechapp.dialogo.NonNull;

public class ManejadorGlobal extends Application {
        public static View snackView;
    private LottieAnimationView imagen;


    public String HoraActual() {
        // Obtener la hora actual
        Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);
        return String.format(new Locale("es", "EC"), "%02d:%02d", hora, minuto);

    }
    public boolean Comprobar(String dActual, String dInicial, String dFinal) throws ParseException {
        Date dateActual = new SimpleDateFormat("HH:mm").parse(dActual.trim());
        Date dateInicial = new SimpleDateFormat("HH:mm").parse(dInicial.trim());
        Date dateFinal = new SimpleDateFormat("HH:mm").parse(dFinal.trim());
        if (dateFinal.before(dateInicial)) { // Nota: Siempre usa nombres de variables descriptivos, nada de a, b, c..
            dateFinal = new Date(dateFinal.getTime() + 24 * 60 * 60 * 1000); // 1 día en milisegundos
        }
        return dateActual.after(dateInicial) && dateActual.before(dateFinal);
    }
    public int MinutoActual() {
        // Obtener la minuto actual
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }
    public View getSnackView() {
        return snackView;
    }

    public static void mostrarSnackbarfailed(String texto, View rootView, Context context) {
        if (rootView != null && context != null) {
            Snackbar snackbar = Snackbar.make(rootView, "", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.RED);
            Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

            LayoutInflater inflater = LayoutInflater.from(context);
            snackView = inflater.inflate(R.layout.push_notification_small, null);
            LottieAnimationView failed = snackView.findViewById(R.id.state_content_animation_failed);
            LottieAnimationView alert = snackView.findViewById(R.id.state_content_animation_alert);
            LottieAnimationView correct = snackView.findViewById(R.id.state_content_animation_correct);
            failed.enableMergePathsForKitKatAndAbove(true);
            alert.setVisibility(View.GONE);
            correct.setVisibility(View.GONE);
            layout.addView(snackView, 0);

            ObjectAnimator animator = ObjectAnimator.ofFloat(snackbarView, "translationX", 0, -20, 0);
            animator.setDuration(2000);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.start();
            snackbar.show();

            // Mueve este código dentro de la condición
            if (snackView != null) {
                TextView textView = snackView.findViewById(R.id.state_content_text);
                failed.setAnimation(R.raw.failed);
                textView.setText(texto);
            }
        }
    }
    public static void mostrarSnackbaralert(String texto, View rootView, Context context) {
        if (rootView != null && context != null) {
            Snackbar snackbar = Snackbar.make(rootView, "", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.rgb(231,247,80));
            Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

            LayoutInflater inflater = LayoutInflater.from(context);
            snackView = inflater.inflate(R.layout.push_notification_small, null);
            LottieAnimationView failed = snackView.findViewById(R.id.state_content_animation_failed);
            LottieAnimationView alert = snackView.findViewById(R.id.state_content_animation_alert);
            LottieAnimationView correct = snackView.findViewById(R.id.state_content_animation_correct);
            alert.enableMergePathsForKitKatAndAbove(true);
            failed.setVisibility(View.GONE);
            correct.setVisibility(View.GONE);
            layout.addView(snackView, 0);

            ObjectAnimator animator = ObjectAnimator.ofFloat(snackbarView, "translationX", 0, -20, 0);
            animator.setDuration(2000);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.start();
            snackbar.show();

            // Mueve este código dentro de la condición
            if (snackView != null) {
                TextView textView = snackView.findViewById(R.id.state_content_text);
                alert.setAnimation(R.raw.alert);
                textView.setText(texto);
            }
        }
    }

    public static void mostrarSnackbarcorrect(String texto, View rootView, Context context) {
        if (rootView != null && context != null) {
            Snackbar snackbar = Snackbar.make(rootView, "", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.GREEN);
            Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

            LayoutInflater inflater = LayoutInflater.from(context);
            snackView = inflater.inflate(R.layout.push_notification_small, null);
            LottieAnimationView failed = snackView.findViewById(R.id.state_content_animation_failed);
            LottieAnimationView alert = snackView.findViewById(R.id.state_content_animation_alert);
            LottieAnimationView correct = snackView.findViewById(R.id.state_content_animation_correct);
            correct.enableMergePathsForKitKatAndAbove(true);
            alert.setVisibility(View.GONE);
            alert.setVisibility(View.GONE);
            layout.addView(snackView, 0);


            snackbar.show();

            // Mueve este código dentro de la condición
            if (snackView != null) {
                TextView textView = snackView.findViewById(R.id.state_content_text);
                correct.setAnimation(R.raw.check);
                textView.setText(texto);
            }
        }
    }

    public void getIp() {
        String token;

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Obtener el token de registro
                        String token = task.getResult();

                        // Enviar el token al servidor Laravel
                        Log.d(TAG, "FCM token: " + token);
                    }
                });

    }







}
















