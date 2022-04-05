package com.jasonsb.simplebible

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasonsb.simplebible.exts.debugList
import com.jasonsb.simplebible.network.Error
import com.jasonsb.simplebible.network.Success
import com.jasonsb.simplebible.network.syncRequest
import kotlinx.coroutines.launch
import timber.log.Timber

class BibleViewModel : ViewModel() {

    companion object {
        const val ERROR_NO_RESULTS_FOUND = 1
        const val ERROR_GENERIC_MSG = 2
    }

    private val _passagesLiveData = MutableLiveData<List<String>>()
    val passagesLiveData: LiveData<List<String>>
        get() = _passagesLiveData

    private val _errorMsgLiveData = MutableLiveData<Int>()
    val errorMsgLiveData: LiveData<Int>
        get() = _errorMsgLiveData

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean>
        get() = _isLoadingLiveData

    fun getEsvPassage(passage: String) {
        viewModelScope.launch {
            val result = Instance.getEsvApi()
                .getPassage(passage = passage, includeShortCopyright = false)
                .syncRequest()
            _isLoadingLiveData.value = false
            if (result is Success) {
                displayEsvPassages(result.data.passages)
            } else if (result is Error) {
                Timber.e(result.body.toString())
                _errorMsgLiveData.value = ERROR_GENERIC_MSG
            }
        }
    }

    private fun displayEsvPassages(passages: List<String>) {
        if (passages.isEmpty()) {
            _errorMsgLiveData.value = ERROR_NO_RESULTS_FOUND
        } else {
            debugList(passages, heading = "Passages:")
            _passagesLiveData.value = passages
        }
    }

    fun setIsLoading() {
        _isLoadingLiveData.value = true
    }
}