package com.dalia.remote.mapper

interface ModelMapper<in M, out E> {

    //takes a model as parameter and returns an entity, so that it passes data from remote to data layer
    fun mapFromModel(model: M): E


}