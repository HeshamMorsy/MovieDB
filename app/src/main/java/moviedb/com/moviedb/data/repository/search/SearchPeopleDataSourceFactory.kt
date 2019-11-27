package moviedb.com.moviedb.data.repository.search

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.disposables.CompositeDisposable
import moviedb.com.moviedb.data.api.PopularPeopleService
import moviedb.com.moviedb.data.pojos.PersonListEntity

/** Factory to provide us with search people data source*/
class SearchPeopleDataSourceFactory(private val peopleService: PopularPeopleService,
                                    private val context: Context,
                                    private val compositeDisposable: CompositeDisposable,
                                    private val query: String
) : DataSource.Factory<Int, PersonListEntity>() {

    private val searchPeopleLivaDataSource = MutableLiveData<SearchPeopleDataSource>()

    override fun create(): DataSource<Int, PersonListEntity> {
        val peopleDataSource =
            SearchPeopleDataSource(peopleService,context, compositeDisposable, query)
        searchPeopleLivaDataSource.postValue(peopleDataSource)
        return peopleDataSource
    }
}