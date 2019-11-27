package moviedb.com.moviedb.ui.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import moviedb.com.moviedb.data.api.PopularPeopleService
import moviedb.com.moviedb.data.pojos.PersonEntity
import moviedb.com.moviedb.data.repository.NetworkState
import moviedb.com.moviedb.data.repository.PeopleDataSource
import moviedb.com.moviedb.data.repository.PeopleDataSourceFactory
import moviedb.com.moviedb.utilities.network.Constants.Companion.PER_PAGE
import java.util.concurrent.Executors

class PeoplePagedListRepository(private val peopleService: PopularPeopleService) {
    private lateinit var peoplePagedList: LiveData<PagedList<PersonEntity>>
    private lateinit var peopleDataSourceFactory: PeopleDataSourceFactory

    fun fetchLivaPopularPeoplePageList(compositeDisposable: CompositeDisposable): LiveData<PagedList<PersonEntity>> {
        peopleDataSourceFactory = PeopleDataSourceFactory(peopleService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PER_PAGE)
            .build()

        peoplePagedList = LivePagedListBuilder(peopleDataSourceFactory, config).build()
        return peoplePagedList
    }

    fun invalidateFactory(){
        peopleDataSourceFactory.create()
    }

    fun getNetworkState(): LiveData<NetworkState>{
        return Transformations.switchMap<PeopleDataSource, NetworkState>(
            peopleDataSourceFactory.peopleLivaDataSource, PeopleDataSource::networkState
        )
    }
}