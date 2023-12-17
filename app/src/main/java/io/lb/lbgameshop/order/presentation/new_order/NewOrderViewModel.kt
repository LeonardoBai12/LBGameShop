package io.lb.lbgameshop.order.presentation.new_order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lb.lbgameshop.core.util.Resource
import io.lb.lbgameshop.order.domain.model.OrderItem
import io.lb.lbgameshop.order.domain.use_cases.OrderUseCases
import io.lb.lbgameshop.sign_in.domain.model.UserData
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private var recentlyDeletedItem: OrderItem? = null
    var userData: UserData? = null

    fun onEvent(event: NewOrderEvent) {
        when (event) {
            is NewOrderEvent.RequestDelete -> {
                with(event.orderItem) {
                    deleteTask(this)
                    recentlyDeletedItem = this
                }
            }
            is NewOrderEvent.RestoreTask -> {
                recentlyDeletedItem?.let {
                    restoreOrderItem(it)
                }
            }
        }
    }

    fun getOrderItems(userData: UserData) {
        getItemsJob?.cancel()
        getItemsJob = useCases.getItemsFromOrderUseCase(userData, state.value.order).onEach { result ->
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

    fun getOrder(userData: UserData) {
        getOrderJob?.cancel()
        getOrderJob = useCases.getUnfinishedOrderUseCase(userData).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { order ->
                        _state.update {
                            it.copy(
                                order = order,
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

    private fun restoreOrderItem(orderItem: OrderItem) {
        viewModelScope.launch {
            userData?.let {
                useCases.addOrderItemUseCase(
                    userData = it,
                    order = state.value.order,
                    game = orderItem.data
                )
                getOrder(it)
            }
        }
    }

    private fun deleteTask(orderItem: OrderItem) {
        viewModelScope.launch {
            userData?.let {
                useCases.removeOrderItemUseCase(it, orderItem)
                getOrder(it)
            }
        }
    }

    fun clearState() {
        _state.update { NewOrderState() }
    }
}
