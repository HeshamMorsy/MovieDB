package moviedb.com.moviedb.data.api

import io.reactivex.Single
import moviedb.com.moviedb.models.responses.GetPopularPeopleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PopularPeopleInterface {
    @GET("person/popular")
    fun getPopularPeople(@Query("page") page: Int): Single<GetPopularPeopleResponse>
}