package me.tellvivk.smileme.app.screens.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import kotlinx.android.synthetic.main.activity_home.*
import me.tellvivk.smileme.app.base.BaseActivity
import me.tellvivk.smileme.app.base.BaseView
import me.tellvivk.smileme.app.base.StateModel
import me.tellvivk.smileme.app.base.ViewEvent
import me.tellvivk.smileme.app.model.Image
import me.tellvivk.smileme.app.screens.fullScreen.FullScreenActivity
import me.tellvivk.smileme.app.screens.home.adapter.HomeImagesAdapter
import me.tellvivk.smileme.app.screens.home.adapter.ImagesListDiffCallback
import me.tellvivk.smileme.app.screens.home.confirmImage.ConfirmNewImageDialog
import me.tellvivk.smileme.app.screens.home.confirmImage.ConfirmNewImageDialogInterface
import me.tellvivk.smileme.databinding.ActivityHomeBinding
import me.tellvivk.smileme.helpers.logger.LoggerI
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class HomeActivity : BaseActivity(), BaseView {

    private lateinit var viewModel: HomeViewModel
    private lateinit var homeAdapter: HomeImagesAdapter

    private lateinit var dataBinding: ActivityHomeBinding

    private val REQUEST_IMAGE_CAPTURE = 1
    private var currentPhotoPath: String = ""

    private val appLogger: LoggerI by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, me.tellvivk.smileme.R.layout.activity_home)
        initView()
    }

    override fun initView() {
        viewModel = get { parametersOf(windowManager) }

        homeAdapter = HomeImagesAdapter(
            listOf(), this, homeImagesAdapterInterface,
            imageHelper = get()
        )
        recyclerHome.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        recyclerHome.setHasFixedSize(true)
        recyclerHome.adapter = homeAdapter

        viewModel.getViewModelObservable()
            .autoDisposable(AndroidLifecycleScopeProvider.from(this))
            .subscribe { updateView(it) }

        viewModel.getViewEventObservable()
            .autoDisposable(AndroidLifecycleScopeProvider.from(this))
            .subscribe { handleEvent(it) }

        fabAddNewPic.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    override fun getParentView(): BaseView? {
        return null
    }

    override fun updateView(stateModel: StateModel) {
        appLogger.d("updateView", stateModel.toString())
        (stateModel as HomeStateModel).apply {

            val updatedItems = ArrayList<Image>()
            updatedItems.addAll(stateModel.images)

            val diffResult = DiffUtil.calculateDiff(
                ImagesListDiffCallback(
                    homeAdapter.items,
                    updatedItems
                )
            )
            diffResult.dispatchUpdatesTo(homeAdapter)
            homeAdapter.items = stateModel.images


            dataBinding.stateModel = this
        }
    }

    override fun handleEvent(event: ViewEvent) {
        appLogger.d("handleEvent", event.toString())
        (event as HomeViewEvent).apply {
            when (this) {
                InitHomeEvent -> {
                    viewModel.getImages()
                }
                is LoadingErrorEvent -> {
                    showToast(this.msg)
                }
                is ShowImageDescriptionDialog -> {
                    val confirmNewImageDialog = ConfirmNewImageDialog(this@HomeActivity,
                        bitmap = this.selectedImageThumbNail, callback =
                        object : ConfirmNewImageDialogInterface {
                            override fun confirmed(title: String, comment: String) {
                                viewModel.saveImage(title = title, comment = comment)
                            }
                        })
                    confirmNewImageDialog.show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            viewModel.gotImage(currentPhotoPath)
        }
    }

    private val homeImagesAdapterInterface =
        object : HomeImagesAdapter.HomeImagesAdapterInterface {
            override fun onImageClicked(image: Image) {
                FullScreenActivity.start(this@HomeActivity, image)
            }
        }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    showToast(ex.message.toString())
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "me.tellvivk.smileme.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
        }
    }


    companion object {
        fun start(activity: BaseActivity) {
            activity.startActivity(Intent(activity, HomeActivity::class.java))
        }
    }
}
