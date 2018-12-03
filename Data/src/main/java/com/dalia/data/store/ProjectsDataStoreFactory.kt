package com.dalia.data.store

import com.dalia.data.repository.ProjectsDataStore
import javax.inject.Inject

//To decide which one (Cache or Remote) Data store that will be used
open class ProjectsDataStoreFactory @Inject constructor(
        private val projectsCacheDataStore: ProjectsCacheDataStore,
        private val projectsRemoteDataStore: ProjectsRemoteDataStore
){

    open fun getDataStore(projectsCached: Boolean, projectsExpired: Boolean): ProjectsDataStore {
        return if(projectsCached && !projectsExpired){
            projectsCacheDataStore
        }else{
            projectsRemoteDataStore
        }
    }

    open fun getCacheDataStore(): ProjectsDataStore{
        return projectsCacheDataStore
    }
}