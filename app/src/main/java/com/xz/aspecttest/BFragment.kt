package com.xz.aspecttest

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.work.WorkManager
import com.xz.aspecttest.workmanager.MyWorker
import com.xz.aspecttest.workmanager.WorkManagerExecut
import kotlinx.android.synthetic.main.fragment_b.*

class BFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_b, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_fragment.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_to_cfragment) }

        /*val queueWork = WorkManagerExecut.queueWork<MyWorker>(requireContext())
        WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(queueWork)
            .observe(viewLifecycleOwner, Observer {
                Log.i("zzzzzzzzzzzzz","${it.state}  ${it.progress}  ${it.id}")
            })*/

        val inputStream = context?.assets?.open("newtest.png")
//        val bitmap = BitmapFactory.decodeStream(inputStream)
//        bv_big.setImageBitmap(bitmap)
        bv_big.setImage(inputStream)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.i("zzzzzzzzzz","bfragment:: $hidden")
    }
}
