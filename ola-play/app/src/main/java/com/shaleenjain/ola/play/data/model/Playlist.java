/*
 * Copyright (c) 2017. Shaleen Jain
 */

package com.shaleenjain.ola.play.data.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.shaleenjain.androidboilerplate.data.model.PlaylistModel;
import com.squareup.sqldelight.ColumnAdapter;
import com.squareup.sqldelight.RowMapper;

import java.util.Arrays;
import java.util.List;

@AutoValue
public abstract class Playlist  implements PlaylistModel {

    Playlist.Update_playlist updatePlaylist;

    private static final ColumnAdapter<List<String>, String> LIST_ADAPTER = new
            ColumnAdapter<List<String>, String>() {
        @NonNull
        @Override
        public List<String> decode(String databaseValue) {
            return Arrays.asList(databaseValue.split(","));
        }

        @Override
        public String encode(@NonNull List<String> value) {
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
    };

    @SuppressWarnings("StaticInitializerReferencesSubClass")
    public static final PlaylistModel.Factory<Playlist> FACTORY =
            new PlaylistModel.Factory<>(AutoValue_Playlist::new, LIST_ADAPTER);

    public static final RowMapper<Playlist> SELECT_ALL_MAPPER = FACTORY.select_by_idMapper();

    public abstract Long id();
    public abstract String name();
    public abstract List<String> mediaids();
}
