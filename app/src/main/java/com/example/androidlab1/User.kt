package com.example.androidlab1

data class User(var login: String, var list: MutableList<Pair>) {
    constructor(): this("", mutableListOf())
}

data class Pair(var points: Int, var date: String) {
    constructor(): this(0, "no date")
}