package moviedb.com.moviedb.data.repository.popularList

import android.content.Context
import androidx.lifecycle.MutableLiveData
import android.util.Log
import android.widget.Toast
import androidx.paging.PageKeyedDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moviedb.com.moviedb.data.api.PopularPeopleService
import moviedb.com.moviedb.data.pojos.PersonListEntity
import moviedb.com.moviedb.data.repository.NetworkState
import moviedb.com.moviedb.data.repository.search.SearchPeopleDataSource
import moviedb.com.moviedb.utilities.Constants.Companion.FIRST_PAGE


/** using android paging library to fetch data */
class PeopleDataSource(private val peopleService: PopularPeopleService,private val context: Context , private val compositeDisposable: CompositeDisposable)
    : PageKeyedDataSource<Int, PersonListEntity>() {

    private val tag : String = SearchPeopleDataSource::class.java.simpleName
    private var page = FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    /** this method is called when list loaded for first time **/
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, PersonListEntity>) {
        networkState.postValue(NetworkState.LOADING)

        // perform the api call
        compositeDisposable.add(peopleService.getPopularPeople(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                callback.onResult(it.peopleList, null, page + 1)
                networkState.postValue(NetworkState.LOADED)
            }, {
                Toast.makeText(context,it.message+"", Toast.LENGTH_SHORT).show()
                networkState.postValue(NetworkState.LOADED)
                Log.e(tag,it.message)
            })
        )
    }

    /** this method called when user scrolls down to the end of list **/
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PersonListEntity>) {
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
                Toast.makeText(context,it.message+"", Toast.LENGTH_SHORT).show()
                networkState.postValue(NetworkState.LOADED)
                Log.e(tag,it.message)
            })
        )    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PersonListEntity>) {

    }

}