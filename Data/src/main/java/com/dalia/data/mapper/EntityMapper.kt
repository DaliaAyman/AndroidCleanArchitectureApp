package com.dalia.data.mapper

interface EntityMapper<E, D> {

    //This will be used when the data is passed:
    fun mapFromEntity(entity: E) : D //from data layer to domain layer

    fun mapToEntity(domain: D) : E //from domain layer to data layer
}