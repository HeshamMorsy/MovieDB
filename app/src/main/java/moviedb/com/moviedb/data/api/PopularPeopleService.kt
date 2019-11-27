package moviedb.com.moviedb.data.api

import io.reactivex.Single
import moviedb.com.moviedb.models.responses.GetPopularPeopleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PopularPeopleService {
    @GET("person/popular")
    fun getPopularPeople(@Query("page") page: Int): Single<GetPopularPeopleResponse>

    @GET("search/person")
    fun searchPopularPeople(@Query("page") page: Int, @Query("query") query: String): Single<GetPopularPeopleResponse>


}