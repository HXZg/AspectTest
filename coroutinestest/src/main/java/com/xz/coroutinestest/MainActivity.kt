package com.xz.coroutinestest

import android.Manifest
import android.os.*
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.widget.TextSwitcher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @title com.xz.coroutinestest  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des MainActivity
 * @DATE 2020/9/11  9:32 星期五
 */
class MainActivity : AppCompatActivity(), Handler.Callback, SurfaceHolder.Callback {

    val mHandler = Handler(Looper.getMainLooper())

    private val mainVM by lazy {
        ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        surface.holder.addCallback(this)

        supportFragmentManager.beginTransaction().replace(R.id.fl_main,RvMainFragment()).commit()
    }

    override fun handleMessage(p0: Message): Boolean {
        return false
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        CameraManager.stopCamera()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA),1)
        }else{
            CameraManager.open()
            CameraManager.startPriview(holder,{})
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        CameraManager.open()
        CameraManager.startPriview(surface.holder,{})
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

}