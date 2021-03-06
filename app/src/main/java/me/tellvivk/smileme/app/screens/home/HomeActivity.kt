package me.tellvivk.smileme.app.screens.home

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import checkAllPermissions
import com.google.android.material.snackbar.Snackbar
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import dispatchTakePictureIntent
import kotlinx.android.synthetic.main.activity_home.*
import me.tellvivk.smileme.R
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
import me.tellvivk.smileme.helpers.stringFetcher.StringFetcherI
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class HomeActivity : BaseActivity(), BaseView {
    private lateinit var dataBinding: ActivityHomeBinding
    private val stringFetcher: StringFetcherI by inject()
    private lateinit var progressDialog: ProgressDialog
    private lateinit var homeAdapter: HomeImagesAdapter
    private lateinit var viewModel: HomeViewModel
    private val appLogger: LoggerI by inject()
    private var currentPhotoPath: String = ""
    private val requestImageCapture = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_home
        )
        initToolbar(stringFetcher.getString(R.string.str_home), toolbarHome)
        this.checkAllPermissions()
        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == requestImageCapture && resultCode == RESULT_OK) {
            viewModel.gotImage(currentPhotoPath)
        }
    }

    override fun initView() {
        viewModel = get { parametersOf(windowManager) }

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Processing...")
        progressDialog.setCancelable(false)

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
            checkPermissionAndDispatch()
        }
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
                    Snackbar.make(dataBinding.root,
                        stringFetcher.getString(me.tellvivk.smileme.R.string.str_network_error),
                        Snackbar.LENGTH_INDEFINITE).setAction("RETRY") {
                        viewModel.getImages()
                    }.show()
                }
                ScrollToTop -> {
                    recyclerHome.smoothScrollToPosition(0)
                }
                ShowBlockingProgress -> {
                    progressDialog.show()
                }
                HideBlockingProgress -> {
                    progressDialog.dismiss()
                }
                is ShowImageDescriptionDialog -> {
                    val confirmNewImageDialog = ConfirmNewImageDialog(this@HomeActivity,
                        bitmap = null, imageHelper = get(),
                        imagePath = this.selectedImagePath, callback =
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

    override fun getParentView(): BaseView? {
        return null
    }

    private val homeImagesAdapterInterface =
        object : HomeImagesAdapter.HomeImagesAdapterInterface {
            override fun onImageClicked(image: Image) {
                FullScreenActivity.start(this@HomeActivity, image)
            }
        }

    @Throws(IOException::class)
    private fun createTempImageFile(): File {
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

    private fun checkPermissionAndDispatch() {
        try {
            val permissionCheck =
                ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED){
                createTempImageFile()
                this.dispatchTakePictureIntent(
                    requestImageCapture,
                    File(currentPhotoPath)
                )
            } else{
                showToast(stringFetcher.getString(R.string.str_grant_storage_permissions))
            }
        } catch (ex: IOException) {
            showToast(ex.message.toString())
        }
    }

    companion object {
        fun start(activity: BaseActivity) {
            activity.startActivity(Intent(activity, HomeActivity::class.java))
        }
    }
}


