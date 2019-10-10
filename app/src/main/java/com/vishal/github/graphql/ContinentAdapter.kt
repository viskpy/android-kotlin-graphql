package com.vishal.github.graphql

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.kotlin.graphql.GetContinentsQuery

/**
 * Adapter class for showing list of all continents.
 *
 * @author Vishal - 10th Oct 2019
 * @since 1.0.0
 */
class ContinentAdapter : ArrayAdapter<GetContinentsQuery.Continent> {
    private var mContext: Context
    private var values: List<GetContinentsQuery.Continent>
    private var inflater: LayoutInflater
    private var resource: Int

    constructor(
        context: Context, textViewResourceId: Int,
        values: List<GetContinentsQuery.Continent>
    ) : super(context, textViewResourceId, values) {

        this.mContext = context
        this.values = values
        this.inflater = LayoutInflater.from(context)
        this.resource = textViewResourceId

    }

    override fun getCount(): Int {
        return values.size
    }

    override fun getItem(position: Int): GetContinentsQuery.Continent? {
        return values[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var viewHolder: ViewHolder
        var mainView: View
        if (convertView == null) {
            viewHolder = ViewHolder()
            mainView = inflater.inflate(resource, parent, false)
            viewHolder.title = mainView.findViewById(R.id.continent_name) as TextView
            mainView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
            mainView = convertView
        }
        viewHolder.title.text = getItem(position)?.name() + "(" + getItem(position)?.code() + ")"
        return mainView
    }

    override fun getDropDownView(
        position: Int, convertView: View?,
        parent: ViewGroup
    ): View {
        return getView(position, convertView, parent)
    }

    class ViewHolder {
        lateinit var title: TextView;
    }
}