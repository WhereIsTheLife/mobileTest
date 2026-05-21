package com.example.mobiletest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.mobiletest.data.local.BookingDatabase
import com.example.mobiletest.data.repository.BookingRepository
import com.example.mobiletest.data.service.BookingService
import com.example.mobiletest.ui.booking.BookingListScreen
import com.example.mobiletest.ui.booking.BookingViewModel
import com.example.mobiletest.ui.theme.MobileTestTheme

class  MainActivity : ComponentActivity() {

    private val viewModel: BookingViewModel by viewModels {
        val db = BookingDatabase.getInstance(applicationContext)
        val service = BookingService(applicationContext)
        val repository = BookingRepository(service, db.bookingDao())
        BookingViewModel.Factory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BookingListScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadBooking()
    }
}
