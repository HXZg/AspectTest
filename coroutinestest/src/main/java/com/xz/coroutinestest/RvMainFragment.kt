package com.xz.coroutinestest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_rv_main.*

/**
 * @title com.xz.coroutinestest  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des RvMainFragment
 * @DATE 2020/10/19  11:13 星期一
 */
class RvMainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return View.inflate(context,R.layout.fragment_rv_main,null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vp2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 2

            override fun createFragment(position: Int): Fragment {
                return if (position == 0) {
                    CameraFragment()
                }else {
                    MainFragment()
                }
            }

        }

        vp2.setCurrentItem(1,false)

        vp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
    }
}