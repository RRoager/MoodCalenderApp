package com.roager.moodcalenderapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
        ref.update("text", text, "mood", mood).addOnCompleteListener(obj -> {
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

    // TODO fix visning af billedet
    public static void downloadBitmapForCurrentMoodDate() {
        String mood = currentMoodDate.getMood() + ".jpeg";
        StorageReference ref = storage.getReference(mood);
        int max = 1024 * 1024;
        ref.getBytes(max).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            currentMoodDate.setBitmap(bitmap);
        }).addOnFailureListener(exception -> {
            System.out.println("No bitmap in DB for this MoodDate");
        });
    }

    // TODO Find ud af hvorfor den ikke går ind i OnCompleteListeneren
    public static void getMoodData() {
        System.out.println("HALLO?");
        db.collection(MOODDATES)
                .whereEqualTo("date", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println("HVAD ER DET HER? " + document.getId() + " => " + document.getData());
                                //int mood = Objects.requireNonNull(document.getLong("mood")).intValue();;
                                //moods.add(mood);
                                //System.out.println("Dette er moods: " + moods);
                            }
                        } else {
                            System.out.println("Failed to get mood data because of exception: " + task.getException());
                        }
                    }
                });
    }
}
