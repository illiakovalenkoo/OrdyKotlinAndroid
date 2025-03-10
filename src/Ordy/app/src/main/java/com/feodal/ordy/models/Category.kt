package com.feodal.ordy.models

class Category {

    var image: String? = null
    var name: String? = null

    constructor()
    constructor(image: String?, name: String?) {
        this.image = image
        this.name = name
    }

}