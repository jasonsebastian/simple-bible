package com.jasonsb.simplebible

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.MediatorLiveData
import com.jasonsb.simplebible.databinding.ActivityMainBinding
import com.jasonsb.simplebible.esv.EsvParser
import com.jasonsb.simplebible.exts.makeClickable


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val bibleViewModel: BibleViewModel by viewModels()
    private val esvParser = EsvParser(this)

    private val resultLiveData = MediatorLiveData<CharSequence>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.initView()
        setupResultLiveData()
        observeLiveData()
    }

    private fun ActivityMainBinding.initView() {
        // TODO: Enable text to be copied
        searchButton.setOnClickListener {
            val passage = binding.searchField.text
            with(bibleViewModel) {
                if (passage.isEmpty()) {
                    // TODO: Show verse of the day
                    setScreenEmpty()
                } else {
                    setIsLoading()
                    setScreenEmpty()
                    getEsvPassage(passage = passage.toString())
                }
            }
        }
        result.makeClickable()
    }

    private fun setupResultLiveData() {
        with(resultLiveData) {
            addSource(bibleViewModel.passagesLiveData) {
                value = esvParser.parsePassages(it)
            }
            addSource(bibleViewModel.errorMsgLiveData) {
                value = getErrorMessageByCode(it)
            }
        }
    }

    private fun observeLiveData() {
        resultLiveData.observe(this) {
            binding.result.text = it
        }
        bibleViewModel.isLoadingLiveData.observe(this) {
            binding.progressBar.isVisible = it
        }
    }

    private fun getErrorMessageByCode(errorCode: Int): String {
        return when (errorCode) {
            BibleViewModel.ERROR_NO_RESULTS_FOUND -> {
                getString(R.string.error_msg)
            }
            BibleViewModel.ERROR_GENERIC_MSG -> {
                getString(R.string.something_went_wrong)
            }
            else -> {
                ""
            }
        }
    }

    private fun setScreenEmpty() {
        binding.result.text = ""
    }
}