package com.example.mapapplication.di

import com.example.mapapplication.repository.LocationRepository
import com.example.mapapplication.repository.PlacesApiService
import com.example.mapapplication.repository.PlacesRepository
import com.example.mapapplication.ui.main.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    viewModel { MainViewModel(get(), get()) }

    single { PlacesRepository(androidContext(),get()) }
    single { createOkkHttpClient() }
    single { LocationRepository(androidContext()) }
    single { createWebService<PlacesApiService>(get(), PlacesApiService.BASE_URL) }

}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, baseUrl: String): T {
    return Retrofit.Builder()
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()
        .create(T::class.java)
}

fun createOkkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor).build()
}
