package moviedb.com.moviedb.ui.adapters.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cell_network_state.view.*
import moviedb.com.moviedb.data.repository.NetworkState

class NetworkStateViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(networkState: NetworkState?) {
        if (networkState != null) {
            when (networkState) {
                NetworkState.LOADING -> itemView.cell_network_state_progress_bar.visibility = View.VISIBLE
                NetworkState.ERROR -> {
                    itemView.cell_network_state_progress_bar.visibility = View.VISIBLE
                    itemView.cell_network_state_error_text.text = networkState.msg
                }
                NetworkState.END_OF_LIST -> {
                    itemView.cell_network_state_progress_bar.visibility = View.GONE
                    itemView.cell_network_state_error_text.text = networkState.msg
                }
                else -> itemView.cell_network_state_progress_bar.visibility = View.GONE
            }


        }

    }
}