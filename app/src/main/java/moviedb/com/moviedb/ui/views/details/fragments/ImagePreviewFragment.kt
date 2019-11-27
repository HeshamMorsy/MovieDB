package moviedb.com.moviedb.ui.views.details.fragments


import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_image_preview.*
import moviedb.com.moviedb.R
import moviedb.com.moviedb.utilities.PermissionHelper
import java.lang.Exception

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
            val isGranted = PermissionHelper.checkExternalStoragePermission(activity as Activity, context!!)
            if (isGranted)
                performSaving()
        }
    }

    private fun performSaving() {

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
//            requestPermissions( <String>{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.EXTERNAL_PERMISSION_CODE);
        }
    }


}
