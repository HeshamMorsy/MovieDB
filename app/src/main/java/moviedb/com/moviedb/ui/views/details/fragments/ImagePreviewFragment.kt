package moviedb.com.moviedb.ui.views.details.fragments


import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_image_preview.*
import moviedb.com.moviedb.R
import moviedb.com.moviedb.utilities.Constants
import moviedb.com.moviedb.utilities.PermissionHelper
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val IMAGE_URL = "image_url"

/**
 * A simple [Fragment] subclass.
 * Use the [ImagePreviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ImagePreviewFragment : Fragment() {
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageUrl = it.getString(IMAGE_URL)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(context!!).load(imageUrl)
            .apply(RequestOptions().placeholder(R.drawable.placeholder))
            .into(celebrity_image)
        saveImageAction()
    }

    private fun saveImageAction() {
        save_image.setOnClickListener {
            val isGranted = PermissionHelper.checkExternalStoragePermission(activity as Activity, context!!,this)
            if (isGranted)
                performSaving()
        }
    }

    private fun performSaving() {
        Glide.with(context!!)
            .asBitmap()
            .load(imageUrl)
            .listener(object : RequestListener<Bitmap> {
                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    val title = "${Calendar.getInstance().timeInMillis}.png"
                    val s = MediaStore.Images.Media.insertImage(
                        context?.contentResolver, resource, title, "image"
                    )
                        if (s != null && s.isNotEmpty()) {
                            Toast.makeText(
                                context,
                                context?.resources?.getString(R.string.saved_successfully),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else
                            Toast.makeText(
                                context,
                                context?.resources?.getString(R.string.saving_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.i(tag,e?.message+"")
                    return false
                }
            }).submit()
    }


    companion object {

        @JvmStatic
        fun newInstance(imageUrl: String) =
            ImagePreviewFragment().apply {
                this.arguments = Bundle().apply {
                    this.putString(IMAGE_URL, imageUrl)
                }
            }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissions[0] == Manifest.permission.WRITE_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            performSaving()
        } else {
            requestPermissions( arrayOf (Manifest.permission.WRITE_EXTERNAL_STORAGE), Constants.EXTERNAL_PERMISSION_CODE);
        }
    }


}
