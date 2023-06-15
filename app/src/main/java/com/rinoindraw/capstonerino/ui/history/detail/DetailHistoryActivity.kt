package com.rinoindraw.capstonerino.ui.history.detail

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.rinoindraw.capstonerino.R
import com.rinoindraw.capstonerino.database.model.History
import com.rinoindraw.capstonerino.databinding.ActivityDetailHistoryBinding
import com.rinoindraw.capstonerino.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportPostponeEnterTransition()

        val historyListJson = intent.getStringExtra(EXTRA_DETAIL_DATA)
        val historyList = Gson().fromJson(historyListJson, Array<History>::class.java)
        val story = historyList?.firstOrNull()
        initStoryData(story)

        initUI()
        initAction()
    }

    private fun initUI() {
        supportActionBar?.hide()
    }

    private fun initAction() {
        binding.apply {
            imgBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
                finish()
            }
        }
    }


    private fun initStoryData(story : History?) {
        if (story != null) {
            binding.apply {

                Glide
                    .with(this@DetailHistoryActivity)
                    .load(story.image)
                    .placeholder(R.drawable.capstone_logo_new)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            supportStartPostponedEnterTransition()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            supportStartPostponedEnterTransition()
                            return false
                        }
                    })
                    .into(ivImage)

                plantType.text = story.tanaman
                resultScan.text = story.penyakit
                descScan.text = story.deskripsi
                penyebabScan.text = story.penyebab
                solusiScan.text = story.solusi.joinToString("\n")
                sourceScan.text = story.source
                penulisScan.text = story.penulis

            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()

    }

    companion object {
        const val EXTRA_DETAIL_DATA = "extra_detail_data"
    }

}