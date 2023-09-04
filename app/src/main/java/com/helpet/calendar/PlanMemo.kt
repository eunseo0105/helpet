package com.helpet.calendar


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.core.util.Pair
import android.widget.Toast
import com.google.android.material.datepicker.MaterialDatePicker
import com.helpet.R
import com.helpet.databinding.ActivityPlanMemoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Calendar.getInstance


class PlanMemo : AppCompatActivity() {

    private lateinit var startDate: String
    private lateinit var endDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPlanMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val date = intent.getStringExtra("date")

        //일정 등록하기
        binding.tvtodayDate.text = date

        //세션 유지_ userId 불러오기
        val sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "") ?: ""


        binding.selectCal.setOnClickListener {
            val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("등록할 기간을 선택해주세요.")
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .build()

            dateRangePicker.show(supportFragmentManager, "date_picker")
            dateRangePicker.addOnPositiveButtonClickListener { selection ->
                val calendar = getInstance()
                calendar.timeInMillis = selection?.first ?: 0
                startDate = SimpleDateFormat("yyyy-MM-dd").format(calendar.time).toString()
                Log.d("start", startDate)

                calendar.timeInMillis = selection?.second ?: 0
                endDate = SimpleDateFormat("yyyy-MM-dd").format(calendar.time).toString()
                Log.d("end", endDate)

                binding.tvtodayDate.text = dateRangePicker.headerText
//                                    getInstance(customerNumString, startDate, endDate, itemNumstring)
            }
        }

        binding.btnClose.setOnClickListener {
            finish()
        }


        binding.btnSave.setOnClickListener {
            binding.btnSave.setOnClickListener {
                val title = binding.edtTitle.text.toString()
                val description = binding.calendarMemo.text.toString()

                if (title.isNotEmpty()) {
                    val intent = Intent()
//                    val schedule = Schedule(startDate, endDate, title, description, calIdx)
//                    intent.putExtra("title", title)
//                    intent.putExtra("startdDate", startDate)
//                    intent.putExtra("endDate", endDate)
//                    intent.putExtra("description", description)
//                    intent.putExtra("schedule", schedule)
                    setResult(RESULT_OK, intent)
                    finish()
                } else {
                    Toast.makeText(this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }

                //retrofit 서버 요청 _ date,userId,title,description
                val server = CalRetrofitInterface.retrofit3.create(CalendarService::class.java)
//            val schedule = Schedule(date, userId, title, description)
                Log.d("일정 등록", userId)
                Log.d("일정 등록", title)
                Log.d("일정 등록", description)



                server.CalendarResult(startDate, endDate, userId, title, description)
                    .enqueue(object : Callback<CalendarPlanResultDTO?> {
                        override fun onResponse(
                            call: Call<CalendarPlanResultDTO?>,
                            response: Response<CalendarPlanResultDTO?>
                        ) {
                            val result = response.body()
                            val status = response.body()?.status
                            Log.d("status", status!!)

                            Log.d("retrofit 서버 요청 성공여부", "$result")
                            if (status.toString() == "success") {
                                val intent = Intent()
                                intent.putExtra("startDate", startDate)
                                intent.putExtra("endDate", endDate)
                                intent.putExtra("title", title)
                                intent.putExtra("description", description)
                                setResult(RESULT_OK, intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "저장 실패",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                            Log.d("retrofit 일정 추가", "$result")

                        }

                        override fun onFailure(call: Call<CalendarPlanResultDTO?>, t: Throwable) {
                            Log.d("에러", t.message!!)

                        }
                    })
            }
        }
    }
}


