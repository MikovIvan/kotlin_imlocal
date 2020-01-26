package ru.imlocal.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.imlocal.models.Action
import ru.imlocal.models.Event
import ru.imlocal.models.Place
import ru.imlocal.models.User

const val BASE_URL = "https://atolserver.xyz/api/"
const val BASE_IMAGE_URL = "https://atolserver.xyz/img/"
const val SHOP_IMAGE_DIRECTION = "shopPhoto/";
const val ACTION_IMAGE_DIRECTION = "eventPhoto/";
const val EVENT_IMAGE_DIRECTION = "happeningPhoto/";

const val FIRST_PAGE = 1
const val PER_PAGE = 10

interface Api {

    @GET("shop")
    fun getAllShops(
        @Query("userPoint") point: String?,
        @Query("range") range: Int,
        @Query("page") page: Int,
        @Query("per-page") perPage: Int
    ): Call<List<Place>>

    @GET("shops/{id}")
    fun getShop(
        @Path("id") id: Int
    ): Call<Place>

    @GET("events/{id}")
    fun getAction(
        @Path("id") id: Int
    ): Call<Action>

    @GET("events")
    fun getAllActions(
        @Query("page") page: Int,
        @Query("per-page") perPage: Int
    ): Call<List<Action>>

    @GET("happenings")
    fun getAllEvents(
        @Query("page") page: Int,
        @Query("per-page") perPage: Int
    ): Call<List<Event>>

    @GET("happenings/{id}")
    fun getEvent(
        @Path("id") id: Int
    ): Call<Event>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("user/register")
    fun registerUser(
        @Body user: User
    ): Call<User>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("user/login")
    fun loginUser(
        @Body user: User
    ): Call<User>

    companion object ApiClient {
        fun getClient(): Api {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)
        }
    }
}