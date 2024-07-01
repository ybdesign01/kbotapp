package com.example.kbotapp


object mathResponse {

    fun solve(equation: String) : Int{

        val mathMessage = equation.replace(" ", "")

        return when {
            mathMessage.contains("+") -> {
                val split = mathMessage.split("+")
                val result = split[0].toInt() + split[1].toInt()
                result
            }
            mathMessage.contains("-") -> {
                val split = mathMessage.split("-")
                val result = split[0].toInt() - split[1].toInt()
                result
            }
            mathMessage.contains("*") -> {
                val split = mathMessage.split("*")
                val result = split[0].toInt() * split[1].toInt()
                result
            }
            mathMessage.contains("/") -> {
                val split = mathMessage.split("/")
                val result = split[0].toInt() / split[1].toInt()
                result
            }
            else -> {
                0
            }
        }
    }
}