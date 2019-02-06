package com.example.adeeliftikhar.kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        Calling method to print Hello World!
        printSomething()
//        var is mutable...
        var a: Int = 45  // immediate assignment
        var c: Int
        c = 45          // deferred assignment
//        Int is inferred (automatically )
        var b = 25
//        val is immutable...
//        ? use where null value is possible to avoid NullPointerException
        val sum: Int? = sumFunction(a, b)
//        Register TextView to get and show the sum in it
        val getSum : TextView = findViewById(R.id.sum) as TextView
//        giving value to TextView
        getSum.text = sum.toString()
        Toast.makeText(this, "Sum is ${getSum.text}", Toast.LENGTH_SHORT).show()
    }

    private fun printSomething() {
        Toast.makeText(this, "Hello World!", Toast.LENGTH_SHORT).show()
    }

    //    return type can be omitted (: Int)
    private fun sumFunction(a: Int, b: Int): Int {
        val sum: Int = a + b
//    Returning sum result
        return sum
    }
}
