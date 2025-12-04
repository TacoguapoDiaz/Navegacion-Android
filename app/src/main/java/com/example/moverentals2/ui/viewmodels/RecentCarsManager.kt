package com.example.moverentals2.data

import android.content.Context

object RecentCarsManager {

    private const val PREFS_NAME = "RecentCarsPrefs"
    private const val KEY_RECENT_CARS = "recent_car_ids"
    private const val MAX_RECENTS = 5

    fun addRecentCar(context: Context, carId: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val recentsString = prefs.getString(KEY_RECENT_CARS, "") ?: ""

        var recentIds = recentsString.split(',').filter { it.isNotBlank() }.toMutableList()


        recentIds.remove(carId)

        recentIds.add(0, carId)

        while (recentIds.size > MAX_RECENTS) {
            recentIds.removeAt(recentIds.size - 1)
        }


        prefs.edit().putString(KEY_RECENT_CARS, recentIds.joinToString(",")).apply()
    }

    fun getRecentCarIds(context: Context): List<String> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val recentsString = prefs.getString(KEY_RECENT_CARS, "") ?: ""
        return recentsString.split(',').filter { it.isNotBlank() }
    }
}
