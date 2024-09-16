package digital.rna.monita_android_sdk

import okhttp3.OkHttpClient

object OkHttpClientProvider {
    private var client: OkHttpClient? = null

    fun setClient(okHttpClient: OkHttpClient) {
        client = okHttpClient
    }

    fun getClient(): OkHttpClient {
        return client ?: throw IllegalStateException("OkHttpClient not initialized")
    }
}