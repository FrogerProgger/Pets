package com.example.pets.Additionals;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pets.MainScreen;
import com.example.pets.R;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class petCardAdapter extends RecyclerView.Adapter<petCardAdapter.petCardHolder> {

    private static List<Animal> mData;

    public petCardAdapter(List<Animal> data){
        mData = data;
    }

    @NonNull
    @Override
    public petCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_card, parent, false);
        return new petCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull petCardHolder holder, int position){
        Animal animal = mData.get(position);
        holder.bind(animal);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class petCardHolder extends RecyclerView.ViewHolder {
        private final TextView petTypeView;
        private final ImageView petImage;
        private final Button createPet;
        private final EditText petNameInput;

        public void bind(Animal animal){
            petTypeView.setText(animal.getPetType());
            petImage.setImageResource(animal.getPetImageId());
        }

        private void writeToPetFile(Animal animal, File file) {
            JSONObject jsonObject = new JSONObject();
            try{
                jsonObject.put("type", animal.getPetType());
                jsonObject.put("name", animal.getPetName());
                jsonObject.put("image", animal.getPetImageId());
                jsonObject.put("energy", animal.getEnergyCounter());
                jsonObject.put("happy", animal.getHappinessCounter());
                jsonObject.put("food", animal.getFoodCounter());

                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(jsonObject.toString());
                fileWriter.flush();
                fileWriter.close();
            }
            catch (Exception ex){
                Toast.makeText(createPet.getContext(), R.string.error + " " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        private File createPetFile() {
            File file = new File(createPet.getContext().getFilesDir(), "pet.json");
            try {
                if(file.createNewFile()){
                    return file;
                }
            }
            catch (Exception ex) {
                Toast.makeText(createPet.getContext(), "awdawaadsdasadswda" + R.string.error + " " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        private void goToMainScreen() {
            Intent intent = new Intent(createPet.getContext(), MainScreen.class);
            createPet.getContext().startActivity(intent);
        }

        public petCardHolder(@NonNull View itemView){
            super(itemView);
            petTypeView = itemView.findViewById(R.id.petType);
            petImage = itemView.findViewById(R.id.petImage);
            createPet = itemView.findViewById(R.id.createPet);
            petNameInput = itemView.findViewById(R.id.petName);

            createPet.setOnClickListener(v -> {
                if(petNameInput.getText().toString().isEmpty()) {
                    Toast.makeText(createPet.getContext(), R.string.nullPetNameInput, Toast.LENGTH_SHORT).show();
                }
                else {
                    Animal animal = petCardAdapter.mData.get(getAdapterPosition());
                    animal.setPetName(petNameInput.getText().toString());
                    writeToPetFile(animal, createPetFile());
                    goToMainScreen();
                }
            });
        }
    }
}
