package com.dalia.domain.interactor.bookmark

import com.dalia.domain.model.Project
import com.orange.domain.executor.PostExecutionThread
import com.orange.domain.interactor.ObservableUseCase
import com.orange.domain.repository.ProjectsRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetBookmarkedProjects @Inject constructor(
        private val projectsRepository: ProjectsRepository,
        postExecutionThread: PostExecutionThread)
    : ObservableUseCase<List<Project>, Nothing>(postExecutionThread){

    override public fun buildUseCaseObservable(params: Nothing?): Observable<List<Project>> {
        return projectsRepository.getBookmarkedProjects()
    } //There is no mapping of models here, as the domain layer is a central layer
    // meaning that it has no reference to the outside layers and no need for this mapping operations

}