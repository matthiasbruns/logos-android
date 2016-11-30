package com.matthiasbruns.logos.network;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mbruns on 30/11/2016.
 */
public interface LogoService {

    @GET("/")
    Observable<List<Logo>> get();

    @GET("/")
    Observable<List<Logo>> query(@Query("q") final String query);

    @GET("/")
    Observable<List<Logo>> query(@Query("q") final String query, @Query("source") final String source);
}
