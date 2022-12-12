package org.wit.playlistmanager.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.wit.playlistmanager.databinding.ActivityRegisterBinding
import org.wit.playlistmanager.main.MainApp

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    lateinit var app: MainApp
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp

        binding.toolbarAdd.title = "Register"
        setSupportActionBar(binding.toolbarAdd)
        auth = Firebase.auth

        binding.btnRegisterUser.setOnClickListener() {
            if (binding.userEmailRegister.text.toString().isEmpty() || binding.userPasswordRegister.text.toString().isEmpty()) {
                Snackbar.make(it, "Please Enter a username or password", Snackbar.LENGTH_LONG).show() //https://firebase.google.com/docs/auth/android/password-auth
            }else{

            val email = binding.userEmailRegister.text.toString()
            val password = binding.userPasswordRegister.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.e("TAG", "createUserWithEmail:success")
                        val user = auth.currentUser
                        val launcherIntent = Intent(this, LoginActivity::class.java)
                        startActivity(launcherIntent)
                    } else {
                        Snackbar.make(it, "Registration Failed", Snackbar.LENGTH_LONG).show()
                    }
                }
                }
        }
    }
}