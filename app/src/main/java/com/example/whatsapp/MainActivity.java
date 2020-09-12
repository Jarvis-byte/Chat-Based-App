package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {
    EditText usernameEditText;
    EditText passeordEditText;
    Button buttonSignIn;
    TextView toggleLoginModeTextView;
    Boolean loginModeActive = false;

    SharedPreferences prefs = null;
    public void redirectIfLoggedIn()
    {
        if(ParseUser.getCurrentUser()!=null)
        {
            Intent intent=new Intent(getApplicationContext(),UserListActivity.class);
            startActivity(intent);
        }
    }
    public void toggleLoginMode(View view)
    {
        Button buttonSignIn=findViewById(R.id.loginSignuoButton);
        toggleLoginModeTextView=findViewById(R.id.toggleLoginModeTextView);
       if(loginModeActive)
       {
           loginModeActive=false;
           buttonSignIn.setText("Sign Up");
           toggleLoginModeTextView.setText("Or,Log In");
       }
       else
       {
           loginModeActive=true;
           buttonSignIn.setText("Log In");
           toggleLoginModeTextView.setText("Or,Sign Up");

       }

    }




   public void signupLogin(View view)
    {
        usernameEditText = findViewById(R.id.editTextUsername);
        passeordEditText = findViewById(R.id.editTextTextPassword);
        if(loginModeActive)
            {
                        ParseUser.logInInBackground(usernameEditText.getText().toString(), passeordEditText.getText().toString(), new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if(e==null)
                                {
                                    Log.i("Info","User Logged In");
                                    redirectIfLoggedIn();
                                }
                                else
                                {
                                    String message=e.getMessage();
                                    if(message.toLowerCase().contains("java"))
                                    {
                                        message=e.getMessage().substring(e.getMessage().indexOf(""));
                                    }
                                    Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        else {

    ParseUser user = new ParseUser();
    user.setUsername(usernameEditText.getText().toString());
    user.setPassword(passeordEditText.getText().toString());
    user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(ParseException e) {
            if (e == null) {
                Log.i("Info", "User signed up");
                redirectIfLoggedIn();
            } else {
                String message=e.getMessage();
                if(message.toLowerCase().contains("java"))
                {
                    message=e.getMessage().substring(e.getMessage().indexOf(""));
                }
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
            }
        }
    });
}

    }

    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {

            ParseUser.logOut();
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
              setTitle("Whatsapp Login");
        redirectIfLoggedIn();
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}