package com.example.pets.Additionals;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;

import com.example.pets.MainScreen;

public class NotificationActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action != null){
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            switch (action){
                case "Eating":
                    MainScreen.animal.eat("Corm");
                    notificationManagerCompat.cancel(2);
                    break;
                case "Play":
                    MainScreen.animal.play("Hand");
                    notificationManagerCompat.cancel(1);
                    break;
                case "Sleep":
                    for(int i = 0; i < 10; i++)
                        MainScreen.animal.sleep();
                    notificationManagerCompat.cancel(0);
                    break;
            }
        }
    }
}
