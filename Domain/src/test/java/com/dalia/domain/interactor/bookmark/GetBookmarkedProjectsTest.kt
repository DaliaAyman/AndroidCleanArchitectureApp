package com.dalia.domain.interactor.bookmark

import com.dalia.domain.interactor.bookmark.GetBookmarkedProjects
import com.dalia.domain.model.Project
import com.dalia.domain.test.ProjectDataFactory
import com.nhaarman.mockito_kotlin.whenever
import com.orange.domain.executor.PostExecutionThread
import com.orange.domain.repository.ProjectsRepository
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*

class GetBookmarkedProjectsTest{

    private lateinit var getBookmarkedProjects: GetBookmarkedProjects
    @Mock lateinit var projectsRepository: ProjectsRepository
    @Mock lateinit var postExecutionThread: PostExecutionThread

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        getBookmarkedProjects = GetBookmarkedProjects(projectsRepository, postExecutionThread)
    }

    @Test
    fun getBookmarkedProjectsCompletes(){
        stubGetBookmarkedProjects(Observable.just(ProjectDataFactory.makeProjectList(2)))

        val testObserver = getBookmarkedProjects.buildUseCaseObservable().test()
        testObserver.assertComplete()
    }

    private fun stubGetBookmarkedProjects(observable: Observable<List<Project>>){
        whenever(projectsRepository.getBookmarkedProjects())
                .thenReturn(observable)
    }

    @Test
    fun getBookmarkedProjectsReturnsData(){
        val bookmarkedProjects = ProjectDataFactory.makeProjectList(2)
        stubGetBookmarkedProjects(Observable.just(bookmarkedProjects))

        val testObserver = getBookmarkedProjects.buildUseCaseObservable().test()
        testObserver.assertValue(bookmarkedProjects)
    }
}