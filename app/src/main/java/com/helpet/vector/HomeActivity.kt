package com.helpet.vector

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.helpet.R
import com.helpet.login.Login
import kotlinx.android.synthetic.main.activity_home.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class HomeActivity : AppCompatActivity() {

    //lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        getHashKey()

        hospital.setOnClickListener {
            val intent = Intent(this, HospitalActivity::class.java  )
            startActivity(intent)
        }

        btn_login.setOnClickListener {
            val intent=Intent(this, Login::class.java)
            startActivity(intent)
        }

        bnv_main.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    // 다른 프래그먼트 화면으로 이동하는 기능
                    R.id.home -> {
                        val mainVectorFragment = VectorMain()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_container, mainVectorFragment).commit()
                    }

                }
                true
            }
            selectedItemId = R.id.home
        }
    }

    private fun getHashKey() {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageInfo == null) Log.e("KeyHash", "KeyHash:null")
        for (signature in packageInfo!!.signatures) {
            try {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch (e: NoSuchAlgorithmException) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=$signature", e)
            }
        }
    }

}


