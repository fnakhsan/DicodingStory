package com.example.dicodingstory.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.dicodingstory.data.Repository
import com.example.dicodingstory.data.model.StoryModel
import com.example.dicodingstory.ui.adapter.StoryAdapter
import com.example.dicodingstory.utils.DataDummy
import com.example.dicodingstory.utils.LiveDataTestUtil.getOrAwaitValue
import com.example.dicodingstory.utils.MainDispatcherRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var repository: Repository
    private val dummy = DataDummy.generateDummyListStory()
    private val dummyToken =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWRFRXJsZVJsV211cGppM1giLCJpYXQiOjE2ODI3NjU3NDN9.AQnEXU3IoAqvsc0xWfCCaz6dfoa3V6pfZDMvaenDKNM"

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val data = StoryPagingSource.snapshot(dummy)
        val expectedStory = MutableLiveData<PagingData<StoryModel>>()

        expectedStory.value = data
        Mockito.`when`(repository.getAllStories(dummyToken)).thenReturn(expectedStory)

        val homeViewModel = HomeViewModel(repository)
        val actualStory = homeViewModel.getAllStories(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummy.size, differ.snapshot().size)
        assertEquals(dummy[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<StoryModel> = PagingData.from(emptyList())
        val expectedQuote = MutableLiveData<PagingData<StoryModel>>()

        expectedQuote.value = data
        Mockito.`when`(repository.getAllStories(dummyToken)).thenReturn(expectedQuote)

        val homeViewModel = HomeViewModel(repository)
        val actualStory: PagingData<StoryModel> =
            homeViewModel.getAllStories(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<StoryModel>>>() {
    companion object {
        fun snapshot(items: List<StoryModel>): PagingData<StoryModel> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryModel>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryModel>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}