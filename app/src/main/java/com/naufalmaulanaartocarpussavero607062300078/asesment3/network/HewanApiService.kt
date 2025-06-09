package com.naufalmaulanaartocarpussavero607062300078.asesment3

import com.naufalmaulanaartocarpussavero607062300078.asesment3.model.ReviewFilm
import com.naufalmaulanaartocarpussavero607062300078.asesment3.model.OpStatus
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://hip-keen-cub.ngrok-free.app/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface HewanApiService {
    @GET("reviews")
    suspend fun getReviewFilm(
        @Header("Authorization") userId: String
    ): List<ReviewFilm>


    @Multipart
    @POST("reviews/store")
    suspend fun postReviewFilm(
        @Header("Authorization") userId: String,
        @Part("judul_film") judul_film: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part("komentar") komentar: RequestBody,
        @Part poster: MultipartBody.Part
        ): OpStatus

    @DELETE("reviews/delete/{id}")
    suspend fun deleteHewan(
        @Header("Authorization") userId: String,
        @Path("id") id: String
    ):OpStatus
}

object HewanApi {
    val service: HewanApiService by lazy {
        retrofit.create(HewanApiService::class.java)
    }

    fun getHewanUrl(imageId: String): String {
        return "https://hip-keen-cub.ngrok-free.app/storage/$imageId"
    }
}
enum class ApiStatus { LOADING, SUCCESS, FAILED }