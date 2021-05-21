package com.example.bankticketadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        SharedPreferences pref = getSharedPreferences("temp", MODE_PRIVATE);
        boolean isFirstTime = pref.getBoolean("isFirstTime", true);
        if (isFirstTime) {
            initializeTickets();
            pref.edit().putBoolean("isFirstTime", false).apply();
        }

    }

    public void resetTicketNumber(View view) {
        initializeTickets();
    }

    private void initializeTickets() {
        Map<String, String> map = new HashMap<>();
        map.put("ticket", "000");
        db
                .collection("ticketNumber")
                .document("ticketNumber")
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, R.string.tickets_initialized
                                , Toast.LENGTH_SHORT).show();
                    }
                });
    }

}