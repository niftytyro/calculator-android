package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {

    private val expression = mutableListOf<String>()
    private val numbers = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    private val operators = listOf('÷', '×', '%', '+', '-')
    private var result = ""

    private fun removeTrailingZeroes(num: Float): String {
        return BigDecimal(num.toString()).stripTrailingZeros().toPlainString()
    }

    private fun setResult() {
        var preview = ""
        for (each in expression) {
            preview += " $each"
        }
        textPreview.text = preview.trim()
        calculateExpression()
        textResult.text = result
    }

    private fun calculateExpression() {
        val expressionCopy: MutableList<String> = expression.toMutableList()
        for (operator in operators) {
            var idx = 1
            while (idx < expressionCopy.size) {
                if (expressionCopy[idx] == operator.toString()) {
                    if (calculateSubExpression(idx, expressionCopy)) idx--
                }
                idx++
            }
            println(expressionCopy)
        }
        try {
            result = expressionCopy[0]
        } catch (e: java.lang.IndexOutOfBoundsException) {}
    }

    private fun calculateSubExpression(index: Int, expression: MutableList<String>): Boolean {
        try {
            when (expression[index]) {
                "÷" -> {
                    if (expression[index + 1] == "0") {
                        expression.clear()
                        expression += "Infinity"
                        return true
                    }
                    expression[index] =
                        removeTrailingZeroes(expression[index - 1].toFloat() / expression[index + 1].toFloat())
                    expression.removeAt(index + 1)
                    expression.removeAt(index - 1)
                }
                "×" -> {
                    expression[index] =
                        removeTrailingZeroes(expression[index - 1].toFloat() * expression[index + 1].toFloat())
                    expression.removeAt(index + 1)
                    expression.removeAt(index - 1)
                }
                "%" -> {
                    expression[index] =
                        removeTrailingZeroes(expression[index - 1].toFloat() * expression[index + 1].toFloat() / 100)
                    expression.removeAt(index + 1)
                    expression.removeAt(index - 1)
                }
                "+" -> {
                    expression[index] =
                        removeTrailingZeroes(expression[index - 1].toFloat() + expression[index + 1].toFloat())
                    expression.removeAt(index + 1)
                    expression.removeAt(index - 1)
                }
                "-" -> {
                    expression[index] =
                        removeTrailingZeroes(expression[index - 1].toFloat() - expression[index + 1].toFloat())
                    expression.removeAt(index + 1)
                    expression.removeAt(index - 1)
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            return false
        }
        return true
    }

    private fun resetValues() {
        expression.clear();
        result = "";
        setResult()
    }

    private fun calculateTotal() {
        expression.clear()
        setResult()
    }

    private fun addToExpression(char: Char) {
        if (char in numbers) {
            try {
                if (expression.last()[0] in operators) {
                    expression += "$char"
                } else if (expression.last().last() in numbers || expression.last().last() == '.') {
                    expression[expression.size - 1] += "$char"
                }
            } catch (e: NoSuchElementException) {
                expression += "$char"
            }
        } else if (char in operators) {
            try {
                if (expression.last()[0] in operators) {
                    expression.removeLast()
                }
                expression += "$char"
            } catch (e: NoSuchElementException) {
                if(result!="") {
                    expression += result
                    expression += "$char"
                }
            }
        } else if (char == '.') {
            try {
                if (char !in expression.last()) {
                    if (expression.last()[0] in operators) {
                        expression += "0."
                    } else if (expression.last().last() in numbers) {
                        expression[expression.size - 1] += "."
                    }
                }
            } catch (e: NoSuchElementException) {
                expression += "0."
            }
        }
        setResult()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonZero.setOnClickListener { addToExpression('0') }
        buttonOne.setOnClickListener { addToExpression('1') }
        buttonTwo.setOnClickListener { addToExpression('2') }
        buttonThree.setOnClickListener { addToExpression('3') }
        buttonFour.setOnClickListener { addToExpression('4') }
        buttonFive.setOnClickListener { addToExpression('5') }
        buttonSix.setOnClickListener { addToExpression('6') }
        buttonSeven.setOnClickListener { addToExpression('7') }
        buttonEight.setOnClickListener { addToExpression('8') }
        buttonNine.setOnClickListener { addToExpression('9') }
        buttonDecimal.setOnClickListener { addToExpression('.') }
        buttonAddition.setOnClickListener { addToExpression('+') }
        buttonDivision.setOnClickListener { addToExpression('÷') }
        buttonMultiplication.setOnClickListener { addToExpression('×') }
        buttonPercentage.setOnClickListener { addToExpression('%') }
        buttonSubtraction.setOnClickListener { addToExpression('-') }
        buttonAC.setOnClickListener{ resetValues() }
        buttonResult.setOnClickListener{ calculateTotal() }
    }
}