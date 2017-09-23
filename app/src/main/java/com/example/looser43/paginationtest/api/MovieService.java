package com.example.looser43.paginationtest.api;


import com.example.looser43.paginationtest.model.TopUpHistoryModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Pagination
 * Created by Suleiman19 on 10/27/16.
 * Copyright (c) 2016. Suleiman Ali Shakir. All rights reserved.
 */

public interface MovieService {

    @GET("api/topuphis")
    Call<TopUpHistoryModel> getTopRatedMovies(
            @Query("authcode") String authcode,
            @Query("logcode") String logcode,
            @Query("current_page") int pageIndex
    );

}
