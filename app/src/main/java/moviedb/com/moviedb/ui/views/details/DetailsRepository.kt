package moviedb.com.moviedb.ui.views.details

import android.content.Context
import androidx.lifecycle.LiveData
import io.reactivex.disposables.CompositeDisposable
import moviedb.com.moviedb.data.api.PopularPeopleService
import moviedb.com.moviedb.data.pojos.CelebrityDetails
import moviedb.com.moviedb.data.repository.NetworkState
import moviedb.com.moviedb.data.repository.details.CelebrityDetailsDataSource
import moviedb.com.moviedb.models.responses.GetImagesResponse

class DetailsRepository (private val peopleService: PopularPeopleService,private val context: Context) {

    lateinit var detailsDataSource: CelebrityDetailsDataSource

    fun fetchDetails(compositeDisposable: CompositeDisposable, personId: Int): LiveData<CelebrityDetails> {
        detailsDataSource = CelebrityDetailsDataSource(peopleService,context, compositeDisposable)
        detailsDataSource.fetchDetails(personId)
        return detailsDataSource.downloadedDetailsResponse
    }


    fun fetchImages(compositeDisposable: CompositeDisposable, personId: Int): LiveData<GetImagesResponse> {
        detailsDataSource = CelebrityDetailsDataSource(peopleService,context, compositeDisposable)
        detailsDataSource.fetchImages(personId)
        return detailsDataSource.ImagesResponse
    }




    fun getNetworkState(): LiveData<NetworkState> {
        return detailsDataSource.networkState
    }
}