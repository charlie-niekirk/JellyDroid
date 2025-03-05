package me.cniekirk.jellydroid.feature.onboarding.landing

import androidx.lifecycle.ViewModel
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.jellydroid.core.analytics.AnalyticsRepository
import me.cniekirk.jellydroid.core.common.errors.LocalDataError
import me.cniekirk.jellydroid.core.data.repository.JellyfinRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LandingViewModel @Inject constructor(
    private val jellyfinRepository: JellyfinRepository,
    private val analyticsRepository: AnalyticsRepository
) : ViewModel(), ContainerHost<LandingState, LandingEffect> {

    override val container = container<LandingState, LandingEffect>(LandingState()) {
        // Check if there has been a previous login
        checkUser()
    }

    private fun checkUser() = intent {
        jellyfinRepository.getCurrentServerAndUser().onSuccess {
            postSideEffect(LandingEffect.NavigateToHome)
        }.onFailure { error ->
            when (error) {
                LocalDataError.DatastoreLoadError -> {
                    // Skip asking about analytics
                    postSideEffect(LandingEffect.NavigateToServerSelection)
                }
                LocalDataError.ServerNotExists -> {
                    reduce {
                        state.copy(isLoading = false)
                    }
                }
            }
        }
    }

    fun analyticsCheckedChanged(checked: Boolean) = intent {
        reduce {
            state.copy(
                analyticsChecked = checked
            )
        }
    }

    fun crashlyticsCheckedChanged(checked: Boolean) = intent {
        reduce {
            state.copy(
                crashlyticsChecked = checked
            )
        }
    }

    fun onContinueButtonPressed() = intent {
        analyticsRepository.setAnalyticsEnabled(state.analyticsChecked)
        analyticsRepository.setCrashlyticsEnabled(state.crashlyticsChecked)
        postSideEffect(LandingEffect.NavigateToServerSelection)
    }
}
