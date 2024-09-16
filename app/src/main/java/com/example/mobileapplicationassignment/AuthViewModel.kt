package com.example.mobileapplicationassignment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth


class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        if(auth.currentUser == null){
            _authState.value = AuthState.Unauthenticated
        }else{
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email:String, password: String){

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email or password is empty!")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{task ->
                if(task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }


    //signup(email:String, pass1:String, onSuccess: (String) -> Unit, onError: (String)-> Unit)
    fun signup(email:String, pass1:String, onSuccess: (String) -> Unit, onError: (String)-> Unit){
        if(email.isEmpty() || pass1.isEmpty()){
            _authState.value = AuthState.Error("Email or password is empty!")
            return
        }else{
            _authState.value = AuthState.Loading
            auth.createUserWithEmailAndPassword(email,pass1)
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        _authState.value = AuthState.Authenticated
                        println("User data successfully written to the authentication database.")
                    }else{
                        _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                        println("Error writing user data: ${task.exception?.message}")
                    }
                }
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        val userId = task.result?.user?.uid
                        if(userId != null){
                            _authState.value = AuthState.Authenticated
                            onSuccess(userId)
                        }else{
                            _authState.value = AuthState.Error("Failed to get user ID!")
                            onError("Failed to get user ID!")
                        }

                    }else{
                        _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                    }
                }
        }
    }

    fun signout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}

sealed class AuthState{
    object Authenticated: AuthState()
    object Unauthenticated: AuthState()
    object Loading: AuthState()
    data class Error(val message: String):AuthState()
}