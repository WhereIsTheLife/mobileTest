package com.example.mobiletest.ui.booking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobiletest.data.model.Booking
import com.example.mobiletest.data.model.Segment

@Composable
fun BookingListScreen(viewModel: BookingViewModel, modifier: Modifier = Modifier) {
//    LaunchedEffect(Unit) {
//        viewModel.loadBooking()
//    }

    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is BookingUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is BookingUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.refresh() }) {
                        Text("Retry")
                    }
                }
            }
        }
        is BookingUiState.Success -> {
            BookingContent(
                booking = state.booking,
                onRefresh = { viewModel.refresh() },
                modifier = modifier
            )
        }
    }
}

@Composable
private fun BookingContent(booking: Booking, onRefresh: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Booking", style = MaterialTheme.typography.headlineMedium)
            Button(onClick = onRefresh) {
                Text("Refresh")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Reference: ${booking.shipReference}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Token: ${booking.shipToken}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Duration: ${booking.duration}s", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Expiry: ${booking.expiryTime}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Segments", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(booking.segments, key = { it.id }) { segment ->
                SegmentCard(segment = segment)
            }
        }
    }
}

@Composable
private fun SegmentCard(segment: Segment) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = "Segment #${segment.id}", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(4.dp))
            val pair = segment.originAndDestinationPair
            Text(text = "From: ${pair.origin.displayName} (${pair.origin.code}), ${pair.originCity}")
            Text(text = "To:   ${pair.destination.displayName} (${pair.destination.code}), ${pair.destinationCity}")
        }
    }
}
