package moviedb.com.moviedb.data.repository.search

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.disposables.CompositeDisposable
import moviedb.com.moviedb.data.api.PopularPeopleService
import moviedb.com.moviedb.data.pojos.PersonEntity


class SearchPeopleDataSourceFactory(private val peopleService: PopularPeopleService,
                                    private val compositeDisposable: CompositeDisposable,
                                    private val query: String
) : DataSource.Factory<Int, PersonEntity>() {

    val searchPeopleLivaDataSource = MutableLiveData<SearchPeopleDataSource>()

    override fun create(): DataSource<Int, PersonEntity> {
        val peopleDataSource =
            SearchPeopleDataSource(peopleService, compositeDisposable, query)
        searchPeopleLivaDataSource.postValue(peopleDataSource)
        return peopleDataSource
    }
}