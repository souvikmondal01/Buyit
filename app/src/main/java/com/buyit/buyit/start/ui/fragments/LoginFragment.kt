package com.buyit.buyit.start.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.buyit.buyit.R
import com.buyit.buyit.databinding.FragmentLoginBinding
import com.buyit.buyit.start.daos.UserDao
import com.buyit.buyit.start.interfaces.LoginListener
import com.buyit.buyit.start.models.User
import com.buyit.buyit.start.repositories.StartRepositoryImp
import com.buyit.buyit.start.ui.activities.GetLocationPermissionActivity
import com.buyit.buyit.start.viewModels.StartViewModel
import com.buyit.buyit.start.viewModels.StartViewModelFactory
import com.buyit.buyit.utils.CommonUtils.auth
import com.buyit.buyit.utils.CommonUtils.changeIconColorWhenEdittextNotInFocus
import com.buyit.buyit.utils.CommonUtils.clearEdittext
import com.buyit.buyit.utils.CommonUtils.hideKeyboard
import com.buyit.buyit.utils.CommonUtils.showPassword
import com.buyit.buyit.utils.Constant.LOGIN_SUCCESS
import com.buyit.buyit.utils.Constant.RC_SIGN_IN
import com.buyit.buyit.utils.hide
import com.buyit.buyit.utils.show
import com.buyit.buyit.utils.toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await


class LoginFragment : Fragment(), LoginListener {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var context: Context
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: StartViewModel
    private val factory = StartViewModelFactory(StartRepositoryImp())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        viewModel = ViewModelProvider(this, factory)[StartViewModel::class.java]
        binding.viewModel = viewModel
        viewModel.loginListener = this
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context = requireContext()

        binding.tvRegister.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        clearEdittext(binding.etEmail, binding.ivCancel)
        changeIconColorWhenEdittextNotInFocus(context, binding.etEmail, binding.ivCancel)
        changeIconColorWhenEdittextNotInFocus(context, binding.etPassword, binding.ivVisibility)

        //show password on visibility icon click
        var isShow = false
        binding.ivVisibility.setOnClickListener {
            showPassword(isShow, binding.etPassword, binding.ivVisibility)
            isShow = !isShow
        }

        binding.cvFacebook.setOnClickListener {
            Toast.makeText(context, "Coming Soon...", Toast.LENGTH_SHORT).show()
        }

        binding.cvGoogle.setOnClickListener {
            googleSignIn()
        }
        binding.cvTwitter.setOnClickListener {
            Toast.makeText(context, "Coming Soon...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun googleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        binding.pb.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            val auth = auth.signInWithCredential(credential).await()
            val currentUser = auth.user!!
            withContext(Dispatchers.Main) {
                updateUI(currentUser)
                val user = User(
                    currentUser.uid,
                    currentUser.displayName,
                    currentUser.email,
                    "",
                    System.currentTimeMillis().toString(),
                    "",
                    currentUser.photoUrl.toString()
                )
                UserDao().addUser(user)
            }
        }

    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(requireActivity(), GetLocationPermissionActivity::class.java))
            requireActivity().finish()
        } else {
            binding.pb.visibility = View.GONE
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
        hideKeyboard()
        viewModel.loginUser {
            if (it == LOGIN_SUCCESS) {
                startActivity(Intent(requireActivity(), GetLocationPermissionActivity::class.java))
                requireActivity().finish()
                binding.pb.hide()
                return@loginUser
            }
            toast(it)
            binding.pb.hide()
        }
    }

    override fun restPassword() {
        hideKeyboard()
        viewModel.resetPassword {
            toast(it)
            binding.pb.hide()
        }
    }

    override fun googleLogin() {
        toast("Google")
    }

}


