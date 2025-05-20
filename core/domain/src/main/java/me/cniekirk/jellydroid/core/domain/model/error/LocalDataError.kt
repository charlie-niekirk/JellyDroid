package me.cniekirk.jellydroid.core.domain.model.error

sealed interface LocalDataError {

    data object DatastoreLoadError : LocalDataError

    data object ServerNotExists : LocalDataError

    data object DatabaseReadError : LocalDataError
}
