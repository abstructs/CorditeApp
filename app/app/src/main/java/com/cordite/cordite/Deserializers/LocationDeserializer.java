package com.cordite.cordite.Deserializers;

import android.location.Location;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class    LocationDeserializer implements JsonDeserializer<Location> {
    @Override
    public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();

        String provider = obj.get("mProvider").getAsString();

        double latitude = obj.get("mLatitude").getAsDouble();
        double longitude = obj.get("mLongitude").getAsDouble();

        Location location = new Location(provider);

        location.setLongitude(longitude);
        location.setLatitude(latitude);

        return location;
    }
}
