package io.lb.lbgameshop.core.di

import android.app.Application
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.lb.lbgameshop.core.util.FakeToaster
import io.lb.lbgameshop.core.util.ORDER_TEST
import io.lb.lbgameshop.core.util.Toaster
import io.lb.lbgameshop.sign_in.data.auth_client.GoogleAuthClient
import io.lb.lbgameshop.sign_in.data.auth_client.GoogleAuthClientImpl
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {
    @Provides
    @Singleton
    fun providesAuth(): FirebaseAuth {
        return Firebase.auth.apply {
            useEmulator("10.0.2.2", 9099)
        }
    }

    @Provides
    @Singleton
    fun providesGoogleAuthUiClient(
        app: Application,
        firebaseAuth: FirebaseAuth
    ): GoogleAuthClient {
        val oneTapClient = Identity.getSignInClient(app.applicationContext)

        return GoogleAuthClientImpl(
            auth = firebaseAuth,
            context = app.applicationContext,
            oneTapClient = oneTapClient
        )
    }

    @Provides
    @Singleton
    fun providesRealtimeDatabase(): FirebaseDatabase {
        return Firebase.database.apply {
            setPersistenceEnabled(false)
            useEmulator("10.0.2.2", 9000)
        }
    }

    @Provides
    @Singleton
    fun providesToaster(): Toaster {
        return FakeToaster()
    }
}
