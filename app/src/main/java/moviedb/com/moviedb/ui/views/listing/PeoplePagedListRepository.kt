package moviedb.com.moviedb.ui.views.listing

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import moviedb.com.moviedb.data.api.PopularPeopleService
import moviedb.com.moviedb.data.pojos.PersonListEntity
import moviedb.com.moviedb.data.repository.NetworkState
import moviedb.com.moviedb.data.repository.popularList.PeopleDataSource
import moviedb.com.moviedb.data.repository.popularList.PeopleDataSourceFactory
import moviedb.com.moviedb.data.repository.search.SearchPeopleDataSourceFactory
import moviedb.com.moviedb.utilities.Constants.Companion.PER_PAGE

class PeoplePagedListRepository(private val peopleService: PopularPeopleService, private val context: Context) {
    private lateinit var peoplePagedList: LiveData<PagedList<PersonListEntity>>
    private lateinit var peopleDataSourceFactory: PeopleDataSourceFactory

    private lateinit var searchPeoplePagedList: LiveData<PagedList<PersonListEntity>>
    private lateinit var searchPeopleDataSourceFactory: SearchPeopleDataSourceFactory

    fun fetchLivaSearchPageList(compositeDisposable: CompositeDisposable,query: String): LiveData<PagedList<PersonListEntity>> {
        searchPeopleDataSourceFactory =
            SearchPeopleDataSourceFactory(peopleService,context, compositeDisposable, query)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PER_PAGE)
            .build()

        searchPeoplePagedList = LivePagedListBuilder(searchPeopleDataSourceFactory, config).build()
        return searchPeoplePagedList
    }

    fun fetchLivaPopularPeoplePageList(compositeDisposable: CompositeDisposable): LiveData<PagedList<PersonListEntity>> {
        peopleDataSourceFactory =
            PeopleDataSourceFactory(peopleService,context, compositeDisposable)

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