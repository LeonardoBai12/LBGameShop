package io.lb.lbgameshop.order.presentation.new_order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lb.lbgameshop.core.util.Resource
import io.lb.lbgameshop.game.domain.model.Game
import io.lb.lbgameshop.order.domain.model.Order
import io.lb.lbgameshop.order.domain.model.OrderItem
import io.lb.lbgameshop.order.domain.use_cases.OrderUseCases
import io.lb.lbgameshop.sign_in.domain.model.UserData
import io.lb.lbgameshop.sign_in.presentation.sing_in.SignInViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewOrderViewModel @Inject constructor(
    private val useCases: OrderUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(NewOrderState())
    val state: StateFlow<NewOrderState> = _state

    private var getItemsJob: Job? = null
    private var getOrderJob: Job? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var recentlyDeletedItem: OrderItem? = null
    var userData: UserData? = null

    init {
        getOrder()
    }

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
        object Finish : UiEvent()
    }

    fun onEvent(event: NewOrderEvent) {
        when (event) {
            is NewOrderEvent.RequestDelete -> {
                with(event.orderItem) {
                    deleteGame(this)
                    recentlyDeletedItem = this
                }
            }
            is NewOrderEvent.RestoreTask -> {
                recentlyDeletedItem?.let {
                    restoreOrderItem(it)
                }
            }

            is NewOrderEvent.RequestInsert -> {
                insertGame(event)
            }

            NewOrderEvent.FinishOrder -> {
                finishOrder()
            }
        }
    }

    private fun finishOrder() {
        viewModelScope.launch {
            try {
                useCases.finishOrderUseCase(
                    userData ?: return@launch,
                    state.value.order,
                    state.value.items
                )
                _eventFlow.emit(UiEvent.ShowToast("Order finished successfully"))
                _eventFlow.emit(UiEvent.Finish)
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowToast(e.message ?: ""))
            }
            clearState()
        }
    }

    private fun insertGame(event: NewOrderEvent.RequestInsert) {
        viewModelScope.launch {
            userData?.let {
                useCases.addOrderItemUseCase(
                    it,
                    state.value.order,
                    event.game
                )
            }
        }
    }

    private fun getOrderItems() {
        getItemsJob?.cancel()
        getItemsJob = useCases.getItemsFromOrderUseCase(
            userData ?: return,
            state.value.order
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { items ->
                        _state.update {
                            it.copy(
                                items = items,
                            )
                        }
                    }
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                    _state.update {
                        it.copy(
                            loading = result.isLoading,
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getOrder() {
        getOrderJob?.cancel()
        getOrderJob = useCases.getUnfinishedOrderUseCase(userData ?: return).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { order ->
                        _state.update {
                            it.copy(
                                order = order,
                            )
                        }
                        getOrderItems()
                    }
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                    _state.update {
                        it.copy(
                            loading = result.isLoading,
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun restoreOrderItem(orderItem: OrderItem) {
        viewModelScope.launch {
            userData?.let {
                useCases.addOrderItemUseCase(
                    userData = it,
                    order = state.value.order,
                    game = Game.fromJson(orderItem.data)
                )
                getOrder()
            }
        }
    }

    private fun deleteGame(orderItem: OrderItem) {
        viewModelScope.launch {
            userData?.let {
                useCases.removeOrderItemUseCase(it, orderItem)
                getOrder()
            }
        }
    }

    private fun clearState() {
        _state.update { NewOrderState() }
    }
}
