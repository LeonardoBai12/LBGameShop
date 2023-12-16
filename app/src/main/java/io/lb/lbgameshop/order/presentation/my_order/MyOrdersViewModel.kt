package io.lb.lbgameshop.order.presentation.my_order

import dagger.hilt.android.lifecycle.HiltViewModel
import io.lb.lbgameshop.order.domain.use_cases.OrderUseCases
import javax.inject.Inject

@HiltViewModel
class MyOrdersViewModel @Inject constructor(
    private val useCases: OrderUseCases
) {

}
