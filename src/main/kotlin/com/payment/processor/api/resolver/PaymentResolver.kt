package com.payment.processor.api.resolver

import org.springframework.stereotype.Component
import graphql.kickstart.tools.GraphQLQueryResolver
import com.payment.processor.api.model.Payment
import com.payment.processor.api.service.PaymentService
import com.payment.processor.api.resource.PaymentFilter

// This resolver provides the Get functions for the endpoints as for the GraphQL side.
@Component
class PaymentResolver(private val paymentService: PaymentService) : GraphQLQueryResolver {
    // getSales provides all the sales without a filter.
    fun getSales(): List<Payment> = paymentService.getPayments()

    // getSalesInterval will request the payments registered in the given time interval.
    fun getSalesInterval(range: PaymentFilter): List<Payment> = 
        paymentService.getPayments(range.startDateTime+"", range.endDateTime+"")
}