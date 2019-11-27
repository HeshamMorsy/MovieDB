package moviedb.com.moviedb.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.cell_network_state.view.*
import moviedb.com.moviedb.R
import moviedb.com.moviedb.data.pojos.PersonEntity
import moviedb.com.moviedb.data.repository.NetworkState
import moviedb.com.moviedb.ui.adapters.viewHolders.NetworkStateViewHolder
import moviedb.com.moviedb.ui.adapters.viewHolders.PopularPeopleViewHolder

class PopularPeoplePagedListAdapter(val context: Context) :
    PagedListAdapter<PersonEntity, RecyclerView.ViewHolder>(PopularPeopleDiffCallback()) {
    val PERSON_TYPE = 1
    val NETWORK_TYPE = 2
    private var networkState: NetworkState? = null

    val openDetailsPublisher: PublishSubject<Int> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        return if (viewType == PERSON_TYPE) {
            view = layoutInflater.inflate(R.layout.cell_person, parent, false)
            PopularPeopleViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.cell_network_state, parent, false)
            NetworkStateViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == PERSON_TYPE) {
            (holder as PopularPeopleViewHolder).bind(getItem(position), context, openDetailsPublisher)
        } else {
            (holder as NetworkStateViewHolder).bind(networkState)
        }
    }


    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }


    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }


    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_TYPE
        } else {
            PERSON_TYPE
        }
    }

    class PopularPeopleDiffCallback : DiffUtil.ItemCallback<PersonEntity>() {
        override fun areItemsTheSame(oldItem: PersonEntity, newItem: PersonEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PersonEntity, newItem: PersonEntity): Boolean {
            return oldItem == newItem
        }

    }


     fun setNetworkState(networkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()

        this.networkState = networkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow)
                notifyItemRemoved(super.getItemCount())   // remove the progress bar
            else
                notifyItemInserted(super.getItemCount())  // add the progress bar
        }else if (hasExtraRow && previousState != networkState)
            notifyItemChanged(itemCount-1)

    }
}