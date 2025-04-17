package me.cniekirk.jellydroid.core.domain.model.error

sealed interface CheckAuthStateError {

    data object NoPreviousAuth : CheckAuthStateError

    data object AuthTokenOutdated : CheckAuthStateError
}