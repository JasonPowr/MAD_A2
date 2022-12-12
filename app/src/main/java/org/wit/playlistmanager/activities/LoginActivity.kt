package org.wit.playlistmanager.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.wit.playlistmanager.databinding.ActivityLoginBinding
import org.wit.playlistmanager.main.MainApp

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth;
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        app = application as MainApp

        binding.toolbarAdd.title = "Login or Register"
        setSupportActionBar(binding.toolbarAdd)
        auth = Firebase.auth

        binding.btnRegister.setOnClickListener(){
            val launcherIntent = Intent(this, RegisterActivity::class.java)
            startActivity(launcherIntent)
        }

        binding.btnLogin.setOnClickListener(){

            if (binding.userEmail.text.toString().isEmpty() || binding.userPassword.text.toString().isEmpty()) {
                Snackbar.make(it, "Please Enter a username or password", Snackbar.LENGTH_LONG).show() //https://firebase.google.com/docs/auth/android/password-auth
            }else{

            val email = binding.userEmail.text.toString()
            val password = binding.userPassword.text.toString()

            auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.e("TAG", "signInWithEmail:success")
                    val firebaseUser = auth.currentUser
                    val launcherIntent = Intent(this, PlaylistListActivity::class.java).putExtra("login",firebaseUser)
                    startActivity(launcherIntent)
                } else {
                    Snackbar.make(it, "Unable to authenticate please try again", Snackbar.LENGTH_LONG).show()
                }
            }
            }
        }

    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            val firebaseUser = auth.currentUser
            val launcherIntent = Intent(this, PlaylistListActivity::class.java).putExtra("login",firebaseUser)
            startActivity(launcherIntent)
        }
    }


}