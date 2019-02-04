package me.tellvivk.smileme

import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.room.Room
import me.tellvivk.smileme.app.db.AppDatabase
import me.tellvivk.smileme.app.model.ImageRepository
import me.tellvivk.smileme.app.model.ImageRepositoryI
import me.tellvivk.smileme.app.screens.fullScreen.FullScreenImageViewModel
import me.tellvivk.smileme.app.screens.home.HomeViewModel
import me.tellvivk.smileme.dataSources.DummyImageDataSource
import me.tellvivk.smileme.dataSources.ImageDataSourceI
import me.tellvivk.smileme.dataSources.LocalImageDataSource
import me.tellvivk.smileme.dataSources.NetworkImageDataSource
import me.tellvivk.smileme.helpers.fileHelper.FileHelper
import me.tellvivk.smileme.helpers.fileHelper.FileHelperI
import me.tellvivk.smileme.helpers.imageHelper.ImageHelper
import me.tellvivk.smileme.helpers.imageHelper.ImageHelperI
import me.tellvivk.smileme.helpers.logger.AppLogger
import me.tellvivk.smileme.helpers.logger.LoggerI
import me.tellvivk.smileme.helpers.networkHelper.NetworkHelper
import me.tellvivk.smileme.helpers.networkHelper.NetworkHelperI
import me.tellvivk.smileme.helpers.stringFetcher.AppStringFetcher
import me.tellvivk.smileme.helpers.stringFetcher.StringFetcherI
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppModule {

    companion object {

        private val appModule = module {
            single<LoggerI> { AppLogger(BuildConfig.DEBUG) }
            single<StringFetcherI> { AppStringFetcher(androidContext()) }
            single<NetworkHelperI> { NetworkHelper(androidContext()) }
            single<FileHelperI> { FileHelper(androidContext()) }
            single<ImageHelperI> { ImageHelper() }
            single(name = "screenSize") { (windowManager: WindowManager) ->
                val displayMetrics = DisplayMetrics()

                windowManager.defaultDisplay.getMetrics(displayMetrics)
                Pair(displayMetrics.heightPixels, displayMetrics.widthPixels)
            }

            single<Retrofit> {
                Retrofit.Builder()
                    .baseUrl(BuildConfig.ROOT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            single<AppDatabase> {
                Room.databaseBuilder(
                    androidContext(), AppDatabase::class.java, "images-db"
                ).fallbackToDestructiveMigration()
                    .build()
            }
            single { get<AppDatabase>().getImagesDao() }

            single<ImageDataSourceI>("network") { NetworkImageDataSource(retrofit = get(),
                networkHelper = get()) }
            single<ImageDataSourceI>("local") {
                LocalImageDataSource(context = androidContext(), imageDao = get())
            }
            single<ImageDataSourceI>("dummy") { DummyImageDataSource() }

            single<ImageRepositoryI> {
                ImageRepository(
                    networkDataSource = get("network"),
                    localDataSource = get("local"), fileHelper = get()
                )
            }

        }

        private val homeModule = module {
            viewModel { (windowManager: WindowManager) ->
                HomeViewModel(
                    imagesRepo = get(),
                    stringFetcher = get(), fileHelper = get(),
                    screenSize = get("screenSize") { parametersOf(windowManager) }
                )
            }
        }

        private val fullScreenModule = module {
            viewModel { FullScreenImageViewModel() }
        }


        val modules = listOf(
            appModule,
            homeModule,
            fullScreenModule
        )
    }

}