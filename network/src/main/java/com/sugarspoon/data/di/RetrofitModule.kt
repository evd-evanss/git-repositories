package com.sugarspoon.data.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.sugarspoon.data.BuildConfig
import com.sugarspoon.data.ServiceFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@InstallIn(ViewModelComponent::class)
@Module
object RetrofitModule {

    @Provides
    fun retrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Provides
    fun providesClient(@ApplicationContext context: Context, chuckerInterceptor: ChuckerInterceptor) = OkHttpClient.Builder()
        .addInterceptor(getHttpInterceptor())
        .addInterceptor(chuckerInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    fun providesRetrofitFactory(
        retrofit: Retrofit.Builder,
        client: OkHttpClient
    ) = ServiceFactory(retrofit, client)

    @Provides
    fun providesChuck(@ApplicationContext context: Context) =
        ChuckerInterceptor.Builder(context)
            .collector(
                ChuckerCollector(
                context = context,
                // Toggles visibility of the push notification
                showNotification = true,
                // Allows to customize the retention period of collected data
                retentionPeriod = RetentionManager.Period.ONE_HOUR
            )
            )
            .maxContentLength(250_000L)
            .redactHeaders("Auth-Token", "Bearer")
            .alwaysReadResponseBody(true)
            .build()

    fun getHttpInterceptor() = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

}