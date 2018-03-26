/*
 * Copyright (c) 2017. Shaleen Jain
 */

package com.shaleenjain.ola.play.data.model;

import android.support.v4.media.MediaMetadataCompat;

import java.util.Iterator;

public interface MusicProviderSource {
    String CUSTOM_METADATA_TRACK_SOURCE = "__SOURCE__";
    Iterator<MediaMetadataCompat> iterator();
}
