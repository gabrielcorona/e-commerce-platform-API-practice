package com.payment.processor.api.resource

import com.notkamui.keval.Keval
import com.payment.processor.api.repository.PaymentCriteriaRepository
import com.payment.processor.api.model.PaymentCriteria
import com.payment.processor.api.model.PaymentAdditional
import com.payment.processor.api.resource.PaymentAdditionalInvalidDataException
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException

class DataTools(val paymentCriteriaRepository: PaymentCriteriaRepository){
    val availableMethods: HashMap<String, HashMap<String,String>> = HashMap<String,HashMap<String,String>>()
    init {
        loadPaymentMethods()
        //loadDefaultMethods()
        //savePaymentMethods()
    }
    fun evaluatePoints (price: Float, method: String) : Int {
        // retrieving mapped points from the configuration for the provided payment method
        var multiplier: String = availableMethods.get(method)?.get("POINTS_TEMP")+""

        // resolving values base on the template formula and the price to obtain the points
        multiplier = multiplier.replace("[PRICE]",price.toString())
        val points = Math.round(Keval.eval(multiplier)).toInt()

        return points
    }
    
    // This function evaluates the value in the allowed range for the final price.
    // It's important to mention that it just validates the range and will adjust it to the min or max accordingly. 
    fun evaluateSales (price: Float, price_modifier: Float, method: String) : String {
        var final_price:Float=price*price_modifier

        // retrieving mapped configuration values
        val max: String = availableMethods.get(method)?.get("PRICE_MAX_TEMP").toString()
        val min: String = availableMethods.get(method)?.get("PRICE_MIN_TEMP")+""

        // resolving template formulas for max and minimun price
        var maxNum: Float = Keval.eval(max.replace("[PRICE]",price.toString())).toFloat()
        var minNum: Float = Keval.eval(min.replace("[PRICE]",price.toString())).toFloat()
        
        // fixing price in case of it getting out of range
        if(final_price > maxNum)
            final_price = maxNum
        if(final_price < minNum)
            final_price = minNum
        return final_price.toString() 
    }

    // This provides a quick parsing for the date time values
    fun validDate(datetime:String):OffsetDateTime{
        // Pending format validation
        var date:OffsetDateTime = OffsetDateTime.parse(datetime);
        return date;
    }

    // This provides the date time default value to ve the current time.
    fun getDateDef(datetime:String):OffsetDateTime{
        var date: OffsetDateTime = OffsetDateTime.now()
        try{
            date = OffsetDateTime.parse(datetime);
        }catch(dtex:DateTimeParseException){
            println("The provided date is null")
        }
        return date;
    }

    fun isValid4Numeric(number: String): Boolean {
        val regex = "^\\d{4}$".toRegex()
        return number.matches(regex)
    }

    // This function validates the additional information to be consistant with the payment method.
    // If an inconcistency is encounter, an exception will be thrown notifying the error in the custom message response.
    fun validateAditional(paymentAdditional:PaymentAdditional?,payment_method:String):Boolean {
        var valid=true
        val last4=!paymentAdditional?.last_4.isNullOrEmpty()
        val bank=!paymentAdditional?.bank.isNullOrEmpty()
        val courier=!paymentAdditional?.courier.isNullOrEmpty()
        val cheque=!paymentAdditional?.cheque.isNullOrEmpty()
        val reqAtt = availableMethods.get(payment_method)?.get("REQUIRE_TEMP").toString()
        when(reqAtt){
            "COURIER|YAMATO,SAGAWA" -> {
                if(last4 || bank || cheque)
                    valid=failCondition("400: Bad Request - Unecessary information has been provided")
                if(courier){
                    if((reqAtt.toString().contains(paymentAdditional?.courier+"")))
                        valid=failCondition("400: Bad Request - Invalid information wrong courier")
                }else{
                    valid=failCondition("400: Bad Request - Missing information, not a valid courier")
                }
            }
            "LAST4" -> {
                if(courier || bank || cheque)
                    valid=failCondition("400: Bad Request - Unecessary information has been provided")
                if(last4){
                    if(!isValid4Numeric(paymentAdditional?.last_4+""))
                        valid=failCondition("400: Bad Request - Invalid information, not a valid card number")
                }else{
                    valid=failCondition("400: Bad Request - Missing information, not card number")
                }                    
            }
            "CHEQUE|BANK" -> {
                if(courier || last4)
                    valid=failCondition("400: Bad Request - Unecessary information has been provided")
                if(!bank || !cheque)
                    valid=failCondition("400: Bad Request - Invalid information, we need to have both bank account and cheque information")
            }
            "BANK" -> {
                if(courier || last4 || cheque)
                    valid=failCondition("400: Bad Request - Unecessary information has been provided")
                if(bank)
                    valid=failCondition("400: Bad Request - Invalid information, we need the bank account")
            }
            
        }
        return valid;
    }

    // This function throws the exception displaying the message in the response
    fun failCondition(message:String):Boolean{
        println(message)
        throw PaymentAdditionalInvalidDataException(message,null)
        return false;
    }

    // This saves in the database the loaded payment criteria that is used to calculate the points and the validity of the request.
    fun savePaymentMethods(){
        println("Saving")
        availableMethods.forEach{ entry ->
            println(entry.key)
            
            paymentCriteriaRepository.save(
            PaymentCriteria(
                    paymentMethod = entry.key,
                    maxPriceTemplate = entry.value.get("PRICE_MAX_TEMP"),
                    minPriceTemplate = entry.value.get("PRICE_MIN_TEMP"),
                    pointsTemplate = entry.value.get("POINTS_TEMP"),
                    requireAttributes = entry.value.get("REQUIRE_TEMP")
                )
            )
            
        }
            
    }

    // This function loads the payment methods from the database.
    fun loadPaymentMethods(){
        println("Loading")
        var methodValues:HashMap<String,String> = HashMap<String,String>()
        for(paymentCriteria:PaymentCriteria in paymentCriteriaRepository.findAll()){
            methodValues.put("PRICE_MAX_TEMP",paymentCriteria.maxPriceTemplate+"")
            methodValues.put("PRICE_MIN_TEMP",paymentCriteria.minPriceTemplate+"")
            methodValues.put("POINTS_TEMP",paymentCriteria.pointsTemplate+"")
            methodValues.put("REQUIRE_TEMP",paymentCriteria.requireAttributes+"")
            availableMethods.put(paymentCriteria.paymentMethod+"",methodValues)
            methodValues = HashMap<String,String>()
        }
    }

    // This function load the default methods without having it loaded from the database.
    fun loadDefaultMethods(){        
        var methodValues:HashMap<String,String> = HashMap<String,String>()
        methodValues.put("PRICE_MAX_TEMP","[PRICE]")
        methodValues.put("PRICE_MIN_TEMP","[PRICE]*0.9")
        methodValues.put("POINTS_TEMP","[PRICE]*0.05")
        methodValues.put("REQUIRE_TEMP","")
        availableMethods.put("CASH",methodValues)
        methodValues = HashMap<String,String>()
        methodValues.put("PRICE_MAX_TEMP","[PRICE]*1.02")
        methodValues.put("PRICE_MIN_TEMP","[PRICE]")
        methodValues.put("POINTS_TEMP","[PRICE]*0.05")
        methodValues.put("REQUIRE_TEMP","COURIER|YAMATO,SAGAWA")
        availableMethods.put("CASH_ON_DELIVERY",methodValues)
        methodValues = HashMap<String,String>()
        methodValues.put("PRICE_MAX_TEMP","[PRICE]")
        methodValues.put("PRICE_MIN_TEMP","[PRICE]*0.95")
        methodValues.put("POINTS_TEMP","[PRICE]*0.03")
        methodValues.put("REQUIRE_TEMP","LAST4")
        availableMethods.put("VISA",methodValues)
        methodValues = HashMap<String,String>()
        methodValues.put("PRICE_MAX_TEMP","[PRICE]")
        methodValues.put("PRICE_MIN_TEMP","[PRICE]*0.95")
        methodValues.put("POINTS_TEMP","[PRICE]*0.03")
        methodValues.put("REQUIRE_TEMP","LAST4")
        availableMethods.put("MASTERCARD",methodValues)
        methodValues = HashMap<String,String>()
        methodValues.put("PRICE_MAX_TEMP","[PRICE]*1.01")
        methodValues.put("PRICE_MIN_TEMP","[PRICE]*0.98")
        methodValues.put("POINTS_TEMP","[PRICE]*0.02")
        methodValues.put("REQUIRE_TEMP","LAST4")
        availableMethods.put("AMEX",methodValues)
        methodValues = HashMap<String,String>()
        methodValues.put("PRICE_MAX_TEMP","[PRICE]")
        methodValues.put("PRICE_MIN_TEMP","[PRICE]*0.95")
        methodValues.put("POINTS_TEMP","[PRICE]*0.05")
        methodValues.put("REQUIRE_TEMP","LAST4")
        availableMethods.put("JCB",methodValues)
        methodValues = HashMap<String,String>()
        methodValues.put("PRICE_MAX_TEMP","[PRICE]")
        methodValues.put("PRICE_MIN_TEMP","[PRICE]")
        methodValues.put("POINTS_TEMP","[PRICE]*0.01")
        methodValues.put("REQUIRE_TEMP","")
        availableMethods.put("LINE PAY",methodValues)
        methodValues = HashMap<String,String>()
        methodValues.put("PRICE_MAX_TEMP","[PRICE]")
        methodValues.put("PRICE_MIN_TEMP","[PRICE]")
        methodValues.put("POINTS_TEMP","[PRICE]*0.01")
        methodValues.put("REQUIRE_TEMP","")
        availableMethods.put("PAYPAY",methodValues)
        methodValues = HashMap<String,String>()
        methodValues.put("PRICE_MAX_TEMP","[PRICE]")
        methodValues.put("PRICE_MIN_TEMP","[PRICE]")
        methodValues.put("POINTS_TEMP","[PRICE]*0.01")
        methodValues.put("REQUIRE_TEMP","")
        availableMethods.put("GRAB PAY",methodValues)
        methodValues = HashMap<String,String>()
        methodValues.put("PRICE_MAX_TEMP","[PRICE]")
        methodValues.put("PRICE_MIN_TEMP","[PRICE]")
        methodValues.put("POINTS_TEMP","0")
        methodValues.put("REQUIRE_TEMP","")
        availableMethods.put("POINTS",methodValues)
        methodValues = HashMap<String,String>()
        methodValues.put("PRICE_MAX_TEMP","[PRICE]")
        methodValues.put("PRICE_MIN_TEMP","[PRICE]")
        methodValues.put("POINTS_TEMP","0")
        methodValues.put("REQUIRE_TEMP","BANK")
        availableMethods.put("BANK TRANSFER",methodValues)
        methodValues = HashMap<String,String>()
        methodValues.put("PRICE_MAX_TEMP","[PRICE]")
        methodValues.put("PRICE_MIN_TEMP","[PRICE]*0.9")
        methodValues.put("POINTS_TEMP","0")
        methodValues.put("REQUIRE_TEMP","CHEQUE|BANK")
        availableMethods.put("CHEQUE",methodValues)
        
    }
}