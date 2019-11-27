package moviedb.com.moviedb.utilities

import moviedb.com.moviedb.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * This class is used to connect to movieDB api
 */
class RetrofitFactory {

    companion object {
        private lateinit var retrofit: Retrofit
        private lateinit var logging: HttpLoggingInterceptor
        private lateinit var httpClient: OkHttpClient.Builder

        public fun createRetrofit(): Retrofit {
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
                .client(getHttpClient())
                .build()

            return retrofit
        }

        /**
         * @return OkHttpClient which has token &  in header
         */
        private fun getHttpClient(): OkHttpClient {
            return httpClient.addInterceptor { chain ->
                val original = chain.request()

                // Request customization: add request headers
                val requestBuilder = original.newBuilder()
                    .header("Authorization", Constants.ACCESS_TOKEN)
                    .header("Content-Type", Constants.CONTENT_TYPE)

                val request = requestBuilder.build()
                chain.proceed(request)
            }.build()
        }
    }
}