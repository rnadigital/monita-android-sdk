package com.rnadigital.monita_android.navControler

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rnadigital.monita_android.MainActivity
import com.rnadigital.monita_android.ProductDetailsScreen
import com.rnadigital.monita_android.ecoomerce.ProductGridScreen

sealed class Screen(val route: String) {
    object ProductGrid : Screen("productGrid")
    object ProductDetails : Screen("productDetails/{productId}/{productName}/{productPrice}/{productUrl}") {
        fun createRoute(productId: Int, productName: String, productPrice: String, productUrl: String): String {
            return "productDetails/$productId/${productName.replace(" ", "%20")}/${productPrice.replace("$", "%24")}/${productUrl.replace("/", "%2F")}"
        }
    }
}

@Composable
fun AppNavGraph(navController: NavHostController ,mainActivity: MainActivity) {
    NavHost(navController = navController, startDestination = Screen.ProductGrid.route) {
        composable(Screen.ProductGrid.route) {
            ProductGridScreen { product ->
                navController.navigate(
                    Screen.ProductDetails.createRoute(
                        product.id,
                        product.name,
                        product.price,
                        product.Url
                    )
                )
            }
        }
        composable(
            route = Screen.ProductDetails.route,
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType },
                navArgument("productName") { type = NavType.StringType },
                navArgument("productPrice") { type = NavType.StringType },
                navArgument("productUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            val productName = backStackEntry.arguments?.getString("productName") ?: ""
            val productPrice = backStackEntry.arguments?.getString("productPrice") ?: ""
            val productUrl = backStackEntry.arguments?.getString("productUrl") ?: ""

            ProductDetailsScreen(productId, productName, productPrice, productUrl,
                onAddToCartClick = { id, name, price ->
                    mainActivity.logFirebaseAddToCart(id, name, price)
                    mainActivity.logFacebookAddToCart(id, name, price)
                    mainActivity.trackAddToCartEvent(id, name, price ,1)
                    mainActivity.showAd()
                })
        }
    }
}
