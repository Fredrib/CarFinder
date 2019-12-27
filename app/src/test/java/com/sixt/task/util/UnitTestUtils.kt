package com.sixt.task.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets

object UnitTestUtils {

    fun <T> getObject(fileName: String, classType: Class<T>): T {
        val json = getJson(
            fileName,
            classType.classLoader!!
        )
        return Gson().fromJson(json, classType)
    }

    fun <T> getListObject(fileName: String, type: Type): List<T> {
        val json =
            getJson(fileName)
        return Gson().fromJson<List<T>>(json, type)
    }

    fun <T> getListObjectFromArray(fileName: String, type: Type): List<T> {
        val json = getJson(fileName)
        val founderListType: Type = object : TypeToken<ArrayList<T?>?>() {}.type

        return Gson().fromJson(json, founderListType)
    }

    private fun getJson(fileName: String, classLoader: ClassLoader): String? {
        return try {
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
    }

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