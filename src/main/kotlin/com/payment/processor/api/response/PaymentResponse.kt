package com.payment.processor.api.response

// Data class used for the payment creation response
data class PaymentResponse(
    val final_price: String, 
    val points: Int
)