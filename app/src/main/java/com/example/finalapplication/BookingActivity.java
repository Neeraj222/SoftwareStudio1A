package com.example.finalapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {
    private static final String TAG = "BookingActivity";

    private Button btnMainPage;
    private Button btnConfirmBooking;
    private EditText etPhoneNumber;
    private EditText etDate;
    private EditText etPostalAddress;
    private EditText etTime;
    private EditText etNotes;
    private String email;
    private CommunitySpinner communitySpinner;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            finish();
            startActivity(new Intent(BookingActivity.this, LoginActivity.class));
        }
        email = user.getEmail() == null ? "test@gmail.com" : user.getEmail();

        etPhoneNumber = findViewById(R.id.tvPhoneNumber);
        etDate = findViewById(R.id.tvDate);
        etPostalAddress = findViewById(R.id.tvPostalAddress);
        etTime = findViewById(R.id.tvTime);
        etNotes = findViewById(R.id.tvAdditionalNotes);
        communitySpinner = new CommunitySpinner(this);

        btnMainPage = findViewById(R.id.btnMainPage);
        btnMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();

            }
        });

        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);
        btnConfirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    saveToDatabase();
                }
            }
        });
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private boolean validate() {
        String phoneNumber = etPhoneNumber.getText().toString();
        String date = etDate.getText().toString();
        String postalAddress = etPostalAddress.getText().toString();
        String time = etTime.getText().toString();
        String notes = etNotes.getText().toString();

        if (email.isEmpty() || phoneNumber.isEmpty() || date.isEmpty() || postalAddress.isEmpty() || time.isEmpty() || communitySpinner.getSelectedCommunity().isEmpty()) {
            showMessage("Please enter all details");
            return false;
        }
        return true;
    }

    private void saveToDatabase() {
        btnConfirmBooking.setEnabled(false);

        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        data.put("phoneNumber", etPhoneNumber.getText().toString());
        data.put("date", etDate.getText().toString());
        data.put("postalAddress", etPostalAddress.getText().toString());
        data.put("time", etTime.getText().toString());
        data.put("notes", etNotes.getText().toString());
        data.put("community", communitySpinner.getSelectedCommunity());

        db.collection("bookings")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Booking added with ID: " + documentReference.getId());
                        btnConfirmBooking.setEnabled(true);
                        openMainActivity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding booking", e);
                        showMessage("Error adding booking");
                        btnConfirmBooking.setEnabled(true);
                    }
                });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private class CommunitySpinner implements AdapterView.OnItemSelectedListener {
        private String selectedCommunity = "";

        CommunitySpinner(Context context) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.communities, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner spinner = findViewById(R.id.spinner1);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
        }

        String getSelectedCommunity() {
            return selectedCommunity;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedCommunity = position == 0 ? "" : parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
