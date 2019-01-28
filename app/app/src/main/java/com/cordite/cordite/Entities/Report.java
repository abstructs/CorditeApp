package com.cordite.cordite.Entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.cordite.cordite.R;
import com.cordite.cordite.Report.ReportType;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.gson.annotations.SerializedName;

public class Report implements Parcelable {
    @SerializedName("location")
    public Location location;

    @SerializedName("type")
    public ReportType type;

    public transient double distanceTo;

    public transient String address;

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
                bitmap = bitmap = BitmapDescriptorFactory.fromBitmap(convertToBitmap(R.drawable.ic_closed));
                break;
            case photo:
                bitmap = bitmap = BitmapDescriptorFactory.fromBitmap(convertToBitmap(R.drawable.ic_photo));
                break;
            case coolPlace:
                bitmap = bitmap = BitmapDescriptorFactory.fromBitmap(convertToBitmap(R.drawable.ic_check));
                break;
            case beCareful:
                bitmap = bitmap = BitmapDescriptorFactory.fromBitmap(convertToBitmap(R.drawable.ic_warning));
                break;
            case waterFountain:
                bitmap = bitmap = BitmapDescriptorFactory.fromBitmap(convertToBitmap(R.drawable.ic_water));
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
