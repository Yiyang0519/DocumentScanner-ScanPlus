package com.example.mobileapplicationassignment.frontEndUi.pdfLists

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults.textFieldColors
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileapplicationassignment.R
import com.example.mobileapplicationassignment.frontEndUi.common.ErrorScreen
import com.example.mobileapplicationassignment.frontEndUi.common.LoadingScreen
import com.example.mobileapplicationassignment.frontEndUi.screen.components.PdfLayout
import com.example.mobileapplicationassignment.frontEndUi.screen.components.RenameDeleteDialog
import com.example.mobileapplicationassignment.utils.showToast
import com.example.mobileapplicationassignment.frontEndUi.viewmodels.PdfViewModel
import com.example.mobileapplicationassignment.utils.Resource


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PdfListsScreen(pdfViewModel: PdfViewModel) {
    LoadingScreen(pdfViewModel = pdfViewModel)
    RenameDeleteDialog(pdfViewModel = pdfViewModel)

    val activity = LocalContext.current as Activity
    val context = LocalContext.current

    val pdfState by pdfViewModel.pdfStateFlow.collectAsState()
    val message = pdfViewModel.message

    var searchQuery by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("None") }

    val filterOptions = listOf("None", "Date", "Name", "Size")

    LaunchedEffect(Unit) {
        message.collect {
            when (it) {
                is Resource.Success -> {
                    context.showToast(it.data)
                }
                is Resource.Error -> {
                    context.showToast(it.message)
                }
                Resource.Idle -> {}
                Resource.Loading -> {}
            }
        }
    }

    // ... (scanner setup remains unchanged)

    Scaffold(
        topBar = @Composable {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                            contentDescription = "Dropdown",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "All Files",
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    // ... (scanner launch remains unchanged)
                },
                text = {
                    Text(text = stringResource(id = R.string.scan))
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = "camera"
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .padding(2.dp)
                    .offset(y = (-80).dp)
            )
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            // Search Bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                placeholder = {
                    Text(text = stringResource(id = R.string.search_pdf))
                },
                trailingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                        contentDescription = "Search Icon"
                    )
                },
                colors = textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp)
            )

            // Filter Dropdown
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Filter by: $selectedFilter")
                TextButton(onClick = { expanded = true }) {
                    Text("Select Filter")
                }
                androidx.compose.material3.DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    filterOptions.forEach { filterOption ->
                        androidx.compose.material3.DropdownMenuItem(
                            text = { Text(filterOption) },
                            onClick = {
                                selectedFilter = filterOption
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Display Result
            pdfState.DisplayResult(
                onLoading = {
                    // Loading UI
                },
                onSuccess = { pdfList ->
                    val filteredList = if (searchQuery.isEmpty()) {
                        pdfList
                    } else {
                        pdfList.filter { it.name.contains(searchQuery, ignoreCase = true) }
                    }

                    // Apply sorting based on selected filter
                    val sortedList = when (selectedFilter) {
                        "Date" -> filteredList.sortedByDescending { it.lastModifiedTime }
                        "Name" -> filteredList.sortedBy { it.name }
                        "Size" -> filteredList.sortedBy { it.size.toLongOrNull() ?: 0L }
                        else -> filteredList
                    }

                    if (sortedList.isEmpty()) {
                        ErrorScreen(message = "No PDF found")
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                                .padding(top = 8.dp)
                        ) {
                            items(items = sortedList, key = { pdfEntity -> pdfEntity.id }) { pdfEntity ->
                                PdfLayout(pdfEntity = pdfEntity, pdfViewModel = pdfViewModel)
                            }
                            // Add a large space at the bottom
                            item {
                                Spacer(modifier = Modifier.height(100.dp))
                            }
                        }
                    }
                },
                onError = {
                    ErrorScreen(message = it)
                }
            )
        }
    }
}


