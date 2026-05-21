package com.example.mobiletest.ui.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobiletest.data.model.Booking
import com.example.mobiletest.data.repository.BookingRepository
import com.example.mobiletest.data.repository.BookingResult
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class BookingUiState {
    object Loading : BookingUiState()
    data class Success(val booking: Booking) : BookingUiState()
    data class Error(val message: String) : BookingUiState()
}

class BookingViewModel(private val repository: BookingRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<BookingUiState>(BookingUiState.Loading)
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    init {
        observeCache()
    }

    private fun observeCache() {
        viewModelScope.launch {
            repository.observeBooking().collect { result ->
                when (result) {
                    is BookingResult.Success -> {
                        _uiState.value = BookingUiState.Success(result.booking)
//                        android.util.Log.d("BookingViewModel-cache", "Booking data: ${result.booking}")
                        android.util.Log.d("BookingViewModel-cache", "Booking JSON: ${Gson().toJson(result.booking)}")
                    }
                    is BookingResult.Error -> {
                        if (_uiState.value is BookingUiState.Loading) {
                            _uiState.value = BookingUiState.Error(result.message)
                        }
                    }
                }
            }
        }
    }

    fun loadBooking() {
        viewModelScope.launch {
            val current = _uiState.value
            if (current !is BookingUiState.Success) {
                _uiState.value = BookingUiState.Loading
            }
            val result = repository.getBooking()
            when (result) {
                is BookingResult.Success -> {
                    _uiState.value = BookingUiState.Success(result.booking)
//                    android.util.Log.d("BookingViewModel-load", "Booking data: ${result.booking}")
                    android.util.Log.d("BookingViewModel-load", "Booking JSON: ${Gson().toJson(result.booking)}")
                }
                is BookingResult.Error -> {
                    if (_uiState.value !is BookingUiState.Success) {
                        _uiState.value = BookingUiState.Error(result.message)
                    }
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            val result = repository.refreshBooking()
//            when (result) {
//                is BookingResult.Success -> {
//                    _uiState.value = BookingUiState.Success(result.booking)
//                    android.util.Log.d("BookingViewModel", "Refreshed booking data: ${result.booking}")
//                }
//                is BookingResult.Error -> {
//                    if (_uiState.value !is BookingUiState.Success) {
//                        _uiState.value = BookingUiState.Error(result.message)
//                    }
//                }
//            }
        }
    }

    class Factory(private val repository: BookingRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            BookingViewModel(repository) as T
    }
}
