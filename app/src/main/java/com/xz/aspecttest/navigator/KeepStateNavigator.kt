package com.xz.aspecttest.navigator

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator

/**
 * @title com.xz.aspecttest.navigator  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des KeepStateNavigator
 * @DATE 2020/8/4  11:50 星期二
 */
@Navigator.Name("keep_state_fragment")
class KeepStateNavigator(val context: Context,val fragmentManager: FragmentManager,val containerId: Int) : FragmentNavigator(context,fragmentManager,containerId) {

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        if (fragmentManager.isStateSaved) {
            return null
        }
        val tag = destination.id.toString()
        val transaction = fragmentManager.beginTransaction()

        var initialNavigate = false
        val currentFragment = fragmentManager.primaryNavigationFragment
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        } else {
            initialNavigate = true
        }

        var fragment = fragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            val className = destination.className
            fragment = fragmentManager.fragmentFactory.instantiate(context.classLoader, className)
            fragment.arguments = args
            transaction.add(containerId, fragment, tag)
        } else {
            transaction.show(fragment)
        }

        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            transaction.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

        transaction.setPrimaryNavigationFragment(fragment)
        transaction.setReorderingAllowed(true)
        transaction.commitNow()

        return destination
    }

}
