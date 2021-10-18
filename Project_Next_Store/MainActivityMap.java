package net.simplifiedlearning.firebaseauth;

import android.app.Dialog;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.*;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import net.simplifiedlearning.firebaseauth.Interface.IFirebaseLoadDone;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import net.simplifiedlearning.firebaseauth.Modle.Product;
import net.simplifiedlearning.firebaseauth.Modle.SearchResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivityMap extends AppCompatActivity implements IFirebaseLoadDone  {

    private static final String TAG = "MainActivityMap";
    private static final int ERROR_DIALOG_REQUEST = 9001;


  //Spinners
    SearchableSpinner categorySpinner;
    SearchableSpinner productSpinner;
    boolean categotySelected=false;

//choosing values.
    String choosingCategory;
    String choosingProduct;


 //Variable
    List<Product> products;
    ArrayAdapter<SearchResult> LocationAdapter;
     ArrayList<SearchResult> LocationAdress;



    //fireBase variable
    DatabaseReference productsRef;
    IFirebaseLoadDone iFirebaseLoadDone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);

        categorySpinner=(SearchableSpinner)findViewById((R.id.category_spinner));
        productSpinner=(SearchableSpinner)findViewById((R.id.product_spinner));
        LocationAdapter= new ArrayAdapter<SearchResult>(this,android.R.layout.simple_list_item_2,LocationAdress);


       //Search button on click.
        findViewById(R.id.searchItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText =choosingProduct;
              if(inputText.isEmpty())
                    Toast.makeText(getApplicationContext(),"please enter a value",Toast.LENGTH_SHORT).show();
               else readOnDb(inputText);

            }
        });


       //first initialization spinner category values.
        SubCategory("category");

        //Spinner category on click.
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,int position, long id) {
                // On selecting a spinner item
                choosingCategory = adapter.getItemAtPosition(position).toString();
                categotySelected=true;
                SubCategory(choosingCategory);


            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        //Spinner product on click.
        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View view, int position, long l) {
                choosingProduct = adapter.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }//end onCreate





    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivityMap.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivityMap.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void goMap(){

        if (isServicesOK()) {

            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        }
    }
//read items from dB
    private void readOnDb( String tmp){

        Query query= FirebaseDatabase.getInstance().getReference(choosingCategory)
                .orderByChild("name")
                .equalTo(choosingProduct);

        query.addListenerForSingleValueEvent(valueEventListener);
    } // end read dB

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
             ArrayList<SearchResult> LocationAdress=new ArrayList<>();
            SearchResult result;
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    result=new SearchResult(product.getAddress(),product.getPhone(),product.getSellerName());
                    LocationAdress.add(result);

                }
                LocationAdapter.notifyDataSetChanged();
            }
            jumpToMap(LocationAdress);

        }


        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };



public void jumpToMap( ArrayList<SearchResult> bdvalue){

       ArrayList<SearchResult> newValue=bdvalue;

       Intent i = new Intent(this, MapActivity.class);

        i.putExtra("key", (Serializable)newValue);

        startActivity(i);

}


//pull category from dB
    public void SubCategory(String selectedCategory){
        //Init Db
        productsRef= FirebaseDatabase.getInstance().getReference("product").child(selectedCategory);

        //Init interface
        iFirebaseLoadDone=this;

        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Product> products=new ArrayList<>();
                for(DataSnapshot productSnapShot:dataSnapshot.getChildren())
                {
                    products.add(productSnapShot.getValue(Product.class));
                }
                iFirebaseLoadDone.onFirebaseLoadSuccess(products );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());
            }
        });

    }

//happen when we success
    public void onFirebaseLoadSuccess(List<Product> productList ) {
        products=productList;
        //Get all name
        List<String> name_list=new ArrayList<>();
        for(Product product:productList)
            name_list.add(product.getName());
        //Create Adapter and set for spinner
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,name_list);


        if(categotySelected==true){
            productSpinner.setAdapter((adapter));

        }else{

            categorySpinner.setAdapter((adapter));

        }

    }
//happen when we faild
    @Override
    public void onFirebaseLoadFailed(String message) {

    }







}































