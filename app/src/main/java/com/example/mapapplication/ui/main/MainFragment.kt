package com.example.mapapplication.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.mapapplication.R
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tbruyelle.rxpermissions2.RxPermissionsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.jar.Manifest

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private  val mainViewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeLocationPermissionAccess()
    }

    private fun observeLocationPermissionAccess() {
        mainViewModel.locationPermissionListener.observe(
            viewLifecycleOwner,
            Observer { permissionAccessed ->
                if (permissionAccessed) {

                } else {
                    RxPermissions(this).request(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ).subscribe()
                }
            })
    }

}
