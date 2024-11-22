package com.example.medbook;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    EditText nameEditText, secondNameEditText, phoneEditText;
    Button sendButton, selectDateButton;
    Spinner slotSpinner;
    TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        nameEditText = findViewById(R.id.editTextTextPersonName);
        secondNameEditText = findViewById(R.id.editTextTextPersonName2);
        phoneEditText = findViewById(R.id.editTextPhone);
        sendButton = findViewById(R.id.button);
        selectDateButton = findViewById(R.id.button2);
        resultTextView = findViewById(R.id.textView);

        // Set up listeners
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMSMessage();
            }
        });

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });
    }

    protected void sendSMSMessage() {
        String phoneNumber = phoneEditText.getText().toString();
        String message = "Hello " + nameEditText.getText().toString()  + ", your slot is booked! \n Details : "+secondNameEditText.getText().toString();

        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            sendSMS(phoneNumber, message);
        }
    }

    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent!", Toast.LENGTH_LONG).show();
            resultTextView.setText("SMS sent to " + phoneNumber);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS sending failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void selectDate() {
        // Placeholder for date selection logic
        // You can use DatePickerDialog for better user experience
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String selectedDate = dateFormat.format(calendar.getTime());
        Toast.makeText(this, "Selected date: " + selectedDate, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMSMessage();  // Send SMS if permission granted
            } else {
                Toast.makeText(getApplicationContext(), "SMS sending failed, permission denied.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
