package com.example.finalapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

public class MyBookings extends AppCompatActivity {
    private static final String TAG = "MyBookings";

    private String email;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private LinearLayout mainLayout;
    private TableLayout table;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            finish();
            startActivity(new Intent(MyBookings.this, LoginActivity.class));
        }
        email = user.getEmail() == null ? "test@gmail.com" : user.getEmail();

        mainLayout = findViewById(R.id.mainLayout);
        table = new TableLayout(this);
        table.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        table.setShrinkAllColumns(true);
        table.setStretchAllColumns(true);
//        table.setColumnShrinkable(0, false);
//        table.setColumnShrinkable(1, false);
        mainLayout.addView(table);

        loadBookings();
    }

    private void loadBookings() {
        db.collection("bookings")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            table.addView(createHeaderRow());
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String date = document.getString("date");
                                String time = document.getString("time");
                                String notes = document.getString("notes");

                                TableRow row = new TableRow(MyBookings.this);
                                row.addView(createCell(date));
                                row.addView(createCell(time));
                                row.addView(createCell(notes));
                                row.addView(createDeleteButton(document.getId(), row));

                                table.addView(row);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: " , task.getException());
                        }
                    }
                });
    }

    private View createDeleteButton(String id, final TableRow row) {
        final Button button = new Button(this);
        button.setText("X");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);

                // TODO remove this (just pretending to delete)
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                table.removeView(row);

                // TODO actually delete
            }
        });
        return button;
    }

    private TableRow createHeaderRow() {
        TableRow row = new TableRow(this);
        row.addView(createCell("Date"));
        row.addView(createCell("Time"));
        row.addView(createCell("Notes"));
        row.addView(createCell(""));
        return row;
    }

    private View createCell(String content) {
        TextView cell = new TextView(this);
        cell.setText(content);
        cell.setTextSize(17);
        cell.setMinWidth(1000);
        return cell;
    }
}
