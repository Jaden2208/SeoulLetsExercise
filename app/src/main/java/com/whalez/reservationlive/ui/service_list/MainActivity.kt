package com.whalez.reservationlive.ui.service_list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.whalez.reservationlive.R
import com.whalez.reservationlive.data.api.ServiceDBClient
import com.whalez.reservationlive.data.api.ServiceDBInterface
import com.whalez.reservationlive.data.repository.NetworkState
import com.whalez.reservationlive.ui.single_service_details.SingleServiceActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val SPAN_COUNT = 2
    }

    private lateinit var viewModel: MainActivityViewModel

    lateinit var servicePagedListRepository: ServicePagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService: ServiceDBInterface = ServiceDBClient.getClient()

        servicePagedListRepository = ServicePagedListRepository(apiService)

        viewModel = getViewModel()

        val serviceAdapter = ServicePagedListAdapter(this)

        val gridLayoutManager = GridLayoutManager(this, SPAN_COUNT)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = serviceAdapter.getItemViewType(position)
                return if (viewType == serviceAdapter.SERVICE_VIEW_TYPE) 1
                else SPAN_COUNT
            }
        }

        rv_service_list.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            adapter = serviceAdapter
        }

        viewModel.servicePagedList.observe(this, Observer {
            serviceAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE
                else View.GONE
            tv_error.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE
                else View.GONE

            if(!viewModel.listIsEmpty()) {
                serviceAdapter.setNetworkState(it)
            }
        })
    }

    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainActivityViewModel(servicePagedListRepository) as T
            }
        })[MainActivityViewModel::class.java]
    }
}
