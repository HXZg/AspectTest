package com.xz.skinmanager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * @title com.xz.skinmanager  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des BaseFragment
 * @DATE 2020/8/27  10:35 星期四
 */
class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}