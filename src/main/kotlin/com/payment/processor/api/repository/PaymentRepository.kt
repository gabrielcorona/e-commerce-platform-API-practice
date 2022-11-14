package com.payment.processor.api.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import javax.transaction.Transactional
import com.payment.processor.api.model.Payment
import java.time.OffsetDateTime

// The repository facilitates basic data transactions to the database.
@Repository
@Transactional(Transactional.TxType.REQUIRED)
interface PaymentRepository : JpaRepository<Payment, Long>{
    // Query used to filter the payments by the given date range provided.
    @Query("FROM Payment WHERE datetime BETWEEN :startDateTime AND :endDateTime")
    fun getPaymentsBetween(@Param("startDateTime") startDateTime: OffsetDateTime,@Param("endDateTime") endDateTime: OffsetDateTime): List<Payment>
}