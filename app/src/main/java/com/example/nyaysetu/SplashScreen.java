package com.example.nyaysetu;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nyaysetu.databinding.ActivitySplashScreenBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.color.DynamicColors;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    GoogleSignInClient signInRequest;

    CountryCodePicker cpp;
    GoogleSignInOptions gso;
    MaterialButton signin,getotp;

    ActivitySplashScreenBinding splashScreenBinding;
    private static final int SIGN_IN=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        signin = findViewById(R.id.signupbtn);
        getotp = findViewById(R.id.next);
        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SplashScreen.this, "It's on progress", Toast.LENGTH_SHORT).show();
            }
        });
        DynamicColors.applyToActivitiesIfAvailable((Application) getApplicationContext());
        SharedPreferences prefs = getSharedPreferences("userdata", MODE_PRIVATE);
        String name = prefs.getString("name", null);
        String email = prefs.getString("email", null);
        if (name != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            startActivity(intent);
        }
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        signInRequest = GoogleSignIn.getClient(this, gso);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Authentication();
                Toast.makeText(SplashScreen.this, "Sign in", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void Authentication() {
        Intent intent = signInRequest.getSignInIntent();
        startActivityForResult(intent,SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                HomeActivity(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void HomeActivity(GoogleSignInAccount account) {
        if (account != null) {
            String idToken = account.getIdToken();
            if (idToken != null) {
                AuthCredential authCredential = GoogleAuthProvider.getCredential(idToken, null);
                auth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        String name = user.getDisplayName();
                        String email = user.getEmail();
                        String username = email.replace("@gmail.com", "");
                        Log.d("username", username);
                        DatabaseReference userRef = database.getReference("Users").child(username);
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    // User data already exists, you can update specific fields here
                                    User existingUser = snapshot.getValue(User.class);
                                    if (!existingUser.getEmail().equals(email)) {
                                        existingUser.setEmail(email);
                                        userRef.setValue(existingUser);
                                    }
                                } else {
                                    // User data doesn't exist, create a new user entry
                                    User muser = new User(name, email);
                                    muser.setEmail(email);
                                    userRef.setValue(muser);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        SharedPreferences.Editor editor = getSharedPreferences("userdata", MODE_PRIVATE).edit();
                        editor.putString("name", user.getDisplayName());
                        editor.putString("email", user.getEmail());
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        // Remove the intent.putExtra lines for "name", "email", and "image"
                        startActivity(intent);
                    }
                });
            }

        }

    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}