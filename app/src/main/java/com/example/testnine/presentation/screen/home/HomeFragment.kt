package com.example.testnine.presentation.screen.home

import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.testnine.databinding.FragmentHomeBinding
import com.example.testnine.presentation.common.base.BaseFragment
import com.example.testnine.presentation.screen.home.bottomsheet.BottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    override fun bind() {

    }

    override fun bindViewActionListener() {
        listener()
    }

    override fun bindObserves() {

    }

    private fun listener() = with(binding) {
        btnAddImage.setOnClickListener {
            BottomSheetFragment().show(parentFragmentManager, "newImage")
        }
    }

}