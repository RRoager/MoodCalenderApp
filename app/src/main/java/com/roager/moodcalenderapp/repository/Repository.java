package com.roager.moodcalenderapp.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.roager.moodcalenderapp.model.MoodDate;
import com.roager.moodcalenderapp.Updatable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


public class Repository {
    private static FirebaseFirestore db;
    private static DocumentReference ref;
    private static FirebaseStorage storage = FirebaseStorage.getInstance();
    private static String MOODDATES = "mood_dates";
    private static List<MoodDate> moodDates = new ArrayList<>();
    private static List<Integer> moods = new ArrayList<>();
    private static MoodDate currentMoodDate;

    public static void init() {
        // Initialiserer Firestore databasen
        db = FirebaseFirestore.getInstance();
        getMoodDates();
    }

    private static void getMoodDates() {
        // Laver en SnapshotListener der lytter efter ændringer i Firestore collectionen
        db.collection(MOODDATES).addSnapshotListener((QuerySnapshot value, FirebaseFirestoreException error) -> {
            if(error == null) {
                // Tømmer moodDates listen
                moodDates.clear();
                // Henter alle dokumenter på Firestore og hvis de har en dato tilføjes de til moodDates listen
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
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // Hvis tasken er succesfuld laves der et DocumentSnapshot af resultatet
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    // Hvis der ikke er en MoodDate laves en tom ud fra datoen og gemmet i collectionen
                    if (!documentSnapshot.exists()) {
                        MoodDate moodDate = new MoodDate(date, "", 0);
                        ref.set(moodDate);
                    }
                } else {
                    System.out.println("Failed to get or create MoodDate because of exception: " + task.getException());
                }
            }
        });
    }

    public static void saveMoodDate(String date, String text, int mood) {
        ref = db.collection(MOODDATES).document(date);

        // Opdatere fields ud fra den nye data indtastet i DateActivity og tjekker om det lykkedes
        ref.update("text", text, "mood", mood, "bitmap", mood + ".jpeg").addOnCompleteListener(obj -> {
            System.out.println("MoodDate updateded");
        }).addOnFailureListener(exception -> {
            System.out.println("Failed to update MoodDate because of exception: "  + exception);
        });
    }

    public static void deleteMoodDate() {
        // Sletter MoodDaten i Firestore ud fra datoen
        db.collection(MOODDATES).document(currentMoodDate.getDate()).delete();
    }

    public static MoodDate getCurrentMoodDate() {
        return currentMoodDate;
    }

    public static void setCurrentMoodDate(String date) {
        Repository.currentMoodDate = new MoodDate(date, "", 0);

        // Finder den rigtige moodDate på moodDates listen, ud fra datoen der er sendt med
        for (MoodDate moodDate : moodDates) {
            if (moodDate.getDate().equals(date) & moodDate.getText() != null) {
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
            caller.update(true);
        }).addOnFailureListener(exception -> {
            System.out.println("No bitmap in DB for this MoodDate");
        });
    }

    public static List<MoodDate> getMoodDatesList() {
        return moodDates;
    }

    /*
    public static void getMoodStatisticsByMonthAndYear(String monthAndYear) {

        //List<Integer> moodList = moodDates.stream().map(MoodDate::getMood).collect(Collectors.toList());

        List<Integer> moodsForMonthOfYearList = new ArrayList<>();

        for (MoodDate mood: Repository.moodDates) {
            if (mood.getDate().contains(monthAndYear)) {
                moodsForMonthOfYearList.add(mood.getMood());
            }
        }

        int noOfGreatDays = Collections.frequency(moodsForMonthOfYearList, 1);
        int noOfGoodDays = Collections.frequency(moodsForMonthOfYearList, 2);
        int noOfAverageDays = Collections.frequency(moodsForMonthOfYearList, 3);
        int noOfBadDays = Collections.frequency(moodsForMonthOfYearList, 4);
        int noOfTerribleDays = Collections.frequency(moodsForMonthOfYearList, 5);

        System.out.println("DETTE ER MOODS FOR DEN SPECIFIKKE MÅNED: " + moodsForMonthOfYearList);

        System.out.println("ANTAL GREAT DAYS: " + noOfGreatDays);
        System.out.println("ANTAL GOOD DAYS: " + noOfGoodDays);
        System.out.println("ANTAL AVERAGE DAYS: " + noOfAverageDays);
        System.out.println("ANTAL BAD DAYS: " + noOfBadDays);
        System.out.println("ANTAL TERRIBLE DAYS: " + noOfTerribleDays);
    }
    */
}
