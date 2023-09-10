package com.example.pets;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pets.Additionals.Animal;
import com.example.pets.Additionals.petCardAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChooseAnimal extends AppCompatActivity {

    private RecyclerView petRecycleView;
    private petCardAdapter petCardAdapter;
    private List<Animal> dataForRecycleView = new ArrayList<>();
    public void addItem(Animal animal) {
        dataForRecycleView.add(animal);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_animal);

        petRecycleView = findViewById(R.id.recyclerView);
        petRecycleView.setLayoutManager(new LinearLayoutManager(this));

        addItem(new Animal("Кошка", R.drawable.catico));
        addItem(new Animal("Собака", R.drawable.dog_and_cat));
        addItem(new Animal("Лягушечка", R.drawable.frog));
        addItem(new Animal("Черепашка", R.drawable.tortilla));

        petCardAdapter = new petCardAdapter(dataForRecycleView);
        petRecycleView.setAdapter(petCardAdapter);
    }
}
