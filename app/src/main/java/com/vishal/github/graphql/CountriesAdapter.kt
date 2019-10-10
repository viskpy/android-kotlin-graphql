package com.vishal.github.graphql

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kotlin.graphql.FindContriesOfAContinentQuery

/**
 * Adapter class for showing list of countries of a selected continent.
 *
 * @author Vishal - 10th Oct 2  019
 * @since 1.0.0
 */
class CountriesAdapter constructor(countriesData: FindContriesOfAContinentQuery.Data?) :
    RecyclerView.Adapter<CountriesAdapter
    .ForecastHolder>() {
    private val countriesData = countriesData!!.continent()!!.countries()

    class ForecastHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val countryNameTV: TextView = view.findViewById(R.id.country_name)
        private val countryPhoneTV: TextView = view.findViewById(R.id.country_phone)
        private val countryCurrency: TextView = view.findViewById(R.id.country_currency)

        fun setCountryData(country: FindContriesOfAContinentQuery.Country) {
            countryNameTV.text = country.name() + "(" + country.emoji() + ")"
            countryPhoneTV.text = country.phone()
            countryCurrency.text = country.currency()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.countries_row, parent, false)
        return ForecastHolder(view)
    }

    override fun getItemCount(): Int {
        return countriesData!!.size
    }

    override fun onBindViewHolder(holder: ForecastHolder, position: Int) {
        holder.setCountryData(countriesData!![position])
    }
}