package net.simplifiedlearning.firebaseauth;

import android.app.Application;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class Module extends Application {

    public ArrayList<String> garrList=new ArrayList<>();
    public ArrayAdapter<String> garrAdp;
    public String gvalue_id;
    public String gvalue_name;


    public String getGvalue_id() {
        return gvalue_id;
    }

    public void setGvalue_id(String gvalue_id) {
        this.gvalue_id = gvalue_id;
    }

    public String getGvalue_name() {
        return gvalue_name;
    }

    public void setGvalue_name(String gvalue_name) {
        this.gvalue_name = gvalue_name;
    }
}
