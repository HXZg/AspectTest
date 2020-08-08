package com.xz.navtest

import android.os.Bundle
import android.util.Log
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
@FragmentDestination(id = R.id.EFragment)
@FixFragmentDestination(id = R.id.EFragment,control = "home")
class EFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_e,container,false)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.i("zzzzzzzzzzz","$isHidden")
    }


    override fun onDestroyView() {
        Log.i("zzzzzzzzzzzz","EFragment on destroy view")
        super.onDestroyView()
    }


    override fun onDestroy() {
        Log.i("zzzzzzzzzzzz","EFragment on destroy")
        super.onDestroy()
    }

}