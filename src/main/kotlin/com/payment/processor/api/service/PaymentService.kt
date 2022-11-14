package com.payment.processor.api.service

import org.springframework.stereotype.Service
import java.util.*
import com.payment.processor.api.repository.PaymentRepository
import com.payment.processor.api.repository.PaymentCriteriaRepository
import com.payment.processor.api.model.Payment
import com.payment.processor.api.resource.DataTools
import javax.persistence.Query


@Service
class PaymentService(private val paymentRepository: PaymentRepository,private val paymentCriteriaRepository: PaymentCriteriaRepository) {
    val dataTools:DataTools=DataTools(paymentCriteriaRepository)
    fun getPayments(startDateTime: String, endDateTime: String): List<Payment> {
        return paymentRepository.getPaymentsBetween(
            dataTools.getDateDef(startDateTime),
            dataTools.getDateDef(endDateTime)
        )
    }

    fun getPayments(): List<Payment> =
        paymentRepository.findAll()
    
    fun addPayment(payment: Payment): Payment {
        return paymentRepository.save(payment)
    }
}