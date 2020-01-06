package ru.imlocal.data.api

import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.imlocal.models.Place

const val BASE_URL = "https://atolserver.xyz/api/"
const val BASE_IMAGE_URL = "https://atolserver.xyz/img/"
const val SHOP_IMAGE_DIRECTION = "shopPhoto/";

const val FIRST_PAGE = 1
const val PER_PAGE = 20

interface Api {

    @GET("shop")
    fun getAllShops(
        @Query("userPoint") point: String?,
        @Query("range") range: Int,
        @Query("page") page: Int,
        @Query("per-page") perPage: Int
    ): Call<List<Place>>
//    ): Single<Response>

    @GET("shops/{id}")
    fun getShop(
        @Path("id") id: Int
    ): Single<Place>
//}

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