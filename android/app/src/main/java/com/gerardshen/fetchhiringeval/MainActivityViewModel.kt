package com.gerardshen.fetchhiringeval

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.TreeMap
import javax.inject.Inject

/**
 * ViewModel for maintaining the main activity's state and external requests.
 */
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val messageService: FetchMessagingService
) : ViewModel() {

    sealed class MainScreenState() {
        data object Loading : MainScreenState()
        data object Error : MainScreenState()
        data class Finished(val itemMap: TreeMap<Int, MutableList<FetchItem>>?) : MainScreenState()
    }

    private val _state = MutableStateFlow<MainScreenState>(MainScreenState.Loading)
    val state: StateFlow<MainScreenState> get() = _state

    /**
     * Request the item list, updating the state based on the request status.
     */
    fun requestFetchItems() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = MainScreenState.Loading
            val items = messageService.requestFetchItems()
            if (null == items) {
                _state.value = MainScreenState.Error
            } else {
                _state.value = MainScreenState.Finished(items)
            }
        }
    }
}