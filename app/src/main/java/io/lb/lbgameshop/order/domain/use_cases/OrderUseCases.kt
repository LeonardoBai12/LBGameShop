package io.lb.lbgameshop.order.domain.use_cases

data class OrderUseCases(
    val addOrderItem: AddOrderItem,
    val finishOrderUseCase: FinishOrderUseCase,
    val getFinishedOrdersUseCase: GetFinishedOrdersUseCase,
    val getUnfinishedOrderUseCase: GetUnfinishedOrderUseCase,
    val resetUnfinishedOrder: ResetUnfinishedOrder,
)
