package io.lb.lbgameshop.order.domain.use_cases

data class OrderUseCases(
    val getItemsFromOrderUseCase: GetItemsFromOrderUseCase,
    val addOrderItemUseCase: AddOrderItemUseCase,
    val removeOrderItemUseCase: RemoveOrderItemUseCase,
    val finishOrderUseCase: FinishOrderUseCase,
    val getFinishedOrdersUseCase: GetFinishedOrdersUseCase,
    val getUnfinishedOrderUseCase: GetUnfinishedOrderUseCase,
    val resetUnfinishedOrderUseCase: ResetUnfinishedOrderUseCase,
)
