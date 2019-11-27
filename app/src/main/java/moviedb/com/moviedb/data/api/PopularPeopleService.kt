package moviedb.com.moviedb.data.api

import io.reactivex.Single
import moviedb.com.moviedb.data.pojos.CelebrityDetails
import moviedb.com.moviedb.models.responses.GetImagesResponse
import moviedb.com.moviedb.models.responses.GetPopularPeopleResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/** This interface represents the endpoint apis of celebrities*/
interface PopularPeopleService {
    @GET("person/popular")
    fun getPopularPeople(@Query("page") page: Int): Single<GetPopularPeopleResponse>

    @GET("search/person")
    fun searchPopularPeople(@Query("page") page: Int, @Query("query") query: String): Single<GetPopularPeopleResponse>

    @GET("person/{person_id}")
    fun getCelebrityDetails(@Path("person_id") id: Int): Single<CelebrityDetails>

    @GET("person/{person_id}/images")
    fun getCelebrityImages(@Path("person_id") id: Int): Single<GetImagesResponse>




}