package com.feodal.ordy.models

class Order {

    var order_list: String? = null
    var phone: String? = null
    var name: String? = null

    constructor()
    constructor(order_list: String?, phone: String?, name: String?) {
        this.order_list = order_list
        this.phone = phone
        this.name = name
    }

}