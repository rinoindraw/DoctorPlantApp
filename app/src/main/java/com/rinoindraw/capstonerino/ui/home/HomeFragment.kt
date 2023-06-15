package com.rinoindraw.capstonerino.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rinoindraw.capstonerino.R
import com.rinoindraw.capstonerino.databinding.FragmentHomeBinding
import com.rinoindraw.capstonerino.ui.history.HistoryViewModel
import com.rinoindraw.capstonerino.ui.history.adapter.HistoryAdapter
import com.rinoindraw.capstonerino.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.toList

@OptIn(ExperimentalPagingApi::class)
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private lateinit var storyAdapter: HistoryAdapter

    private val historyViewModel: HistoryViewModel by viewModels()

    private lateinit var pref: SessionManager
    private lateinit var token: String
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(LayoutInflater.from(requireActivity()))
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = SessionManager(requireContext())
        token = pref.fetchAuthToken().toString()
        username = pref.getUsername().toString()


        initUI()
        initAction()

        initSwipeRefreshLayout()
        initStoryRecyclerView()
        getAllStories()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {

        binding?.tvGreetingName?.text = getString(R.string.label_greeting_user, pref.getUserName)

    }

    private fun initAction() {

        binding?.btnInsert?.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_insertFragment)
        )

        binding?.btnHistory?.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_historyFragment)
        )

    }

    private fun getAllStories() {
        lifecycleScope.launchWhenResumed {
            try {
                val result = historyViewModel.getHistory(username).toList().first()
                result.onSuccess { response ->
                    val historyResult = response.data
                    storyAdapter.setStories(historyResult.flatten())
                }
                result.onFailure {

                }
            } catch (_: Exception) {

            } finally {
                binding!!.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun initSwipeRefreshLayout() {
        binding!!.swipeRefresh.setOnRefreshListener {
            getAllStories()
        }
    }

    private fun initStoryRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        storyAdapter = HistoryAdapter()

        binding!!.rvHistory.apply {
            adapter = storyAdapter
            layoutManager = linearLayoutManager
        }

        storyAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                linearLayoutManager.scrollToPosition(0)
            }
        })
    }


}