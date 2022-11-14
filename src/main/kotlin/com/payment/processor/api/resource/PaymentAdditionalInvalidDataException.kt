package com.payment.processor.api.resource

import com.payment.processor.api.GraphQLException
import graphql.*

open class PaymentAdditionalInvalidDataException(
    errorMessage: String? = "",
    private val parameters: Map<String, Any>? = mutableMapOf()
) : GraphQLException(errorMessage) {
    override val message: String?
    get() = super.message

    override fun getExtensions(): MutableMap<String, Any> {
        return mutableMapOf("parameters" to (parameters ?: mutableMapOf()))
    }

    override fun getErrorType(): ErrorClassification {
        return ErrorType.DataFetchingException
    }
}