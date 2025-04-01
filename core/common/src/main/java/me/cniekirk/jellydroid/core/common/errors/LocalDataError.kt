package me.cniekirk.jellydroid.core.common.errors

sealed interface LocalDataError {

    data object DatastoreLoadError : LocalDataError

    data object ServerNotExists : LocalDataError

    data object DatabaseReadError : LocalDataError
}
