package com.minerdev.exermate.view.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.*
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
import java.lang.Float.max
import java.util.*
import kotlin.collections.ArrayList

class TodayGoalFragment : Fragment(), SensorEventListener {
    private val labels = listOf("일", "월", "화", "수", "목", "금", "토")
    private val requestActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    goalSteps = intent.getIntExtra("goalSteps", 10000)
                    refreshGoalSteps()
                }
            }
        }
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_DATE_CHANGED) {
                dateChanged()
            }
        }
    }

    private val binding by lazy { FragmentTodayGoalBinding.inflate(layoutInflater) }
    private val sensorManager by lazy { requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private val stepCountSensor by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) }

    private var dayOfWeek = -1
    private var goalSteps = 10000
    private var currentSteps = 1000
    private var startSteps = 0

    private var isFirst = true
    private var alertDialog : AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val builder = AlertDialog.Builder(requireContext()).apply {
            setTitle("앱 권한")
            setMessage("해당 앱의 원활한 기능을 이용하시려면 애플리케이션 정보>권한에서 '신체 활동' 권한을 허용해 주십시오.")
            setPositiveButton("권한설정") { dialog: DialogInterface, _ ->
                val intent =
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                        Uri.parse("package:" + requireContext().packageName)
                    )
                startActivity(intent)
                dialog.dismiss()
            }
            setNegativeButton("취소") { _, _ ->
                return@setNegativeButton
            }
        }

        alertDialog = builder.create()

        val sharedPreferences =
            requireActivity().getSharedPreferences("todayGoal", AppCompatActivity.MODE_PRIVATE)
        startSteps = sharedPreferences.getInt("startSteps", -1)
        goalSteps = sharedPreferences.getInt("goalSteps", 10000)

        binding.tvGoalSteps.text = goalSteps.toString()

        val dataList = MutableList(7) { 0 }
        initBarChart(dataList)

        val intentFilter = IntentFilter(Intent.ACTION_DATE_CHANGED)
        requireActivity().registerReceiver(receiver, intentFilter)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences =
            requireActivity().getSharedPreferences("todayGoal", AppCompatActivity.MODE_PRIVATE)
        val prevDayOfWeek = sharedPreferences.getInt("dayOfWeek", -1)
        val calendar = Calendar.getInstance()
        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1

        if (prevDayOfWeek != currentDayOfWeek) {
            dateChanged()

        } else {
            dayOfWeek = currentDayOfWeek
        }

        if (stepCountSensor == null) {
            Toast.makeText(requireContext(), "걸음수 센서가 존재하지 않습니다!", Toast.LENGTH_LONG).show()

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val permissionChecked = ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACTIVITY_RECOGNITION
                )

                if (permissionChecked != PackageManager.PERMISSION_GRANTED) {
                    if (isFirst) {
                        isFirst = false
                        requestPermissions(arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 101)

                    } else {
                        alertDialog?.let {
                            if (!it.isShowing) {
                                it.show()
                            }
                        }
                    }

                } else {
                    sensorManager.registerListener(
                        this,
                        stepCountSensor,
                        SensorManager.SENSOR_DELAY_GAME
                    )
                }

            } else {
                sensorManager.registerListener(
                    this,
                    stepCountSensor,
                    SensorManager.SENSOR_DELAY_GAME
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(receiver)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar_today_goal, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_more_menu -> {
                requestActivity.launch(Intent(requireContext(), GoalSettingActivity::class.java))
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
                    alertDialog?.let {
                        if (!it.isShowing) {
                            it.show()
                        }
                    }
                }

            else -> {
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            if (startSteps < 0) {
                startSteps = event.values[0].toInt()
                val sharedPreferences = requireActivity().getSharedPreferences(
                    "todayGoal",
                    AppCompatActivity.MODE_PRIVATE
                )

                val editor = sharedPreferences.edit()
                editor.putInt("startSteps", startSteps)
                editor.apply()
            }

            currentSteps = event.values[0].toInt() - startSteps
            binding.tvCurrentSteps.text = "$currentSteps"
            binding.tvProgress.text = "${currentSteps * 100 / goalSteps}%"
            binding.tvGoalSteps.text = "/$goalSteps 걸음"
            binding.progressBar.progress = currentSteps * 100 / goalSteps

            refreshBarChart()

            Log.d("TAG", "move $currentSteps")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun refreshGoalSteps() {
        binding.tvProgress.text = "${currentSteps * 100 / goalSteps}%"
        binding.tvGoalSteps.text = "/$goalSteps 걸음"
        binding.progressBar.progress = currentSteps * 100 / goalSteps

        binding.barChart.axisLeft.axisMaximum =
            max(goalSteps * 1.1F, binding.barChart.data.yMax * 1.1F)
        binding.barChart.invalidate()
    }

    private fun initBarChart(dataList: MutableList<Int>) {
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
            binding.barChart.axisLeft.axisMaximum =
                max(goalSteps * 1.1F, binding.barChart.data.yMax * 1.1F)
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

    private fun refreshBarChart() {
        val dataList = MutableList(7) { 0 }
        dataList[dayOfWeek] = currentSteps
        val entryList = ArrayList<BarEntry>()
        for ((i, data) in dataList.withIndex()) {
            entryList.add(BarEntry(i.toFloat(), data.toFloat()))
        }

        val barDataSet = BarDataSet(entryList, "StepDataSet")
        val barData = BarData(barDataSet)

        binding.barChart.data = barData
        binding.barChart.notifyDataSetChanged()
        binding.barChart.invalidate()
    }

    private fun dateChanged() {
        val calendar = Calendar.getInstance()
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
        startSteps = -1
        currentSteps = 0

        binding.tvCurrentSteps.text = "$currentSteps"
        binding.tvProgress.text = "${currentSteps * 100 / goalSteps}%"
        binding.tvGoalSteps.text = "/$goalSteps 걸음"
        binding.progressBar.progress = currentSteps * 100 / goalSteps

        if (dayOfWeek == 0) {
            val dataList = MutableList(7) { 0 }
            dataList[dayOfWeek] = currentSteps
            val entryList = ArrayList<BarEntry>()
            for ((i, data) in dataList.withIndex()) {
                entryList.add(BarEntry(i.toFloat(), data.toFloat()))
            }

            val barDataSet = BarDataSet(entryList, "StepDataSet")
            val barData = BarData(barDataSet)

            binding.barChart.data = barData
            binding.barChart.notifyDataSetChanged()
            binding.barChart.invalidate()
        }

        val sharedPreferences =
            requireActivity().getSharedPreferences(
                "todayGoal",
                AppCompatActivity.MODE_PRIVATE
            )

        val editor = sharedPreferences.edit()
        editor.putInt("dayOfWeek", dayOfWeek)
        editor.apply()
    }
}