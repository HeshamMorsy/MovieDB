package moviedb.com.moviedb.data.repository.details

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moviedb.com.moviedb.data.api.PopularPeopleService
import moviedb.com.moviedb.data.pojos.CelebrityDetails
import moviedb.com.moviedb.data.repository.NetworkState
import moviedb.com.moviedb.models.responses.GetImagesResponse
import java.lang.Exception

class CelebrityDetailsDataSource(private val peopleService: PopularPeopleService,private val context: Context,private val compositeDisposable: CompositeDisposable)

{
    private  val tag = CelebrityDetailsDataSource::class.java.simpleName
    private val pNetworkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = pNetworkState

    private val pDownloadedDetailsResponse = MutableLiveData<CelebrityDetails>()
    val downloadedDetailsResponse: LiveData<CelebrityDetails>
        get() = pDownloadedDetailsResponse

    private val pImagesResponse= MutableLiveData<GetImagesResponse>()
    val ImagesResponse: LiveData<GetImagesResponse>
        get() = pImagesResponse




    fun fetchDetails(personId: Int) {
        pNetworkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                peopleService.getCelebrityDetails(personId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        pDownloadedDetailsResponse.postValue(it)
                        pNetworkState.postValue(NetworkState.LOADED)
                    }, {
                        pNetworkState.postValue(NetworkState.ERROR)
                        Toast.makeText(context,it.message+"", Toast.LENGTH_SHORT).show()
                        Log.e(tag,it.message+"")
                    })
            )

        } catch (e: Exception) {
            Log.e(tag,e.message+"")
        }

    }

    fun fetchImages(personId: Int) {
        pNetworkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                peopleService.getCelebrityImages(personId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        pImagesResponse.postValue(it)
                        pNetworkState.postValue(NetworkState.LOADED)
                    }, {
                        pNetworkState.postValue(NetworkState.ERROR)
                        Toast.makeText(context,it.message+"", Toast.LENGTH_SHORT).show()
                        Log.e(tag,it.message+"")
                    })
            )

        } catch (e: Exception) {
            Log.e(tag,e.message+"")
        }

    }



}