package me.tellvivk.smileme

import me.tellvivk.smileme.app.model.ImageRepository
import me.tellvivk.smileme.app.model.ImageRepositoryI
import me.tellvivk.smileme.app.screens.fullScreen.FullScreenImageViewModel
import me.tellvivk.smileme.app.screens.home.HomeViewModel
import me.tellvivk.smileme.dataSources.DummyImageDataSource
import me.tellvivk.smileme.dataSources.FileImageDataSource
import me.tellvivk.smileme.dataSources.ImageDataSourceI
import me.tellvivk.smileme.dataSources.NetworkImageDataSource
import me.tellvivk.smileme.helpers.logger.AppLogger
import me.tellvivk.smileme.helpers.logger.LoggerI
import me.tellvivk.smileme.helpers.networkHelper.NetworkHelper
import me.tellvivk.smileme.helpers.networkHelper.NetworkHelperI
import me.tellvivk.smileme.helpers.stringFetcher.AppStringFetcher
import me.tellvivk.smileme.helpers.stringFetcher.StringFetcherI
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppModule {

    companion object {

        private val appModule = module {
            single<LoggerI> { AppLogger(BuildConfig.DEBUG) }
            single<StringFetcherI> { AppStringFetcher(androidContext()) }
            single<NetworkHelperI> { NetworkHelper(androidContext()) }

            single<Retrofit> {
                Retrofit.Builder()
                    .baseUrl(BuildConfig.ROOT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            single<ImageDataSourceI>("network") { NetworkImageDataSource(retrofit = get()) }
            single<ImageDataSourceI>("file") { FileImageDataSource(androidContext()) }
            single<ImageDataSourceI>("dummy") { DummyImageDataSource() }

            single<ImageRepositoryI> { ImageRepository(networkDataSource = get("network")) }

        }

        private val homeModule = module {
            viewModel { HomeViewModel(imagesRepo = get(), stringFetcher = get()) }
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