package com.dalia.cache.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.dalia.cache.db.ProjectConstants.QUERY_BOOKMARKED_PROJECTS
import com.dalia.cache.db.ProjectConstants.QUERY_DELETE_PROJECTS
import com.dalia.cache.db.ProjectConstants.QUERY_GET_PROJECTS
import com.dalia.cache.db.ProjectConstants.QUERY_UPDATE_BOOKMARK_STATUS
import com.dalia.cache.model.CachedProject
import io.reactivex.Flowable

@Dao
abstract class CachedProjectDao{

    @Query(QUERY_GET_PROJECTS)
    abstract fun getProjects(): Flowable<List<CachedProject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertProjects(projects: List<CachedProject>)

    @Query(QUERY_DELETE_PROJECTS)
    abstract fun deleteProjects()

    @Query(QUERY_BOOKMARKED_PROJECTS)
    abstract fun getBookmarkedProjects(): Flowable<List<CachedProject>>

    @Query(QUERY_UPDATE_BOOKMARK_STATUS)
    abstract fun setBookmarkStatus(isBookmarked: Boolean, projectId: String)

}