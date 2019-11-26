package moviedb.com.moviedb.data.api

import moviedb.com.moviedb.BuildConfig
import moviedb.com.moviedb.utilities.network.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/** created to provide popular people client **/
object PopularPeopleClient {
    fun getClient() : PopularPeopleInterface{
        val requestInterceptor = Interceptor {chain ->

            // as all requests will contain api key as query parameter , so I added it to all popular people client requests
            val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key", Constants.API_KEY)
                .build()

            // adding Authorization access token and content type as these headers are common in all api calls too
            val request = chain.request()
                .newBuilder()
                .header("Authorization", Constants.ACCESS_TOKEN)
                .header("Content-Type", Constants.CONTENT_TYPE)
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)
        }

        // prepare okHttp client with request interceptor and connection time out
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        // here I will return retrofit object which has the okHttp client , and ready to connect to popular people apis
        return Retrofit.Builder()
            .baseUrl(BuildConfig.baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(PopularPeopleInterface::class.java)
    }
}