package moviedb.com.moviedb.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.subjects.PublishSubject
import moviedb.com.moviedb.R
import moviedb.com.moviedb.data.pojos.CelebrityImageEntity
import moviedb.com.moviedb.ui.adapters.viewHolders.ImagesViewHolder

class ImagesAdapter (private val imagesList: ArrayList<CelebrityImageEntity>, private val context: Context)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return imagesList.size
    }

    val openImageListener: PublishSubject<String> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.cell_person, parent, false)
        return ImagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemPhoto = imagesList[position]
        (holder as ImagesViewHolder).bind(itemPhoto,context ,openImageListener)
    }


}