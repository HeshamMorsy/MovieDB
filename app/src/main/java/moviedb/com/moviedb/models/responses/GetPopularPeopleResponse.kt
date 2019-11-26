package moviedb.com.moviedb.models.responses

import com.google.gson.annotations.SerializedName
import moviedb.com.moviedb.models.entities.Person

data class GetPopularPeopleResponse (
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    val page: Int,
    val results: ArrayList<Person>
)