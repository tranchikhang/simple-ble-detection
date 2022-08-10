package com.example.simplebledetection

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.util.Log
import android.widget.TextView

class ScanService {

    private val bluetoothManager: BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter
    private val bluetoothLeScanner: BluetoothLeScanner

    private val TAG = "ScanService"

    private var isScanning = false
    private val handler = Handler()

    // Stops scanning after 10 seconds.
    private var SCAN_PERIOD: Long = 10000

    private lateinit var bleDeviceText: TextView

    constructor(context: Context, textview: TextView) {
        bleDeviceText = textview
        bluetoothManager = context.getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter
        if (bluetoothAdapter==null) {
            // Device doesn't support Bluetooth
            throw Exception("Device doesn't support Bluetooth")
        }

        if (!bluetoothAdapter.isEnabled) {
            throw Exception("Bluetooth is disabled")

        }
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
    }

    public fun startBLEScan() {
        try {
            if (!isScanning) {
                handler.postDelayed({
                    isScanning = false
                    bluetoothLeScanner.stopScan(leScanCallback)
                }, SCAN_PERIOD)
                isScanning = true

                bluetoothLeScanner.startScan(leScanCallback)
            } else {
                isScanning = false
            bluetoothLeScanner.stopScan(leScanCallback)
        }

    } catch (e: SecurityException) {
            Log.e(TAG, "@startScan SecurityException: " + e.message)
        }
    }

    private val leScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            try {
                Log.d(TAG, "Device Name: " + result?.getDevice()?.getName() + " rssi: " + result?.getRssi() + "\n")
                bleDeviceText.text = bleDeviceText.text.toString() + "Device Name: " + result?.getDevice()?.getName() + " rssi: " + result?.getRssi() + "\n"
            } catch (e: SecurityException) {
                Log.e(TAG, "@startScan SecurityException: " + e.message)
            }
            return
        }

    }

    public fun isScanning(): Boolean {
        return isScanning
    }
}