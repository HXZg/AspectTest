package com.xz.navtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xz.lib.FixFragmentDestination
import com.xz.lib.FragmentDestination

/**
 * @title com.xz.navtest  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des AFragment
 * @DATE 2020/8/7  15:42 星期五
 */
@FragmentDestination(id = R.id.DFragment,pageUrl = "www.test.cfrag")
@FixFragmentDestination(id = R.id.DFragment,control = "home")
class DFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_d,container,false)
    }

}