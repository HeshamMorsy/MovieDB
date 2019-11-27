package moviedb.com.moviedb.ui.views.details

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_popular_details.*
import kotlinx.android.synthetic.main.fragment_image_preview.*
import moviedb.com.moviedb.R
import moviedb.com.moviedb.data.api.PopularPeopleClient
import moviedb.com.moviedb.data.pojos.CelebrityDetails
import moviedb.com.moviedb.data.pojos.CelebrityImageEntity
import moviedb.com.moviedb.data.repository.NetworkState
import moviedb.com.moviedb.models.responses.GetImagesResponse
import moviedb.com.moviedb.ui.adapters.ImagesAdapter
import moviedb.com.moviedb.ui.views.details.fragments.ImagePreviewFragment
import moviedb.com.moviedb.utilities.Constants

class PopularDetailsActivity : AppCompatActivity() {
    private val tag = PopularDetailsActivity::class.java.simpleName
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var viewModel: DetailsViewModel
    private lateinit var detailsRepository: DetailsRepository
    private lateinit var imagesAdapter: ImagesAdapter
    private var movieId: Int? = null
    private lateinit var imagesList: ArrayList<CelebrityImageEntity>
    private var imageUrl: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular_details)
        getCelebrityId()
        initRecycler()
        initComponents()
        observeOnOpenImageListener()
        attachListeners()
    }

    private fun observeOnOpenImageListener() {
        compositeDisposable.add(
            imagesAdapter.openImageListener.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    supportFragmentManager.beginTransaction().add(R.id.container, ImagePreviewFragment.newInstance(it))
                        .addToBackStack(null)
                        .commit()
                }, {
                    Log.i(tag, it.message + "")
                })
        )
    }

    private fun initRecycler() {
        imagesList = ArrayList()
        val gridLayout = GridLayoutManager(this, 4)
        imagesAdapter = ImagesAdapter(imagesList, this)
        popular_details_recycler.layoutManager = gridLayout
        popular_details_recycler.adapter = imagesAdapter
        popular_details_recycler.setHasFixedSize(true)
    }

    private fun initComponents() {
        val peopleService = PopularPeopleClient.getClient()
        detailsRepository = DetailsRepository(peopleService,this)
        viewModel = getDetailsViewModel()
    }

    private fun attachListeners() {
        popular_details_image.setOnClickListener {
            imagesAdapter.openImageListener.onNext(imageUrl!!)
        }

        viewModel.details.observe(this, Observer {
            imageUrl = Constants.IMAGE_BASE_URL + Constants.IMAGE_ORIGINAL_SIZE + it?.profilePath
            fillViews(it)
        })

        viewModel.images.observe(this, Observer {
            addImagesToAdapter(it)
        })

        viewModel.networkState.observe(this, Observer {
            when (it) {
                NetworkState.LOADING -> {
                    details_progress.visibility = View.VISIBLE
                }
                NetworkState.LOADED -> {
                    details_progress.visibility = View.GONE
                }
                NetworkState.ERROR -> {
                    Toast.makeText(this, it.msg, Toast.LENGTH_SHORT).show()
                    details_progress.visibility = View.GONE
                }
            }
        })
    }


    private fun addImagesToAdapter(it: GetImagesResponse?) {
        imagesList.clear()
        imagesList.addAll(it!!.profiles)
        imagesAdapter.notifyDataSetChanged()
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
            RequestOptions()/*.placeholder(R.drawable.placeholder)*/.override(120, 120)
        )
            .into(popular_details_image)
    }


    private fun getCelebrityId() {
        val mIntent = intent
        movieId = mIntent.extras?.getInt(Constants.CELEBRITY_ID)
    }

    /** method to get instance of DetailsViewModel**/
    private fun getDetailsViewModel(): DetailsViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UnCHECKED_CAST")
                return DetailsViewModel(detailsRepository, movieId!!) as T
            }
        })[DetailsViewModel::class.java]
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissions[0] == Manifest.permission.WRITE_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            save_image.performClick()
        }
    }


    /** disposing composite disposable **/
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}
