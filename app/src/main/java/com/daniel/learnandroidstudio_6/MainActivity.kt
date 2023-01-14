package com.daniel.learnandroidstudio_6

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.daniel.learnandroidstudio_6.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.sbTipPercent.progress = INITIAL_TIP_PERCENT
        mainBinding.tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"


        updateTipDescription(INITIAL_TIP_PERCENT)

        mainBinding.sbTipPercent.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG, "onProgressChanged $p1")
                mainBinding.tvTipPercentLabel.text = "$p1%"
                computeTipAndTotal()
                updateTipDescription(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        mainBinding.etBaseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "afterTextChanged $p0")
                computeTipAndTotal()
            }
        })
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0..9 -> "Poor 😔"
            in 10..14 -> "Acceptable 😑"
            in 15..19 -> "Good 😊"
            in 20..24 -> "Great 😃"
            else -> "Amazing 😁"
        }
        mainBinding.tvTipDescription.text = tipDescription

        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / mainBinding.sbTipPercent.max,
            ContextCompat.getColor(this, R.color.c_worst),
            ContextCompat.getColor(this, R.color.c_best),
        ) as Int
        mainBinding.tvTipDescription.setTextColor(color)

    }

    private fun computeTipAndTotal() {
        if (mainBinding.etBaseAmount.text.isEmpty()){
            mainBinding.tvTipAmount.text = ""
            mainBinding.tvTotalAmount.text = ""
            return
        }

        val baseAmount = mainBinding.etBaseAmount.text.toString().toDouble()
        val tipPercent = mainBinding.sbTipPercent.progress

        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount

        mainBinding.tvTipAmount.text = "%.2f".format(tipAmount)
        mainBinding.tvTotalAmount.text = "%.2f".format(totalAmount)
    }
}