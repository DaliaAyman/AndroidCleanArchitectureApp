package com.dalia.data

import com.dalia.data.mapper.ProjectMapper
import com.dalia.data.repository.ProjectsCache
import com.dalia.data.store.ProjectsDataStoreFactory
import com.dalia.domain.model.Project
import com.orange.domain.repository.ProjectsRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

//To provide access to data to the use case classes
class ProjectsDataRepository @Inject constructor(
        private val mapper: ProjectMapper,
        private val cache: ProjectsCache,
        private val factory: ProjectsDataStoreFactory
) : ProjectsRepository {

    override fun getProjects(): Observable<List<Project>> {
        return Observable.zip(
                cache.areProjectsCached().toObservable(),
                cache.isProjectsCacheExpired().toObservable(),
                BiFunction<Boolean, Boolean, Pair<Boolean, Boolean>> { areCached, isExpired ->
                    Pair(areCached, isExpired)
                }
        ) //found: Observable<Pair<Boolean, Boolean>>
                .flatMap {
                    factory.getDataStore(it.first, it.second).getProjects()
                } //found: Observable<List<ProjectEntity>>
                .flatMap { projects ->
                    factory.getCacheDataStore()
                            .saveProjects(projects) //found: Completable
                            .andThen(Observable.just(projects))
                } //found: Observable<List<ProjectEntity>>
                .map {
                    it.map {
                        mapper.mapFromEntity(it)
                    }
                } // Observable<List<Project>>
    }

    override fun bookmarkProject(projectId: String): Completable {
        return factory.getCacheDataStore().setProjectAsBookmarked(projectId)
    }

    override fun unbookmarkProject(projectId: String): Completable {
        return factory.getCacheDataStore().setProjectAsNotBookmarked(projectId)
    }

    override fun getBookmarkedProjects(): Observable<List<Project>> {
        return factory.getCacheDataStore().getBookmarkedProjects()
                .map {
                    it.map {
                        mapper.mapFromEntity(it)
                    }
                }
    }


}