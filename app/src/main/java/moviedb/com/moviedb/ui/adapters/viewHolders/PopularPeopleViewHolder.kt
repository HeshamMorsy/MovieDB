package moviedb.com.moviedb.ui.adapters.viewHolders

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.cell_person.view.*
import moviedb.com.moviedb.R
import moviedb.com.moviedb.data.pojos.PersonEntity
import moviedb.com.moviedb.utilities.Constants

class PopularPeopleViewHolder(view : View) : RecyclerView.ViewHolder(view){

    fun bind(person: PersonEntity?, context: Context, publishSubject: PublishSubject<Int>){
        itemView.cell_person_name.text = person?.name
        Glide.with(context).load(Constants.IMAGE_BASE_URL + Constants.IMAGE_LIST_SIZE + person?.profilePath)
            .apply(RequestOptions().override(92,92).placeholder(R.drawable.placeholder))
            .into(itemView.cell_person_image)

        itemView.setOnClickListener {
            publishSubject.onNext(person!!.id)
        }
    }
}