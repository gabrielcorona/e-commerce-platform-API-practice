package com.payment.processor.api.model

import javax.persistence.*

// This is the data object corresponding to the Payment, this data is used to define the corresponding database table.
@Entity
@Table(name = "criteria")
data class PaymentCriteria(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val paymentMethod: String?,
    val maxPriceTemplate: String?,
    val minPriceTemplate: String?,
    val requireAttributes: String?,
    val pointsTemplate: String?
)