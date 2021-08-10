package com.minerdev.exermate.view.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.minerdev.exermate.R
import com.minerdev.exermate.databinding.FragmentTodayGoalBinding
import com.minerdev.exermate.view.activity.GoalSettingActivity

class TodayGoalFragment : Fragment(), SensorEventListener {
    private val labels = listOf("일", "월", "화", "수", "목", "금", "토")

    private val binding by lazy { FragmentTodayGoalBinding.inflate(layoutInflater) }
    private val sensorManager by lazy { requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private val stepCountSensor by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) }

    private var currentSteps = 0
    private var startSteps = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        if (stepCountSensor == null) {
            Toast.makeText(requireContext(), "걸음수 센서가 존재하지않습니다!", Toast.LENGTH_LONG).show()
            requireActivity().finishAffinity()

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val permissionChecked = ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACTIVITY_RECOGNITION
                )

                if (permissionChecked != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 101)

                } else {
                    sensorManager.registerListener(
                        this,
                        stepCountSensor,
                        SensorManager.SENSOR_DELAY_GAME
                    )
                }
            }
        }

        val dataList = arrayListOf(1000, 2000, 3000, 4000, 5000, 10000, 6000)
        drawBarChart(dataList)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar_today_goal, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_more_menu -> {
                startActivity(Intent(requireContext(), GoalSettingActivity::class.java))
            }
            else -> requireActivity().finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            101 ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sensorManager.registerListener(
                        this@TodayGoalFragment,
                        stepCountSensor,
                        SensorManager.SENSOR_DELAY_GAME
                    )

                } else {
                    val alertDialog = AlertDialog.Builder(context)
                    alertDialog.setTitle("앱 권한")
                    alertDialog.setMessage("해당 앱의 원활한 기능을 이용하시려면 애플리케이션 정보>권한에서 '저장공간' 권한을 허용해 주십시오.")
                    alertDialog.setPositiveButton("권한설정") { dialog: DialogInterface, _: Int ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                            Uri.parse("package:" + requireContext().packageName)
                        )
                        startActivity(intent)
                        dialog.cancel()
                    }
                    alertDialog.setNegativeButton("취소") { dialog: DialogInterface, _: Int -> dialog.cancel() }
                    alertDialog.show()
                }

            else -> {
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            if (startSteps == 0) {
                startSteps = event.values[0].toInt()
            }

            currentSteps = event.values[0].toInt() - startSteps
            binding.tvNowSteps.text = currentSteps.toString()
            binding.tvProgress.text = (currentSteps / 10000).toString() + "%"
            Log.d("TAG", "move $currentSteps")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    private fun drawBarChart(dataList: ArrayList<Int>) {
        val entryList = ArrayList<BarEntry>()
        for ((i, data) in dataList.withIndex()) {
            entryList.add(BarEntry(i.toFloat(), data.toFloat()))
        }

        val barDataSet = BarDataSet(entryList, "StepDataSet")
        val barData = BarData(barDataSet)

        val barChart = binding.barChart.apply {
            setScaleEnabled(false)
            setPinchZoom(false)
            data = barData
            description = null
            legend.isEnabled = false
            axisLeft.axisMinimum = 0F
            axisLeft.axisMaximum = 11000F
            axisRight.setDrawLabels(false)
            axisRight.setDrawGridLines(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.labelCount = entryList.size
            xAxis.setDrawGridLines(false)
        }

        barChart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return labels[value.toInt()]
            }
        }

        barChart.invalidate()
    }
}