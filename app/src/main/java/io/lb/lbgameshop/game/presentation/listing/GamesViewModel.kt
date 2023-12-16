package io.lb.lbgameshop.game.presentation.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lb.lbgameshop.core.util.Resource
import io.lb.lbgameshop.game.domain.use_cases.GameUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    private val useCases: GameUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var searchJob: Job? = null
    private var getGamesJob: Job? = null

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
    }

    fun onEvent() {

    }

    fun getGames() {
        viewModelScope.launch {
            useCases.getGamesUseCase().collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { games ->
                            _state.update {
                                state.value.copy(
                                    games = games,
                                )
                            }
                        }
                    }

                    is Resource.Error -> {
                        _state.update {
                            state.value.copy(
                                games = emptyList(),
                            )
                        }

                        _eventFlow.emit(
                            UiEvent.ShowToast(
                                result.message ?: "Something went wrong!"
                            )
                        )
                    }

                    is Resource.Loading -> {
                        _state.update {
                            state.value.copy(
                                loading = result.isLoading,
                            )
                        }
                    }
                }
            }
        }
    }
}
