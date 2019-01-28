package com.cordite.cordite.Entities;

import com.cordite.cordite.Validators.UserValidator;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {
    @SerializedName("firstName")
    public String firstName;

    @SerializedName("lastName")
    public String lastName;

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    private HashMap<ErrorKey, List<String>> errors;

    public User() {
        this.errors = new HashMap<>();
    }

    public enum ErrorKey {
        EMAIL, PASSWORD, FIRST_NAME, LAST_NAME
    }

    public HashMap<ErrorKey, List<String>> getErrors() {
        return this.errors;
    }

    public void addError(ErrorKey key, String errorMsg) {
        List<String> itemErrors = this.errors.get(key);

        if(itemErrors != null) {
            itemErrors.add(errorMsg);
        } else {
            itemErrors = new ArrayList<>();
            itemErrors.add(errorMsg);
        }

        this.errors.put(key, itemErrors);
    }

    public void setErrors() {
        errors.put(ErrorKey.EMAIL, UserValidator.getEmailErrors(this.email));
        errors.put(ErrorKey.PASSWORD, UserValidator.getPasswordErrors(this.password));
        errors.put(ErrorKey.FIRST_NAME, UserValidator.getFirstNameErrors(this.firstName));
        errors.put(ErrorKey.LAST_NAME, UserValidator.getLastNameErrors(this.lastName));
    }
}
