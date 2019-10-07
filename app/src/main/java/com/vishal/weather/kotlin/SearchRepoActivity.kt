package com.vishal.weather.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.kotlin.graphql.FindRepoQuery
import kotlinx.android.synthetic.main.repo_layout.*
import okhttp3.OkHttpClient
import java.util.logging.Logger
import okhttp3.logging.HttpLoggingInterceptor



/**
 * This activity will list the information about searched repo from github.
 * API call is graphql call
 *
 * @author Vishal - 7th Oct 2019
 * @since 1.0.0
 */
class SearchRepoActivity : AppCompatActivity() {

    lateinit var activity: SearchRepoActivity
    private val BASE_URL = "https://api.github.com/graphql"
    private lateinit var client: ApolloClient

    companion object {
        val LOG: Logger = Logger.getLogger(SearchRepoActivity::class.java.name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activity = this
        client = setUpApolloClient()
        button_find.setOnClickListener {
            progress_bar.visibility = View.VISIBLE
            client.query(
                FindRepoQuery
                    .builder()
                    .name("butterknife")
                    .owner("jakewharton")
                    .build()
            )
                .enqueue(object : ApolloCall.Callback<FindRepoQuery.Data>() {
                    override fun onFailure(e: ApolloException) {
                        LOG.info(e.message.toString())
                    }

                    override fun onResponse(response: Response<FindRepoQuery.Data>) {
                        LOG.info(" " + response.data()?.repository())
                        runOnUiThread {
                            progress_bar.visibility = View.GONE
                            name_text_view.text = String.format(
                                getString(R.string.name_text),
                                response.data()?.repository()?.name()
                            )
                            description_text_view.text = String.format(
                                getString(R.string.description_text),
                                response.data()?.repository()?.description()
                            )
                            forks_text_view.text = String.format(
                                getString(R.string.fork_count_text),
                                response.data()?.repository()?.forkCount().toString()
                            )
                            url_text_view.text = String.format(
                                getString(R.string.url_count_text),
                                response.data()?.repository()?.url().toString()
                            )
                        }
                    }

                })
        }

    }

    private fun setUpApolloClient(): ApolloClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttp = OkHttpClient
            .Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder().method(original.method(), original.body())
                builder.addHeader(
                    "Authorization",
                    "Bearer 78fcad15100f24d42449098b50251510f3b66998"
                )
                chain.proceed(builder.build())
            }
        return ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttp.build())
            .build()
    }

}
