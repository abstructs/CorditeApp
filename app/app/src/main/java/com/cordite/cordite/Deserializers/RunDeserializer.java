package com.cordite.cordite.Deserializers;

import android.os.Build;

import com.cordite.cordite.Entities.Run;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class RunDeserializer implements JsonDeserializer<Run> {
    @Override
    public Run deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Run run = new Run();

        JsonObject obj = json.getAsJsonObject();

        // TODO: deserialize locations
//        JsonArray objArray = obj.getAsJsonArray("locations");

        double averageSpeed = obj.get("averageSpeed").getAsDouble();
        int timeElapsed = obj.get("timeElapsed").getAsInt();
        int rating = obj.get("rating").getAsInt();
        double distanceTravelled = obj.get("distanceTravelled").getAsDouble();

        try {

            SimpleDateFormat dateObj = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            dateObj.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date mongoDate = dateObj.parse(obj.get("createdAt").getAsString());

            run.date = mongoDate.toString();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        run.timeElapsed = timeElapsed;
        run.averageSpeed = averageSpeed;
        run.rating = rating;
        run.distanceTravelled = distanceTravelled;

        return run;
    }
}
