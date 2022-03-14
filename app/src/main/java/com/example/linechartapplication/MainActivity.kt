package com.example.linechartapplication

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.linechartapplication.databinding.ActivityMainBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class MainActivity : AppCompatActivity() {

    private val dataList = arrayListOf(
        70800.0,
        70900.0,
        71000.0,
        71200.0,
        71200.0,
        70900.0,
        71000.0,
        70900.0,
        71100.0,
        71000.0,
        71200.0,
        71300.0,
        71500.0,
        71600.0,
        71200.0,
        70900.0,
        70800.0,
        70700.0,
        70800.0,
        71000.0
    )

    private lateinit var binding: ActivityMainBinding

    private val _priceLiveData = MutableLiveData<String>()
    val priceLiveData: LiveData<String> get() = _priceLiveData

    private var prevSelected: Entry? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.apply {
            lifecycleOwner = this@MainActivity
            activity = this@MainActivity

            lineChart.apply {
                // zoom disabled.
                setPinchZoom(false)
                setScaleEnabled(false)
                isDoubleTapToZoomEnabled = false

                // right, left, x axis disabled.
                // legend, description disabled.
                axisRight.isEnabled = false
                axisLeft.isEnabled = false
                xAxis.isEnabled = false
                legend.isEnabled = false
                description.isEnabled = false

                // 데이터를 표현하는 entry list 설정
                val entryList = arrayListOf<Entry>()
                dataList.forEachIndexed { index, d ->
                    entryList.add(Entry(index.toFloat(), d.toFloat()))
                }
                val lineDataSet = LineDataSet(entryList, "data").apply {
                    // 원 크기 설정 - 선이 끊어지지 않는 것처럼 보이기 위해 선 두께에 맞춰서 설정
                    circleRadius = 1.0F
                    circleHoleRadius = 1.0F
                    // 원 색상 설정
                    setCircleColor(ContextCompat.getColor(applicationContext, R.color.red))
                    circleHoleColor = ContextCompat.getColor(applicationContext, R.color.red)
                    // 각 지점의 데이터 텍스트 크기
                    valueTextSize = 11.0F
                    // 텍스트 색상
                    valueTextColor = ContextCompat.getColor(applicationContext, R.color.red)
                    // 선 두께
                    lineWidth = 2.0F
                    // 선 색상
                    color = ContextCompat.getColor(applicationContext, R.color.red)

                    highLightColor = ContextCompat.getColor(applicationContext, R.color.grey)
                }
                data = LineData(listOf(lineDataSet))
                invalidate()

                setOnChartValueSelectedListener(
                    object : OnChartValueSelectedListener {
                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            e?.let {
                                prevSelected?.icon = null
                                _priceLiveData.value = e.y.toInt().toString()
                                e.icon = ContextCompat.getDrawable(
                                    applicationContext,
                                    R.drawable.selected_icon
                                )
                                (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)
                                    .vibrate(100L)
                            }
                            prevSelected = e
                        }

                        override fun onNothingSelected() {

                        }
                    }
                )
                setOnTouchListener { view, motionEvent ->
                    if (motionEvent.action == MotionEvent.ACTION_UP) {
                        highlightValue(null)
                        prevSelected?.icon = null
                    }

                    false
                }
            }
        }
    }
}