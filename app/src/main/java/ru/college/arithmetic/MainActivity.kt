package ru.college.arithmetic

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.content.res.ColorStateList
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import java.util.Locale

class MainActivity : Activity() {
    private var exercise: Exercise? = null
    private var checked = false
    private var wasCorrect = false
    private var correct = 0
    private var wrong = 0
    private lateinit var example: TextView
    private lateinit var answer: EditText
    private lateinit var start: Button
    private lateinit var check: Button
    private lateinit var total: TextView
    private lateinit var correctCount: TextView
    private lateinit var wrongCount: TextView
    private lateinit var percentage: TextView
    private lateinit var message: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            if (it.containsKey("left")) exercise = Exercise(it.getInt("left"), it.getInt("right"),
                Operation.valueOf(requireNotNull(it.getString("operation"))))
            checked = it.getBoolean("checked")
            wasCorrect = it.getBoolean("wasCorrect")
            correct = it.getInt("correct")
            wrong = it.getInt("wrong")
        }
        buildScreen()
        answer.setText(savedInstanceState?.getString("input") ?: "")
        render()
        if (savedInstanceState?.getBoolean("inputError") == true) {
            answer.error = getString(R.string.invalid_answer)
        }
        start.setOnClickListener {
            exercise = Exercise.generate()
            checked = false
            wasCorrect = false
            answer.text.clear()
            answer.error = null
            render()
            answer.requestFocus()
        }
        check.setOnClickListener {
            val current = exercise ?: return@setOnClickListener
            if (checked) return@setOnClickListener
            val entered = answer.text.toString().trim().toIntOrNull()
            if (entered == null) {
                answer.error = getString(R.string.invalid_answer)
                return@setOnClickListener
            }
            wasCorrect = entered == current.answer
            if (wasCorrect) correct++ else wrong++
            checked = true
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(answer.windowToken, 0)
            render()
        }
    }

    private fun render() {
        val current = exercise
        val solving = current != null && !checked
        start.isEnabled = !solving
        check.isEnabled = solving
        answer.isEnabled = solving
        example.text = current?.let { "${it.left}  ${it.operation.symbol}  ${it.right}  =" } ?: "—  +  —  ="
        example.background = rounded(when {
            !checked -> Color.WHITE
            wasCorrect -> Color.rgb(152, 230, 165)
            else -> Color.rgb(255, 160, 160)
        })
        total.text = getString(R.string.total, correct + wrong)
        correctCount.text = getString(R.string.correct_count, correct)
        wrongCount.text = getString(R.string.wrong_count, wrong)
        percentage.text = String.format(Locale.US, "%.2f%%",
            if (correct + wrong == 0) 0.0 else correct * 100.0 / (correct + wrong))
        message.text = when {
            current == null -> getString(R.string.initial_message)
            !checked -> getString(R.string.solve_message)
            wasCorrect -> getString(R.string.correct_message)
            else -> getString(R.string.wrong_message, current.answer)
        }
    }

    private fun buildScreen() {
        val scroll = ScrollView(this).apply { isFillViewport = true }
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(dp(24), dp(24), dp(24), dp(24))
        }
        scroll.addView(content)
        // System bar insets also cover Android 15's enforced edge-to-edge layout.
        scroll.setOnApplyWindowInsetsListener { view, insets ->
            view.setPadding(insets.systemWindowInsetLeft, insets.systemWindowInsetTop,
                insets.systemWindowInsetRight, insets.systemWindowInsetBottom)
            insets
        }
        fun label(size: Float) = TextView(this).apply {
            textSize = size
            gravity = Gravity.CENTER
            setTextColor(Color.rgb(35, 28, 50))
            setPadding(0, dp(12), 0, dp(12))
        }
        fun add(view: View) = content.addView(view, LinearLayout.LayoutParams(-1, -2))
        add(label(26f).apply { text = getString(R.string.app_name); setTypeface(null, Typeface.BOLD) })
        total = label(20f); add(total)
        val counts = LinearLayout(this)
        correctCount = label(17f)
        wrongCount = label(17f)
        counts.addView(correctCount, LinearLayout.LayoutParams(0, -2, 1f))
        counts.addView(wrongCount, LinearLayout.LayoutParams(0, -2, 1f))
        add(counts)
        percentage = label(48f); add(percentage)
        add(label(14f).apply { text = getString(R.string.accuracy) })
        example = label(32f).apply { minHeight = dp(88) }; add(example)
        answer = EditText(this).apply {
            hint = getString(R.string.answer)
            contentDescription = getString(R.string.answer)
            textSize = 24f
            gravity = Gravity.CENTER
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
            setSingleLine(true)
            isSaveEnabled = false // The input is restored explicitly from the Bundle.
            minHeight = dp(56)
        }
        add(answer)
        message = label(16f).apply { accessibilityLiveRegion = View.ACCESSIBILITY_LIVE_REGION_POLITE }
        add(message)
        fun button(title: Int) = Button(this).apply {
            setText(title)
            minHeight = dp(56)
            backgroundTintList = ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled), intArrayOf()),
                intArrayOf(Color.rgb(98, 0, 238), Color.rgb(220, 215, 228)))
            setTextColor(ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled), intArrayOf()),
                intArrayOf(Color.WHITE, Color.rgb(105, 99, 114))))
        }
        check = button(R.string.check); add(check)
        start = button(R.string.start); add(start)
        setContentView(scroll)
        scroll.requestApplyInsets()
    }

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()
    private fun rounded(color: Int) = GradientDrawable().apply {
        setColor(color)
        cornerRadius = dp(16).toFloat()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        exercise?.let {
            outState.putInt("left", it.left)
            outState.putInt("right", it.right)
            outState.putString("operation", it.operation.name)
        }
        outState.putBoolean("checked", checked)
        outState.putBoolean("wasCorrect", wasCorrect)
        outState.putInt("correct", correct)
        outState.putInt("wrong", wrong)
        outState.putString("input", answer.text.toString())
        outState.putBoolean("inputError", answer.error != null)
        super.onSaveInstanceState(outState)
    }
}
