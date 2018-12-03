package com.dalia.remote.test

import com.dalia.data.model.ProjectEntity
import com.dalia.remote.ProjectsRemoteImpl
import com.dalia.remote.mapper.ProjectsResponseModelMapper
import com.dalia.remote.model.ProjectModel
import com.dalia.remote.model.ProjectsResponseModel
import com.dalia.remote.service.GithubTrendingService
import com.dalia.remote.test.factory.ProjectDataFactory
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class ProjectsRemoteImplTest {

    private val service = mock<GithubTrendingService>()
    private val mapper = mock<ProjectsResponseModelMapper>()

    private val remote = ProjectsRemoteImpl(service, mapper)


    private fun stubGithubTrendingServiceSearchRepositories(observable: Observable<ProjectsResponseModel>){
        whenever(service.searchRepositories(any(), any(), any()))
                .thenReturn(observable)
    }

    private fun stubProjectsResponseModelMapperMapFromModel(model: ProjectModel, entity: ProjectEntity){
        whenever(mapper.mapFromModel(model))
                .thenReturn(entity)
    }

    //********** getProjects Completes, ReturnsData and CallServer *******//
    @Test
    fun getProjectsCompletes(){
        val projectsResponseModel = ProjectDataFactory.makeProjectsResponse()
        stubGithubTrendingServiceSearchRepositories(Observable.just(projectsResponseModel))

        stubProjectsResponseModelMapperMapFromModel(ProjectDataFactory.makeProject(),
                ProjectDataFactory.makeProjectEntity())

        val testObservable = remote.getProjects().test()
        testObservable.assertComplete()
    }

    @Test
    fun getProjectsCallServer(){
        val projectsResponseModel = ProjectDataFactory.makeProjectsResponse()
        stubGithubTrendingServiceSearchRepositories(Observable.just(projectsResponseModel))

        stubProjectsResponseModelMapperMapFromModel(ProjectDataFactory.makeProject(),
                ProjectDataFactory.makeProjectEntity())

        remote.getProjects().test()
        verify(service).searchRepositories(any(), any(), any())
    }

    @Test
    fun getProjectsReturnsData(){
        val response = ProjectDataFactory.makeProjectsResponse()

        stubGithubTrendingServiceSearchRepositories(
                Observable.just(response))

        val entities = mutableListOf<ProjectEntity>()

        response.items.forEach{
            val entity = ProjectDataFactory.makeProjectEntity()
            entities.add(entity)

            stubProjectsResponseModelMapperMapFromModel(it, entity)
        }

        val testObservable = remote.getProjects().test()
        testObservable.assertValue(entities)
    }

    @Test
    fun getProjectsCallServerWithCorrectParameters(){
        val projectsResponseModel = ProjectDataFactory.makeProjectsResponse()
        stubGithubTrendingServiceSearchRepositories(Observable.just(projectsResponseModel))

        stubProjectsResponseModelMapperMapFromModel(ProjectDataFactory.makeProject(),
                ProjectDataFactory.makeProjectEntity())

        remote.getProjects().test()
        verify(service).searchRepositories("language:kotlin", "starts", "desc")
    }
}