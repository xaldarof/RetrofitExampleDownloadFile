package com.example.advansedretrofit

/**
 * @Author: Temur
 * @Date: 25/06/2022
 */

fun main() {
    val numbers = listOf<String>("1")
    println(numbers.sum())

    getResult(onSuccess = { name, age ->
        println("Success download")
    }, onError = {
        println("Error")
    })
}

inline fun getResult(onSuccess: (name: String, age: Int) -> Unit, onError: (Exception) -> Unit) {
    try {

        onSuccess.invoke("File nomi", 1)

    } catch (e: Exception) {
        onError.invoke(e)
    }
}


fun List<String>.sum(): Int {
    var count = 0;
    forEach {
        if (it.isNotEmpty()) count += it.length;
    }

    return count
}