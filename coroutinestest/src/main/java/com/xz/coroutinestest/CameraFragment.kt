package com.xz.coroutinestest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * @title com.xz.coroutinestest  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des CameraFragment
 * @DATE 2020/10/19  11:12 星期一
 */
class CameraFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return View.inflate(context,R.layout.fragment_camera,null)
    }
}