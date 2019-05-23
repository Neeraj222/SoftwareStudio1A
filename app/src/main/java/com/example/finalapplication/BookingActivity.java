package com.example.finalapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class BookingActivity extends AppCompatActivity {

    private Button btnMainPage;
    private Button btnConfirmBooking;
    private EditText etEmail;
    private EditText etPhoneNumber;
    private EditText etDate;
    private EditText etPostalAddress;
    private EditText etTime;
    private EditText etNotes;
    private CommunitySpinner communitySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        etEmail = findViewById(R.id.tvEmail);
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
        String email = etEmail.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        String date = etDate.getText().toString();
        String postalAddress = etPostalAddress.getText().toString();
        String time = etTime.getText().toString();
        String notes = etNotes.getText().toString();

        if (email.isEmpty() || phoneNumber.isEmpty() || date.isEmpty() || postalAddress.isEmpty() || time.isEmpty() || communitySpinner.getSelectedCommunity().isEmpty()) {
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveToDatabase() {
        String email = etEmail.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        String date = etDate.getText().toString();
        String postalAddress = etPostalAddress.getText().toString();
        String time = etTime.getText().toString();
        String notes = etNotes.getText().toString();

        // TODO database stuff
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

        public String getSelectedCommunity() {
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
