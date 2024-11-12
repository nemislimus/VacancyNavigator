import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.network.api.HhSearchApi
import ru.practicum.android.diploma.data.network.api.NetworkClient


fun getHhApi(): HhSearchApi {
    val interceptor = Interceptor { chain ->

        val token = BuildConfig.HH_ACCESS_TOKEN
        val appname = ""
        val mail = ""

        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .header("HH-User-Agent", "$appname ($mail)")
        val newRequest = builder.build()
        chain.proceed(newRequest)

    }

    val okHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(interceptor)
        .build()

    return Retrofit.Builder()
        .baseUrl("https://api.hh.ru/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(HhSearchApi::class.java)
}


val client: NetworkClient = RetrofitNetworkClient(
    hhSearchApi = getHhApi(),
    connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager,
    mapper = get()
)