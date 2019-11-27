package moviedb.com.moviedb.ui.views.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.reactivex.disposables.CompositeDisposable
import moviedb.com.moviedb.data.api.PopularPeopleService
import moviedb.com.moviedb.data.pojos.CelebrityDetails
import moviedb.com.moviedb.data.repository.NetworkState
import moviedb.com.moviedb.data.repository.details.CelebrityDetailsDataSource
import moviedb.com.moviedb.data.repository.popularList.PeopleDataSource

class DetailsRepository (private val peopleService: PopularPeopleService) {

    lateinit var detailsDataSource: CelebrityDetailsDataSource

    fun fetchDetails(compositeDisposable: CompositeDisposable, movieId: Int): LiveData<CelebrityDetails> {
        detailsDataSource = CelebrityDetailsDataSource(peopleService, compositeDisposable)
        detailsDataSource.fetchDetails(movieId)
        return detailsDataSource.downloadedDetailsResponse
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return detailsDataSource.networkState
    }
}