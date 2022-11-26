package com.example.simplebledetection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

const val VIEW_TYPE_BLE = 0
const val VIEW_TYPE_IBEACON = 1

class DeviceListAdapter(private val deviceList: ArrayList<Any>) : Adapter<RecyclerView.ViewHolder>() {

    // https://stackoverflow.com/questions/47531755/how-to-inflate-different-layout-in-recyclerview-based-on-its-position-in-oncreat
    // https://stackoverflow.com/questions/26245139/how-to-create-recyclerview-with-multiple-view-types
    // https://stackoverflow.com/questions/47457326/is-it-an-anti-pattern-to-use-inheritance-when-handling-recyclerview-list-items
    class BLEViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val address: TextView
        val rssi: TextView

        init {
            address = view.findViewById(R.id.text_address_value)
            rssi = view.findViewById(R.id.text_rssi_value)
        }
    }

    class IBeaconViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val uuid: TextView
        val major: TextView
        val minor: TextView
        val address: TextView
        val rssi: TextView

        init {
            uuid = view.findViewById(R.id.text_uuid_value)
            major = view.findViewById(R.id.text_major_value)
            minor = view.findViewById(R.id.text_minor_value)
            address = view.findViewById(R.id.text_address_value)
            rssi = view.findViewById(R.id.text_rssi_value)
        }
    }

    /**
     * Get viewtype based on BLE device type
     * If BLE device is iBeacon, use iBeacon layout
     */
    override fun getItemViewType(position: Int): Int {
        val dev: Any = deviceList.get(position)
        if (dev is IBeacon)
            return VIEW_TYPE_IBEACON
        return VIEW_TYPE_BLE
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup,  viewType: Int): RecyclerView.ViewHolder {
        // Create a new view, which defines the UI of the list item
        if (viewType == VIEW_TYPE_IBEACON) {
            return IBeaconViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.ibeacon_item, viewGroup,false))
        }
        return BLEViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.ble_item, viewGroup,false))

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_IBEACON -> {
                val iBeaconView = holder as IBeaconViewHolder
                val ibeacon =  deviceList[position] as IBeacon
                iBeaconView.uuid.text = ibeacon.getUUID()
                iBeaconView.major.text = ibeacon.getMajor().toString()
                iBeaconView.minor.text = ibeacon.getMinor().toString()
                iBeaconView.address.text = ibeacon.getAddress()
                iBeaconView.rssi.text = ibeacon.getRssi().toString()
            }
            VIEW_TYPE_BLE -> {
                val bleView = holder as BLEViewHolder
                val ble =  deviceList[position] as BLEDevice
                bleView.address.text = ble.getAddress()
                bleView.rssi.text = ble.getRssi().toString()
            }
        }

    }

    override fun getItemCount(): Int {
        return deviceList.size
    }
}