package uk.me.mungorae.eltinterview.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.me.mungorae.eltinterview.api.Type

@Entity
class DbTask(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String,
    val type: Type
)