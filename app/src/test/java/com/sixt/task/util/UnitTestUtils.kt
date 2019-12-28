package com.sixt.task.util

import java.io.IOException
import java.nio.charset.StandardCharsets

object UnitTestUtils {

    fun getJson(fileName: String): String? {
        val json: String?
        json = try {
            val classLoader = ClassLoader.getSystemClassLoader()
            val inputStream = classLoader.getResourceAsStream(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)

            inputStream.read(buffer)
            inputStream.close()

            String(buffer, StandardCharsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }

        return json
    }
}