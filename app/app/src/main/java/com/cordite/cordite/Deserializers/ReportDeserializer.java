package com.cordite.cordite.Deserializers;

import android.location.Location;
import android.util.Log;

import com.cordite.cordite.Entities.Report;
import com.cordite.cordite.Report.ReportType;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ReportDeserializer implements JsonDeserializer<Report> {
    @Override
    public Report deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Report report = new Report();
        LocationDeserializer locationDeserializer = new LocationDeserializer();

        JsonObject reportObj = json.getAsJsonObject();

        String typeStr = reportObj.get("type").getAsString();
        String address = reportObj.get("address").getAsString();
        JsonElement locationElement = reportObj.get("location");

        report.address = address;
        report.type = ReportType.valueOf(typeStr);
        report.location = locationDeserializer.deserialize(locationElement, Location.class, context);


        SimpleDateFormat dateObj = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        dateObj.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date mongoDate = dateObj.parse(reportObj.get("createdAt").getAsString());
            report.timestamp = String.valueOf(mongoDate.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return report;
    }
}
