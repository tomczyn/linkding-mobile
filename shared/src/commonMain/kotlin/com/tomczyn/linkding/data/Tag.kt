package com.tomczyn.linkding.data

import com.tomczyn.linkding.data.remote.TagRemote
import com.tomczyn.linkding.database.TagEntity

data class Tag(
    val name: String,
    val dateAdded: String,
)

fun TagRemote.toTag(): Tag {
    return Tag(
        name = name,
        dateAdded = dateAdded
    )
}

fun TagEntity.toTag(): Tag {
    return Tag(
        name = name,
        dateAdded = dateAdded,
    )
}

fun Tag.toTagEntity(): TagEntity {
    return TagEntity(
        name = name,
        dateAdded = dateAdded
    )
}
