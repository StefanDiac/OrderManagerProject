package com.example.air_book.ordermanagerproject.Activities;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Measure;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.air_book.ordermanagerproject.Model.DesertAdapter;
import com.example.air_book.ordermanagerproject.Model.Deserts;
import com.example.air_book.ordermanagerproject.Model.Drink;
import com.example.air_book.ordermanagerproject.Model.DrinkAdapter;
import com.example.air_book.ordermanagerproject.Model.ItemType;
import com.example.air_book.ordermanagerproject.Model.MainCourse;
import com.example.air_book.ordermanagerproject.Model.MainCourseAdapter;
import com.example.air_book.ordermanagerproject.Model.Starter;
import com.example.air_book.ordermanagerproject.Model.StarterAdapter;
import com.example.air_book.ordermanagerproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ViewMenuActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_menu);

        final ArrayList<Starter> starters = new ArrayList<>();
        final ArrayList<MainCourse> mainCourses = new ArrayList<>();
        final ArrayList<Deserts> deserts = new ArrayList<>();
        final ArrayList<Drink> drinks = new ArrayList<>();

        final ArrayList<Bitmap> starterImages = new ArrayList<>();
        final ArrayList<Bitmap> mainCourseImages = new ArrayList<>();
        final ArrayList<Bitmap> desertsImages = new ArrayList<>();
        final ArrayList<Bitmap> drinksImages = new ArrayList<>();

        final Context context = ViewMenuActivity.this;

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously().addOnCompleteListener(ViewMenuActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    final FirebaseUser user = mAuth.getCurrentUser();

                    database = FirebaseDatabase.getInstance().getReference();
                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageReference = storage.getReference();

                            String myUID = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UID", "");

                            if(dataSnapshot.child("Users").hasChild(myUID)){
                                StorageReference userReference = storageReference.child(myUID);
                                StorageReference imageReference = userReference.child("images");

                                DataSnapshot userSnapshot = dataSnapshot.child("Users").child(myUID).child("Menu");

                                DataSnapshot startersSnapshot = userSnapshot.child(ItemType.STARTER_DISH.toString());
                                DataSnapshot mainCourseSnapshot = userSnapshot.child(ItemType.MAIN_COURSE.toString());
                                DataSnapshot desertsSnapshot = userSnapshot.child(ItemType.DESERT.toString());
                                DataSnapshot drinksSnapshot = userSnapshot.child(ItemType.DRINKS.toString());

                                for(DataSnapshot starter: startersSnapshot.getChildren()){
                                    String starterName = starter.getKey();
                                    Boolean isCold = Boolean.parseBoolean(starter.child("IsCold").getValue(String.class));
                                    double price = Double.parseDouble(starter.child("Price").getValue(String.class));

                                    Starter starterToAdd = new Starter(starterName,price,isCold);
                                    starters.add(starterToAdd);

                                    Boolean hasImage = starter.child("hasImage").getValue(Boolean.class);

                                    if(hasImage){
                                        /*StorageReference imageToAddRef = imageReference.child(ItemType.STARTER_DISH.toString()).child(starterName+ ".jpg");
                                        try{
                                            final File localFile = File.createTempFile("imageToAdd","bmp");
                                            imageToAddRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                    Bitmap imageToAdd = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                    starterImages.add(imageToAdd);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    e.printStackTrace();
                                                }
                                            });
                                        }catch (IOException e){
                                            e.printStackTrace();
                                        }*///TO DO - // FIXME: 12/16/17
                                        starterImages.add(null);
                                    } else {
                                        starterImages.add(null);
                                    }
                                }

                                for(DataSnapshot mainCourse: mainCourseSnapshot.getChildren()){
                                    String mainCourseName = mainCourse.getKey();
                                    int spiceLevel = Integer.parseInt(mainCourse.child("SpiceLevel").getValue(String.class));
                                    double price = Double.parseDouble(mainCourse.child("Price").getValue(String.class));

                                    MainCourse mainCourseToAdd = new MainCourse(mainCourseName,price,spiceLevel);
                                    mainCourses.add(mainCourseToAdd);

                                    Boolean hasImage = mainCourse.child("hasImage").getValue(Boolean.class);

                                    if(hasImage){
                                       /* StorageReference imageToAddRef = imageReference.child(ItemType.MAIN_COURSE.toString()).child(mainCourseName+ ".jpg");
                                        try{
                                            final File localFile = File.createTempFile("imageToAdd","bmp");
                                            imageToAddRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                    Bitmap imageToAdd = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                    mainCourseImages.add(imageToAdd);
                                                }
                                            });
                                        }catch (IOException e){
                                            e.printStackTrace();
                                        }*/
                                       mainCourseImages.add(null);
                                    }else{
                                        mainCourseImages.add(null);
                                    }
                                }

                                for(DataSnapshot desert: desertsSnapshot.getChildren()){
                                    String desertName = desert.getKey();
                                    double price = Double.parseDouble(desert.child("Price").getValue(String.class));
                                    Boolean storeBought = Boolean.parseBoolean(desert.child("StoreBought").getValue(String.class));

                                    Deserts desertToAdd = new Deserts(price,desertName,storeBought);
                                    deserts.add(desertToAdd);

                                    Boolean hasImage = desert.child("hasImage").getValue(Boolean.class);

                                    if(hasImage){
                                        /*StorageReference imageToAddRef = imageReference.child(ItemType.DESERT.toString()).child(desertName + ".jpg");
                                        try{
                                            final File localFile = File.createTempFile("imageToAdd","bmp");
                                            imageToAddRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                    Bitmap imageToAdd = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                    desertsImages.add(imageToAdd);
                                                }
                                            });
                                        }catch (IOException e){
                                            e.printStackTrace();
                                        }*/
                                        desertsImages.add(null);
                                    }else{
                                        desertsImages.add(null);
                                    }
                                }

                                for(DataSnapshot drink: drinksSnapshot.getChildren()){
                                    String drinkName = drink.getKey();
                                    double price = Double.parseDouble(drink.child("Price").getValue(String.class));

                                    Drink drinkToAdd = new Drink(price,drinkName);
                                    drinks.add(drinkToAdd);

                                    Boolean hasImage = drink.child("hasImage").getValue(Boolean.class);

                                    if(hasImage){
                                       /* StorageReference imageToAddRef = imageReference.child(ItemType.DRINKS.toString()).child(drinkName + ".jpg");
                                        try{
                                            final File localFile = File.createTempFile("imageToAdd","bmp");
                                            imageToAddRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                    Bitmap imageToAdd = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                    drinksImages.add(imageToAdd);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    e.printStackTrace();
                                                }
                                            });
                                        }catch (IOException e) {
                                            e.printStackTrace();
                                        }*/
                                       drinksImages.add(null);
                                    }else{
                                        drinksImages.add(null);
                                    }
                                }

                                ViewMenuActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ListView starterLV = findViewById(R.id.starterListView);
                                        ListView mainCourseLV = findViewById(R.id.mainCourseListView);
                                        ListView desertLV = findViewById(R.id.desertListView);
                                        ListView drinkLV = findViewById(R.id.drinksListView);

                                        if(!starters.isEmpty()) {
                                            StarterAdapter starterAdapter = new StarterAdapter(context, R.layout.listview_item_row_starter, starters, starterImages);
                                            View starterHeader = getLayoutInflater().inflate(R.layout.gridview_header_row_starter, null);
                                            starterLV.addHeaderView(starterHeader);
                                            starterLV.setAdapter(starterAdapter);
                                        }

                                        if(!mainCourses.isEmpty()) {
                                            MainCourseAdapter mainCourseAdapter = new MainCourseAdapter(context, R.layout.listview_item_row_maincourse, mainCourses, mainCourseImages);
                                            View mainCourseHeader = getLayoutInflater().inflate(R.layout.gridview_header_row_maincourses, null);
                                            mainCourseLV.addHeaderView(mainCourseHeader);
                                            mainCourseLV.setAdapter(mainCourseAdapter);
                                        }

                                        if(!deserts.isEmpty()) {
                                            DesertAdapter desertAdapter = new DesertAdapter(context, R.layout.listview_item_row_desert, deserts, desertsImages);
                                            View desertHeader = getLayoutInflater().inflate(R.layout.gridview_header_row_deserts, null);
                                            desertLV.addHeaderView(desertHeader);
                                            desertLV.setAdapter(desertAdapter);
                                        }

                                        if(!drinks.isEmpty()) {
                                            DrinkAdapter drinkAdapter = new DrinkAdapter(context, R.layout.listview_item_row_drink, drinks, drinksImages);
                                            View drinkHeader = getLayoutInflater().inflate(R.layout.gridview_header_row_drinks, null);
                                            drinkLV.addHeaderView(drinkHeader);
                                            drinkLV.setAdapter(drinkAdapter);
                                        }

                                        if(!starters.isEmpty()){
                                            setDynamicHeight(starterLV);
                                        }

                                        if(!mainCourses.isEmpty()){
                                            setDynamicHeight(mainCourseLV);
                                        }

                                        if(!deserts.isEmpty()){
                                            setDynamicHeight(desertLV);
                                        }

                                        if(!drinks.isEmpty()){
                                            setDynamicHeight(drinkLV);
                                        }
                                    }
                                });
                            }else{
                                ViewMenuActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getApplicationContext());
                                        alertBuilder.setTitle("Menu hasn't been created");
                                        alertBuilder.setMessage("We have encountered an error while connecting to the " +
                                                "Firebase Database, please create a menu.");
                                        alertBuilder.setPositiveButton("Understood", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });
                                        alertBuilder.setCancelable(false);
                                        alertBuilder.create();
                                        alertBuilder.show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getApplicationContext());
                    alertBuilder.setTitle("Could not connect to Firebase");
                    alertBuilder.setMessage("We have encountered an error while connecting to the " +
                            "Firebase Database, please check internet conenction");
                    alertBuilder.setPositiveButton("Understood", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    alertBuilder.setCancelable(false);
                    alertBuilder.create();
                    alertBuilder.show();
                }
            }
        });
    }

    public static void setDynamicHeight(ListView listview){
        ListAdapter adapter = listview.getAdapter();

        if(adapter==null){
            return;
        }

        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listview.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for(int i =0;i<adapter.getCount();i++){
            View listItem = adapter.getView(i,null,listview);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listview.getLayoutParams();
        layoutParams.height = height + (listview.getDividerHeight() * (adapter.getCount() - 1));
        listview.setLayoutParams(layoutParams);
        listview.requestLayout();
    }
}
