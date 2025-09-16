package com.aarevalo.trackit.core.domain.location

class LocationCalculations {

    fun getTotalDistanceMeters(locations:List<List<LocationWithTimestamp>>): Int{
        return locations
            .sumOf{ timestampLocation ->
                timestampLocation.zipWithNext{ location1, location2 ->
                    location1.location.distanceTo(location2.location).toInt()
                }.sum()
            }
    }
}