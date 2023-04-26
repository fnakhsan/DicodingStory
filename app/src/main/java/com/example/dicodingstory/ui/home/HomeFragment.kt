package com.example.dicodingstory.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingstory.data.model.StoryModel
import com.example.dicodingstory.databinding.FragmentHomeBinding
import com.example.dicodingstory.ui.adapter.LoadingStateAdapter
import com.example.dicodingstory.ui.adapter.StoryAdapter
import com.example.dicodingstory.ui.detail.DetailActivity
import com.example.dicodingstory.ui.upload.UploadActivity
import com.example.dicodingstory.utils.ViewModelFactory

@OptIn(ExperimentalPagingApi::class)
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val homeViewModel: HomeViewModel by viewModels {
            factory
        }

        binding.apply {
            rvStory.layoutManager = layoutManager
            rvStory.addItemDecoration(itemDecoration)
            fabUpload.setOnClickListener {
                val intent = Intent(requireActivity(), UploadActivity::class.java)
                startActivity(intent)
            }
        }

        homeViewModel.getToken().observe(viewLifecycleOwner) { token ->
            if (token != null) {
                setListStories(homeViewModel, token)
            }
        }
    }

    private fun setListStories(homeViewModel: HomeViewModel, token: String) {
        val adapter = StoryAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        homeViewModel.getAllStories(token).observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)

            adapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
                override fun onItemClicked(data: StoryModel, optionsCompat: ActivityOptionsCompat) {
                    val intent = Intent(requireActivity(), DetailActivity::class.java)
                    intent.putExtra(EXTRA_ID, data.id)
                    startActivity(intent, optionsCompat.toBundle())
                }
            })

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EXTRA_ID = "id"
    }
}