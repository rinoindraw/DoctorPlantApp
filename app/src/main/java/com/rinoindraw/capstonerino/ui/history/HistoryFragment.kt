package com.rinoindraw.capstonerino.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rinoindraw.capstonerino.R
import com.rinoindraw.capstonerino.databinding.FragmentHistoryBinding
import com.rinoindraw.capstonerino.ui.history.adapter.HistoryAdapter
import com.rinoindraw.capstonerino.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.toList

@Suppress("SameParameterValue")
@AndroidEntryPoint
@ExperimentalPagingApi
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var storyAdapter: HistoryAdapter

    private val historyViewModel: HistoryViewModel by viewModels()

    private lateinit var pref: SessionManager
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = SessionManager(requireContext())
        username = pref.getUsername().toString()

        initAction()
        initSwipeRefreshLayout()
        initStoryRecyclerView()
        getAllStories()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAction() {
        binding.apply {
            imgBack.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_historyFragment_to_navigation_home)
            )
        }
    }

    private fun getAllStories() {
        showLoading(true)
        lifecycleScope.launchWhenResumed {
            try {
                val result = historyViewModel.getHistory(username).toList().first()
                result.onSuccess { response ->
                    val historyResult = response.data
                    storyAdapter.setStories(historyResult.flatten())
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.history_load),
                        Toast.LENGTH_SHORT
                    ).show()
                    showLoading(false)
                }
                result.onFailure {
                    showLoading(false)
                    Snackbar.make(
                        binding.root,
                        getString(R.string.you_dont_have_history),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                showLoading(false)
                Snackbar.make(
                    binding.root,
                    getString(R.string.you_dont_have_history),
                    Snackbar.LENGTH_SHORT
                ).show()
            } finally {
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun initSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            getAllStories()
        }
    }

    private fun initStoryRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        storyAdapter = HistoryAdapter()

        binding.rvHistory.apply {
            adapter = storyAdapter
            layoutManager = linearLayoutManager
        }

        storyAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                linearLayoutManager.scrollToPosition(0)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.show() else binding.progressBar.gone()
    }

}