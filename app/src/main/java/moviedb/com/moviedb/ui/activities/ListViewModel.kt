package moviedb.com.moviedb.ui.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import moviedb.com.moviedb.data.pojos.PersonEntity
import moviedb.com.moviedb.data.repository.NetworkState

class ListViewModel(private val peopleRepository: PeoplePagedListRepository) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val peoplePagedList : LiveData<PagedList<PersonEntity>> by lazy {
        peopleRepository.fetchLivaPopularPeoplePageList(compositeDisposable)
    }

    val networkState : LiveData<NetworkState> by lazy {
        peopleRepository.getNetworkState()
    }

    fun listIsEmpty() : Boolean{
        return peoplePagedList.value?.isEmpty() ?: true
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }



}