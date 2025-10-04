package com.example.calendario;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class NotificationUtils {

    /**
     * Agenda uma notificação para a tarefa.
     * @param context Contexto da aplicação.
     * @param title O título da tarefa.
     * @param description A descrição da tarefa.
     * @param isUrgent true se a tarefa for urgente, false caso contrário.
     */
    public static void scheduleNotification(Context context, String title, String description, boolean isUrgent) {
        // Cria um Intent para o nosso BroadcastReceiver
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("taskTitle", title);
        intent.putExtra("taskDescription", description);
        intent.putExtra("isUrgent", isUrgent);

        // Cria um PendingIntent para o AlarmManager
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                (int) System.currentTimeMillis(), // Usa um ID único para cada notificação
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Define o tempo para a notificação
        long delayMillis;
        if (isUrgent) {
            // Agenda uma notificação para 5 minutos no futuro (tarefa urgente)
            delayMillis = 5 * 60 * 1000;
        } else {
            // Agenda uma notificação para 30 minutos no futuro (tarefa normal)
            delayMillis = 30 * 60 * 1000;
        }

        // Agenda o alarme
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + delayMillis,
                    pendingIntent);
        }
    }
}
