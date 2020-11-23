package com.example.exoplayerapp

import retrofit2.Call
import retrofit2.http.GET

interface jsonapiholder {
    @GET("/")
    fun getUsers(): Call<List<User>>
}