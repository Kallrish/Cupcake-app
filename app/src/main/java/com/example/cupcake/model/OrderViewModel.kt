package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00
private const val PRICE_PER_CUPCAKE = 2.00
const val ZERO_VALUE = 0

class OrderViewModel : ViewModel() {

    private var _customerName = MutableLiveData<String>()
    val customerName: LiveData<String> get() = _customerName

    private var _date = MutableLiveData<String>()
    val date: LiveData<String> get() = _date

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private var _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> get() = _flavor

    private var _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    private var _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> get() = _quantity

    val dateOptions = getPickupOptions()

    init {
        resetOrder()
    }

    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()

        // Create a list of dates starting with the current date and the following 3 dates
        repeat(4) {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return options
    }

    fun hasNoFlavorSet(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

    fun resetOrder() {
        _customerName.value = ""
        _date.value = dateOptions[0]
        _flavor.value = ""
        _price.value = 0.0
        _quantity.value = 0
        _errorMessage.value = ""
    }

    fun setDate(pickupDate: String) {
        _date.value = pickupDate
        updatePrice()
    }

    fun setFlavor(desiredFlavor: String) {
        _flavor.value = desiredFlavor
    }

    fun setName(typedName: String) {
        _customerName.value = typedName
    }

    fun setQuantity(numberCupCakes: Int) {
        _quantity.value = numberCupCakes
        updatePrice()
    }

    private fun updatePrice() {
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE
        if(dateOptions[0] == _date.value) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        _price.value = calculatedPrice
    }

//    fun verifyIfNameIsNotEmpty(text: CharSequence) {
//        if (text.length == ZERO_VALUE) {
//            _errorMessage.value = "@string/enter_name_error"
//        } else {
//            _errorMessage.value = ""
//        }
//    }
}