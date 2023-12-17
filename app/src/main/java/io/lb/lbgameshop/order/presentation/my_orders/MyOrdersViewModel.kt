package io.lb.lbgameshop.order.presentation.my_orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lb.lbgameshop.core.util.Resource
import io.lb.lbgameshop.order.domain.model.Order
import io.lb.lbgameshop.order.domain.model.OrderItem
import io.lb.lbgameshop.order.domain.use_cases.OrderUseCases
import io.lb.lbgameshop.sign_in.domain.model.UserData
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
class MyOrdersViewModel @Inject constructor(
    private val useCases: OrderUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(MyOrdersState())
    val state: StateFlow<MyOrdersState> = _state

    private var getItemsJob: Job? = null
    private var getOrdersJob: Job? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var userData: UserData? = null

    init {
        getOrders()
    }

    sealed class UiEvent {
        data class NavigateToOrderDetails(
            val order: Order,
            val items: List<OrderItem>
        ) : UiEvent()
    }

    fun onEvent(event: MyOrderEvent) {
        when (event) {
            is MyOrderEvent.NavigateToOrderDetails -> {
                getOrderItems(event.order) { items ->
                    viewModelScope.launch {
                        _eventFlow.emit(
                            UiEvent.NavigateToOrderDetails(
                                event.order,
                                items
                            )
                        )
                    }
                }
            }
        }
    }

    fun getOrders() {
        getOrdersJob?.cancel()
        getOrdersJob = useCases.getFinishedOrdersUseCase(
            userData ?: return
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { orders ->
                        _state.update {
                            it.copy(
                                orders = orders,
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

    private fun getOrderItems(order: Order, onFinish: (List<OrderItem>) -> Unit) {
        getItemsJob?.cancel()
        getItemsJob = useCases.getItemsFromOrderUseCase(
            userData ?: return,
            order
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { items ->
                        onFinish.invoke(items)
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
}
