package com.xz.aspecttest

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.IntRange
import androidx.annotation.IntegerRes
import com.xz.aspectlib.annotation.*
import com.xz.aspectlib.intercept.InterceptExecutor
import com.xz.aspectlib.intercept.InterceptUtils
import com.xz.aspectlib.utils.ThreadEnum

@ActivityTime
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        InterceptUtils.setIntercept(object : InterceptExecutor {
            override fun intercept(type: Int): Boolean {
                return type == 1
            }
        })

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this,ThreeActivity::class.java))
            finish()
        },5000)
//        aop()
//        sd()
//        intercept()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @IOThread(value = ThreadEnum.MAIN)
    fun aop() {
        Log.i("zzzzzzzzz",(Looper.getMainLooper() == Looper.myLooper()).toString())
    }

    @Intercept(type = [2])
    fun intercept() {
        Log.i("zzzzzzzzzzz","intercept")
    }

    @Permission(value = [Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE],requestCode = 1)
    fun sd() {  // 全部权限 同意后  才执行
        Log.i("zzzzzzzzz","sd")
    }

    @PermissionCancel  // 权限请求被拒绝
    fun cancel(code: Int) {
        Log.i("zzzzzzzzzz","cancel  $code")
    }

    @PermissionDenied  // 永久拒绝某权限
    fun denied(code: Int) {
        Log.i("zzzzzzzzzz","denied  $code")
    }


}
