/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.mypicturesapp.ui.details

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mypicturesapp.R
import com.example.mypicturesapp.data.network.AlbumRemote
import com.example.mypicturesapp.model.PictureInterface
import com.example.mypicturesapp.ui.AppViewModelProvider
import com.example.mypicturesapp.ui.PicturesTopAppBar
import com.example.mypicturesapp.ui.home.HomeDestination
import com.example.mypicturesapp.ui.navigation.NavigationDestination


object DetailsDestination : NavigationDestination {
    override val route = "details"
    override val titleRes = R.string.picture_details
    const val pictureIdArg = "Id"
    val routeWithArgs = "$route/{$pictureIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: DetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            PicturesTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        },
    ) { innerPadding ->
        DetailsBody(
            detailsUiState = viewModel.detailsUiState,
            retryAction = viewModel::getPhoto,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}

@Composable
fun DetailsBody(
    modifier: Modifier = Modifier,
    detailsUiState: DetailsUiState,
    retryAction: () -> Unit,
    onSaveClicked: () -> Unit = {},
    onShowClicked: (Int) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    when (detailsUiState) {
        is DetailsUiState.Loading -> DetailsLoadingScreen(modifier = modifier.fillMaxSize())
        is DetailsUiState.Success -> DetailsResultScreen(
            photo = detailsUiState.photo,
            album = detailsUiState.album,
            contentPadding = contentPadding,
        )
        is DetailsUiState.Error -> DetailsErrorScreen(
            modifier = modifier.fillMaxSize(),
            retryAction = retryAction
        )
    }
}

/**
 * The home screen displaying the loading message.
 */
@Composable
fun DetailsLoadingScreen(modifier: Modifier = Modifier) {
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
fun DetailsErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
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
fun DetailsResultScreen(
    modifier: Modifier = Modifier,
    photo: PictureInterface,
    album: AlbumRemote,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = contentPadding,
        modifier = modifier.fillMaxSize(),
    ) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
//                modifier = Modifier.fillMaxWidth()
            ) {
                PictureBox(
                    photo = photo,
                )
                Card {
                    Row {
                        Text(text = stringResource(id = R.string.id))
                        Text(text = photo.id.toString())
                    }
                    Row {
                        Text(text = stringResource(id = R.string.title))
                        Text(text = photo.title)
                    }
                    Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_padding)))
                    Row {
                        Text(text = stringResource(id = R.string.album_id))
                        Text(text = photo.albumId.toString())
                    }
                    Row {
                        Text(text = stringResource(id = R.string.album_title))
                        Text(text = album.title)
                    }
                }
            }

        }
    }
}



@Composable
fun PictureBox(
    modifier: Modifier = Modifier,
    photo: PictureInterface,
) {
    Box(
        modifier = Modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(photo.url)
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(R.string.mars_photo),
            contentScale = ContentScale.None,
        )
    }
}


@Preview
@Composable
fun SummaryPreview() {

}
