package io.tux.wallet.testnet.appModules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.tux.wallet.testnet.apis.ApiInterface
import io.tux.wallet.testnet.utils.Constants.ACCEPT_LANGUAGE
import io.tux.wallet.testnet.utils.Constants.NONCE
import io.tux.wallet.testnet.utils.Constants.USER
import io.tux.wallet.testnet.utils.HmacEncryption.getHmacEncryptedKey
import io.tux.wallet.testnet.utils.SharedPref
import io.tux.wallet.testnet.utils.Urls.BASE_URL
import io.tux.wallet.testnet.utils.Utils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context: Context) = SharedPref(context)

//    @Provides
//    @Singleton
//    fun provideCryptoCoin(@ApplicationContext context: Context,sharedPref: SharedPref,apiInterface: ApiInterface) = CryptoCoins(context ,sharedPref,apiInterface)


    @Provides
    @Singleton
    fun getApiClient(session: SharedPref): Retrofit {
        val okHttpClient = OkHttpClient()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                okHttpClient.newBuilder()
                    .connectTimeout(2 * 60, TimeUnit.SECONDS)
                    .readTimeout(2 * 60, TimeUnit.SECONDS)
                    .writeTimeout(2 * 60, TimeUnit.SECONDS)
                    .addInterceptor(headerInterceptor(session)).addInterceptor(interceptor)
                    .build()
            ).build()

    }
//


    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)

    fun headerInterceptor(session: SharedPref? = null) =
        Interceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder().apply {
                addHeader("Content-Type", "application/json")
                if (session?.getLanguage() != null) {
                    addHeader(ACCEPT_LANGUAGE, Utils.getLanguageSymbol(session))
                }
                val date = getDate()
                val data = getData(date)
                addHeader(
                    "userauth",
                    "HmacSHA512 $USER:$NONCE:${getHmacEncryptedKey(data)}"
                )
                addHeader("url", BASE_URL.dropLast(1))
                addHeader("requestdate", date)
            }.method(original.method, original.body)
            val request = requestBuilder.build()
            chain.proceed(request)
        }

    private fun getData(date: String): String {
        return "$date#${BASE_URL.dropLast(1)}#$USER#$NONCE#"
    }

    private fun getDate(): String {
        val calender = Calendar.getInstance()
        val format = SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS", Locale.getDefault())
        return format.format(calender.time)
    }

}
