package com.xz.coroutinestest

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_rv.*

/**
 * @title com.xz.coroutinestest  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des MainFragment
 * @DATE 2020/10/19  10:16 星期一
 */
class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rv,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_main.layoutManager = GridLayoutManager(context,3)
        rv_main.adapter = RvAdapter()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
    }

    class RvAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return RvViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rv,parent,false))
        }

        override fun getItemCount(): Int {
            return 100
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        }

        class RvViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        }

    }
}