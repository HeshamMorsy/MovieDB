package moviedb.com.moviedb.utilities.network

import moviedb.com.moviedb.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * This class is used to connect to movieDB api
 */
class RetrofitCreation {

    companion object {
        private lateinit var retrofit: Retrofit
        private lateinit var logging: HttpLoggingInterceptor
        private lateinit var httpClient: OkHttpClient.Builder

        fun createRetrofit(): Retrofit {
            logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)

            /** making the baseUrl in BuildConfig so we can set staging api for debugging
               and production api for release ing gradle so it changes automatically **/
            retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.baseUrl)
                .client(httpClient.build())
                .build()

            return retrofit
        }
    }
}