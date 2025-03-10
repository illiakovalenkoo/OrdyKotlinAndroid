package com.feodal.ordy.models

class User {

    var name: String? = null;
    var pass: String? = null;

    constructor()
    constructor(name: String?, pass: String?) {
        this.name = name
        this.pass = pass
    }
}