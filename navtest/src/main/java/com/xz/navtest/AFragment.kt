package com.xz.navtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.xz.lib.FixFragmentDestination
import com.xz.lib.FragmentDestination
import com.xz.navtest.utils.NavGraphBuilder

/**
 * @title com.xz.navtest  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des AFragment
 * @DATE 2020/8/7  15:42 星期五
 */
@FragmentDestination(id = R.id.AFragment,isStart = false)
@FixFragmentDestination(id = R.id.AFragment,isStart = true,control = "home")
class AFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_a,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragment = childFragmentManager.findFragmentById(R.id.nav_fragment)
        val navControl = fragment!!.findNavController()
        NavGraphBuilder.setNavGraph(requireContext(),navControl)
    }

}