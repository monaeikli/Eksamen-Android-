package no.kristiania.pgr208.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import no.kristiania.pgr208.data.room.AppDatabase
import no.kristiania.pgr208.data.room.Favorite
import no.kristiania.pgr208.data.room.ShoppingCart
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProductRepository {

    // Retrofit needs us to set up a HttpClient-object
    // We add HttpLoggingInterceptor to log Http requests/responses to LogCat for
    // easier debugging..
    private val _httpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()

    // We create our Retrofit client - It's used to create our data sources (productService) below.
    // This builder requires us to provide a HttpClient (created above) and a base-url to our API.
    // We add the GsonConverterFactor to the client, so that it can automatically parse the
    // JSON-response from Retrofit/Http into our app's data classes (product, productResponse)
    private val _retrofit =
        Retrofit.Builder()
            .client(_httpClient)
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // We use Retrofit to create an INSTANCE of our productService, so that we can call the
    // functions that requests data from the API
    private val _productService = _retrofit.create(ProductService::class.java)

    private lateinit var _appDatabase: AppDatabase
    private val _productDao by lazy { _appDatabase.productDao() }
    private val _favoriteDao by lazy { _appDatabase.favoriteDao() }
    private val _shoppingCartDao by lazy { _appDatabase.shoppingCartDao() }

    fun initializeDatabase(context: Context) {
        _appDatabase = Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "product-database"
        ).fallbackToDestructiveMigration().build()
    }

    suspend fun getProducts(): List<Product> {
        try {
            val response = _productService.getAllProducts()
            Log.d("ProductRepository", "API Response Code: ${response.code()}")
            Log.d("ProductRepository", "API Response Body: ${response.body()}")
            // We first need to check if the response was successful. If we don't handle it, our app can
            // crash if we for example don't have an internet connection..
            if(response.isSuccessful) {
                // The "response body" might be NULL (the API response was successful, but didn't
                // return any data (NULL) -- If we don't handle this, we might also run into a crash!!
                val products = response.body()?.products ?: emptyList()

                // Here we insert the resulting products from the API into our database!!
                _productDao.insertProducts(products)

                // We retrieve the data only from our DB, making our app support offline mode..
                return _productDao.getProducts()
            } else {
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
}



