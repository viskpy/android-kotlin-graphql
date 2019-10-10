package com.vishal.github.graphql

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.kotlin.graphql.FindContriesOfAContinentQuery
import com.kotlin.graphql.GetContinentsQuery
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.logging.Logger


/**
 * This activity will list the information about countries based on continents.
 * API call is graphql call
 *
 * @author Vishal - 10th Oct 2019
 * @since 1.0.0
 */
class ListCountriesActivity : AppCompatActivity() {

    lateinit var activity: ListCountriesActivity
    private val BASE_URL = "https://countries.trevorblades.com"
    private lateinit var client: ApolloClient

    lateinit var countriesRV: RecyclerView
    lateinit var continentSP: Spinner

    companion object {
        val LOG: Logger = Logger.getLogger(ListCountriesActivity::class.java.name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        setContentView(R.layout.activity_main)
        countriesRV = findViewById(R.id.countries_view)
        continentSP = findViewById(R.id.continent_spinner)
        countriesRV.layoutManager = LinearLayoutManager(activity)
        client = setUpApolloClient()

        client.query(
            GetContinentsQuery
                .builder()
                .build()
        )
            .enqueue(object : ApolloCall.Callback<GetContinentsQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    LOG.info(e.message.toString())
                }

                override fun onResponse(response: Response<GetContinentsQuery.Data>) {
                    runOnUiThread {
                        val continents = response.data()!!.continents()!!.toMutableList()
                        continentSP.adapter = ContinentAdapter(
                            this@ListCountriesActivity,
                            R.layout.continent_row, continents
                        )
                        continentSP.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                }

                                override fun onItemSelected(
                                    p0: AdapterView<*>?,
                                    p1: View?,
                                    p2: Int,
                                    p3: Long
                                ) {
                                    if (continents[p2].code() != null) {
                                        callForCountriesList(continents[p2].code()!!)
                                    } else {
                                        callForCountriesList("AS")
                                    }
                                }

                            }
                    }
                }

            })
    }

    private fun callForCountriesList(code: String) {
        client.query(
            FindContriesOfAContinentQuery
                .builder()
                .code(code?.let { code })
                .build()
        )
            .enqueue(object : ApolloCall.Callback<FindContriesOfAContinentQuery.Data>() {
                override fun onFailure(e: ApolloException) {
                    LOG.info(e.message.toString())
                }

                override fun onResponse(response: Response<FindContriesOfAContinentQuery.Data>) {
                    runOnUiThread {
                        countriesRV.adapter = CountriesAdapter(response.data())
                    }
                }

            })
    }

    /**
     * Basic set up for graphql API, OkHttp is used for graphql with apollo client
     */
    private fun setUpApolloClient(): ApolloClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttp = OkHttpClient
            .Builder()
            .addInterceptor(logging)
        return ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttp.build())
            .build()
    }

}
