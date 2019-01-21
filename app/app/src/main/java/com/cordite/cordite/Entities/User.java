package com.cordite.cordite.Entities;

import android.accounts.NetworkErrorException;
import android.net.Network;

import com.cordite.cordite.Api.APIClient;
import com.cordite.cordite.Api.UserService;
import com.cordite.cordite.Validators.UserValidator;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

public class User {
    @SerializedName("firstName")
    public String firstName;

    @SerializedName("lastName")
    public String lastName;

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    public enum ErrorKey {
        EMAIL, PASSWORD, FIRST_NAME, LAST_NAME
    }

    private HashMap<ErrorKey, List<String>> cachedErrors;

    // prevents from making additional network requests when we don't have to
    public HashMap<ErrorKey, List<String>> getCachedErrors() {
        return this.cachedErrors;
    }

    public HashMap<ErrorKey, List<String>> getErrors() throws NetworkErrorException {
        HashMap<ErrorKey, List<String>> errors = new HashMap();

        errors.put(ErrorKey.EMAIL, UserValidator.getSyncEmailErrors(this.email));
        errors.put(ErrorKey.PASSWORD, UserValidator.getPasswordErrors(this.password));
        errors.put(ErrorKey.FIRST_NAME, UserValidator.getFirstNameErrors(this.firstName));
        errors.put(ErrorKey.LAST_NAME, UserValidator.getLastNameErrors(this.lastName));

        cachedErrors = errors;

        return errors;
    }
}
