package com.xz.navtest

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.xz.lib.FixFragmentDestination
import com.xz.lib.FragmentDestination
import kotlinx.android.synthetic.main.fragment_b.*

/**
 * @title com.xz.navtest  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des AFragment
 * @DATE 2020/8/7  15:42 星期五
 */
@FragmentDestination(id = R.id.BFragment,isStart = true)
@FixFragmentDestination(id = R.id.BFragment,control = "home")
class BFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_b,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_fragment.setOnClickListener { findNavController().navigate(Uri.parse("https://www.test.cfrag")) }
    }

}