package com.cordite.cordite.Validators;

import android.accounts.NetworkErrorException;
import android.view.View;
import android.widget.TextView;

import com.cordite.cordite.Api.APIClient;
import com.cordite.cordite.Api.UserService;
import com.cordite.cordite.Entities.User;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class UserValidator extends Validator {

    private UserValidator() {

    }

    public static List<String> getEmailErrors(String email) {
        ArrayList<String> errors = new ArrayList<>();

        if(!validLength(email, 1, 50)) {
            errors.add("Email should be between 1 and 50 characters");
        }

        return errors;
    }

    public static List<String> getPasswordErrors(String password) {
        ArrayList<String> errors = new ArrayList<>();

        if(!validLength(password, 1, 50)) {
            errors.add("Password should be between 1 and 50 characters");
        }

        return errors;
    }

    public static List<String> getFirstNameErrors(String firstName) {
        ArrayList<String> errors = new ArrayList<>();

        if(!validLength(firstName, 1, 50)) {
            errors.add("First name should be between 1 and 50 characters");
        }

        return errors;
    }

    public static List<String> getLastNameErrors(String lastName) {
        ArrayList<String> errors = new ArrayList<>();

        if(!validLength(lastName, 1, 50)) {
            errors.add("Last name should be between 1 and 50 characters");
        }

        return errors;
    }
}
