package com.example.pets.Additionals;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.pets.MainScreen;
import com.example.pets.R;

public class MyPlayDialog extends DialogFragment {

    private Context context;
    public MyPlayDialog(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("Во что поиграть с " + MainScreen.animal.getPetName())
                .setMessage("Давайте поиграем с котом!")
                .setIcon(R.drawable.play_with_cat)
                .setPositiveButton("Погладить +10/-10", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Закрываем диалоговое окно
                        MainScreen.animal.play("Hand");
                        dialog.cancel();
                    }
                }).setNeutralButton("Поиграть мячиком +20/-20", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Закрываем диалоговое окно
                    MainScreen.animal.play("Ball");
                    dialog.cancel();
                }
        });;
        return builder.create();
    }
}
