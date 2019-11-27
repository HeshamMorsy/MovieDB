package moviedb.com.moviedb.ui.views.listing

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.cell_network_state.*
import moviedb.com.moviedb.R
import moviedb.com.moviedb.data.api.PopularPeopleClient
import moviedb.com.moviedb.data.api.PopularPeopleService
import moviedb.com.moviedb.data.repository.NetworkState
import moviedb.com.moviedb.ui.adapters.PopularPeoplePagedListAdapter
import moviedb.com.moviedb.ui.views.details.PopularDetailsActivity
import moviedb.com.moviedb.utilities.Constants

class ListActivity : AppCompatActivity() {
    private lateinit var viewModel: ListViewModel
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    lateinit var peopleRepository: PeoplePagedListRepository
    lateinit var peopleAdapter: PopularPeoplePagedListAdapter
    var searchView: SearchView? = null
    var searchMode: Boolean = false
    var refreshing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val popularPeopleService: PopularPeopleService = PopularPeopleClient.getClient()
        peopleRepository = PeoplePagedListRepository(popularPeopleService,this)

        viewModel = getListViewModel()

        peopleAdapter = PopularPeoplePagedListAdapter(this)

        observeOnOpenDetails()

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
            if (!searchMode) {
                peopleAdapter.submitList(it)
                if (swipe_refresh.isRefreshing || refreshing) {
                    swipe_refresh.isRefreshing = false
                    refreshing = true
                    // new paging library has a bug after refreshing
                    // so I made a workaround to handle it by changing recycler visibility
                    list_recycler.visibility = View.GONE
                    Handler().postDelayed({
                        list_recycler.scrollToPosition(0)
                        list_recycler.visibility = View.VISIBLE
                    }, 600)
                }
            }
        })




        viewModel.networkState.observe(this, Observer {
            cell_network_state_progress_bar?.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE

            if (viewModel.listIsEmpty() && it == NetworkState.ERROR) {
                Toast.makeText(this, it.msg, Toast.LENGTH_SHORT).show()
                cell_network_state_error_text?.visibility = View.VISIBLE
            } else
                cell_network_state_error_text?.visibility = View.GONE

            if (!viewModel.listIsEmpty())
                peopleAdapter.setNetworkState(it)
        })


        swipe_refresh.setOnRefreshListener {
            refreshing = true
            if (searchView != null && searchView?.query!!.isEmpty())
                searchMode = false
            viewModel.refresh()
        }
    }

    private fun observeOnOpenDetails() {
        compositeDisposable.add(peopleAdapter.openDetailsPublisher.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val mIntent = Intent(this, PopularDetailsActivity::class.java)
                mIntent.putExtra(Constants.CELEBRITY_ID, it)
                startActivity(mIntent)
            })
    }

    private fun startSearchObserve() {
        viewModel.searchPagedList.observe(this, Observer {
            if (searchMode) {
                peopleAdapter.submitList(it)
                if (swipe_refresh.isRefreshing || refreshing) {
                    swipe_refresh.isRefreshing = false
                    refreshing = true
                    // new paging library has a bug after refreshing
                    // so I made a workaround to handle it by changing recycler visibility
                    list_recycler.visibility = View.GONE
                    Handler().postDelayed({
                        list_recycler.scrollToPosition(0)
                        list_recycler.visibility = View.VISIBLE
                    }, 600)
                }
            }
        })
    }

    private fun getListViewModel(): ListViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UnCHECKED_CAST")
                return ListViewModel(peopleRepository) as T
            }
        })[ListViewModel::class.java]
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = searchItem?.actionView as SearchView

        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Toast like print
                if (!searchView!!.isIconified) {
                    searchView!!.isIconified = true
                }
                searchItem.collapseActionView()
                performSearch(query)
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                performSearch(s)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun performSearch(s: String) {
        if (s.isNotEmpty()) {
            searchMode = true
            viewModel.setQuery(s)
            startSearchObserve()
        } else {
            searchMode = false
            viewModel.refresh()
            refreshing = true
        }
    }


    /** dispose compositeDisposable to avoid memory leak **/
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
