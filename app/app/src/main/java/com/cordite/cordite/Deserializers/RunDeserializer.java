package com.cordite.cordite.Deserializers;

import com.cordite.cordite.Entities.Run;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class RunDeserializer implements JsonDeserializer<Run> {
    @Override
    public Run deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Run run = new Run();

        JsonObject obj = json.getAsJsonObject();

        double averageSpeed = obj.get("averageSpeed").getAsDouble();
        int timeElapsed = obj.get("timeElapsed").getAsInt();
        int rating = obj.get("rating").getAsInt();
        double distanceTravelled = obj.get("distanceTravelled").getAsDouble();
        String date = obj.get("createdAt").getAsString();

        run.timeElapsed = timeElapsed;
        run.averageSpeed = averageSpeed;
        run.rating = rating;
        run.distanceTravelled = distanceTravelled;
        run.date = date;

        return run;
    }
}
