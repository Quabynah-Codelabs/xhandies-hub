package io.codelabs.xhandieshub.core.payment

import io.codelabs.xhandieshub.model.Cart
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentService {

    /**
     * Payment Request Body
     */
    data class PaymentRequest(
        val uid: String,
        val items: MutableList<Cart>,
        val timestamp: Long = System.currentTimeMillis()
    )

    /**
     * Response from server
     */
    data class ApiResponse(val message: String?, val response: Any, val error: Boolean = false)

    @POST("/payment/checkout")
    fun makePaymentAsync(@Body request: PaymentRequest): Deferred<Response<ApiResponse>>
}