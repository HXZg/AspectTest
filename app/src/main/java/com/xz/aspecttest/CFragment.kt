package com.xz.aspecttest

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_c.*

/**
 * A simple [Fragment] subclass.
 * Use the [CFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_c, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString("name")

        Log.i("zzzzzzzzzzzz","$name")

        btn_fragment.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_to_dfragment) }
    }
}
