package com.example.chattingnew

class User {

    var mail : String? = null
    var pw : String? = null
    var uid : String? = null

    constructor(){}

    constructor(mail : String?, pw: String?, uid : String?)
    {

        this.mail=mail
        this.pw=pw
        this.uid=uid
    }
}