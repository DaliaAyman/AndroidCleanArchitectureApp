package com.dalia.cache.model

import android.arch.persistence.room.Entity
import com.dalia.cache.db.ConfigConstants

@Entity(tableName = ConfigConstants.TABLE_NAME)
class Config(val lastCacheTime: Long)