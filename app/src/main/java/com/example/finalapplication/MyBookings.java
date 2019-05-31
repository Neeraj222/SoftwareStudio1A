package com.example.finalapplication;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private Button button;
    private ProgressDialog progressDialog;
    private TableLayout table;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        button = findViewById(R.id.btnGoback);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenMainActivity();
            }
        });

        progressDialog = new ProgressDialog(this);

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
        progressDialog.setMessage("Loading Bookings");
        progressDialog.show();


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
                            progressDialog.dismiss();
                        } else {
                            Log.d(TAG, "Error getting documents: " , task.getException());
                        }
                    }
                });
    }

    private View createDeleteButton(final String id, final TableRow row) {
        final Button button = new Button(this);
        button.setText("X");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);

                db.collection("bookings").document(id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Document " + id + " successfully deleted!");
                                Toast.makeText(MyBookings.this, "Booking successfully deleted", Toast.LENGTH_SHORT).show();
                                table.removeView(row);
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document " + id, e);
                                Toast.makeText(MyBookings.this, "Error deleting the booking", Toast.LENGTH_SHORT).show();
                                button.setEnabled(true);
                            }
                        });
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

    public void OpenMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
