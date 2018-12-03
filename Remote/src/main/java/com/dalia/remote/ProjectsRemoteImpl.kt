package com.dalia.remote

import com.dalia.data.model.ProjectEntity
import com.dalia.data.repository.ProjectsRemote
import com.dalia.remote.mapper.ProjectsResponseModelMapper
import com.dalia.remote.service.GithubTrendingService
import io.reactivex.Observable
import javax.inject.Inject

class ProjectsRemoteImpl @Inject constructor(
        private val service: GithubTrendingService,
        private val mapper: ProjectsResponseModelMapper)
    : ProjectsRemote{

    override fun getProjects(): Observable<List<ProjectEntity>> {
        return service.searchRepositories("language:kotlin", "starts", "desc")
                .map {
                    it.items.map {
                        mapper.mapFromModel(it)
                    }
                }
    }
}