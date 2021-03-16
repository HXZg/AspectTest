package com.xz.navtest

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.xz.navtest.utils.NavGraphBuilder
import com.xz.skinmanager.BaseActivity
import com.xz.skinmanager.SkinManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : BaseActivity() {

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        SkinManager.initContext(this)
        val path = externalCacheDir?.absolutePath + "/skin.apk"
        SkinManager.loadPath(path)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_fragment)

        NavGraphBuilder.build(this,navController,R.id.nav_fragment,"home")

        NavigationUI.setupWithNavController(bottom_view,navController)
        bottom_view.setOnNavigationItemSelectedListener {
            navController.navigate(23)
//            navController.navigate(it.itemId)
//            NavigationUI.onNavDestinationSelected(it,navController)  // 里面会携带 popId 参数 导致 fragment 出栈  界面显示重叠
            return@setOnNavigationItemSelectedListener false
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.i("zzzzzzzzzzzzz","${destination.id}")
            val menu = bottom_view.menu
            for (i in 0 until menu.size()) {
                val item = menu.getItem(i)
                if (item.itemId == destination.id) {
                    item.setChecked(true)
                    return@addOnDestinationChangedListener
                }
            }
        }
    }
}
