package moviedb.com.moviedb.models.responses

import com.google.gson.annotations.SerializedName

class GetPopularPeopleResponse {
    @SerializedName("total_results")
    private var totalResults: Int? = null

    @SerializedName("total_pages")
    private var totalPages: Int? = null

    private var page: Int? = null
    private var results: ArrayList<String>? = null


}