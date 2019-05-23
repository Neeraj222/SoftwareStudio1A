package com.example.finalapplication;

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

public class BookingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button btnMainPage;
    private Button btnConfirmBooking;
    private EditText etEmail;
    private EditText etPhoneNumber;
    private EditText etDate;
    private EditText etPostalAddress;
    private EditText etTime;
    private EditText etNotes;

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

        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.communities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

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

        if (email.isEmpty() || phoneNumber.isEmpty() || date.isEmpty() || postalAddress.isEmpty() || time.isEmpty()) {
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
