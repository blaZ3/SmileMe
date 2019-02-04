package me.tellvivk.smileme

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import me.tellvivk.smileme.app.model.Image
import me.tellvivk.smileme.app.model.ImageRepository
import me.tellvivk.smileme.app.model.ImageRepositoryI
import me.tellvivk.smileme.dataSources.DataResponse
import me.tellvivk.smileme.dataSources.DummyImageDataSource
import me.tellvivk.smileme.dataSources.ImageDataSourceI
import me.tellvivk.smileme.dataSources.NetworkImageDataSource
import me.tellvivk.smileme.helpers.TestHelper
import me.tellvivk.smileme.helpers.networkHelper.NetworkHelperI
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ImageRepositoryTests {

    private lateinit var networkDataSource: ImageDataSourceI
    private lateinit var localDataSource: ImageDataSourceI
    private lateinit var imageRepo: ImageRepositoryI
    private var networkHelper: NetworkHelperI = mock()
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @Test
    fun `when imageRepo getImages called with dummyDataSource it should not fail and emit once`() {
        localDataSource = mock()
        whenever(localDataSource.loadImages()).thenReturn(Single.just(DataResponse(
            success = true, items = listOf<Image>()
        )))
        imageRepo = ImageRepository(
            networkDataSource = DummyImageDataSource(),
            localDataSource = localDataSource
        )


        val testObserver = imageRepo.getImages().test()
        Thread.sleep(TestHelper.DUMMY_DELAY + 100)
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
    }

    @Test
    fun `when getImages response should be false when not connected to network`() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(TestHelper.DUMMY_JSON)
        )

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        localDataSource = mock()
        whenever(localDataSource.loadImages()).thenReturn(Single.just(DataResponse(
            success = true, items = listOf<Image>()
        )))

        whenever(networkHelper.isConncected()).doReturn(false)
        networkDataSource = NetworkImageDataSource(
            retrofit = retrofit, networkHelper = networkHelper
        )

        imageRepo = ImageRepository(
            networkDataSource = networkDataSource, localDataSource = localDataSource
        )

        val networkTestObserver = networkDataSource.loadImages().test()
        Thread.sleep(100)
        networkTestObserver
            .assertValueCount(1)
            .assertResult(DataResponse(success = false, items = listOf<Image>()))

        val testObserver = imageRepo.getImages().test()
        Thread.sleep(100)
        testObserver
            .assertNoErrors()
    }

    @Test
    fun `when getImages called with 200 OK response and network connected`() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(TestHelper.DUMMY_JSON)
        )

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        localDataSource = mock()
        whenever(localDataSource.loadImages()).thenReturn(Single.just(DataResponse(
            success = true, items = listOf<Image>()
        )))

        whenever(networkHelper.isConncected()).doReturn(true)
        networkDataSource = NetworkImageDataSource(
            retrofit = retrofit, networkHelper = networkHelper
        )

        imageRepo = ImageRepository(
            networkDataSource = networkDataSource, localDataSource = localDataSource
        )

        val networkTestObserver = networkDataSource.loadImages().test()
        Thread.sleep(100)
        networkTestObserver
            .assertValueCount(1)
            .assertResult(
                DataResponse(
                success = true,
                items = TestHelper.getDummyImages()
            ))

        val testObserver = imageRepo.getImages().test()
        Thread.sleep(100)
        testObserver
            .assertNoErrors()
    }

    @Test
    fun `when getImages response should be false when 500 resoibse and connected to network`() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody(TestHelper.DUMMY_JSON)
        )

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        localDataSource = mock()
        whenever(localDataSource.loadImages()).thenReturn(Single.just(DataResponse(
            success = true, items = listOf<Image>()
        )))

        whenever(networkHelper.isConncected()).doReturn(true)
        networkDataSource = NetworkImageDataSource(
            retrofit = retrofit, networkHelper = networkHelper
        )

        imageRepo = ImageRepository(
            networkDataSource = networkDataSource, localDataSource = localDataSource
        )

        val networkTestObserver = networkDataSource.loadImages().test()
        Thread.sleep(100)
        networkTestObserver
            .assertValueCount(1)
            .assertResult(DataResponse(success = false))

        val testObserver = imageRepo.getImages().test()
        Thread.sleep(100)
        testObserver
            .assertNoErrors()
    }

    @After
    fun shutDown(){
        mockWebServer.shutdown()
    }

}