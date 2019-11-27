package moviedb.com.moviedb.ui.views.listing

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import moviedb.com.moviedb.data.pojos.PersonListEntity
import moviedb.com.moviedb.data.repository.NetworkState

class ListViewModel(private val peopleRepository: PeoplePagedListRepository) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    var searchPagedList : LiveData<PagedList<PersonListEntity>>? = null
    private lateinit var query:String

    val peoplePagedList : LiveData<PagedList<PersonListEntity>> by lazy {
        peopleRepository.fetchLivaPopularPeoplePageList(compositeDisposable)
    }

    fun setQuery(query: String){
        this.query = query
        searchPagedList = peopleRepository.fetchLivaSearchPageList(compositeDisposable, query)
    }




    val networkState : LiveData<NetworkState> by lazy {
        peopleRepository.getNetworkState()
    }

    fun refresh(){
        peoplePagedList.value?.dataSource?.invalidate()
        searchPagedList?.value?.dataSource?.invalidate()
    }

    fun listIsEmpty() : Boolean{
        return peoplePagedList.value?.isEmpty() ?: true
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }



}