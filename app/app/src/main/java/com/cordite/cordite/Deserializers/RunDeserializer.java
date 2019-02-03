package com.cordite.cordite.Deserializers;

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
import java.util.Date;
import java.util.Locale;

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

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

        try {
            Date convertedDate = date.parse(obj.get("createdAt").getAsString());
            run.date = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(convertedDate);
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
