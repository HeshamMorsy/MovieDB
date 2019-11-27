package moviedb.com.moviedb.ui.views.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import moviedb.com.moviedb.data.pojos.CelebrityDetails
import moviedb.com.moviedb.data.repository.NetworkState
import moviedb.com.moviedb.models.responses.GetImagesResponse

class DetailsViewModel (private val detailsRepository: DetailsRepository, movieId: Int) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val details: LiveData<CelebrityDetails> by lazy {
        detailsRepository.fetchDetails(compositeDisposable,movieId)
    }

    val images: LiveData<GetImagesResponse> by lazy {
        detailsRepository.fetchImages(compositeDisposable,movieId)
    }



    val networkState: LiveData<NetworkState> by lazy {
        detailsRepository.getNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}