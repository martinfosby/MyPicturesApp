/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.mypicturesapp.ui.home

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mypicturesapp.R
import com.example.mypicturesapp.data.network.PictureRemote
import com.example.mypicturesapp.model.PictureInterface
import com.example.mypicturesapp.ui.AppViewModelProvider
import com.example.mypicturesapp.ui.PicturesTopAppBar
import com.example.mypicturesapp.ui.navigation.NavigationDestination
import com.example.mypicturesapp.ui.theme.MyPicturesAppTheme
import kotlinx.coroutines.launch

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onShowClicked: (Int) -> Unit = {},
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            PicturesTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        HomeBody(
            homeNetworkUiState = viewModel.homeNetworkUiState,
            homeOfflineUiState = viewModel.homeOfflineUiState.collectAsState().value,
            retryAction = viewModel::getPhotosFromNetwork,
            onSaveClicked = {
                coroutineScope.launch {
                    viewModel.savePicture(it)
                }
            },
            onShowClicked = onShowClicked,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}

@Composable
fun HomeBody(
    modifier: Modifier = Modifier,
    homeNetworkUiState: HomeNetworkUiState,
    homeOfflineUiState: HomeOfflineUiState,
    retryAction: () -> Unit,
    onSaveClicked: (PictureInterface) -> Unit = {},
    onShowClicked: (Int) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    when (homeNetworkUiState) {
        is HomeNetworkUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is HomeNetworkUiState.Success -> {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
            ) {
                item {
                    ResultScreen(
                        photos = homeOfflineUiState.pictureList,
                        onShowClicked = onShowClicked,
                        onSaveClicked = onSaveClicked,
                        contentPadding=contentPadding,
                    )
                }
                item {
                    ResultScreen(
                        photos = homeNetworkUiState.photos,
                        onShowClicked = onShowClicked,
                        onSaveClicked = onSaveClicked,
                        contentPadding=contentPadding,
                    )
                }
            }
        }
        is HomeNetworkUiState.Error -> ErrorScreen(
            modifier = modifier.fillMaxSize(),
            retryAction = retryAction
        )
    }
}

/**
 * The home screen displaying the loading message.
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

/**
 * The home screen displaying error message with re-attempt button.
 */
@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

/**
 * ResultScreen displaying number of photos retrieved.
 */
@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    onSaveClicked: (PictureInterface) -> Unit = {},
    onShowClicked: (Int) -> Unit = {},
    photos: List<PictureInterface>,
    contentPadding: PaddingValues,
) {
    PhotosColumnScreen(
        photos,
        modifier,
        onSaveClicked = onSaveClicked,
        onShowClicked = onShowClicked,
        contentPadding = contentPadding
    )
}


@Composable
fun PhotosColumnScreen(
    photos: List<PictureInterface>,
    modifier: Modifier = Modifier,
    onSaveClicked: (PictureInterface) -> Unit = {},
    onShowClicked: (Int) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 4.dp),
        contentPadding = contentPadding,
    ) {
        items(items = photos, key = { photo -> photo.id }) { photo ->
            MyPictureCard(
                photo = photo,
                onSaveClicked = onSaveClicked,
                onShowClicked = onShowClicked,
                modifier = modifier
                    .padding(4.dp)
                    .fillMaxWidth()
//                    .aspectRatio(1.5f)
            )
        }
    }
}

@Composable
fun MyPictureCard(
    modifier: Modifier = Modifier,
    onSaveClicked: (PictureInterface) -> Unit = {},
    onShowClicked: (Int) -> Unit = {},
    photo: PictureInterface,
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.Start) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(photo.thumbnailUrl)
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(R.string.mars_photo),
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.small_padding))
            ) {
                Text(text = stringResource(id = R.string.title), style = MaterialTheme.typography.titleMedium)
                Text(text = photo.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row {
                    Text(text = stringResource(id = R.string.id), style = MaterialTheme.typography.titleMedium)
                    Text(text = photo.id.toString(), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Row(
                ) {
                    Button(onClick = { onShowClicked(photo.id) }) {
                        Text(text = stringResource(id = R.string.show))
                    }
                    Button(onClick = { onSaveClicked(photo) }) {
                        Text(text = stringResource(id = R.string.save))
                    }
                }

            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    MyPicturesAppTheme {
        LoadingScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    MyPicturesAppTheme {
        ErrorScreen({})
    }
}

@Preview(showBackground = true)
@Composable
fun PhotosGridScreenPreview() {
    MyPicturesAppTheme {
        val mockData = List(10) {
            PictureRemote(
                it,
                it,
                title = "accusamus beatae ad facilis cum similique qui sunt",
                url = "https://via.placeholder.com/600/92c952",
                thumbnailUrl = "https://via.placeholder.com/150/92c952"
            )
        }
        PhotosColumnScreen(mockData)
    }
}