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

public class MyEatDialog extends DialogFragment {

    private Context context;
    public MyEatDialog(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("Чем покормить? " + MainScreen.animal.getPetName())
                .setMessage("Корми животинку!")
                .setIcon(R.drawable.play_with_cat)
                .setPositiveButton("Корм +10/-5", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Закрываем диалоговое окно
                        MainScreen.animal.eat("Corm");
                        dialog.cancel();
                    }
                }).setNeutralButton("Мясо +20/-10", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Закрываем диалоговое окно
                        MainScreen.animal.eat("Meat");
                        dialog.cancel();
                    }
                });;
        return builder.create();
    }
}
