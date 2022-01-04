package com.roager.moodcalenderapp.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.roager.moodcalenderapp.model.MoodDate;
import com.roager.moodcalenderapp.Updatable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Repository {
    private static FirebaseFirestore db;
    private static DocumentReference ref;
    private static FirebaseStorage storage;
    private static String MOODDATES = "mood_dates";
    private static List<MoodDate> moodDates = new ArrayList<>();
    private static MoodDate currentMoodDate;

    public static void init() {
        // Initialiserer Firestore databasen og Storage
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        getMoodDates();
    }

    private static void getMoodDates() {
        // Laver en SnapshotListener der lytter efter ændringer i Firestore collectionen
        db.collection(MOODDATES).addSnapshotListener((QuerySnapshot value, FirebaseFirestoreException error) -> {
            if(error == null) {
                // Tømmer moodDates listen
                moodDates.clear();
                // Henter alle dokumenter på Firestore og hvis de har en dato laves de til et MoodDate objekt og tilføjes til moodDates listen
                for(DocumentSnapshot documentSnapshot : value.getDocuments()) {
                    if(documentSnapshot.get("date") != null) {
                        String date = documentSnapshot.getString("date");
                        String text = documentSnapshot.getString("text");
                        int mood = Objects.requireNonNull(documentSnapshot.getLong("mood")).intValue();;
                        MoodDate moodDate = new MoodDate(date, text, mood);
                        moodDates.add(moodDate);
                    }
                }
            }else {
                System.out.println("Error retrieving Moods from Firestore: " + error);
            }
        });
    }

    public static void createMoodDate(String date) {
        ref = db.collection(MOODDATES).document(date);

        // Tjekker om der allerede findes data på denne dato
        ref.get().addOnCompleteListener(task -> {
            // Hvis tasken er succesfuld laves der et DocumentSnapshot af resultatet
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();

                // Hvis der ikke er en MoodDate laves en tom ud fra datoen og gemmes i kollektionen
                if (!documentSnapshot.exists()) {
                    MoodDate moodDate = new MoodDate(date, "", 0);
                    ref.set(moodDate);
                }
            }
        }).addOnFailureListener(exception -> {
            System.out.println("Failed to create MoodDate because of exception: " + exception);
        });
    }

    public static void saveMoodDate(String date, String text, int mood) {
        // Laver en reference til datoens dokument
        ref = db.collection(MOODDATES).document(date);

        // Opdatere dokumentets fields ud fra den nye data indtastet i DateActivity og tjekker om det lykkedes
        ref.update("text", text, "mood", mood, "bitmap", mood + ".jpeg").addOnCompleteListener(obj -> {
            System.out.println("MoodDate updateded");
        }).addOnFailureListener(exception -> {
            System.out.println("Failed to update MoodDate because of exception: " + exception);
        });
    }

    public static void deleteMoodDate() {
        // Sletter MoodDaten i DB ud fra datoen/IDet
        db.collection(MOODDATES).document(currentMoodDate.getDate()).delete();
    }

    public static MoodDate getCurrentMoodDate() {
        return currentMoodDate;
    }

    public static void setCurrentMoodDate(String date) {
        // Sætter currentMoodDate til at være en tom MoodDate med datoen der er sendt med
        Repository.currentMoodDate = new MoodDate(date, "", 0);

        // Hvis der findes en MoodDate på listen med datoen og moodet er sat på denne sættes den til at være currentMoodDate i stedet
        for (MoodDate moodDate : moodDates) {
            if (moodDate.getDate().equals(date) & moodDate.getMood() != 0) {
                Repository.currentMoodDate = moodDate;
            }
        }
    }

    public static void downloadBitmapForCurrentMoodDate(Updatable caller) {
        // Laver en String der matcher billedets navn og sætter StorageRef til at være denne
        String moodImage = currentMoodDate.getMood() + ".jpeg";
        StorageReference ref = storage.getReference(moodImage);

        // Sætter max opløsningen
        int maxRes = 256 * 256;

        // Henter billedet ud fra reffen og omdanner det til et bitmap samt sætter bitmappet på currentMoodDate
        ref.getBytes(maxRes).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            currentMoodDate.setBitmap(bitmap);
            // Caller = DateActivty kalder updateMoodImage, så billedet kan blive sat på Viewet
            caller.updateMoodImage(true);
        }).addOnFailureListener(exception -> {
            System.out.println("No bitmap in DB for this MoodDate: " + exception);
        });
    }

    // For at kunne bruge moodDates listen i StatisticsActivity
    public static List<MoodDate> getMoodDatesList() {
        return moodDates;
    }
}
