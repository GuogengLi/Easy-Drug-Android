package com.example.easydrug

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.easydrug.NetService.Api.SignService
import io.reactivex.android.schedulers.AndroidSchedulers


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private var startScan: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startScan = findViewById<TextView>(R.id.start_scan)

        startScan?.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),1);
            } else {
                startActivity(Intent(this, ScanDrugActivity::class.java))
            }

        }

        SignService.getInstance().signUp("1234", "123").observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.i(TAG, it)
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] !== PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        this@MainActivity,
                        Manifest.permission.CAMERA
                    )
                ) {
                    Toast.makeText(this, "被永遠拒絕，只能使用者手動給予權限", Toast.LENGTH_LONG).show()

                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri: Uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "按下拒絕", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "允許權限", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, ScanDrugActivity::class.java))
            }
        }
    }
}