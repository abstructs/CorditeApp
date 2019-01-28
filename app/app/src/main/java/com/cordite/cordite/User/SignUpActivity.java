package com.cordite.cordite.User;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cordite.cordite.Api.APIClient;
import com.cordite.cordite.Api.UserService;
import com.cordite.cordite.Home.HomeActivity;
import com.cordite.cordite.R;
import com.cordite.cordite.Entities.User;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private UserService userService;

    private TextView firstNameInput;
    private TextView lastNameInput;
    private TextView emailInput;
    private TextView passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_preferences_key),
                Context.MODE_PRIVATE);

        if(preferences.contains("token")) {
            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);

            startActivity(intent);

            finish();
        } else {
            this.userService = APIClient.getClient().create(UserService.class);

            setupButtons();
            setupFields();
        }
    }

    private void setupFields() {
        this.firstNameInput = findViewById(R.id.firstNameInput);
        this.lastNameInput = findViewById(R.id.lastNameInput);
        this.emailInput = findViewById(R.id.emailInput);
        this.passwordInput = findViewById(R.id.passwordInput);
    }

    private void setupButtons() {
        final Button submitBtn = findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForErrorsAndSignUp(getUser());
            }
        });
    }

    private boolean thereAreErrors(final User user) {
        System.out.println(user.getErrors());
        for(List<String> errors : user.getErrors().values()) {
            if(errors.size() != 0) {
                return true;
            }
        }

        return false;
    }

    private void signUp(final User user) {
        Call<JsonObject> request = userService.signUp(user);

        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                handleSuccess(response);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Network error! :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkForErrorsAndSignUp(final User user) {
        Call<JsonObject> emailTakenReq = userService.emailTaken(user);

        emailTakenReq.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                boolean emailTaken = response.body().get("emailTaken").getAsBoolean();

                user.setErrors();

                if(emailTaken) {
                    user.addError(User.ErrorKey.EMAIL, "That email is already taken");
                }

                if(thereAreErrors(user)) {
                    showErrors(user);
                } else {
                    signUp(user);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Network error! :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showErrors(User user) {
        Toast.makeText(SignUpActivity.this, "Oh no! Check for errors!.", Toast.LENGTH_SHORT).show();

        HashMap<User.ErrorKey, List<String>> errors = user.getErrors();

        List<String> emailErrors = errors.get(User.ErrorKey.EMAIL);
        List<String> passwordErrors = errors.get(User.ErrorKey.PASSWORD);
        List<String> firstNameErrors = errors.get(User.ErrorKey.FIRST_NAME);
        List<String> lastNameErrors = errors.get(User.ErrorKey.LAST_NAME);

        if(emailErrors.size() > 0)
            emailInput.setError(emailErrors.get(0));
        if(passwordErrors.size() > 0)
            passwordInput.setError(passwordErrors.get(0));
        if(firstNameErrors.size() > 0)
            firstNameInput.setError(firstNameErrors.get(0));
        if(lastNameErrors.size() > 0)
            lastNameInput.setError(lastNameErrors.get(0));
    }

    private User getUser() {
        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        User user = new User();

        user.firstName = firstName;
        user.lastName = lastName;
        user.email = email;
        user.password = password;

        return user;
    }

    private void handleSuccess(Response<JsonObject> response) {
        String token = response.body().get("token").getAsString();

        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.shared_preferences_key),
                Context.MODE_PRIVATE).edit();

        editor.putString("token", token);

        editor.apply();

        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);

        startActivity(intent);

        finish();
    }
}
