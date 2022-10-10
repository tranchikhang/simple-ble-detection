package com.example.simplebledetection

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.util.Log

class ScanService {

    private val bluetoothManager: BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothLeScanner: BluetoothLeScanner

    private val TAG = "ScanService"

    // Scan service flag
    private var isScanning = false
    // BLE scan flag
    private var isScanningBLE = false
    private val handler = Handler()

    // Scan for x seconds, then stop for x seconds, then scan again
    private var SCAN_PERIOD: Long = 5000

    private lateinit var deviceList: ArrayList<IBeacon>
    private lateinit var adapter: DeviceListAdapter

    constructor(context: Context, deviceList: ArrayList<IBeacon>, adapter: DeviceListAdapter) {
        this.deviceList = deviceList
        this.adapter = adapter
        bluetoothManager = context.getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            throw Exception("Device doesn't support Bluetooth")
        }
        if (isBluetoothEnabled()) {
            bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        }
    }

    fun initScanner() {
        if (!this::bluetoothLeScanner.isInitialized) {
            bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        }
    }

    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter.isEnabled
    }

    fun startBLEScan() {
        isScanning = true
        try {
            handler.post(object : Runnable {
                override fun run() {
                    if (isScanningBLE) {
                        Log.e(TAG, "@startBLEScan stop scan")
                        isScanningBLE = false
                        bluetoothLeScanner.stopScan(leScanCallback)
                    } else {
                        Log.e(TAG, "@startBLEScan start scan")
                        isScanningBLE = true
                        bluetoothLeScanner.startScan(leScanCallback)
                    }
                    Log.e(TAG, "@startBLEScan post new message")
                    // Stops scanning after a pre-defined scan period.
                    handler.postDelayed(this, SCAN_PERIOD)
                }
            })
        } catch (e: SecurityException) {
            Log.e(TAG, "@startScan SecurityException: " + e.message)
        }
    }

    fun stopBLEScan() {
        isScanning = false
        if (isScanningBLE) {
            bluetoothLeScanner.stopScan(leScanCallback)
        }
    }

    private val leScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            if (result != null) {
                val scanRecord = result.scanRecord
                super.onScanResult(callbackType, result)
                try {
                    if (scanRecord != null && isIBeacon(scanRecord.bytes)) {
                        val iBeacon = IBeacon(result, scanRecord.bytes)
                        val idx = checkDeviceExists(result)
                        if (idx == -1) {
                            deviceList.add(iBeacon)
                        } else {
                            // update
                            deviceList[idx] = iBeacon
                        }
                        adapter.notifyDataSetChanged()
                    }
                } catch (e: SecurityException) {
                    Log.e(TAG, "@startScan SecurityException: " + e.message)
                }
            }
            return
        }
    }

    /**
     * check if our device list already has a scan result whose MAC address is identical to the new incoming ScanResult
     * @param result BLE scan result
     * @return -1 if doesn't exist
     */
    fun checkDeviceExists(result: ScanResult): Int {
        val indexQuery = deviceList.indexOfFirst { it.getAddress() == result.device.address }
        return indexQuery
    }

    /**
     * Check if packet is from an iBeacon
     * @param packetData packet data which app captured
     * @return true if packet is from iBeacon, otherwise false
     */
    fun isIBeacon(packetData: ByteArray): Boolean {
        var startByte = 2
        while (startByte <= 5) {
            if (packetData[startByte + 2].toInt() and 0xff == 0x02 && packetData[startByte + 3].toInt() and 0xff == 0x15) {
                // debug result: startByte = 5
                return true
            }
            startByte++
        }
        return false
    }

    fun isScanning(): Boolean {
        return isScanning
    }

}