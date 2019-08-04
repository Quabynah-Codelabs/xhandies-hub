package io.codelabs.xhandieshub.core.payment

import android.app.Application
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import io.codelabs.sdk.util.network.LiveDataCallAdapterFactory
import io.codelabs.xhandieshub.BuildConfig
import io.codelabs.xhandieshub.core.Utils
import io.codelabs.xhandieshub.core.debugger
import io.codelabs.xhandieshub.core.prefs.AppPreferences
import okhttp3.Cache
import okhttp3.Call
import okhttp3.EventListener
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.net.InetSocketAddress
import java.net.Proxy

class PaymentAPI private constructor(app: Application, private val prefs: AppPreferences) {

    /**
     * Cache
     */
    private val cache: Cache by lazy {
        Cache(
            File(
                app.applicationContext.cacheDir,
                Utils.CACHE_NAME
            ), CACHE_SIZE
        )
    }

    /**
     * Logging requests and responses
     */
    private val logger: HttpLoggingInterceptor
        get() = HttpLoggingInterceptor().apply {
            this.level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        }

    /**
     * OkHttp client
     */
    private val client: OkHttpClient
        get() = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(logger)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authentication", prefs.uid ?: "Anonymous").build()

                return@addInterceptor chain.proceed(request)
            }
            .build()

    /**
     * Endpoint build
     */
    val service: PaymentService
        get() = Retrofit.Builder()
            .baseUrl(BuildConfig.ENDPOINT_BASE_URL)
            .client(client)
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(PaymentService::class.java)

    companion object {
        private const val CACHE_SIZE = 10.times(1024).times(60).toLong()
        @Volatile
        private var instance: PaymentAPI? = null

        fun getInstance(app: Application, prefs: AppPreferences): PaymentAPI =
            instance ?: synchronized(this) {
                instance ?: PaymentAPI(app, prefs).also { instance = it }
            }
    }

}