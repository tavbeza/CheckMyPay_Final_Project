package com.example.checkmypay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    //private CollectionReference usersCollection = db.collection("Users");
    private EditText email, password;
    private Button btn_signup;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.text_signup_email);
        password = findViewById(R.id.text_signup_pass);
        btn_signup = findViewById(R.id.btn_signup_signup);
        btn_signup.setOnClickListener(this);
    }

    private void signUp() {
        if (!emailValidation()) {
            Toast.makeText(this, "Invalid email!", Toast.LENGTH_SHORT).show();
            return;
        }
        initUser(email, password);
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        writeUserToDB();
                    } else {
                        Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void writeUserToDB() {
        user.setId(mAuth.getCurrentUser().getUid());
        db.collection("Users")
                .document(user.getId())
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getApplicationContext(), email.getText().toString() + " Registered Successfully", Toast.LENGTH_SHORT).show();
                    goToSignInActivity();
                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), " An error has occurred", Toast.LENGTH_SHORT).show());


        // Create a new user with a email and password
        /*Map<String, Object> user = new HashMap<>();
        user.put("email", this.user.getEmail());
        user.put("password", this.user.getPassword());
        user.put("hourlyWage", 0);
        user.put("travelFee", 0);
        user.put("startDate", 0);
        user.put("endDate", 0);
        user.put("fromHour", 0);
        user.put("fromMinute", 0);
        user.put("toHour", 0);
        user.put("toMinute", 0);
        user.put("providentFund", 0);
        user.put("advancedStudyFund", 0);
        user.put("credits", 0);
        user.put("shifts", new ArrayList<Shift>());
        user.put("paychecks", new HashMap<String, Paycheck>());*/

        //db.document(this.user.getEmail()).set(user);

        // Add a new document with a generated ID
        /*db.collection("Users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        goToSignInActivity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "Signup failed!", Toast.LENGTH_SHORT).show();
                    }
                });*/

    }

    public void goToSignInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }

    public void initUser(EditText email, EditText password) {
        user = new User(email.getText().toString(), password.getText().toString());
    }

    public boolean emailValidation() {
        String emailAddress = email.getText().toString().trim();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            email.setError("please enter valid email address");
            email.requestFocus();
            return false;
        }
        if (email.getText().toString().equals("")) {
            email.setError("please enter email address");
            email.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_signup_signup:
                signUp();
                break;
        }
    }
}
