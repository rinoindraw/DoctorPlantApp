package com.rinoindraw.capstonerino.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.rinoindraw.capstonerino.R
import com.rinoindraw.capstonerino.databinding.FragmentHomeBinding
import com.rinoindraw.capstonerino.utils.SessionManager


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private lateinit var pref: SessionManager
    private lateinit var token: String

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

        binding?.tvGreetingName?.text = getString(R.string.label_greeting_user, pref.getUserName)

        binding?.btnInsert?.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_insertFragment)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}