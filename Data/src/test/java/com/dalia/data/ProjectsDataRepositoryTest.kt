package com.dalia.data

import com.dalia.data.mapper.ProjectMapper
import com.dalia.data.model.ProjectEntity
import com.dalia.data.repository.ProjectsCache
import com.dalia.data.repository.ProjectsDataStore
import com.dalia.data.store.ProjectsDataStoreFactory
import com.dalia.data.test.factory.DataFactory
import com.dalia.data.test.factory.ProjectFactory
import com.dalia.domain.model.Project
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ProjectsDataRepositoryTest {

    private val mapper = mock<ProjectMapper>()
    private val factory = mock<ProjectsDataStoreFactory>()
    private val store = mock<ProjectsDataStore>()
    private val cache = mock<ProjectsCache>()
//    private val repository = mock<ProjectsRepository>() //TODO
    private val repository = ProjectsDataRepository(mapper, cache, factory)


    @Before
    fun setup(){
        stubFactoryGetDataStore()
        stubFactoryGetCacheDataStore()

        stubIsCacheExpired(Single.just(false))
        stubAreProjectsCached(Single.just(false))
        stubSaveProjects(Completable.complete())
    }


    // ******** store ************ //

    /*
    function is to make its stub (from ProjectsDataStoreFactory):

    open fun getDataStore(projectsCached: Boolean, projectsExpired: Boolean): ProjectsDataStore {
        return if(projectsCached && !projectsExpired){
            projectsCacheDataStore
        }else{
            projectsRemoteDataStore
        }
    }
     */
    private fun stubFactoryGetDataStore(){
        whenever(factory.getDataStore(any(), any()))
                .thenReturn(store)
    }

    private fun stubFactoryGetCacheDataStore(){
        whenever(factory.getCacheDataStore())
                .thenReturn(store)
    }

    /*
    function is to make its stub (from ProjectsDataRepository):
    fun {@see ProjectsDataRepository#getProjects()}
     */
    private fun stubGetProjects(observable: Observable<List<ProjectEntity>>){
        whenever(store.getProjects())
                .thenReturn(observable)
    }

    private fun stubSaveProjects(completable: Completable){
        whenever(store.saveProjects(any()))
                .thenReturn(completable)
    }

    private fun stubGetBookmarkedProjects(observable: Observable<List<ProjectEntity>>){
        whenever(store.getBookmarkedProjects())
                .thenReturn(observable)
    }

    private fun stubSetProjectAsBookmarked(completable: Completable){
        whenever(store.setProjectAsBookmarked(any()))
                .thenReturn(completable)
    }

    private fun stubSetProjectAsNotBookmarked(completable: Completable){
        whenever(store.setProjectAsNotBookmarked(any()))
                .thenReturn(completable)
    }

    //****** mapper *********//

    private fun stubMapper(model: Project, entity: ProjectEntity){
        whenever(mapper.mapFromEntity(entity))
                .thenReturn(model)
    }

    //********** getProjects Completes and ReturnsData *******//

    @Test
    fun getProjectsCompletes(){
        stubGetProjects(Observable.just(listOf(ProjectFactory.makeProjectEntity())))
        stubMapper(ProjectFactory.makeProject(), any())

        val testObserver = repository.getProjects().test()
        testObserver.assertComplete()
    }

    @Test
    fun getProjectsReturnsData(){
        val projectEntity = ProjectFactory.makeProjectEntity()
        val project = ProjectFactory.makeProject()

        stubGetProjects(Observable.just(listOf(projectEntity)))
        stubMapper(project, projectEntity)

        val testObserver = repository.getProjects().test()
        testObserver.assertValue(listOf(project))
    }

    //****** cache *********//
    private fun stubIsCacheExpired(single: Single<Boolean>){
        whenever(cache.isProjectsCacheExpired())
                .thenReturn(single)
    }

    private fun stubAreProjectsCached(single: Single<Boolean>){
        whenever(cache.areProjectsCached())
                .thenReturn(single)
    }

    //********** getBookmarkedProjects Completes and ReturnsData *******//

    @Test
    fun getBookmarkedProjectsCompletes(){
        stubGetBookmarkedProjects(Observable.just(listOf(ProjectFactory.makeProjectEntity())))
        stubMapper(ProjectFactory.makeProject(), any())

        val testObserver = repository.getBookmarkedProjects().test()
        testObserver.assertComplete()
    }

    @Test
    fun getBookmarkedProjectsReturnsData(){
        val projectEntity = ProjectFactory.makeProjectEntity()
        val project = ProjectFactory.makeProject()

        stubGetBookmarkedProjects(Observable.just(listOf(projectEntity)))
        stubMapper(project, projectEntity)

        val testObserver = repository.getBookmarkedProjects().test()
        testObserver.assertValue(listOf(project))
    }

    //********** bookmarkProjects Completes and ReturnsData *******//

    @Test
    fun bookmarkProjectCompletes(){
        stubSetProjectAsBookmarked(Completable.complete())

        val testObserver = repository.bookmarkProject(DataFactory.randomString()).test()
        testObserver.assertComplete()
    }

    @Test
    fun unbookmarkProjectCompletes(){
        stubSetProjectAsNotBookmarked(Completable.complete())

        val testObserver = repository.unbookmarkProject(DataFactory.randomString()).test()
        testObserver.assertComplete()
    }
}