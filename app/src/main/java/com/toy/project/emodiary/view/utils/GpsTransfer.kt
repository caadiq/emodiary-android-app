package com.toy.project.emodiary.view.utils

import kotlin.math.*

data class GpsTransfer(
    var lat: Double = 0.0, //gps로 반환받은 위도
    var lon: Double = 0.0, //gps로 반환받은 경도
    var xLat: Double = 0.0, //x좌표로 변환된 위도
    var yLon: Double = 0.0 //y좌표로 변환된 경도
) {
    fun transfer(mode: Int) {
        val RE = 6371.00877 // 지구 반경(km)
        val GRID = 5.0 // 격자 간격(km)
        val SLAT1 = 30.0 // 투영 위도1(degree)
        val SLAT2 = 60.0 // 투영 위도2(degree)
        val OLON = 126.0 // 기준점 경도(degree)
        val OLAT = 38.0 // 기준점 위도(degree)
        val XO = 43.0 // 기준점 X좌표(GRID)
        val YO = 136.0 // 기1준점 Y좌표(GRID)

        val DEGRAD = PI / 180.0
        val RADDEG = 180.0 / PI

        val re = RE / GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olon = OLON * DEGRAD
        val olat = OLAT * DEGRAD

        var sn = tan(PI * 0.25 + slat2 * 0.5) / tan(PI * 0.25 + slat1 * 0.5)
        sn = ln(cos(slat1) / cos(slat2)) / ln(sn)
        var sf = tan(PI * 0.25 + slat1 * 0.5)
        sf = sf.pow(sn) * cos(slat1) / sn
        var ro = tan(PI * 0.25 + olat * 0.5)
        ro = re * sf / ro.pow(sn)

        if (mode == 0) {
            var ra = tan(PI * 0.25 + (lat) * DEGRAD * 0.5)
            ra = re * sf / ra.pow(sn)
            var theta = lon * DEGRAD - olon
            if (theta > PI) theta -= 2.0 * PI
            if (theta < -PI) theta += 2.0 * PI
            theta *= sn
            xLat = floor(ra * sin(theta) + XO + 0.5)
            yLon = floor(ro - ra * cos(theta) + YO + 0.5)
        } else {
            val xn = xLat - XO
            val yn = ro - yLon + YO
            var ra = sqrt(xn * xn + yn * yn)
            if (sn < 0.0) {
                ra = -ra
            }
            var alat = (re * sf / ra).pow((1.0 / sn))
            alat = 2.0 * atan(alat) - PI * 0.5

            var theta: Double
            if (abs(xn) <= 0.0) {
                theta = 0.0
            } else {
                if (abs(yn) <= 0.0) {
                    theta = PI * 0.5
                    if (xn < 0.0) {
                        theta = -theta
                    }
                } else theta = atan2(xn, yn)
            }
            val alon = theta / sn + olon
            lat = alat * RADDEG
            lon = alon * RADDEG
        }
    }
}