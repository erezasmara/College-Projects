package net.simplifiedlearning.firebaseauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.simplifiedlearning.firebaseauth.Modle.Product;

import java.util.ArrayList;
import java.util.List;

public class ViewProductActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    ListView listView;
    ArrayList<String> arrayList=new ArrayList<>();

    ArrayAdapter<String> arrayAdapter;
    ImageButton btnDelete;
    ImageButton btnUpdate;
    Module module;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        user= FirebaseAuth.getInstance().getCurrentUser();



        listView=(ListView)findViewById(R.id.listviewtxt);
        arrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        btnDelete=(ImageButton) findViewById(R.id.btnDelete);
        btnUpdate=(ImageButton)findViewById(R.id.btnUpdate);
        module=((Module)getApplicationContext());



        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            //GETTING ALL PARENT ROOT (E.G CELLULAR ,COMPUTERS).
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    String tmp =snapshot.getKey();
                    addToList(tmp);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
               // Failed to read value
                Toast.makeText(getApplicationContext(), "Failed to read value." +
                        error.toException(), Toast.LENGTH_SHORT).show();
            }
        });







        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                module.setGvalue_id(arrayList.get(position));
                module.setGvalue_name(arrayList.get(position));


            }
        });

        //Update a Product from database
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(module.getGvalue_id().equals("")){
                    Toast.makeText(ViewProductActivity.this,"Please Select a item!",Toast.LENGTH_LONG).show();
                }else{
                    finish();
                    Intent intphto=new Intent(ViewProductActivity.this,UpdateProducatActivity.class);
                    startActivity(intphto);
                }


            }
        });

     //Delete Product from database
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting post id
                final String str=module.getGvalue_id().substring(3,23);

                //getting category root
                final String name=module.getGvalue_name().substring(33,50);
                String categoryRoot="";
                categoryRoot= name.substring(categoryRoot.indexOf("=")+1);
                categoryRoot = name.substring(0, categoryRoot.indexOf("\n"));



                if(str==""){
                    Toast.makeText(ViewProductActivity.this,"Please Select item to delete!",Toast.LENGTH_LONG).show();

                }else{
                    databaseReference= FirebaseDatabase.getInstance().getReference(categoryRoot);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            databaseReference.child(str).removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Toast.makeText(ViewProductActivity.this,"item delete!",Toast.LENGTH_LONG).show();

                    finish();
                    Intent intent = new Intent(getApplicationContext(), ViewProductActivity.class);
                    startActivity(intent);


                }

            }
        });



    }//END OnCreate.

// ADDING PRODUCT TO LIST VIEW
    private void addToList(final String category){
        String _sellerId=user.getUid();

        Query query= FirebaseDatabase.getInstance().getReference(category).orderByChild("idSeller").
                equalTo(_sellerId);
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    //Waiting to adding a product/`
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    arrayList.add(String.valueOf(product));
                }
                arrayAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
