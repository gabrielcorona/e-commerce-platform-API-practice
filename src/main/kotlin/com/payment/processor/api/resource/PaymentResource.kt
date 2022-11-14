package com.payment.processor.api.resource

import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import org.springframework.http.ResponseEntity
import com.payment.processor.api.service.PaymentService
import com.payment.processor.api.model.Payment
import com.payment.processor.api.response.PaymentResponse

// Restfuk fuctions
@RestController
@RequestMapping("/v1/api/payments")
class PaymentResource(private val paymentService: PaymentService) {

  // All sales endpoint.
  @GetMapping
  fun getPayments(): List<Payment> =
    paymentService.getPayments()

  // Filter interval sales endpoint.
  @GetMapping("/salesInterval")
  fun getPaymentsInterval(@Valid @RequestBody range: PaymentFilter): List<Payment> =
    paymentService.getPayments(range.startDateTime+"",range.endDateTime+"")

  // Create a new payment that will be considered a sale.  
  @PostMapping
  fun addPayment(@Valid @RequestBody payment: Payment): ResponseEntity<PaymentResponse> {
    val paymentResult: Payment = paymentService.addPayment(payment)
    return ResponseEntity.ok(PaymentResponse(paymentResult.sales,paymentResult.points))
  }
    

}