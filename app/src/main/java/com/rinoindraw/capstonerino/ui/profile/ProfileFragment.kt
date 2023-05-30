package com.rinoindraw.capstonerino.ui.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.rinoindraw.capstonerino.R
import com.rinoindraw.capstonerino.databinding.FragmentProfileBinding
import com.rinoindraw.capstonerino.ui.auth.AuthActivity
import com.rinoindraw.capstonerino.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var pref: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = SessionManager(requireContext())

        initUI()
        initAction()

    }

    private fun initUI() {
        binding.tvName.text = pref.getUserName
        binding.tvEmail.text = pref.getEmail

    }

    private fun initAction() {

        binding.apply {

            btnLogout.setOnClickListener {
                initLogoutDialog()
            }

            imgLanguage.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initLogoutDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle(getString(R.string.message_logout_confirm))
            ?.setPositiveButton(getString(R.string.action_yes)) { _, _ ->
                pref.clearAuthToken()
                pref.clearPreferences()
                val intent = Intent(requireContext(), AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                requireActivity().finish()
            }
            ?.setNegativeButton(getString(R.string.action_cancel), null)
        val alert = alertDialog.create()
        alert.show()
    }



}
