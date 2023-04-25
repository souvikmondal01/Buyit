package com.buyit.buyit.start.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.buyit.buyit.R
import com.buyit.buyit.databinding.FragmentRegisterBinding
import com.buyit.buyit.start.interfaces.RegisterListener
import com.buyit.buyit.start.repositories.StartRepositoryImp
import com.buyit.buyit.start.viewModels.StartViewModel
import com.buyit.buyit.start.viewModels.StartViewModelFactory
import com.buyit.buyit.utils.CommonUtils.changeIconColorWhenEdittextNotInFocus
import com.buyit.buyit.utils.CommonUtils.clearEdittext
import com.buyit.buyit.utils.CommonUtils.hideKeyboard
import com.buyit.buyit.utils.CommonUtils.showPassword
import com.buyit.buyit.utils.hide
import com.buyit.buyit.utils.show
import com.buyit.buyit.utils.toast

class RegisterFragment : Fragment(), RegisterListener {
    private lateinit var context: Context
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: StartViewModel
    private val factory = StartViewModelFactory(StartRepositoryImp())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        viewModel = ViewModelProvider(this, factory)[StartViewModel::class.java]
        binding.viewModel = viewModel
        viewModel.registerListener = this
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context = requireContext()
        binding.tvLogin.setOnClickListener {
            it.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        clearEdittext(binding.etName, binding.ivCancelName)
        clearEdittext(binding.etEmail, binding.ivCancelEmail)

        changeIconColorWhenEdittextNotInFocus(context, binding.etName, binding.ivCancelName)
        changeIconColorWhenEdittextNotInFocus(context, binding.etEmail, binding.ivCancelEmail)
        changeIconColorWhenEdittextNotInFocus(context, binding.etPassword, binding.ivVisibility)

        //show password on visibility icon click
        var isShow = false
        binding.ivVisibility.setOnClickListener {
            showPassword(isShow, binding.etPassword, binding.ivVisibility)
            isShow = !isShow
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onEmptyName() {
        binding.apply {
            etName.apply {
                error = "Enter name"
                requestFocus()
            }
            pb.hide()
        }
    }

    override fun onEmptyEmail() {
        binding.apply {
            etEmail.apply {
                error = "Enter email"
                requestFocus()
            }
            pb.hide()
        }
    }

    override fun onEmptyPassword() {
        binding.apply {
            etPassword.apply {
                error = "Enter password"
                requestFocus()
            }
            pb.hide()
        }
    }

    override fun onStarted() {
        binding.pb.show()
    }

    override fun onSuccess() {
        viewModel.registerUser {
            toast(it)
            binding.pb.hide()
            hideKeyboard()
        }
    }


}