package com.dalia.cache.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.dalia.cache.db.ConfigConstants
import com.dalia.cache.model.Config

@Dao
abstract class ConfigDao{

    @Query(ConfigConstants.QUERY_CONFIG)
    abstract fun getConfig(): Config

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertConfig(config: Config)

}