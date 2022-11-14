package com.payment.processor.api.model

import graphql.scalars.datetime.*
import java.time.*
import javax.persistence.*
import com.payment.processor.api.model.PaymentAdditional

// This is the data object corresponding to the Payment, this data is used to define the corresponding database table.
@Entity
@Table(name = "payments")
data class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val customer_id: String,
    val price: String,
    val price_modifier: Float,
    val sales: String,    
    val payment_method: String,
    val points: Int,
    @OneToOne(cascade = [(CascadeType.ALL)])
    var additional_item: PaymentAdditional?,
    val datetime: OffsetDateTime? = OffsetDateTime.now()
)