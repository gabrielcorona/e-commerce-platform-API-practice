package com.payment.processor.api.resolver

import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component
import com.payment.processor.api.service.PaymentService
import com.payment.processor.api.model.Payment
import com.payment.processor.api.model.PaymentAdditional
import com.payment.processor.api.response.PaymentResponse
import com.payment.processor.api.resource.DataTools
import java.time.OffsetDateTime

@Component
class MutationResolver (private val paymentService: PaymentService) : GraphQLMutationResolver {
    
    fun createPayment(customer_id: String, price: String, price_modifier: Float, payment_method: String, datetime: String, additional_item: PaymentAdditional?=null): PaymentResponse {
        var payment: Payment
        var paymentAdditional:PaymentAdditional? = null

        // Validating additional information before processing.
        paymentService.dataTools.validateAditional(additional_item,payment_method)
        
        // Validate and create the Payment Additional information instance if the valid data has been provided.
        if(additional_item?.last_4 != null || additional_item?.bank != null || additional_item?.cheque != null || additional_item?.courier != null){
                paymentAdditional = 
                    PaymentAdditional(
                        last_4 = additional_item.last_4,
                        bank = additional_item.bank,
                        cheque = additional_item.cheque,
                        courier = additional_item.courier
                    )
        }
        // Creating the payment entify once we have already instanced the Payment Additional information
        payment = paymentService.addPayment(
            Payment(
                customer_id = customer_id,
                price = price,
                price_modifier = price_modifier,
                sales = paymentService.dataTools.evaluateSales(price.toFloat(), price_modifier, payment_method),
                payment_method = payment_method,
                points = paymentService.dataTools.evaluatePoints(price.toFloat(), payment_method),
                additional_item = paymentAdditional,
                datetime = paymentService.dataTools.validDate(datetime)
            )
        )
        return PaymentResponse(payment.sales,payment.points)
    }
}