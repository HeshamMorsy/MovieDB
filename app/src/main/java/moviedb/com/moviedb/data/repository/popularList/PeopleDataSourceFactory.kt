package moviedb.com.moviedb.data.repository.popularList

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.disposables.CompositeDisposable
import moviedb.com.moviedb.data.api.PopularPeopleService
import moviedb.com.moviedb.data.pojos.PersonEntity


class PeopleDataSourceFactory (private val peopleService: PopularPeopleService,private val context: Context,private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, PersonEntity>() {

    val peopleLivaDataSource = MutableLiveData<PeopleDataSource>()

    override fun create(): DataSource<Int, PersonEntity> {
        val peopleDataSource =
            PeopleDataSource(peopleService,context, compositeDisposable)
        peopleLivaDataSource.postValue(peopleDataSource)
        return peopleDataSource
    }
}