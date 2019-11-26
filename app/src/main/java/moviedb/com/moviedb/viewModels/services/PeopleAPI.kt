package moviedb.com.moviedb.viewModels.services

import io.reactivex.Single
import moviedb.com.moviedb.models.responses.GetPopularPeopleResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * interface created to represent all popular people api calls
 */
interface PeopleAPI {
    @GET("person/popular")
    fun getPopularPeople(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Single<GetPopularPeopleResponse>

}