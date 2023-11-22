package com.example.calendertest

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.util.Calendar
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;



import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.TemporalAdjusters

class MainActivity : BaseActivity() {

    lateinit var calendarView: MaterialCalendarView
    lateinit var EmojiView: ImageView
    lateinit var selectedDateTextView: TextView
    lateinit var diaryTextView: TextView
    lateinit var imgBtn: Button
    lateinit var navigationView: BottomNavigationView
    private val REQUEST_PERMISSIONS=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calendarView=findViewById(R.id.calendarView)
        navigationView = findViewById(R.id.navigationView)
        selectedDateTextView = findViewById(R.id.selectedDateTextView)
        EmojiView = findViewById(R.id.emojiView)
        imgBtn=findViewById(R.id.imgBtn)
        diaryTextView = findViewById(R.id.diaryTextView)
        checkPermission()

        // 월, 요일을 한글로 설정
        calendarView.setTitleFormatter(MonthArrayTitleFormatter(resources.getTextArray(R.array.custom_months)))
        calendarView.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))
        calendarView.setHeaderTextAppearance(R.style.CalendarWidgetHeader)// 좌우 화살표 사이 연, 월의 폰트 스타일 설정


        // 좌우 화살표 가운데의 연/월이 보이는 방식을 커스텀
        calendarView.setTitleFormatter(object : TitleFormatter {
            override fun format(day: CalendarDay): CharSequence {
                val inputText = day.date
                val calendarHeaderElements = inputText.toString().split("-")
                val calendarHeaderBuilder = StringBuilder()
                calendarHeaderBuilder.append(calendarHeaderElements[0])
                    .append(" ")
                    .append(calendarHeaderElements[1])
                return calendarHeaderBuilder.toString()
            }
        })

        val saturdayDecorator = SaturdayDecorator()
        val sundayDecorator = SundayDecorator()
        val todayDecorator = TodayDecorator()
        calendarView.addDecorators(saturdayDecorator,sundayDecorator, todayDecorator)

        //오늘 날짜를 기본값으로 설정
        val today = Calendar.getInstance()
        val year = today.get(Calendar.YEAR)
        val month = today.get(Calendar.MONTH)
        val dayOfMonth = today.get(Calendar.DAY_OF_MONTH)
        diaryTextView.visibility = View.GONE
        diaryTextView.text = String.format("%d / %d / %d", year, month + 1 , dayOfMonth)

        //txt 파일 읽어오기
        fun readTextFromFile(fileName: String): String {
            return try {
                val inputStream: FileInputStream = openFileInput(fileName)
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                val stringBuilder = StringBuilder()
                var text: String?

                while (bufferedReader.readLine().also { text = it } != null) {
                    stringBuilder.append(text)
                }
                inputStream.close()
                stringBuilder.toString()
            } catch (e: IOException) {
                e.printStackTrace()
                "파일을 읽어오는 데 문제가 발생했습니다."
            }
        }

        //원하는 부분 추출
        fun extractEmotionFromFileContent(fileContent: String): String {
            val emotionRegex = Regex("감정:\\s*(.+) 글귀") // 감정 부분부터 글귀 전 부분까지 추출

            val matchResult = emotionRegex.find(fileContent)
            return matchResult?.groupValues?.get(1) ?: "일기가 등록되지 않았습니다."
        }

        calendarView.setOnDateChangedListener { widget, date, selected ->
            imgBtn.visibility = View.VISIBLE

            val year = date.year
            val month = date.month
            val dayOfMonth = date.day

            diaryTextView.text = String.format("%d / %d / %d", year, month, dayOfMonth) // 날짜 표시

            val selectedDate = "$year 년 ${month} 월 $dayOfMonth 일"
            val fileName = "$selectedDate"
            Log.d("MainActivity", "Selected date's file name: $fileName")

            // 파일명으로부터 내용 읽어오기
            val fileContent = readTextFromFile(fileName)
            Log.d("MainActivity", "File content for $fileName: $fileContent")

            // 읽어온 파일 내용을 TextView에 표시
            val fileContent2 = extractEmotionFromFileContent(fileContent)
            selectedDateTextView.text = fileContent2

            val drawableRes = when (fileContent2) {
                "기쁨" -> R.drawable.happy
                "슬픔" -> R.drawable.sorrow
                "놀람" -> R.drawable.suprise
                "분노" -> R.drawable.anger
                "공포" -> R.drawable.fear
                "혐오" -> R.drawable.hatred
                else -> com.google.android.material.R.drawable.m3_tabs_transparent_background // 감정 정보를 찾을 수 없는 경우 기본 이미지 설정
            }

            EmojiView.setImageResource(drawableRes)
        }

        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.calenderFragment -> {
                    val intent = Intent(this@MainActivity, MainActivity::class.java)
                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.homeFragment -> {
                    val intent = Intent(this@MainActivity, HistoryActivity::class.java)
                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.myPageFragment -> {
                    val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
        imgBtn.setOnClickListener{
            val intent = Intent(this, ImgActivity::class.java)
            startActivity(intent)

        }
    }
    private fun checkPermission() {
        val permission = mutableMapOf<String, String>()
        permission["camera"] = Manifest.permission.CAMERA
        permission["storageRead"] = Manifest.permission.READ_EXTERNAL_STORAGE
        permission["storageWrite"] =  Manifest.permission.WRITE_EXTERNAL_STORAGE

        // 현재 권한 상태 검사
        val denied = permission.count { ContextCompat.checkSelfPermission(this, it.value)  == PackageManager.PERMISSION_DENIED }

        // 마시멜로 버전 이후
        if(denied > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission.values.toTypedArray(), REQUEST_PERMISSIONS)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_PERMISSIONS) {
            /*권한 요청을 거부했다면 안내 메시지 보여주며 앱 종료*/
            grantResults.forEach {
                if(it == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(applicationContext, "서비스의 필요한 권한입니다.\n권한에 동의해주세요.", Toast.LENGTH_SHORT).show()
                    //finish()
                }
            }
        }
    }

    class SaturdayDecorator: DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            val sunday = day?.date?.with(DayOfWeek.SATURDAY)?.dayOfMonth
            return sunday == day?.day
        }
        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(object: ForegroundColorSpan(Color.BLUE){})
        }
    }

    class SundayDecorator : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            val sunday = day?.date?.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))?.dayOfMonth
            return sunday == day?.day
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(ForegroundColorSpan(Color.RED))
        }
    }

    class TodayDecorator : DayViewDecorator {
        private val today = CalendarDay.today()

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return day?.equals(today) ?: false
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(ForegroundColorSpan(Color.MAGENTA))
        }
    }
}



