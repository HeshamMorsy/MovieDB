package moviedb.com.moviedb.ui.activities

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.cell_network_state.*
import moviedb.com.moviedb.R
import moviedb.com.moviedb.data.api.PopularPeopleClient
import moviedb.com.moviedb.data.api.PopularPeopleService
import moviedb.com.moviedb.data.repository.NetworkState
import moviedb.com.moviedb.ui.adapters.PopularPeoplePagedListAdapter

class ListActivity : AppCompatActivity() {
    private lateinit var viewModel: ListViewModel
    lateinit var peopleRepository: PeoplePagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val popularPeopleService: PopularPeopleService = PopularPeopleClient.getClient()
        peopleRepository = PeoplePagedListRepository(popularPeopleService)

        viewModel = getListViewModel()

        val peopleAdapter = PopularPeoplePagedListAdapter(this)
        val gridLayout = GridLayoutManager(this, 1)
//        gridLayout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                val viewType = peopleAdapter.getItemViewType(position)
//                return if (viewType == peopleAdapter.PERSON_TYPE) 1
//                else 1
//            }
//        }

        list_recycler.layoutManager = gridLayout
        list_recycler.setHasFixedSize(true)
        list_recycler.adapter = peopleAdapter

        viewModel.peoplePagedList.observe(this, Observer {
            peopleAdapter.submitList(it)
            if (swipe_refresh.isRefreshing) {
                swipe_refresh.isRefreshing = false
                // new paging library has a bug after refreshing
                // so I made a workaround to handle it by changing recycler visibility
                list_recycler.visibility = View.GONE
                Handler().postDelayed({
                    list_recycler.scrollToPosition(0)
                    list_recycler.visibility = View.VISIBLE
                }, 600)
            }
        })

        viewModel.networkState.observe(this, Observer {
            cell_network_state_progress_bar?.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            cell_network_state_error_text?.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty())
                peopleAdapter.setNetworkState(it)
        })


        swipe_refresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun getListViewModel(): ListViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UnCHECKED_CAST")
                return ListViewModel(peopleRepository) as T
            }
        })[ListViewModel::class.java]
    }
}
