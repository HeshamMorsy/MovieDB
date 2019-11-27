package moviedb.com.moviedb.ui.views.details

import android.annotation.SuppressLint
import android.net.Network
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_popular_details.*
import moviedb.com.moviedb.R
import moviedb.com.moviedb.data.api.PopularPeopleClient
import moviedb.com.moviedb.data.api.PopularPeopleService
import moviedb.com.moviedb.data.pojos.CelebrityDetails
import moviedb.com.moviedb.data.repository.NetworkState
import moviedb.com.moviedb.ui.views.listing.ListViewModel
import moviedb.com.moviedb.ui.views.listing.PeoplePagedListRepository
import moviedb.com.moviedb.utilities.Constants

class PopularDetailsActivity : AppCompatActivity() {
    private lateinit var viewModel: DetailsViewModel
    private lateinit var detailsRepository: DetailsRepository
    private var movieId: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_details)
        getMovieId()
        initComponents()
    }

    private fun initComponents() {
        val peopleService = PopularPeopleClient.getClient()
        detailsRepository = DetailsRepository(peopleService)
        viewModel = getDetailsViewModel()
        viewModel.details.observe(this, Observer {
            fillViews(it)
        })

        viewModel.networkState.observe(this, Observer {
            when(it){
                NetworkState.LOADING ->{details_progress.visibility = View.VISIBLE}
                NetworkState.LOADED ->{details_progress.visibility = View.GONE}
                NetworkState.ERROR ->{
                    Toast.makeText(this,it.msg,Toast.LENGTH_SHORT).show()
                    details_progress.visibility = View.GONE}
            }
        })

    }

    @SuppressLint("SetTextI18n")
    private fun fillViews(details: CelebrityDetails?) {
        popular_details_name_text.text = "Name : " + details?.name
        popular_details_gender.text =
            "Gender : " + if (details?.gender == 2) resources.getString(R.string.male) else resources.getString(R.string.female)
        popular_details_bd.text = "Birthday : " + details?.birthday
        popular_details_job.text = "Job : " + details?.knownForDepartment
        popular_details_popularity.text = "Popularity : " + details?.popularity
        popular_details_biography.text = details?.biography

        Glide.with(this).load(Constants.IMAGE_BASE_URL + Constants.IMAGE_LIST_SIZE + details?.profilePath).apply(
            RequestOptions().placeholder(R.drawable.placeholder).override(120,120))
            .into(popular_details_image)
    }

    private fun getMovieId() {
        val mIntent = intent
        movieId = mIntent.extras?.getInt(Constants.CELEBRITY_ID)
    }

    private fun getDetailsViewModel(): DetailsViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UnCHECKED_CAST")
                return DetailsViewModel(detailsRepository, movieId!!) as T
            }
        })[DetailsViewModel::class.java]
    }

}
