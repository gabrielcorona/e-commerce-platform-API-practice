package com.payment.processor.api.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional
import com.payment.processor.api.model.PaymentCriteria

// The repository facilitates basic data transactions to the database.
@Repository
@Transactional(Transactional.TxType.REQUIRED)
interface PaymentCriteriaRepository : JpaRepository<PaymentCriteria, Long>