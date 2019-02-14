package com.cordite.cordite.Entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.cordite.cordite.Map.MapsActivity;
import com.cordite.cordite.R;
import com.cordite.cordite.Report.ReportType;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Report implements Parcelable {
    @SerializedName("location")
    public Location location;

    @SerializedName("type")
    public ReportType type;

    @SerializedName("address")
    public String address;

    public transient double distanceTo;

    public transient String timestamp;

    private Context context;

    public BitmapDescriptor getIcon(Context context) {
        this.context = context;

        BitmapDescriptor bitmap;

        switch(this.type) {
            case construction:
                bitmap = BitmapDescriptorFactory.fromBitmap(convertToBitmap(R.drawable.ic_construction));
                break;
            case trailClosed:
                bitmap = BitmapDescriptorFactory.fromBitmap(convertToBitmap(R.drawable.ic_closed));
                break;
            case photo:
                bitmap = BitmapDescriptorFactory.fromBitmap(convertToBitmap(R.drawable.ic_photo));
                break;
            case coolPlace:
                bitmap = BitmapDescriptorFactory.fromBitmap(convertToBitmap(R.drawable.ic_check));
                break;
            case beCareful:
                bitmap = BitmapDescriptorFactory.fromBitmap(convertToBitmap(R.drawable.ic_warning));
                break;
            case waterFountain:
                bitmap = BitmapDescriptorFactory.fromBitmap(convertToBitmap(R.drawable.ic_water));
                break;
            default:
                bitmap = BitmapDescriptorFactory.defaultMarker();
                break;
        }

        return bitmap;
    }

    private Bitmap convertToBitmap(int drawableId) {
        Drawable drawable = this.context.getDrawable(drawableId);
        Canvas canvas = new Canvas();

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);

        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private String fromCamelCase(String str) {
        if(str.length() == 0) return "";

        StringBuilder builder = new StringBuilder();

        builder.append(Character.toUpperCase(str.charAt(0)));

        for(int i = 1; i < str.length(); i++) {
            char strChar = str.charAt(i);

            if(Character.isUpperCase(strChar)) {
                builder.append(" ");
                builder.append(strChar);
            } else {
                builder.append(strChar);
            }
        }
        return builder.toString();
    }

    public String getTypeString() {
        return fromCamelCase(this.type.toString());
    }

    public String getAddress(Context context, Location location) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        return addresses.get(0).getAddressLine(0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
