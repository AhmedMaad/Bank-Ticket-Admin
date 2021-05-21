package com.example.bankticketadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
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
                        deleteAllTickets();
                    }
                });
    }

    private void deleteAllTickets() {
        db
                .collection("tickets")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<TicketModel> tickets =
                                queryDocumentSnapshots.toObjects(TicketModel.class);
                        for (int i = 0; i < tickets.size(); ++i) {
                            deleteTicketById(queryDocumentSnapshots.getDocuments().get(i).getId());
                        }
                        Toast.makeText(MainActivity.this, R.string.tickets_initialized
                                , Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteTicketById(String id) {
        db
                .collection("tickets")
                .document(id)
                .delete();
    }

}