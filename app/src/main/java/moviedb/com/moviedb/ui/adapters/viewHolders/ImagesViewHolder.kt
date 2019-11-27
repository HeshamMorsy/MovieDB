package moviedb.com.moviedb.ui.adapters.viewHolders

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.cell_person.view.*
import moviedb.com.moviedb.R
import moviedb.com.moviedb.data.pojos.CelebrityImageEntity
import moviedb.com.moviedb.utilities.Constants

class ImagesViewHolder(view : View) : RecyclerView.ViewHolder(view){

    fun bind(image: CelebrityImageEntity?, context: Context, publishSubject: PublishSubject<String>){
        val imageUrl = Constants.IMAGE_BASE_URL + Constants.IMAGE_ORIGINAL_SIZE + image?.filePath
        Glide.with(context).load(imageUrl)
            .apply(RequestOptions().placeholder(R.drawable.placeholder))
            .into(itemView.cell_person_image)

        itemView.setOnClickListener {
            publishSubject.onNext(imageUrl)
        }
    }
}