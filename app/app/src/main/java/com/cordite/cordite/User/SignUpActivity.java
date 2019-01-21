package com.cordite.cordite.User;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Response;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Network;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cordite.cordite.Api.APIClient;
import com.cordite.cordite.Api.UserService;
import com.cordite.cordite.HomeActivity;
import com.cordite.cordite.MainActivity;
import com.cordite.cordite.R;
import com.cordite.cordite.Entities.User;
import com.cordite.cordite.Validators.UserValidator;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;
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
                signUp(getUser());
            }
        });
    }

    private void signUp(final User user) {
        class SubmitSignUpForm extends AsyncTask<Void, Void, Response<JsonObject>> {
            @Override
            protected Response<JsonObject> doInBackground(Void... voids) {
                if(thereAreNoErrors(user)) {
                    return makeSignUpRequest(user);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Response<JsonObject> response) {
                if(response == null || !response.isSuccessful()) {
                    showErrors(user);
                } else {
                    handleSignUpSuccess(response);
                }
            }
        }

        new SubmitSignUpForm().execute();
    }

    private boolean thereAreNoErrors(User user) {
        try {
            for (List<String> errors : user.getErrors().values()) {
                if (errors.size() != 0) {
                    return false;
                }
            }
        } catch(NetworkErrorException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void showErrors(User user) {

        Toast.makeText(SignUpActivity.this, "Oh no! Check for errors!.", Toast.LENGTH_SHORT).show();

        HashMap<User.ErrorKey, List<String>> errors = user.getCachedErrors();

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

    // returns a boolean indicating whether the request was successful
    private Response<JsonObject> makeSignUpRequest(User user) {
        Call<JsonObject> call = userService.signUp(user);

        try {
            return call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void handleSignUpSuccess(Response<JsonObject> response) {
        if(response != null && response.body() != null) {
            String token = response.body().get("token").getAsString();

            SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.shared_preferences_key),
                    Context.MODE_PRIVATE).edit();

            editor.putString("token", token);

            editor.apply();

            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);

            startActivity(intent);

            finish();
        } else {
            // redirect to login, sign-up worked but couldn't parse token.
            System.out.println("Response was null");
        }
    }
}
