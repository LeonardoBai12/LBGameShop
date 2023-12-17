package io.lb.lbgameshop.order.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.lb.lbgameshop.order.data.remote.RealtimeDatabaseClient
import io.lb.lbgameshop.order.data.repository.OrderRepositoryImpl
import io.lb.lbgameshop.order.domain.repository.OrderRepository
import io.lb.lbgameshop.order.domain.use_cases.AddOrderItemUseCase
import io.lb.lbgameshop.order.domain.use_cases.FinishOrderUseCase
import io.lb.lbgameshop.order.domain.use_cases.GetFinishedOrdersUseCase
import io.lb.lbgameshop.order.domain.use_cases.GetItemsFromOrderUseCase
import io.lb.lbgameshop.order.domain.use_cases.GetUnfinishedOrderUseCase
import io.lb.lbgameshop.order.domain.use_cases.OrderUseCases
import io.lb.lbgameshop.order.domain.use_cases.RemoveOrderItemUseCase
import io.lb.lbgameshop.order.domain.use_cases.ResetUnfinishedOrderUseCase

@Module
@InstallIn(ViewModelComponent::class)
object OrderModule {
    @Provides
    fun providesTaskRepository(
        realtimeDatabaseClient: RealtimeDatabaseClient,
    ): OrderRepository {
        return OrderRepositoryImpl(realtimeDatabaseClient)
    }

    @Provides
    fun providesTaskUseCases(repository: OrderRepository) =
        OrderUseCases(
            getItemsFromOrderUseCase = GetItemsFromOrderUseCase(repository),
            addOrderItemUseCase = AddOrderItemUseCase(repository),
            removeOrderItemUseCase = RemoveOrderItemUseCase(repository),
            finishOrderUseCase = FinishOrderUseCase(repository),
            getFinishedOrdersUseCase = GetFinishedOrdersUseCase(repository),
            getUnfinishedOrderUseCase = GetUnfinishedOrderUseCase(repository),
            resetUnfinishedOrderUseCase = ResetUnfinishedOrderUseCase(repository),
        )
}
