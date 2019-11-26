package moviedb.com.moviedb.data.repository

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moviedb.com.moviedb.data.api.PopularPeopleInterface
import moviedb.com.moviedb.models.entities.Person
import moviedb.com.moviedb.utilities.network.Constants.Companion.FIRST_PAGE


/** using android paging library to fetch data */
class PeopleListDataSource(private val peopleService: PopularPeopleInterface, private val compositeDisposable: CompositeDisposable)
    : PageKeyedDataSource<Int, Person>() {

    private val TAG : String = PeopleListDataSource::class.java.simpleName
    private var page = FIRST_PAGE
    private val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    /** this method is called when list loaded for first time **/
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Person>) {
        networkState.postValue(NetworkState.LOADING)

        // perform the api call
        compositeDisposable.add(peopleService.getPopularPeople(page)
            .subscribeOn(Schedulers.io())
            .subscribe({
                callback.onResult(it.peopleList, null, page + 1)
                networkState.postValue(NetworkState.LOADED)
            }, {
                networkState.postValue(NetworkState.LOADED)
                Log.e(TAG,it.message)
            })
        )
    }

    /** this method called when user scrolls down to the end of list **/
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Person>) {
        networkState.postValue(NetworkState.LOADING)

        // perform the api call
        compositeDisposable.add(peopleService.getPopularPeople(params.key)
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (it.totalPages >= params.key){  // this means there is more data to fetch
                    callback.onResult(it.peopleList,  params.key + 1)
                    networkState.postValue(NetworkState.LOADED)
                }else{
                    networkState.postValue(NetworkState.END_OF_LIST)
                }
            }, {
                networkState.postValue(NetworkState.LOADED)
                Log.e(TAG,it.message)
            })
        )    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Person>) {

    }

}