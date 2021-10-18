package net.simplifiedlearning.firebaseauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.simplifiedlearning.firebaseauth.Modle.Product;

public class UpdateProducatActivity extends AppCompatActivity {

    EditText description,address,quantity,tID;
    Button btnApply ,btnCancel;
    DatabaseReference databaseReference;
    Module module;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_producat);

        tID=(EditText)findViewById(R.id.id);
        description=(EditText)findViewById(R.id.description);
        address=(EditText)findViewById(R.id.address);
        quantity=(EditText)findViewById(R.id.quantity);

        btnApply=(Button)findViewById(R.id.btnApply);
        btnCancel=(Button)findViewById(R.id.btnCancel);






        module=((Module)getApplicationContext());
        //Getting post id
        final String str=module.getGvalue_id().substring(3,23);
        tID.setText(str);
        tID.setEnabled(false);


        //Getting category root
        final String name=module.getGvalue_name().substring(33,50);
        String categoryRoot="";
        categoryRoot= name.substring(categoryRoot.indexOf("=")+1);
        categoryRoot = name.substring(0, categoryRoot.indexOf("\n"));

        databaseReference= FirebaseDatabase.getInstance().getReference(categoryRoot);



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Product product=dataSnapshot.child(str).getValue(Product.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateArrayList();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cleartxt();
            }
        });
    }



    private void Cleartxt(){
        description.setText("");
        address.setText("");
        quantity.setText("");
        tID.requestFocus();

    }

    private void  updateArrayList() {
        final String ID = tID.getText().toString().trim();
        final String _description = description.getText().toString().trim();
        final String _adrress = address.getText().toString().trim();
        final String _quantity = quantity.getText().toString().trim();


        if (TextUtils.isEmpty(ID)) {
            tID.setError("have problem with your ID!");
        } else if (TextUtils.isEmpty(_adrress)) {
            address.setError("Please enter your Name!");
        } else if (TextUtils.isEmpty(_description)) {
            description.setError("Please enter your Email!");
        } else if (TextUtils.isEmpty(_quantity)) {
            quantity.setError("Please enter your Password!");
        } else {

            Product product = new Product(_description, _adrress, _quantity);
            databaseReference.child("").child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    databaseReference.child(ID).child("address").setValue(_adrress);
                    databaseReference.child(ID).child("description").setValue(_description);
                    databaseReference.child(ID).child("quantity").setValue(_quantity);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Toast.makeText(this, "Product is updated", Toast.LENGTH_LONG).show();

            finish();
            Intent intent = new Intent(getApplicationContext(), ViewProductActivity.class);
            startActivity(intent);


        }
    }


    }

