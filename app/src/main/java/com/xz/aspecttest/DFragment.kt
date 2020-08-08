package com.xz.aspecttest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_d.*

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
        btn_fragment.setOnClickListener { Navigation.findNavController(view).navigate(R.id.EFragment) }
    }
}
