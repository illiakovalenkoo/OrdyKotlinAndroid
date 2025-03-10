package com.feodal.ordy.helpers

import android.content.Context
import com.feodal.ordy.models.Cart
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader

class JSONHelper {

    companion object {
        private val fileName = "cart.json"

        fun exportToJSON(context: Context, dataList: List<Cart?>?) {
            val jsonString = Gson().toJson(dataList)

            val file: FileOutputStream? = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            file?.write(jsonString.toByteArray())
            file?.close()
        }

        fun importFromJSON(context: Context): List<Cart?>? {
            val file = File(context.filesDir, fileName)
            if(!file.exists())
                return null
            val fileInputStream: FileInputStream? = context.openFileInput(fileName)
            val stream = InputStreamReader(fileInputStream)

            val listType = object : TypeToken<List<Cart>>() {}.type
            val dataItems: List<Cart?>? = Gson().fromJson(stream, listType)

            stream.close()
            fileInputStream?.close()

            return dataItems
        }
    }
}