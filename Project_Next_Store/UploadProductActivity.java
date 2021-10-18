package net.simplifiedlearning.firebaseauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import net.simplifiedlearning.firebaseauth.Interface.IFirebaseLoadDone;
import net.simplifiedlearning.firebaseauth.Modle.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UploadProductActivity extends AppCompatActivity implements IFirebaseLoadDone {


    //Spinners
    SearchableSpinner categorySpinner;
    SearchableSpinner productSpinner;
    boolean categorySelected=false;
    //choosing values.
    String choosingCategory;
    String choosingProduct;

     FirebaseUser user;
     String uid;

     EditText model;

     EditText state;
     EditText street;
     EditText numberH;

     EditText description;
     EditText quantity;
     Button upLoadProduct;
     EditText phone;


    DatabaseReference productsRef;
    IFirebaseLoadDone iFirebaseLoadDone;
    List<Product> products;


    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);




        categorySpinner=(SearchableSpinner)findViewById((R.id.category_spinner));
        productSpinner=(SearchableSpinner)findViewById((R.id.product_spinner));


        state=(EditText)findViewById(R.id.state);
        street=(EditText)findViewById(R.id.street);
        numberH=(EditText)findViewById(R.id.numberH);



        model=(EditText)findViewById(R.id.model);
        phone=(EditText)findViewById(R.id.phoneNumber);
        description=(EditText) findViewById((R.id.description));
        quantity =(EditText)findViewById(R.id.quantity);
        upLoadProduct=(Button)findViewById(R.id.button);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);


        upLoadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();

            }

        });

        user= FirebaseAuth.getInstance().getCurrentUser();


        SubCategory("category");

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,int position, long id) {
                // On selecting a spinner item
                choosingCategory = adapter.getItemAtPosition(position).toString();
                categorySelected=true;
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


    }


    private void addProduct(){
        String _name = productSpinner.getSelectedItem().toString();
        String _model=model.getText().toString().trim();

        String _state=state.getText().toString().trim();
        String _street=street.getText().toString().trim();
        String _numberH=numberH.getText().toString().trim();
        String finalAddress= _state + " "+ _street + " " + _numberH;



        String _description=description.getText().toString().trim();
        String _quantity=quantity.getText().toString().trim();
        String _sellerId=user.getUid();
        String _sellerName=user.getDisplayName();
        String phoneNum=phone.getText().toString();

        Date CurrectDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(CurrectDate);
        //need to add prodact id! erez dont forget.



        if(!TextUtils.isEmpty(_name)){
            String idPostProduct=productsRef.push().getKey();
            productsRef= FirebaseDatabase.getInstance().getReference(choosingCategory);

            Product product=new Product(_sellerName,_name,_model,"image",_description,finalAddress,_quantity,_sellerId,"1234",
                    phoneNum,formattedDate,idPostProduct,choosingCategory);

            productsRef.child(idPostProduct).setValue(product);
            Toast.makeText(this,"Product added!",Toast.LENGTH_LONG).show();


            finish();
            startActivity(new Intent(this, MainActivity.class));

        }else{
        Toast.makeText(this,"you should enter a name",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onFirebaseLoadFailed(String message) {

    }


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
            iFirebaseLoadDone.onFirebaseLoadSuccess(products);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());
        }
    });

}

    @Override
    public void onFirebaseLoadSuccess(List<Product> productList) {
        products=productList;
        //Get all name
        List<String> name_list=new ArrayList<>();
        for(Product product:productList)
            name_list.add(product.getName());
        //Create Adapter and set for spinner
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,name_list);


        if(categorySelected==true){
            productSpinner.setAdapter((adapter));

        }else{

            categorySpinner.setAdapter((adapter));

        }


    }

}
