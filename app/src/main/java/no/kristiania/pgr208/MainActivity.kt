package no.kristiania.pgr208

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import no.kristiania.pgr208.data.ProductRepository
import no.kristiania.pgr208.screens.favorite_list.FavoriteListScreen
import no.kristiania.pgr208.screens.favorite_list.FavoriteListViewModel
import no.kristiania.pgr208.screens.order_list.OrderHistoryScreen
import no.kristiania.pgr208.screens.order_list.OrderHistoryViewModel
import no.kristiania.pgr208.screens.product_details.ProductDetailsScreen
import no.kristiania.pgr208.screens.product_details.ProductDetailsViewModel
import no.kristiania.pgr208.screens.product_list.ProductListScreen
import no.kristiania.pgr208.screens.product_list.ProductListViewModel
import no.kristiania.pgr208.screens.shoppingCart_list.ShoppingCartScreen
import no.kristiania.pgr208.screens.shoppingCart_list.ShoppingCartViewModel
import no.kristiania.pgr208.theme.PGR208Theme

class MainActivity : ComponentActivity() {
    // ViewModels for different screens
    private val _productListViewModel: ProductListViewModel by viewModels()
    private val _favoriteListViewModel: FavoriteListViewModel by viewModels()
    private val _shoppingCartViewModel: ShoppingCartViewModel by viewModels()
    private val _productDetailsViewModel: ProductDetailsViewModel by viewModels()
    private val _orderHistoryViewModel: OrderHistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the database when the activity is created
        ProductRepository.initializeDatabase(applicationContext)

        setContent {
            PGR208Theme {
                // Apply the theme and create a NavController for navigation
                val navController = rememberNavController()

                // Set up navigation using NavHost with Jetpack Compose
                NavHost(
                    navController = navController,
                    startDestination = "productListScreen"
                ) {

                    // We add new destinations by using 'composable()', and give the destination a name ('route')
                    composable(
                        route = "productListScreen") {
                        // Destination for the product list screen
                        ProductListScreen(
                            viewModel = _productListViewModel,
                            onProductClick = { productId ->
                                navController.navigate("productDetailsScreen/$productId")
                            },
                            navigateToFavoriteList = {
                                navController.navigate("favoriteListScreen")
                            },
                            navigateToShoppingCart = {
                                navController.navigate("shoppingCartScreen")
                            },
                            navigateToOrderHistory = {
                                navController.navigate("orderHistoryScreen")
                            },
                        )
                    }


                    composable(
                        route = "productDetailsScreen/{productId}",
                        arguments = listOf(
                            navArgument(name = "productId") {
                                type = NavType.IntType
                            }
                        )
                    ) { backStackEntry ->
                        val productId = backStackEntry.arguments?.getInt("productId") ?: -1

                        // LaunchedEffect will run it's code block whenever 'productId' is updated
                        LaunchedEffect(productId) {
                            _productDetailsViewModel.setSelectedProduct(productId)
                        }

                        ProductDetailsScreen(
                            viewModel = _productDetailsViewModel,
                            onBackButtonClick = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = "favoriteListScreen") {
                        // LaunchedEffect will run it's code block first time we navigate to favoriteListScreen
                        LaunchedEffect(Unit) {
                            _favoriteListViewModel.loadFavorites()
                        }

                        FavoriteListScreen(
                            viewModel = _favoriteListViewModel,
                            onBackButtonClick = { navController.popBackStack() },
                            onProductClick = { productId ->
                                navController.navigate("productDetailsScreen/$productId")
                            }
                        )
                    }
                    composable(
                        route = "shoppingCartScreen") {
                        // LaunchedEffect will run it's code block first time we navigate to favoriteListScreen
                        LaunchedEffect(Unit) {
                            _shoppingCartViewModel.loadCart()
                        }

                        ShoppingCartScreen(
                            viewModel = _shoppingCartViewModel,
                            onBackButtonClick = { navController.popBackStack() },
                            onProductClick = { productId ->
                                navController.navigate("productDetailsScreen/$productId")
                            },
                            onOrderClick = {/*TODO*/}
                        )
                    }
                    composable(
                        route = "orderHistoryScreen") {
                        // LaunchedEffect will run it's code block first time we navigate to favoriteListScreen
                        LaunchedEffect(Unit) {
                            _orderHistoryViewModel.loadOrderHistory()
                        }

                        OrderHistoryScreen(
                            viewModel = _orderHistoryViewModel,
                            onBackButtonClick = { navController.popBackStack() },
                        )
                    }
            }
        }
    }
}}