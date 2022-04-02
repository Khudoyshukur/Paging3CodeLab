package com.example.android.codelabs.paging.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by: androdev
 * Date: 4/2/2022
 * Time: 4:34 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Entity(tableName = "remote_keys")
class RemoteKeys(
    @PrimaryKey
    val repoId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)