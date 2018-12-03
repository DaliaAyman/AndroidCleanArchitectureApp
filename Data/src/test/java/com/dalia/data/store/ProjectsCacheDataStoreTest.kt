package com.dalia.data.store

import com.dalia.data.model.ProjectEntity
import com.dalia.data.repository.ProjectsCache
import com.dalia.data.repository.ProjectsDataStore
import com.dalia.data.test.factory.DataFactory
import com.dalia.data.test.factory.ProjectFactory
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ProjectsCacheDataStoreTest {
    val cache = mock<ProjectsCache>()
    val store = ProjectsCacheDataStore(cache)

    //********* getProjects **********//

    @Test
    fun getCachedProjectsCompletes(){
        stubProjectsCacheGetProjects(Observable.just(listOf(
                ProjectFactory.makeProjectEntity())))

        val testObserver = store.getProjects().test()
        testObserver.assertComplete()
    }

    @Test
    fun getCachedProjectsReturnsData(){
        val data = listOf(ProjectFactory.makeProjectEntity())
        stubProjectsCacheGetProjects(Observable.just(data))

        val testObserver = store.getProjects().test()
        testObserver.assertValue(data)
    }

    @Test
    fun getProjectsCallsCacheSource(){
        stubProjectsCacheGetProjects(Observable.just(listOf(
                ProjectFactory.makeProjectEntity())))
        store.getProjects().test()
        verify(cache).getProjects()
    }

    private fun stubProjectsCacheGetProjects(observable: Observable<List<ProjectEntity>>){
        whenever(cache.getProjects())
                .thenReturn(observable)
    }

    //********* saveProjects **********//

    @Test
    fun saveProjectsCacheProjectsCompletes(){
        val projects = listOf(ProjectFactory.makeProjectEntity())
        stubProjectsCacheSaveProjects(Completable.complete())
        stubProjectsSetLastCacheTime(Completable.complete())

        val testObserver = store.saveProjects(projects).test()
        testObserver.assertComplete()
    }

    @Test
    fun saveProjectsCacheCallsCacheStore(){
        val projects = listOf(ProjectFactory.makeProjectEntity())
        stubProjectsCacheSaveProjects(Completable.complete())
        stubProjectsSetLastCacheTime(Completable.complete())

        store.saveProjects(projects).test()
        verify(cache).saveProjects(projects)
    }

    private fun stubProjectsCacheSaveProjects(completable: Completable){
        whenever(cache.saveProjects(any()))
                .thenReturn(completable)
    }
    private fun stubProjectsSetLastCacheTime(completable: Completable){
        whenever(cache.setLastCacheTime(any()))
                .thenReturn(completable)
    }

    //********* clearProjects **********//

    @Test
    fun clearProjectsCacheProjectsCompletes(){
        stubProjectsCacheClearProjects(Completable.complete())

        val testObserver = store.clearProjects().test()
        testObserver.assertComplete()
    }

    @Test
    fun clearProjectsCacheCallsCacheStore(){
        stubProjectsCacheClearProjects(Completable.complete())
        store.clearProjects().test()
        verify(cache).clearProjects()
    }

    private fun stubProjectsCacheClearProjects(completable: Completable){
        whenever(cache.clearProjects())
                .thenReturn(completable)
    }

    //********* getBookmarkedProjects **********//

    @Test
    fun getCachedBookmarkedProjectsCompletes(){
        stubProjectsCacheGetBookmarkedProjects(Observable.just(listOf(
                ProjectFactory.makeProjectEntity())))

        val testObserver = store.getBookmarkedProjects().test()
        testObserver.assertComplete()
    }

    @Test
    fun getCachedBookmarkedProjectsReturnsData(){
        val data = listOf(ProjectFactory.makeProjectEntity())
        stubProjectsCacheGetBookmarkedProjects(Observable.just(data))

        val testObserver = store.getBookmarkedProjects().test()
        testObserver.assertValue(data)
    }

    @Test
    fun getBookmarkedProjectsCallsCacheSource(){
        stubProjectsCacheGetBookmarkedProjects(Observable.just(listOf(
                ProjectFactory.makeProjectEntity())))
        store.getBookmarkedProjects().test()
        verify(cache).getBookmarkedProjects()
    }

    private fun stubProjectsCacheGetBookmarkedProjects(observable: Observable<List<ProjectEntity>>){
        whenever(cache.getBookmarkedProjects())
                .thenReturn(observable)
    }

    //********* setProjectAsBookmarked **********//

    @Test
    fun setProjectAsBookmarkedCompletes(){
        stubProjectsCacheBookmarkProject(Completable.complete())

        val testObserver = store.setProjectAsBookmarked(
                DataFactory.randomString()).test()
        testObserver.assertComplete()
    }

    @Test
    fun setProjectAsBookmarkedCallsCacheStore(){
        val projectId = DataFactory.randomString()
        stubProjectsCacheBookmarkProject(Completable.complete())

        store.setProjectAsBookmarked(projectId).test()
        verify(cache).setProjectAsBookmarked(projectId)
    }

    private fun stubProjectsCacheBookmarkProject(completable: Completable){
        whenever(cache.setProjectAsBookmarked(any()))
                .thenReturn(completable)
    }


    //********* setProjectAsNotBookmarked **********//

    @Test
    fun setProjectAsNotBookmarkedCompletes(){
        stubProjectsCacheNotBookmarkProject(Completable.complete())

        val testObserver = store.setProjectAsNotBookmarked(
                DataFactory.randomString()).test()
        testObserver.assertComplete()
    }

    @Test
    fun setProjectAsNotBookmarkedCallsCacheStore(){
        val projectId = DataFactory.randomString()
        stubProjectsCacheNotBookmarkProject(Completable.complete())

        store.setProjectAsNotBookmarked(projectId).test()
        verify(cache).setProjectAsNotBookmarked(projectId)
    }

    private fun stubProjectsCacheNotBookmarkProject(completable: Completable){
        whenever(cache.setProjectAsNotBookmarked(any()))
                .thenReturn(completable)
    }
}