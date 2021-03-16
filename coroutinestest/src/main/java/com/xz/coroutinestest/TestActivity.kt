package com.xz.coroutinestest

import android.animation.ObjectAnimator
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_test.*

/**
 * @title com.xz.coroutinestest  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des TestActivity
 * @DATE 2020/10/19  16:26 星期一
 */
class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
//        custom_search.startIndexAnim()
        /*custom_search.postDelayed({
            custom_search.startAnimator()
        },3000)*/

        val ofFloat = ObjectAnimator.ofFloat(test_vp2,"alpha", 1f, 0f)
        ofFloat.start()

        val lables = arrayOf("one","two","three")
        test_vp2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 3
            }

            override fun createFragment(position: Int): Fragment {
                return TestRvFragment()
            }

        }

        TabLayoutMediator(test_tab,test_vp2,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = lables[position]
            }).attach()
    }
}