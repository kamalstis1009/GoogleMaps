package com.apps.googlemaps.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.apps.googlemaps.R;
import com.apps.googlemaps.models.MapLocation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MarkerInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private Context mContext;
    private String[] arr;
    private MapLocation location;

    public MarkerInfoAdapter(Context mContext, String[] arr, MapLocation location) {
        this.mContext = mContext;
        this.arr = arr;
        this.location = location;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.marker_info_window, null);
        ((TextView) row.findViewById(R.id.address)).setText(location.getAddress());
        ((TextView) row.findViewById(R.id.snippet)).setText(marker.getSnippet());
        return row;
    }
}
