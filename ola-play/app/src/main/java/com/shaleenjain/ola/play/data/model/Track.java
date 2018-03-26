package com.shaleenjain.ola.play.data.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.shaleenjain.androidboilerplate.data.model.TrackModel;
import com.squareup.sqldelight.ColumnAdapter;
import com.squareup.sqldelight.RowMapper;

import java.util.Arrays;
import java.util.List;

@AutoValue
public abstract class Track implements TrackModel {

    private static final ColumnAdapter<List<String>, String> ARTISTS_ADAPTER = new ColumnAdapter<List<String>, String>() {
        @NonNull
        @Override
        public List<String> decode(String databaseValue) {
            return Arrays.asList(databaseValue.split(","));
        }

        @Override
        public String encode(@NonNull List<String> value) {
            return getArtistsAsString(value);
        }
    };

    @SuppressWarnings("StaticInitializerReferencesSubClass")
    public static final TrackModel.Factory<Track> FACTORY =
            new TrackModel.Factory<>((id, song, url, artists, cover_image) ->
                    new AutoValue_Track(song, url, artists, cover_image));

    public static final RowMapper<Track> SELECT_ALL_MAPPER = FACTORY.selectALLMapper();

    @NonNull
    public String id() {
        return String.valueOf(url().hashCode());
    }
    public abstract String song();
    public abstract String url();
    public abstract String artists();
    public abstract String cover_image();


    public static String getArtistsAsString(@NonNull List<String> value) {
        StringBuilder sbString = new StringBuilder("");

        for(String artist : value){
            sbString.append(artist).append(",");
        }

        String strList = sbString.toString();

        //remove last comma from String
        if( strList.length() > 0 )
            strList = strList.substring(0, strList.length() - 1);

        return strList;
    }

    public static TypeAdapter<Track> typeAdapter(Gson gson) {
        return new AutoValue_Track.GsonTypeAdapter(gson);
    }

}

