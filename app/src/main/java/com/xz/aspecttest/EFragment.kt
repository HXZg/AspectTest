package com.xz.aspecttest

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_e.*

/**
 * A simple [Fragment] subclass.
 * Use the [EFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_e, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_fragment.setOnClickListener {
            if (activity is MainActivity) {
//                Navigation.findNavController(view).navigate(R.id.action_to_efragment)
            } else {
                startActivity(Intent(context,MainActivity::class.java))
            }
        }
    }
}
