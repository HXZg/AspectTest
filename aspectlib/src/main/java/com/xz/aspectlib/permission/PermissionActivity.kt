package com.xz.aspectlib.permission

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

/**
 * @title com.xz.aspectlib.permission  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des PermissionActivity
 * @DATE 2020/7/31  10:45 星期五
 */
class PermissionActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_LIST = "permission_list"
        private const val REQUEST_CODE = "request_code"
        fun launcherPermission(context: Context,permission: Array<String>,requestCode: Int) {
            context.startActivity(Intent(context,PermissionActivity::class.java).apply {
                putExtra(PERMISSION_LIST,permission)
                putExtra(REQUEST_CODE,requestCode)
            })
        }
    }

    private val permissions by lazy { intent?.getStringArrayExtra(PERMISSION_LIST) }
    private val requestCode by lazy { intent?.getIntExtra(REQUEST_CODE,-1) ?: -1 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (permissions.isNullOrEmpty() || requestCode == -1) {
            PermissionUtils.callResult?.invoke(0)
            finish()
            return
        } else {
            ActivityCompat.requestPermissions(this,permissions!!,requestCode)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        var grantedSize = 0
        grantResults.forEachIndexed { index, grant ->
            if (grant != PackageManager.PERMISSION_GRANTED) {  // 该权限被同意
                val reason = shouldShowRequestPermissionRationale(permissions[index])
                if (!reason) { // 权限被永久拒绝
                    grantedSize = -1
                    return@forEachIndexed
                }
            } else {
                grantedSize++
            }
        }
        if (grantedSize == permissions.size) {  // 权限 全部同意
            PermissionUtils.callResult?.invoke(0)
        } else if (grantedSize >= 0){  // 有权限被拒绝
            PermissionUtils.callResult?.invoke(2)
        } else {  // 有权限被永久拒绝
            PermissionUtils.callResult?.invoke(1)
        }
        finish()
        return
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}