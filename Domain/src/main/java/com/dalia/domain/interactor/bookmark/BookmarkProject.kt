package com.dalia.domain.interactor.bookmark

import com.orange.domain.executor.PostExecutionThread
import com.orange.domain.interactor.CompletableUseCase
import com.orange.domain.repository.ProjectsRepository
import io.reactivex.Completable
import javax.inject.Inject

class BookmarkProject @Inject constructor(
        private val projectsRepository: ProjectsRepository,
        postExecutionThread: PostExecutionThread)
    : CompletableUseCase<BookmarkProject.Params>(postExecutionThread){

    override public fun buildUseCaseCompletable(params: Params?): Completable {
        if(params == null) throw IllegalArgumentException("Params can't be null!")

        return projectsRepository.bookmarkProject(params.projectId)
    }

    data class Params constructor(val projectId: String){
        companion object {
            fun forProject(projectId: String): Params{
                return Params(projectId)
            }
        }
    }
}