package com.orange.domain.repository

import com.dalia.domain.model.Project
import io.reactivex.Completable
import io.reactivex.Observable

/*
* Domain's layer way of enforcing that what can be done by the application data
* without having any knowledge of the wheres and hows
* This interface acts as an abstraction for the use cases in this layer
*/

interface ProjectsRepository{

    fun getProjects(): Observable<List<Project>>

    fun bookmarkProject(projectId: String): Completable
    fun unbookmarkProject(projectId: String): Completable

    fun getBookmarkedProjects(): Observable<List<Project>>

}