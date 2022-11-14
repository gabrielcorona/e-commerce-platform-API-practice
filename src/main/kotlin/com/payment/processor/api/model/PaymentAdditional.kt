package com.payment.processor.api.model

import com.fasterxml.jackson.annotation.*
import javax.persistence.*

// This data class describes the additional information sometimes provided at the payment event, this data is used to define the corresponding database table..
@Entity
@Table(name = "payments_additional")
data class PaymentAdditional(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val last_4: String? = "",
    val bank: String? = "",
    val cheque: String? = "",
    val courier: String? = "",
    @OneToOne
    var payment: Payment?=null
)