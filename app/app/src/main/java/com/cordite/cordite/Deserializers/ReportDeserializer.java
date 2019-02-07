package com.cordite.cordite.Deserializers;

import android.location.Location;

import com.cordite.cordite.Entities.Report;
import com.cordite.cordite.Report.ReportType;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ReportDeserializer implements JsonDeserializer<Report> {
    @Override
    public Report deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Report report = new Report();
        LocationDeserializer locationDeserializer = new LocationDeserializer();

        JsonObject reportObj = json.getAsJsonObject();

        String typeStr = reportObj.get("type").getAsString();
        JsonElement locationElement = reportObj.get("location");

        report.type = ReportType.valueOf(typeStr);
        report.location = locationDeserializer.deserialize(locationElement, Location.class, context);

        return report;
    }
}
