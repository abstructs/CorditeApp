package com.cordite.cordite.Deserializers;

import android.location.Location;
import android.os.Build;

import com.cordite.cordite.Entities.Run;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;


public class RunDeserializer implements JsonDeserializer<Run> {
    @Override
    public Run deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Run run = new Run();

        JsonObject obj = json.getAsJsonObject();

        // TODO: deserialize locations


        double averageSpeed = obj.get("averageSpeed").getAsDouble();
        int timeElapsed = obj.get("timeElapsed").getAsInt();
        int rating = obj.get("rating").getAsInt();
        double distanceTravelled = obj.get("distanceTravelled").getAsDouble();

        JsonArray objArray = obj.getAsJsonArray("locations");

        try {
            SimpleDateFormat dateObj = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            dateObj.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date date = dateObj.parse(obj.get("createdAt").getAsString());

            run.date = date.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        LocationDeserializer locationDeserializer = new LocationDeserializer();

        List<Location> locations = new ArrayList<>();

        for(JsonElement locationElement : objArray) {
            System.out.println(locationElement);
            Location location = locationDeserializer.deserialize(locationElement, Location.class, null);
            locations.add(location);
        }

        run.locations = locations;
        run.timeElapsed = timeElapsed;
        run.averageSpeed = averageSpeed;
        run.rating = rating;
        run.distanceTravelled = Math.round(distanceTravelled * 100d) / 1000d;

        return run;
    }
}
