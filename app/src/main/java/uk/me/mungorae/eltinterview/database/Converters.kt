package uk.me.mungorae.eltinterview.database

import androidx.room.TypeConverter
import uk.me.mungorae.eltinterview.api.Type

class Converters {
    @TypeConverter
    fun fromTypeString(value: String): Type {
        return Type.valueOf(value)
    }

    @TypeConverter
    fun toTypeString(value: Type): String {
        return value.name
    }
}