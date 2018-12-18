package com.joosung.pickme

import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*
import kotlin.test.assertEquals

class DateFormatUnitTest {
    @Test
    fun testStringToDate() {
        val dateTime = "2018-05-05T09:09:01.000+09:00"

        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        val date = format.parse(dateTime)
        println("date ${SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date)}")

        val calendar = Calendar.getInstance()
        calendar.time = date
        assertEquals(calendar.get(Calendar.YEAR), 2018)
        assertEquals(calendar.get(Calendar.MONTH) + 1, 5)
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), 5)
    }
}