package com.bistrocart.base

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.Network.RetrofitClient
import com.retailer.Network.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import java.security.MessageDigest
import java.util.Base64.getEncoder
import java.util.concurrent.TimeUnit

open class BaseActivity : AppCompatActivity() {

    companion object {
        var apiService: ApiService? = null
       // var appPref: AppPref? = null
    }

    var dialog: ProgressDialog? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiService = RetrofitClient.getApiService(httpClient)
    //    appPref = AppPref.getInstance(this)!!
    }

    val httpClient: OkHttpClient
        get() {
            val loggingInterceptor = HttpLoggingInterceptor { message ->
                // Log the request and response messages
                Log.d("OkHttp", message)
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            return OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor) // Add the logging interceptor
                .addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val builder = originalRequest.newBuilder()
                        .header("Authorization", "Bearer " /*+ appPref?.getString(AppPref.API_KEY)*/)

                    Log.e("Authorization", "Bearer "  /*+ appPref?.getString(AppPref.API_KEY)*/)
                    val newRequest = builder.build()
                    chain.proceed(newRequest)
                }
                .build()
        }


    fun showToast(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun isValidEmail(editText: EditText): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(editText.text.toString().trim())
                .matches()
        ) {
            editText.requestFocus()
            return false
        }
        return true
    }


    fun gotoActivity(className: Class<*>?, bundle: Bundle?, isClearStack: Boolean) {
        val intent = Intent(this, className)
        if (bundle != null) intent.putExtras(bundle)
        if (isClearStack) {
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    protected open fun isSuccess(res: Response<*>, baseRes: String): Boolean {
        if (res.code() == 200) {
            return true
        } else if (res.code() == 401) {
           // showToast("Login Again")
           // logout()
        } else {
            showToast(if (baseRes != null) baseRes else baseRes)
        }
        return false
    }

    open fun onFailure(msg: Throwable) {
        Log.e("TAG", "onFailure->$msg")
      //  showToast(getString(com.bistrocart.R.string.server_error))
        //       hideLoading()
    }

    open fun showLoading(msg: String) {
        runOnUiThread {
            if (dialog != null) hideLoading()
            if (dialog == null) {
                dialog = ProgressDialog(this@BaseActivity)
            }
            dialog!!.setMessage(if (!TextUtils.isEmpty(msg)) msg else "loading")
            if (!dialog!!.isShowing()) dialog!!.show()
        }
    }

    open fun hideLoading() {
        if (dialog != null && dialog!!.isShowing()) {
            dialog!!.dismiss()
        }
    }

    open fun isOnline(): Boolean {
        val manager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        var isAvailable = false
        if (networkInfo != null && networkInfo.isConnected) {
            isAvailable = true
        }
        if (!isAvailable) {
            // AppDialog.showNoNetworkDialog(this)
        }
        return isAvailable
    }
}