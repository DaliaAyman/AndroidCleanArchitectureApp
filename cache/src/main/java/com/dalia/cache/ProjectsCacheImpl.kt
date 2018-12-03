package com.dalia.cache

import com.dalia.cache.db.ProjectsDatabase
import com.dalia.cache.mapper.CachedProjectMapper
import com.dalia.cache.model.CachedProject
import com.dalia.cache.model.Config
import com.dalia.data.model.ProjectEntity
import com.dalia.data.repository.ProjectsCache
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.toSingle
import javax.inject.Inject

class ProjectsCacheImpl @Inject constructor(
        private val projectsDatabase: ProjectsDatabase,
        private val mapper: CachedProjectMapper)
    : ProjectsCache {

    override fun clearProjects(): Completable {
        return Completable.defer{
            projectsDatabase.cachedProjectDao().deleteProjects()
            Completable.complete()
        }
    }

    override fun saveProjects(projects: List<ProjectEntity>): Completable {
        return Completable.defer{
            projectsDatabase.cachedProjectDao().insertProjects(
                    projects.map{mapper.mapToCached(it)})
            Completable.complete()
        }
    }

    override fun getProjects(): Observable<List<ProjectEntity>> {
        return projectsDatabase.cachedProjectDao().getProjects()
                .toObservable()
                .map { it.map { mapper.mapFromCached(it) }}
    }

    override fun getBookmarkedProjects(): Observable<List<ProjectEntity>> {
        return projectsDatabase.cachedProjectDao().getBookmarkedProjects()
                .toObservable()
                .map { it.map { mapper.mapFromCached(it) } }
    }

    override fun setProjectAsBookmarked(projectId: String): Completable {
        return Completable.defer{
            projectsDatabase.cachedProjectDao().setBookmarkStatus(true, projectId)
            Completable.complete()
        }
    }

    override fun setProjectAsNotBookmarked(projectId: String): Completable {
        return Completable.defer{
            projectsDatabase.cachedProjectDao().setBookmarkStatus(false, projectId)
            Completable.complete()
        }
    }

    override fun areProjectsCached(): Single<Boolean> {
        return projectsDatabase.cachedProjectDao().getProjects().isEmpty
                .map { !it }
    }

    override fun setLastCacheTime(lastCache: Long): Completable {
        return Completable.defer{
            projectsDatabase.configDao().insertConfig(Config(lastCache))
            Completable.complete()
        }
    }

    override fun isProjectsCacheExpired(): Single<Boolean> {
        val currentTime = System.currentTimeMillis()
        val expirationTime = (60 * 10 * 100).toLong()

        return projectsDatabase.configDao().getConfig()
                .toSingle()
                .map { currentTime - it.lastCacheTime > expirationTime }
    }
}