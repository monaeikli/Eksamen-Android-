package no.kristiania.pgr208.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {
    @GET("products/{id}")
    suspend fun getProduct(
        @Path("id") id: Int
    ): Response<Product>
    @GET("products")
    suspend fun getAllProducts(): Response<ProductListResponse>


}
