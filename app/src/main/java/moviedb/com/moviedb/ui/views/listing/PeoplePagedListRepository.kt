package moviedb.com.moviedb.ui.views.listing

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import moviedb.com.moviedb.data.api.PopularPeopleService
import moviedb.com.moviedb.data.pojos.PersonEntity
import moviedb.com.moviedb.data.repository.NetworkState
import moviedb.com.moviedb.data.repository.popularList.PeopleDataSource
import moviedb.com.moviedb.data.repository.popularList.PeopleDataSourceFactory
import moviedb.com.moviedb.data.repository.search.SearchPeopleDataSourceFactory
import moviedb.com.moviedb.utilities.Constants.Companion.PER_PAGE

class PeoplePagedListRepository(private val peopleService: PopularPeopleService) {
    private lateinit var peoplePagedList: LiveData<PagedList<PersonEntity>>
    private lateinit var peopleDataSourceFactory: PeopleDataSourceFactory

    private lateinit var searchPeoplePagedList: LiveData<PagedList<PersonEntity>>
    private lateinit var searchPeopleDataSourceFactory: SearchPeopleDataSourceFactory

    fun fetchLivaSearchPageList(compositeDisposable: CompositeDisposable,query: String): LiveData<PagedList<PersonEntity>> {
        searchPeopleDataSourceFactory =
            SearchPeopleDataSourceFactory(peopleService, compositeDisposable, query)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PER_PAGE)
            .build()

        searchPeoplePagedList = LivePagedListBuilder(searchPeopleDataSourceFactory, config).build()
        return searchPeoplePagedList
    }

    fun fetchLivaPopularPeoplePageList(compositeDisposable: CompositeDisposable): LiveData<PagedList<PersonEntity>> {
        peopleDataSourceFactory =
            PeopleDataSourceFactory(peopleService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PER_PAGE)
            .build()

        peoplePagedList = LivePagedListBuilder(peopleDataSourceFactory, config).build()
        return peoplePagedList
    }


    fun getNetworkState(): LiveData<NetworkState>{
        return Transformations.switchMap<PeopleDataSource, NetworkState>(
            peopleDataSourceFactory.peopleLivaDataSource, PeopleDataSource::networkState
        )
    }
}