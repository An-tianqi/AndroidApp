package com.example.androidapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
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
fun Details(navController: NavController, filmId: String?) {
    val apiService = TMDB.create()
    var filmsDetails by remember { mutableStateOf<Film?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(filmId) {
        if (!filmId.isNullOrBlank()) {
            scope.launch {
                val response = withContext(Dispatchers.IO) {
                    apiService.getMovieDetails(filmId)
                }
                filmsDetails = response
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(
            modifier = Modifier.padding(16.dp),
            onClick = { navController.navigate("accueil") }
        ) {
            Text(text = "retour ")
        }
        filmsDetails?.let { film ->
            Text(text = film.title)
            val imageSize = "w300"
            val fullImageUrl = "https://image.tmdb.org/t/p/$imageSize/${film.poster_path}"
            Image(
                painter = rememberImagePainter(fullImageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = film.overview ?: "",
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Votes: ${film.vote_average}",
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}