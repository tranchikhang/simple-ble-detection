package com.example.simplebledetection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class DeviceListAdapter : BaseAdapter {

    private var deviceList : ArrayList<IBeacon>
    private var context : Context

    constructor(context: Context, dataList: ArrayList<IBeacon>) {
        this.deviceList = dataList
        this.context = context
    }

    override fun getCount(): Int {
        return deviceList.size
    }

    override fun getItem(position: Int): Any {
        return deviceList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = LayoutInflater.from(context).inflate(R.layout.device_listview, parent, false)
        val uuid: TextView = rowView.findViewById(R.id.txtUuid)
        uuid.text = deviceList[position].getUUID()

        val major: TextView = rowView.findViewById(R.id.txtMajor)
        major.text = deviceList[position].getMajor().toString()

        val minor: TextView = rowView.findViewById(R.id.txtMinor)
        minor.text = deviceList[position].getMinor().toString()

        val address: TextView = rowView.findViewById(R.id.txtAddress)
        address.text = deviceList[position].getAddress()

        val rssi: TextView = rowView.findViewById(R.id.txtRssi)
        rssi.text = deviceList[position].getRssi().toString()

        return rowView
    }
}