package com.alvaroquintana.edadperruna.data.localfiles

import android.content.Context
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

class FileLocalDb(private val context: Context) {

    fun loadJSONFromAsset(name: String): String? {
        val json: String? = try {
            val `is`: InputStream = context.assets.open("$name.json")
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}