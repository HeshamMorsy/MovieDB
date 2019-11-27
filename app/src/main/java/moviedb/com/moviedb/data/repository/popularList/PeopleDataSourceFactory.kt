package moviedb.com.moviedb.data.repository.popularList

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.disposables.CompositeDisposable
import moviedb.com.moviedb.data.api.PopularPeopleService
import moviedb.com.moviedb.data.pojos.PersonListEntity


class PeopleDataSourceFactory (private val peopleService: PopularPeopleService,private val context: Context,private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, PersonListEntity>() {

    val peopleLivaDataSource = MutableLiveData<PeopleDataSource>()

    override fun create(): DataSource<Int, PersonListEntity> {
        val peopleDataSource =
            PeopleDataSource(peopleService,context, compositeDisposable)
        peopleLivaDataSource.postValue(peopleDataSource)
        return peopleDataSource
    }
}