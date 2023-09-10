package com.example.pets;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pets.Additionals.Animal;
import com.example.pets.Additionals.MyEatDialog;
import com.example.pets.Additionals.MyPlayDialog;
import com.example.pets.Additionals.NotificationActionReceiver;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import pl.droidsonroids.gif.GifImageView;

public class MainScreen extends AppCompatActivity {

    private TextView petName, energyOutOfHundred, happyOutOfHundred, foodOutOfHundred;
    private ImageView petImage;

    private ImageButton playButton;
    private GifImageView sleepGif;

    private ProgressBar energyBar, happyBar, foodBar;

    private boolean isAnimalSleep;
    public static Animal animal;
    private final int ONSLEEPSADTIME = 10000;
    private final int ONNOTSLEEPSADTIME = 5000;
    private final int ONNOTSLEEPHUNGRYYIME = ONNOTSLEEPSADTIME / 2;

    private File FILE;

    private  SadTask sadTask;

    @NonNull
    private String readFromPetJson() {
        StringBuilder stringBuilder = new StringBuilder();
        if (FILE.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(FILE);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
            } catch (Exception ex) {
                Toast.makeText(this, R.string.error + " " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return stringBuilder.toString();

    }

    private Animal loadPetData() {
        try {
            JSONObject jsonObject = new JSONObject(readFromPetJson());
            Animal animal = new Animal(jsonObject.getString("type"), jsonObject.getInt("image"));
            animal.setPetName(jsonObject.getString("name"));
            animal.setEnergyCounter(jsonObject.getInt("energy"));
            animal.setHappinessCounter(jsonObject.getInt("happy"));
            animal.setFoodCounter(jsonObject.getInt("food"));

            petName.setText(animal.getPetName());
            petImage.setImageResource(animal.getPetImageId());
            energyBar.setProgress(animal.getEnergyCounter());
            happyBar.setProgress(animal.getHappinessCounter());
            energyOutOfHundred.setText(animal.getEnergyCounter() + getString(R.string.outOfHundred));
            happyOutOfHundred.setText(animal.getHappinessCounter() + getString(R.string.outOfHundred));
            sleepGif = findViewById(R.id.sleepGif);


            return animal;
        } catch (Exception ex) {
            Toast.makeText(this, R.string.error + " " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    private void initializingComponents() {
        petImage = findViewById(R.id.petImageView);
        petName = findViewById(R.id.petNameView);
        energyBar = findViewById(R.id.energyBar);
        happyBar = findViewById(R.id.happyBar);
        foodBar = findViewById(R.id.foodBar);

        foodOutOfHundred = findViewById(R.id.foodOutFromHundred);
        happyOutOfHundred = findViewById(R.id.happyOutFromHundred);
        energyOutOfHundred = findViewById(R.id.energyOutFromHundred);
        playButton = findViewById(R.id.playButton);
    }

    private void updateMetrics(Animal animal) {
        if (animal.getHappinessCounter() < 100) {
            energyBar.setProgress(animal.getEnergyCounter());
            happyBar.setProgress(animal.getHappinessCounter());
            foodBar.setProgress(animal.getFoodCounter());

            energyOutOfHundred.setText(animal.getEnergyCounter() + getString(R.string.outOfHundred));
            happyOutOfHundred.setText(animal.getHappinessCounter() + getString(R.string.outOfHundred));
            foodOutOfHundred.setText(animal.getFoodCounter() + getString(R.string.outOfHundred));
        } else {
            happyBar.setProgress(100);
            foodBar.setProgress(100);
            energyBar.setProgress(animal.getEnergyCounter());

            energyOutOfHundred.setText(animal.getEnergyCounter() + getString(R.string.outOfHundred));
            happyOutOfHundred.setText(100 + getString(R.string.outOfHundred));
            foodOutOfHundred.setText(100 + getString(R.string.outOfHundred));
        }

    }

    private void startButtonClickAnimation(View view) {
        view.setAlpha(0.7f);
        new CountDownTimer(50, 50) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                view.setAlpha(1f);
            }
        }.start();
    }

    private class SleepTask extends AsyncTask<Void, Animal, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            long startTime = System.currentTimeMillis();
            while (animal.getEnergyCounter() < 100) {
                long currentTime = System.currentTimeMillis();
                if (isAnimalSleep) {
                    if (currentTime - startTime >= 1000) {
                        animal.sleep();
                        publishProgress(animal);
                        startTime = currentTime;
                    }
                } else break;
            }
            wakeUp();
            return null;
        }

        @Override
        protected void onProgressUpdate(Animal... values) {
            super.onProgressUpdate(values);
            updateMetrics(values[0]);
        }
    }

    private void saveMetricsInJson(){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("type", animal.getPetType());
            jsonObject.put("name", animal.getPetName());
            jsonObject.put("image", animal.getPetImageId());
            jsonObject.put("energy", animal.getEnergyCounter());
            jsonObject.put("happy", animal.getHappinessCounter());

            FileWriter fileWriter = new FileWriter(FILE);
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
            fileWriter.close();
        }
        catch (Exception ex){
            Toast.makeText(this, R.string.error + " " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private class SadTask extends AsyncTask<Void, Animal, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            long sadStartTime = System.currentTimeMillis();
            long hungryStartTime = System.currentTimeMillis();
            while (animal.getEnergyCounter() != 0 || animal.getHappinessCounter() != 0 || animal.getFoodCounter() != 0) {
                try {
                    long elapsedTime = 0;
                    long currentTime = System.currentTimeMillis();
                    if (isAnimalSleep) {
                        if (currentTime - sadStartTime >= ONSLEEPSADTIME) {
                            animal.sad();
                            updateMetrics(animal);
                            sadStartTime = currentTime;
                        }

                        int ONSLEEPHUNGRYYIME = ONSLEEPSADTIME / 2;
                        if (currentTime - hungryStartTime >= ONSLEEPHUNGRYYIME) {
                            animal.hungry();
                            updateMetrics(animal);
                            hungryStartTime = currentTime;
                        }
                    } else {
                        if (currentTime - sadStartTime >= ONNOTSLEEPSADTIME) {
                            animal.sad();
                            updateMetrics(animal);
                            sadStartTime = currentTime;
                        }
                        if (currentTime - hungryStartTime >= ONNOTSLEEPHUNGRYYIME) {
                            animal.hungry();
                            updateMetrics(animal);
                            hungryStartTime = currentTime;
                        }

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Animal... values) {
            super.onProgressUpdate(values);
            updateMetrics(values[0]);
        }

        @Override
        protected void onCancelled(Void unused) {
            super.onCancelled(unused);
            return;
        }
    }

    private void wakeUp() {
        isAnimalSleep = false;
        sleepGif.setVisibility(View.VISIBLE);
    }

    private void showPlayDialog() {
        FragmentManager manager = getSupportFragmentManager();
        DialogFragment myDialogFragment = new MyPlayDialog(this);

        FragmentTransaction transaction = manager.beginTransaction();
        myDialogFragment.show(transaction, "Во что поиграть с животным?");
    }

    private void showEatDialog() {
        FragmentManager manager = getSupportFragmentManager();
        DialogFragment myDialogFragment = new MyEatDialog(this);

        FragmentTransaction transaction = manager.beginTransaction();
        myDialogFragment.show(transaction, "Что дать животному поесть?");
    }

    private void startBackgroundCheck() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long lastEnergyNotificationTime = 0, lastHappyNotificationTime = 0;
                while (animal.getEnergyCounter() > 0 && animal.getHappinessCounter() > 0 && animal.getFoodCounter() > 0) {
                    long currentTime = System.currentTimeMillis();
                    if (animal.getEnergyCounter() == 30 || animal.getEnergyCounter() <= 20 || animal.getEnergyCounter() == 10) {
                        if (currentTime - lastEnergyNotificationTime >= 10000) { // проверка на прошедшие 10 секунд
                            showNotification(
                                    "Животное устало!",
                                    animal.getPetName() + " устал(а). Позволите её отдохнуть и восстановить силы. " +
                                            "Осталось " + animal.getEnergyCounter(),
                                    true,
                                    "Sleep"
                            );
                            lastEnergyNotificationTime = currentTime; // сохранение времени последней отправки уведомления
                        }
                        continue;
                    }
                    if (animal.getHappinessCounter() == 30 || animal.getHappinessCounter() <= 20 || animal.getHappinessCounter() == 10) {
                        if (currentTime - lastHappyNotificationTime >= 10000) { // проверка на прошедшие 10 секунд
                            showNotification(
                                    "Животному скучно!",
                                    animal.getPetName() + " скучно. Поиграйте с ним (нею)!" +
                                            "Осталось " + animal.getHappinessCounter(),
                                    true,
                                    "Play"
                            );
                            lastHappyNotificationTime = currentTime; // сохранение времени последней отправки уведомления
                        }
                    }
                    if (animal.getFoodCounter() == 30 || animal.getFoodCounter() <= 20 || animal.getFoodCounter() == 10) {
                        if (currentTime - lastHappyNotificationTime >= 10000) { // проверка на прошедшие 10 секунд
                            showNotification(
                                    "Животное голодает!",
                                    animal.getPetName() + " голодает. Покорми его (её)!" +
                                            "Осталось " + animal.getFoodCounter(),
                                    true,
                                    "Eating"
                            );
                            lastHappyNotificationTime = currentTime; // сохранение времени последней отправки уведомления
                        }
                    }

                }

                if (animal.getEnergyCounter() <= 0 || animal.getHappinessCounter() <= 0 || animal.getFoodCounter() <= 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(playButton.getContext(), "Ты ужасный человек!\n ***Неожиданно произносит твое животное, после чего исчезает из твоей жизни навсегда...***", Toast.LENGTH_SHORT).show();
                        }
                    });

                    File file = new File(getFilesDir(), "pet.json");
                    file.delete();
                    showNotification("Животное ушло!", "Вы ужасный хозяин. Животное ушло от вас!", false, "");
                    sadTask.cancel(true);
                    System.exit(0);
                    return;
                }
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        FILE = new File(getFilesDir(), "pet.json");
        initializingComponents();
        animal = loadPetData();

        sadTask = new SadTask();
        sadTask.execute();
        updateMetrics(animal);

        startBackgroundCheck();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("My Channel Description");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

        saveMetricsInJson();
    }

    @Override
    protected void onStop() {
        super.onStop();

        saveMetricsInJson();
    }

    public void onSleepButtonClick(View view) {
        startButtonClickAnimation(view);
        if (!isAnimalSleep) {
            isAnimalSleep = true;
            sleepGif.setVisibility(View.INVISIBLE);
            new Thread(() -> {
                SleepTask sleepTask = new SleepTask();
                sleepTask.doInBackground();
            }).start();
            return;
        }
        Toast.makeText(this, "Сейчас животное итак спит!", Toast.LENGTH_SHORT).show();
    }

    public void onWakeUpButtonClick(View view) {
        startButtonClickAnimation(view);
        if (isAnimalSleep) {
            wakeUp();
            return;
        }
        Toast.makeText(this, "Сейчас животное не спит!", Toast.LENGTH_SHORT).show();
    }

    public void onPlayButtonClick(View view) {
        startButtonClickAnimation(view);
        if (!isAnimalSleep) {
            showPlayDialog();
            updateMetrics(animal);
            return;
        }
        Toast.makeText(this, "Сейчас животное спит!", Toast.LENGTH_SHORT).show();
    }

    public void onEatButtonClick(View view) {
        startButtonClickAnimation(view);
        if (!isAnimalSleep) {
            showEatDialog();
            updateMetrics(animal);
            return;
        }
        Toast.makeText(this, "Сейчас животное спит!", Toast.LENGTH_SHORT).show();
    }

    public void showNotification(String title, String message, boolean withButtons, String doing) {
        // Создание намерения для запуска активности по нажатию на уведомление
        Intent intent = new Intent(getApplication(), MainScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Создание уведомления
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle(title)
                .setContentText(message)
                .setChannelId("1")
                .setSmallIcon(R.drawable.dog_and_cat)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        int notificationId = 0;
        if (withButtons) {
            switch (doing) {
                case "Eating":
                    Intent eatingIntent = new Intent(this, NotificationActionReceiver.class);
                    eatingIntent.setAction("Eating");
                    PendingIntent eatingPendingIntent = PendingIntent.getBroadcast(this, 0, eatingIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    notificationId = 2;
                    builder.addAction(R.drawable.food_button, "Дать покушать", eatingPendingIntent);
                    break;
                case "Play":
                    Intent playingIntent = new Intent(this, NotificationActionReceiver.class);
                    playingIntent.setAction("Play");
                    PendingIntent playingPendingIntent = PendingIntent.getBroadcast(this, 0, playingIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    notificationId = 1;
                    builder.addAction(R.drawable.food_button, "Поиграть с животным", playingPendingIntent);
                    break;
                case "Sleep":
                    Intent sleepIntent = new Intent(this, NotificationActionReceiver.class);
                    sleepIntent.setAction("Sleep");
                    PendingIntent sleepPendingIntent = PendingIntent.getBroadcast(this, 0, sleepIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    builder.addAction(R.drawable.food_button, "Вырубить животное", sleepPendingIntent);
                    break;
            }
        }

        // Отображение уведомления
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(notificationId, builder.build());
    }
}