package me.cniekirk.core.jellydroid.domain.mapping

interface DomainMapper<T, U> {

    fun toUiModel(dataModel: T): U
}