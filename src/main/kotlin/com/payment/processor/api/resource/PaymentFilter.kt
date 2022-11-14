package com.payment.processor.api.resource

// This is the data object used for the sales interval filter
data class PaymentFilter(
    val startDateTime: String?,
    val endDateTime: String?
)