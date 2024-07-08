/*
 * Copyright (c) 2021 Hai Zhang <dreaming.in.code.zh@gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.files.storage

import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import me.zhanghai.android.files.BuildConfig
import android.os.Handler
import android.os.Looper
import android.os.storage.StorageVolume
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import me.zhanghai.android.files.app.application
import me.zhanghai.android.files.app.storageManager
import me.zhanghai.android.files.app.usbManager
import me.zhanghai.android.files.compat.isPrimaryCompat
import me.zhanghai.android.files.compat.registerReceiverCompat
import me.zhanghai.android.files.compat.storageVolumesCompat

object StorageVolumeListLiveData : LiveData<List<StorageVolume>>() {
    init {
        loadValue()
        application.registerReceiverCompat(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    loadValue()
                }
            }, IntentFilter().apply {
                // @see android.os.storage.VolumeInfo#sEnvironmentToBroadcast
                addAction(Intent.ACTION_MEDIA_UNMOUNTED)
                addAction(Intent.ACTION_MEDIA_CHECKING)
                addAction(Intent.ACTION_MEDIA_MOUNTED)
                addAction(Intent.ACTION_MEDIA_EJECT)
                addAction(Intent.ACTION_MEDIA_UNMOUNTABLE)
                addAction(Intent.ACTION_MEDIA_REMOVED)
                addAction(Intent.ACTION_MEDIA_BAD_REMOVAL)
                // The "file" data scheme is required to receive these broadcasts.
                // @see https://stackoverflow.com/a/7143298
                addDataScheme(ContentResolver.SCHEME_FILE)
            }, ContextCompat.RECEIVER_EXPORTED
        )
        application.registerReceiverCompat(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    Thread {
                        repeat(50) {
                            Thread.sleep(100)
                            if (storageManager.storageVolumesCompat != value) {
                                Handler(Looper.getMainLooper()).post(::loadValue)
                            }
                        }
                    }.start()
                }
            }, IntentFilter().apply {
                addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
                addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
            }, ContextCompat.RECEIVER_EXPORTED
        )
    }

    fun loadValue() {
        // as we don't have a criteria how to mach storage value objects to usb device objects,
        // so if there is any non usb or sdcard device connected, than block all external usb or sd devices
        val isNonUsbOrSdCardDeviceConnected = usbManager.deviceList.values.any { device ->
            device.getInterface(0).let { it.interfaceClass != 8 || it.interfaceSubclass != 6 }
        }
        value = storageManager.storageVolumesCompat.filter {
            (!BuildConfig.IS_TALK_ONLY_BUILD && !isNonUsbOrSdCardDeviceConnected) || it.isPrimaryCompat
        }
    }
}
