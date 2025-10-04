package com.example.calendario;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "task_reminder_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Extrai os dados da tarefa do Intent
        String title = intent.getStringExtra("taskTitle");
        String description = intent.getStringExtra("taskDescription");
        boolean isUrgent = intent.getBooleanExtra("isUrgent", false);

        // Cria o canal de notificação (obrigatório a partir do Android 8.0)
        createNotificationChannel(context);

        // Cria a notificação
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo) // Use seu próprio ícone aqui
                .setContentTitle("Lembrete: " + title)
                .setContentText(description)
                .setPriority(isUrgent ? NotificationCompat.PRIORITY_HIGH : NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true); // Faz com que a notificação desapareça ao ser clicada

        // Exibe a notificação
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }

    private void createNotificationChannel(Context context) {
        // A partir do Android 8.0 (Oreo), é necessário criar um canal de notificação
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Lembretes de Tarefas";
            String description = "Canal para lembretes de tarefas agendadas.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
