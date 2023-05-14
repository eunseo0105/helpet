package com.helpet.vector

import android.content.Context
import com.helpet.R
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_vector_choice_pet.*
import kotlinx.android.synthetic.main.custom_petlist_item.*
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.stream.IntStream.range
import javax.security.auth.callback.Callback

class VectorChoicePet : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vector_choice_pet)
        // SharedPreferences 객체 생성
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        // 유저아이디 데이터 읽기
        val value = sharedPreferences.getString("userId", "null")

        Log.d("value",value!!)

        back.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        //유저가 이미 저장해둔 반려동물 정보 가져오는 데이터 값들
        val textuser = value?.toRequestBody()
        val server3=  RetrofitApi2.retrofit2.create(GetPetService::class.java)

        server3.getPetRegister(textuser!!).enqueue(object :retrofit2.Callback<petListResponseDTO>{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<petListResponseDTO?>?, response: Response<petListResponseDTO?>){
                Log.d("반려동물 리스트", "" + response.body().toString())

                    val agepet = response.body()?.result?.get(0)?.petAge
                    val birthpet = response.body()?.result?.get(0)?.petBirth
                    val imgpet = response.body()?.result?.get(0)?.petImg
//                    val imgpet2 = stringToBitmap(imgpet!!)
                    val namepet = response.body()?.result?.get(0)?.petName
    //              val statuspet = response.body()?.status
    //              val genderpet = response.body()?.result?.get(i)?.petGender
    //              val idxpet = response.body()?.result?.get(i)?.petIdx
    //              val speciespet = response.body()?.result?.get(i)?.petSpecies
    //              val useridpet = response.body()?.result?.get(i)?.userId

//                    choicePet.setImageBitmap(imgpet2)
                    choicePetName.text= namepet
                    choicePetAge.text = agepet.toString()
                    choicePetBirth.text = birthpet
            }
            override fun onFailure(call: Call<petListResponseDTO>, t: Throwable) {
                Log.d("에러", t.message!!)
            }
        })


        petRegister.setOnClickListener {
            val intent= Intent(this, PetRegisterActivity::class.java  )
            startActivity(intent)
        }
    }
    //bitmap 을  string 형태로 변환하는 메서드 (이렇게 string 으로 변환된 데이터를 mysql 에서 longblob 의 형태로 저장하는식으로 사용가능)
    @RequiresApi(Build.VERSION_CODES.O)
    fun bitmapToString(bitmap: Bitmap): String? {
        var image = ""
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        image = Base64.getEncoder().encodeToString(byteArray)
        return image
    }

    //string 을  bitmap 형태로 변환하는 메서드
    @RequiresApi(Build.VERSION_CODES.O)
    fun stringToBitmap(data: String?): Bitmap? {
        var bitmap: Bitmap? = null
        val byteArray: ByteArray = Base64.getDecoder().decode(data)
        val stream = ByteArrayInputStream(byteArray)
        bitmap = BitmapFactory.decodeStream(stream)
        return bitmap
    }
}