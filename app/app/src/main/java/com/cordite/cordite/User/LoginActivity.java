package com.cordite.cordite.User;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cordite.cordite.Api.APIClient;
import com.cordite.cordite.Api.UserService;
import com.cordite.cordite.Entities.User;
import com.cordite.cordite.Home.HomeActivity;
import com.cordite.cordite.R;
import com.google.gson.JsonObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private TextView emailInput;
    private TextView passwordInput;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_key),
                Context.MODE_PRIVATE);

        if(preferences.contains("token")) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

            startActivity(intent);

            finish();
        } else {
            this.userService = APIClient.getClient().create(UserService.class);

            setupButtons();
            setupFields();
        }
    }

    private void setupButtons() {
        Button submitBtn = findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(getUser());
            }
        });
    }

    private void setupFields() {
        this.emailInput = findViewById(R.id.emailInput);
        this.passwordInput = findViewById(R.id.passwordInput);
    }

    private User getUser() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        User user = new User();

        user.email = email;
        user.password = password;

        return user;
    }

    private void login(final User user) {
        Call<JsonObject> request = userService.login(user);

        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                switch(response.code()) {
                    case 200:
                        handleSuccess(response);
                        break;
                    case 401:
                        passwordInput.setError("Invalid password");
                        Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                        break;
                    case 404:
                        emailInput.setError("Email not found");
                        Toast.makeText(LoginActivity.this, "Email not found", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Network error! :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSuccess(Response<JsonObject> response) {
        if(response.body() != null) {
            String token = response.body().get("token").getAsString();

            SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.shared_preferences_key),
                    Context.MODE_PRIVATE).edit();

            editor.putString("token", token);

            editor.apply();

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

            startActivity(intent);

            finish();
        }
    }

}
