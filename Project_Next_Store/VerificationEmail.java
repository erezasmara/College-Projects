package net.simplifiedlearning.firebaseauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerificationEmail extends AppCompatActivity {

    TextView title,body_text,singOut,wrongText;


    Button btn;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_email);

        title=(TextView)findViewById(R.id.title);
        body_text=(TextView)findViewById(R.id.body_text);
        singOut=(TextView)findViewById(R.id.signOut_text);
        wrongText=(TextView)findViewById(R.id.wrongText);

        btn = (Button) findViewById(R.id.btnVerified);

        mAuth = FirebaseAuth.getInstance();


        loadUserInformation();

        singOut.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(VerificationEmail.this, MainActivity.class);
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(intent);


        }
        });



    }




    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();

            if (user == null) {
                title.setText("Loading Page...");
                body_text.setText("");
                singOut.setText("");
                wrongText.setText("");
                btn.setVisibility(View.GONE);
                finish();
                Intent intent = new Intent(VerificationEmail.this, Navigation.class);
                startActivity(intent);

            }

            if (user != null) {
                user.reload();
                if (user.isEmailVerified()) {
                    title.setText("Loading Page...");
                    body_text.setText("");
                    singOut.setText("");
                    wrongText.setText("");
                    btn.setVisibility(View.GONE);
                    finish();
                    Intent intent = new Intent(VerificationEmail.this, Navigation.class);
                    startActivity(intent);
            } else {

                title.setText("Email Confirmation");
                body_text.setText("Welcome ,you're almost ready to start enjoy NextStore. " +
                        "\nSimply click the big blue button bellow and we send you verify email.");
                wrongText.setText("You have already confirmed?? ");

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(VerificationEmail.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();

                            }

                        });
                    }
                });
            }
        }
    }



}
