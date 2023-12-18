package no.kristiania.pgr208.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import no.kristiania.pgr208.data.room.AppDatabase
import no.kristiania.pgr208.data.room.Favorite
import no.kristiania.pgr208.data.room.Order
import no.kristiania.pgr208.data.room.ShoppingCart
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProductRepository {

    // To use retrofit we need to set up a HttpClient-object
    // Adding a HttpLoggingInterceptor to log Http requests
    // and responses to LogCat for easier debugging
    private val _httpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()

    // Creating our Retrofit client -
    // We connect the API with our baseUrl and adding the
    // GsonConverterFactor to the client. This way it can automatically parse the
    // JSON-response from Retrofit/Http into our data classes
    private val _retrofit =
        Retrofit.Builder()
            .client(_httpClient)
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // We use Retrofit to create an instance of our productService,
    // this way we can call the functions that requests data from the API
    private val _productService = _retrofit.create(ProductService::class.java)

    private lateinit var _appDatabase: AppDatabase
    private val _productDao by lazy { _appDatabase.productDao() }
    private val _favoriteDao by lazy { _appDatabase.favoriteDao() }
    private val _shoppingCartDao by lazy { _appDatabase.shoppingCartDao() }
    private val _orderDao by lazy { _appDatabase.OrderDao() }

    fun initializeDatabase(context: Context) {
        _appDatabase = Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "product-database"
        ).fallbackToDestructiveMigration().build()
    }

    //Suspend function to get the list of products from the API
    // and insert to our database
    suspend fun getProducts(): List<Product> {
        try {
            val response = _productService.getAllProducts()
            // Firstly we have to check if the response is successful.
            if(response.isSuccessful) {
                val products = response.body()?.products ?: emptyList()

                _productDao.insertProducts(products)

                // We get the data from the database making our app
                // possible to use offline
                return _productDao.getProducts()
            } else {
                //Error-message that displays if its not successful
                throw Exception("Response was not successful")
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Failed to get list of products", e)
            return _productDao.getProducts()
        }
    }

    suspend fun getProductById(productId: Int): Product? {
        return _productDao.getProductById(productId)
    }

    suspend fun getProductsByIds(idList: List<Int>): List<Product> {
        return _productDao.getProductsByIds(idList)
    }

    suspend fun getProductsByCategory(categoryList: List<String>): List<Product>{
        return _productDao.getProductsByCategory(categoryList)
    }

    // --- Favorite Functions ---

    suspend fun getFavorites(): List<Favorite> {
        return _favoriteDao.getFavorites()
    }

    suspend fun addFavorite(favorite: Favorite) {
        _favoriteDao.insertFavorite(favorite)
    }

    suspend fun removeFavorite(favorite: Favorite){
        _favoriteDao.removeFavorite(favorite)
    }
    // --- ShoppingCart Functions ---

    suspend fun getShoppingCart(): List<ShoppingCart> {
        return _shoppingCartDao.getShoppingCart()
    }

    suspend fun addToShoppingCart(shoppingCart: ShoppingCart) {
        _shoppingCartDao.insertProductToCart(shoppingCart)
    }

    suspend fun removeFromCart(shoppingCart: ShoppingCart){
        _shoppingCartDao.removeFromCart(shoppingCart)
    }

    suspend fun getCartIdByProductId(productId: Int): Int? {
        return _shoppingCartDao.getCartIdByProductId(productId)
    }

    // --- OrderHistory Functions ---

    suspend fun getOrderHistory(): List<Order> {
        return _orderDao.getOrder()
    }

    suspend fun addToOrderHistory(order: Order) {
        _orderDao.insertOrder(order)
    }

}



