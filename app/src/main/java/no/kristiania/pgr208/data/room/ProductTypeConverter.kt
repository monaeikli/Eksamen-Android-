package no.kristiania.pgr208.data.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken




object  ProductTypeConverter {

    private val gson = Gson()

    // Converter for List<String> (genres)
    @TypeConverter
    @JvmStatic
    fun fromStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    @JvmStatic
    fun fromListString(list: List<String>): String {
        return gson.toJson(list)
    }
}
