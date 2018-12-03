package com.dalia.data.store

import com.dalia.data.model.ProjectEntity
import com.dalia.data.repository.ProjectsRemote
import com.dalia.data.test.factory.DataFactory
import com.dalia.data.test.factory.ProjectFactory
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ProjectsRemoteDataStoreTest{
    val remote = mock<ProjectsRemote>()
    val store = ProjectsRemoteDataStore(remote)

    @Test
    fun getRemoteProjectsCompletes(){
        stubProjectsRemoteGetProjects(Observable.just(
                listOf(ProjectFactory.makeProjectEntity())))
        val testObserver = store.getProjects().test()
        testObserver.assertComplete()
    }

    @Test
    fun getRemoteProjectsReturnsData(){
        val responseProjects = listOf(ProjectFactory.makeProjectEntity())
        stubProjectsRemoteGetProjects(Observable.just(responseProjects))
        val testObserver = store.getProjects().test()
        testObserver.assertValue(responseProjects)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun saveProjectsThrowsException(){
        store.saveProjects(listOf(ProjectFactory.makeProjectEntity())).test()
    }

    @Test(expected = UnsupportedOperationException::class)
    fun clearProjectsThrowsException(){
        store.clearProjects().test()
    }

    @Test(expected = UnsupportedOperationException::class)
    fun getBookmarkedProjectsThrowsException(){
        store.getBookmarkedProjects().test()
    }

    @Test(expected = UnsupportedOperationException::class)
    fun setProjectAsBookmarkedThrowsException(){
        store.setProjectAsBookmarked(DataFactory.randomString()).test()
    }


    @Test(expected = UnsupportedOperationException::class)
    fun setProjectAsNotBookmarkedThrowsException(){
        store.setProjectAsNotBookmarked(DataFactory.randomString()).test()
    }

    private fun stubProjectsRemoteGetProjects(observable: Observable<List<ProjectEntity>>){
        whenever(remote.getProjects())
                .thenReturn(observable)
    }

}