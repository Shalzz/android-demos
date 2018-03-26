package com.shaleenjain.ola.play.data.remote;

import com.shaleenjain.ola.play.data.model.Track;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface APIService {

    String ENDPOINT = "http://starlord.hackerearth.com/";

    @GET("studio")
    Observable<List<Track>> getTracks();

}
