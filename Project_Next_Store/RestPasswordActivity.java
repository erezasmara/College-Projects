package net.simplifiedlearning.firebaseauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RestPasswordActivity extends AppCompatActivity {

    ProgressBar progressBar;
    EditText editText;
    Button button;

   FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_password);


        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        editText=(EditText)findViewById(R.id.editTextEmail);
        button=(Button)findViewById(R.id.btnRest);

        firebaseAuth=FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                SendResetEmail();

            }
        });

    }


    public void SendResetEmail(){

        String emailCheck= editText.getText().toString().trim();

        if(emailCheck.isEmpty())
        {
            editText.setError("Required email address");
            editText.requestFocus();
            return;
        }


        if(!Patterns.EMAIL_ADDRESS.matcher(emailCheck).matches())
        {
            editText.setError("Please enter a valid email");
            editText.requestFocus();
            return;
        }




        firebaseAuth.sendPasswordResetEmail(editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);


                if(task.isSuccessful()){

                    Toast.makeText(RestPasswordActivity.this,
                            "password send to your email",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(RestPasswordActivity.this,MainActivity.class));
                }else{
                    Toast.makeText(RestPasswordActivity.this,
                            task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                }

            }
        });


    }
}
