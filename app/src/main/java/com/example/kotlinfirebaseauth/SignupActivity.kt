package com.example.kotlinfirebaseauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.edit_text_email
import kotlinx.android.synthetic.main.activity_signup.edit_text_password
import kotlinx.android.synthetic.main.activity_signup.progressbar

class SignupActivity : AppCompatActivity() {

    private lateinit var mAuth :  FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        mAuth = FirebaseAuth.getInstance()

        button_sign_up.setOnClickListener{
            val email = edit_text_email.text.toString().trim()
            val password = edit_text_password.text.toString().trim()

            if(email.isEmpty()){
                edit_text_email.error = "Email Required"
                edit_text_email.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                edit_text_email.error = "Valid Email Required"
                edit_text_email.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6){
                edit_text_password.error = "6 char password required"
                edit_text_password.requestFocus()
                return@setOnClickListener
            }
            
            registerUser(email, password)
        }

        text_view_login.setOnClickListener{
            startActivity(Intent(this@SignupActivity, SignupActivity::class.java))
        }
    }

    private fun registerUser(email: String, password: String) {
        progressbar.visibility = View.VISIBLE

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressbar.visibility = View.GONE
                if (task.isSuccessful){
                    // Registration Success then login the user
                    // Login  the user when registration success
                    login()
                }else{
                    task.exception?.message?.let {
                        toast(it)
                    }
                }
            }
    }

    override fun onStart() {
        super.onStart()

        // If currentUser is not null means user already loggedin
        mAuth.currentUser?.let {
            login()
        }
    }
}
