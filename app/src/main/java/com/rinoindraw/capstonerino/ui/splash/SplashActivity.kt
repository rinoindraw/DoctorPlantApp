package com.rinoindraw.capstonerino.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import com.rinoindraw.capstonerino.ui.auth.AuthActivity
import com.rinoindraw.capstonerino.ui.main.MainActivity
import com.rinoindraw.capstonerino.R
import com.rinoindraw.capstonerino.databinding.ActivitySplashBinding
import com.rinoindraw.capstonerino.utils.SessionManager
import com.rinoindraw.capstonerino.utils.UiConstValue
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!

    private lateinit var pref: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        initUserDirection()

    }

    private fun initUserDirection() {
        pref = SessionManager(this)
        val isLogin = pref.isLogin
        Handler(Looper.getMainLooper()).postDelayed({
            when {
                isLogin -> {
                    val splashContent: ConstraintLayout = findViewById(R.id.splash_content)
                    splashContent.alpha = 1f
                    splashContent.animate().setDuration(1000).alpha(0f).withEndAction {
                        val intentSplash = Intent(this, MainActivity::class.java)
                        startActivity(intentSplash)
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                        finish()
                    }
                }
                else -> {
                    val splashContent: ConstraintLayout = findViewById(R.id.splash_content)
                    splashContent.alpha = 1f
                    splashContent.animate().setDuration(1000).alpha(0f).withEndAction {
                        val intentSplash = Intent(this, AuthActivity::class.java)
                        startActivity(intentSplash)
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                        finish()
                    }
                }
            }
        }, UiConstValue.LOADING_TIME)
    }
}