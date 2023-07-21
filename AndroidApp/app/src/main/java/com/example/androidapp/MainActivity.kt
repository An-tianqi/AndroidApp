package com.example.androidapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidapp.db.Film
import com.example.androidapp.details.Details
import com.example.androidapp.favoris.Favoris
import com.example.androidapp.accueil.Accueil

class MainActivity : AppCompatActivity() {
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController: NavHostController = rememberNavController()
            val favorites = mutableListOf<Film>()

            // Set up the navigation graph
            NavHost(navController, startDestination = "accueil") {
                composable("accueil") {
                    Accueil(navController)
                }
                composable("details/{filmId}") { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getString("filmId")
                    Details(navController, movieId, favorites)
                }
                composable("favoris") {
                    Favoris(navController, favorites)
                }
            }

        }
    }
}