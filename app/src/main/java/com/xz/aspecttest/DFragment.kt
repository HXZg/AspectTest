package com.xz.aspecttest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.xz.aspecttest.view.QQBubbleView
import kotlinx.android.synthetic.main.fragment_d.*
import q.rorbin.badgeview.QBadgeView

/**
 * A simple [Fragment] subclass.
 * Use the [DFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_d, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_fragment.setOnClickListener {
        }

        val badge = QBadgeView(requireContext()).bindTarget(btn_fragment).setBadgeNumber(5)

        badge.setOnDragStateChangedListener { dragState, badge, targetView ->

        }
    }
}
