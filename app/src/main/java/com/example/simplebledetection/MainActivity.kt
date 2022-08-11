package com.example.simplebledetection

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.simplebledetection.databinding.ActivityMainBinding
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"

    private lateinit var scanService: ScanService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scanBtn.setOnClickListener { startScan() }
        binding.exitBtn.setOnClickListener { exitApp() }


        // catch bluetooth is disabled exception
        isLocationGranted(this)
        isBluetoothGranted(this)

        scanService = ScanService(this, binding.bleDevice)

    }

    private fun exitApp() {
        // exit application
        this@MainActivity.finish()
        exitProcess(0)
    }

    private fun startScan() {
        // start scanning BLE device
        if (scanService != null) {
            scanService.startBLEScan()
            if (scanService.isScanning()) {
                binding.scanBtn.text = "Scanning"
            } else {
                binding.scanBtn.text = "Scan"
                scanService.stopBLEScan()
            }
        }
    }

    private fun isLocationGranted(context: Context) {
        Log.d(TAG, "@isLocationGranted")
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "@isLocationGranted location is off")
            return
        }
    }

    private val BLE_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private val ANDROID_12_BLE_PERMISSIONS = arrayOf(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private fun isBluetoothGranted(context: Context) {
        Log.d(TAG, "@BluetoothGranted check bluetooth")
        if ((ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED) ||
            (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED) ||
            (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED) ||
            (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_ADMIN
            ) != PackageManager.PERMISSION_GRANTED)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                ActivityCompat.requestPermissions(this, ANDROID_12_BLE_PERMISSIONS, 1);
            else
                ActivityCompat.requestPermissions(this, BLE_PERMISSIONS, 1);

            Log.d(TAG, "@BluetoothGranted Bluetooth is off")
            return
        }
    }


}