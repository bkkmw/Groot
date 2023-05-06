package com.chocobi.groot.view.pot

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.chocobi.groot.MainActivity
import com.chocobi.groot.R
import com.chocobi.groot.data.PERMISSION_GALLERY

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PotDiaryCreateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PotDiaryCreateFragment : Fragment() {

    private val TAG = "PotDiaryCreateFragment"
    private var param1: String? = null
    private var param2: String? = null

    private var myImageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mActivity = activity as MainActivity
        val rootView = inflater.inflate(R.layout.fragment_pot_diary_create, container, false)

//        사진 첨부 취소 버튼
        val attachCancleBtn = rootView.findViewById<ImageButton>(R.id.attachCancleBtn)
//        사진 첨부 섹션
        val attachPhotoSection = rootView.findViewById<LinearLayout>(R.id.attachPhotoSection)
//        첨부된 이미지 섹션
        val attachedPhotoSection = rootView.findViewById<ConstraintLayout>(R.id.attachedPhotoSection)
        val attachedPhoto = rootView.findViewById<ImageView>(R.id.attachedPhoto)

        myImageView = rootView.findViewById(R.id.attachedPhoto)

        Log.d(TAG, "onCreateView() 포토 이니셜라이즈")
//        사진 첨부하기
//        attachPhotoSection.setOnClickListener {
//            mActivity.requirePermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_GALLERY)
////            attachedPhotoSection.visibility = View.VISIBLE
//        }
        attachPhotoSection.setOnClickListener {
            mActivity.setGalleryStatus("pot_diary_create")
            mActivity.requirePermissions(
                arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                PERMISSION_GALLERY
            )
        }
        attachCancleBtn.setOnClickListener {
            attachPhotoSection.visibility = View.VISIBLE
            attachedPhotoSection.visibility = View.GONE
        }

        return rootView
    }

    fun attachPhoto(uri: Uri) {
        Log.d(TAG, "attachPhoto() 사진을 첨부합니다 $uri")
        myImageView?.setImageURI(uri)
    }

    fun getPhotoImageView(): ImageView? {
        return myImageView
    }

}