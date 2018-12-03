package com.dalia.remote.test.factory

import com.dalia.data.model.ProjectEntity
import com.dalia.remote.model.OwnerModel
import com.dalia.remote.model.ProjectModel
import com.dalia.remote.model.ProjectsResponseModel

object ProjectDataFactory {

    fun makeOwner(): OwnerModel{
        return OwnerModel(DataFactory.randomUuid(), DataFactory.randomUuid())
    }

    fun makeProject(): ProjectModel{
        return ProjectModel(DataFactory.randomUuid(), DataFactory.randomUuid(),
                DataFactory.randomUuid(), DataFactory.randomInt(), DataFactory.randomUuid(), makeOwner())
    }

    fun makeProjectEntity(): ProjectEntity{
        return ProjectEntity(DataFactory.randomUuid(), DataFactory.randomUuid(), DataFactory.randomUuid(),
                DataFactory.randomUuid(), DataFactory.randomUuid(), DataFactory.randomUuid(), DataFactory.randomUuid())
    }

    fun makeProjectsResponse(): ProjectsResponseModel{
        return ProjectsResponseModel(listOf(makeProject(), makeProject()))
    }
}