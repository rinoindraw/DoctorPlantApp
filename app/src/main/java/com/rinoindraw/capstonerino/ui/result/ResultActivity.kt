package com.rinoindraw.capstonerino.ui.result

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rinoindraw.capstonerino.R
import com.rinoindraw.capstonerino.database.model.ResultData
import com.rinoindraw.capstonerino.database.model.UploadResponse
import com.rinoindraw.capstonerino.databinding.ActivityResultBinding
import com.rinoindraw.capstonerino.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportPostponeEnterTransition()

        val uploadResponse = intent.getParcelableExtra<UploadResponse>(EXTRA_RESULT_DATA)
        val story = uploadResponse?.data?.firstOrNull()
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
                super.onBackPressed()

                val intent = Intent(this@ResultActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun initStoryData(story: ResultData?) {
        if (story != null) {
            binding.apply {

                Glide
                    .with(this@ResultActivity)
                    .load(story.file)
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

                plantType.text = story.plant
                resultScan.text = story.result
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
        const val EXTRA_RESULT_DATA = "extra_result_data"
    }

}