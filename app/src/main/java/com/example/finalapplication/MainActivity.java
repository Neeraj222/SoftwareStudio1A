package com.example.finalapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity<Æ> extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button Logout;
    private Button view;
    private Button view2;
    private Button view3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        view = findViewById(R.id.btnCreateBookings);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenBookingActivity();
            }
        });

        view2 = findViewById(R.id.btnViewMyBookings);
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenMyBookings();
            }
        });

        view3 = findViewById(R.id.btnViewDonationHistory);
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenMyDonations();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        Logout = findViewById(R.id.btnLogout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
//                Toast.makeText(this, "Item 1 Selected",Toast.LENGTH_SHORT).show();
                Logout();
                return true;

            case R.id.item2:
                Toast.makeText(this, "Item 2 Selected",Toast.LENGTH_SHORT).show();
            return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void OpenBookingActivity(){
        Intent intent = new Intent(this, BookingActivity.class);
        startActivity(intent);
    }

    public void OpenMyBookings(){
        Intent intent = new Intent(this, MyBookings.class);
        startActivity(intent);
    }

    public void OpenMyDonations(){
        Intent intent = new Intent(this, DonorsHistory.class);
        startActivity(intent);
    }

    private void Logout() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));

    }
}
