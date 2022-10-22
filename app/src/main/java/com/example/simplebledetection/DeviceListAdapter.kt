package com.example.simplebledetection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

class DeviceListAdapter(private val deviceList: ArrayList<IBeacon>) : Adapter<DeviceListAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val uuid: TextView
        val major: TextView
        val minor: TextView
        val address: TextView
        val rssi: TextView

        init {
            uuid = view.findViewById(R.id.txtUuid)
            major = view.findViewById(R.id.txtMajor)
            minor = view.findViewById(R.id.txtMinor)
            address = view.findViewById(R.id.txtAddress)
            rssi = view.findViewById(R.id.txtRssi)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup,  viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.device_listview, viewGroup,false)

        return ViewHolder(view)

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.uuid.text = deviceList[position].getUUID()
        holder.major.text = deviceList[position].getMajor().toString()
        holder.minor.text = deviceList[position].getMinor().toString()
        holder.address.text = deviceList[position].getAddress()
        holder.rssi.text = deviceList[position].getRssi().toString()
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }
}