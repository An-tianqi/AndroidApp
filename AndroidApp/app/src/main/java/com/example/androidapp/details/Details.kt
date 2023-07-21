package com.example.androidapp.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.androidapp.db.Film
import com.example.androidapp.db.TMDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Details(
    navController: NavController,
    filmId: String?,
    favoriteMovies: MutableList<Film>
) {
    val apiService = TMDB.create()
    var movieDetails by remember { mutableStateOf<Film?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(filmId) {
        if (!filmId.isNullOrBlank()) {
            coroutineScope.launch {
                val response = withContext(Dispatchers.IO) {
                    apiService.getMovieDetails(filmId)
                }
                movieDetails = response
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 16.dp),
            onClick = { navController.navigate("accueil") }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Back")
        }

        movieDetails?.let { movie ->
            Text(
                text = movie.title,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            val imageSize = "w300"
            val fullImageUrl = "https://image.tmdb.org/t/p/$imageSize/${movie.poster_path}"
            Image(
                painter = rememberImagePainter(fullImageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                text = movie.overview ?: "",
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "Votes: ${movie.vote_average}",
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (movieDetails != null && !favoriteMovies.contains(movieDetails)) {
                        favoriteMovies.add(movieDetails!!)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Add to Favorites")
            }
        }
    }
}
