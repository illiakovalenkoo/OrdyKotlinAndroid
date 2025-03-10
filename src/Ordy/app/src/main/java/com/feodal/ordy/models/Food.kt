package com.feodal.ordy.models

class Food {
    var price: String? = null
    var full_text: String? = null

    constructor()
    constructor(price: String?, fullText:String?) {
        this.price = price
        this.full_text = fullText
    }
}