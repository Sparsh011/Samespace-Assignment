package com.sparshchadha.samespaceassignment.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.sparshchadha.samespaceassignment.features.music_player.data.remote.api.SongsApi
import com.sparshchadha.samespaceassignment.features.music_player.data.repository.MusicPlayerRepositoryImpl
import com.sparshchadha.samespaceassignment.features.music_player.domain.repository.MusicPlayerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedModule {

    @Provides
    @Singleton
    fun provideHttpClient(
        @ApplicationContext context: Context,
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .addInterceptor(ChuckerInterceptor(context))
            .build()
    }

    @Provides
    @Singleton
    fun provideSongsApi(
        okHttpClient: OkHttpClient
    ): SongsApi {
        return Retrofit.Builder()
            .baseUrl(SongsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(SongsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMusicPlayerRepository(
        songsApi: SongsApi
    ): MusicPlayerRepository {
        return MusicPlayerRepositoryImpl(songsApi = songsApi)
    }
}