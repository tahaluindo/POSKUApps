package com.postkudigital.app.services.api;

import com.postkudigital.app.models.location.Kecamatan;
import com.postkudigital.app.models.location.Kelurahan;
import com.postkudigital.app.models.location.Kota;
import com.postkudigital.app.models.location.Provinsi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ReferenceService {
    @GET("provinces.json")
    Call<List<Provinsi>> provinsi();

    @GET("regencies/{id}.json")
    Call<List<Kota>> kota(@Path("id") String id);

    @GET("districts/{id}.json")
    Call<List<Kecamatan>> kecamatan(@Path("id") String id);

    @GET("villages/{id}.json")
    Call<List<Kelurahan>> desa(@Path("id") String id);
}
