package com.dalia.domain.interactor.browse

import com.dalia.domain.model.Project
import com.orange.domain.executor.PostExecutionThread
import com.orange.domain.interactor.ObservableUseCase
import com.orange.domain.repository.ProjectsRepository
import io.reactivex.Observable
import javax.inject.Inject

/*
 * Within Clean Architecture,
 * the Domain layer defines Use Cases which define operations which can be performed in our application.
 * In this lesson, we’ll be implementing the Get Projects Use Cases classes for our application
 * so that we can retrieve a list of projects from the external data source
 * through the domain layer of our Android project.
 */

class GetProjects @Inject constructor(
        private val projectsRepository: ProjectsRepository,
        postExecutionThread: PostExecutionThread)
    : ObservableUseCase<List<Project>, Nothing>(postExecutionThread){

    override public fun buildUseCaseObservable(params: Nothing?): Observable<List<Project>> {
        return projectsRepository.getProjects()
    } //There is no mapping of models here, as the domain layer is a central layer
    // meaning that it has no reference to the outside layers and no need for this mapping operations

}