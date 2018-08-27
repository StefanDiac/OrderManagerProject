package com.example.air_book.ordermanagerproject.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.air_book.ordermanagerproject.Model.Deserts;
import com.example.air_book.ordermanagerproject.Model.Drink;
import com.example.air_book.ordermanagerproject.Model.ItemType;
import com.example.air_book.ordermanagerproject.Model.MainCourse;
import com.example.air_book.ordermanagerproject.Model.Starter;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddMenuItemActivity extends AppCompatActivity {

    boolean hasName = false;
    boolean hasPrice = false;
    private FirebaseAuth mAuth;
    DatabaseReference database;
    String UID;
    ProgressBar progressBar;
    public static int PICK_IMAGE = 1;
    boolean hasImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        UID = bundle.getString("UID");
        progressBar = findViewById(R.id.firebasePB);
        progressBar.setMax(0);
        progressBar.setProgress(0);



        final Spinner itemTypeSpinner = (Spinner) findViewById(R.id.itemTypeSpinner);

        itemTypeSpinner.setAdapter(new ArrayAdapter<ItemType>(this, android.R.layout.simple_list_item_1, ItemType.values()));

        itemTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((EditText)findViewById(R.id.priceTB)).setText("");
                ((EditText)findViewById(R.id.itemNameTB)).setText("");

                switch(position) {
                    case 0:
                        findViewById(R.id.isStoreBoughtRadio).setVisibility(View.INVISIBLE);
                        findViewById(R.id.coldStarterCB).setVisibility(View.VISIBLE);
                        findViewById(R.id.spiceLevelTV).setVisibility(View.INVISIBLE);
                        findViewById(R.id.spiceRating).setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        findViewById(R.id.isStoreBoughtRadio).setVisibility(View.INVISIBLE);
                        findViewById(R.id.coldStarterCB).setVisibility(View.INVISIBLE);
                        findViewById(R.id.spiceLevelTV).setVisibility(View.VISIBLE);
                        findViewById(R.id.spiceRating).setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        findViewById(R.id.isStoreBoughtRadio).setVisibility(View.VISIBLE);
                        findViewById(R.id.coldStarterCB).setVisibility(View.INVISIBLE);
                        findViewById(R.id.spiceLevelTV).setVisibility(View.INVISIBLE);
                        findViewById(R.id.spiceRating).setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        findViewById(R.id.isStoreBoughtRadio).setVisibility(View.INVISIBLE);
                        findViewById(R.id.coldStarterCB).setVisibility(View.INVISIBLE);
                        findViewById(R.id.spiceLevelTV).setVisibility(View.INVISIBLE);
                        findViewById(R.id.spiceRating).setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do something?
            }
        });

        (findViewById(R.id.itemNameTB)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText = (EditText) v;

                if(editText.getText().toString() != "") {
                    hasName = true;
                } else {
                    hasName = false;
                }

                if(hasName&&hasPrice) {
                    (findViewById(R.id.addMenuItemBtn)).setEnabled(true);
                }
            }
        });

        (findViewById(R.id.priceTB)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText = (EditText) v;

                if(editText.getText().toString() != "") {
                    hasPrice = true;
                } else {
                    hasPrice = false;
                }

                if(hasName&&hasPrice) {
                    (findViewById(R.id.addMenuItemBtn)).setEnabled(true);
                }
            }
        });

        ((EditText)findViewById(R.id.priceTB)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                EditText editText = (EditText) v;

                if(editText.getText().toString() != "") {
                    hasPrice = true;
                } else {
                    hasPrice = false;
                }

                if(actionId== EditorInfo.IME_ACTION_DONE) {
                    if(hasName&&hasPrice) {
                        (findViewById(R.id.addMenuItemBtn)).setEnabled(true);
                    }
                }
                return false;
            }
        });

        (findViewById(R.id.addMenuItemBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasName && hasPrice) {
                    mAuth = FirebaseAuth.getInstance();

                    mAuth.signInAnonymously().addOnCompleteListener(AddMenuItemActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();

                                database = FirebaseDatabase.getInstance().getReference();

                                AddMenuItemActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.VISIBLE);
                                        progressBar.setMax(100);
                                        progressBar.setProgress(0);
                                    }
                                });

                                Thread thread = new Thread() {
                                    public void run() {
                                        while(progressBar.getProgress() < 100) {
                                        }
                                        AddMenuItemActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressBar.setVisibility(View.INVISIBLE);
                                            }
                                        });
                                    }
                                };
                                thread.start();

                                database.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        FirebaseStorage storage = FirebaseStorage.getInstance();
                                        StorageReference storageReference = storage.getReference();

                                        StorageReference userReference = storageReference.child(UID);
                                        StorageReference imageReference = userReference.child("images");


                                        updateProgressBar(25);
                                        if(dataSnapshot.child("Users").hasChild(UID)){
                                            updateProgressBar(30);
                                            ItemType type = ItemType.values()[((Spinner)findViewById(R.id.itemTypeSpinner)).getSelectedItemPosition()];
                                            updateProgressBar(50);
                                            addToDatabaseItem(database,type,UID, progressBar, imageReference);
                                        } else {
                                            createEntity(database,UID);
                                            updateProgressBar(40);
                                            ItemType type = ItemType.values()[((Spinner)findViewById(R.id.itemTypeSpinner)).getSelectedItemPosition()];
                                            updateProgressBar(60);
                                            addToDatabaseItem(database,type,UID, progressBar, imageReference);
                                        }

                                        AddMenuItemActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            } else {
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
            }
        });

    }

    public void addToDatabaseItem(DatabaseReference database, ItemType type, String UID, ProgressBar progressBar, StorageReference userImagesReference) {

        String itemName = ((EditText)findViewById(R.id.itemNameTB)).getText().toString();
        Double price = Double.parseDouble(((EditText)findViewById(R.id.priceTB)).getText().toString());
        updateProgressBar(75);

        switch(type) {
            case STARTER_DISH:
                Starter starter = new Starter(itemName,price,((CheckBox)findViewById(R.id.coldStarterCB)).isChecked());
                database.child("Users").child(UID).child("Menu").child(ItemType.STARTER_DISH.toString()).child(starter.getName())
                        .setValue(starter.getStarterValues());
                StorageReference starterReference = userImagesReference.child(ItemType.STARTER_DISH.toString()).child(starter.getName());
                if (hasImage) {
                    database.child("Users").child(UID).child("Menu").child(ItemType.STARTER_DISH.toString()).child(starter.getName())
                            .child("hasImage").setValue(true);
                    uploadImage(starterReference);
                } else {
                    database.child("Users").child(UID).child("Menu").child(ItemType.STARTER_DISH.toString()).child(starter.getName())
                            .child("hasImage").setValue(false);
                }
                break;
            case MAIN_COURSE:
                MainCourse mainCourse = new MainCourse(itemName,price,((RatingBar)findViewById(R.id.spiceRating)).getNumStars()*2);
                database.child("Users").child(UID).child("Menu").child(ItemType.MAIN_COURSE.toString()).child(mainCourse.getName())
                        .setValue(mainCourse.getCourseValues());
                StorageReference mainCourseReference = userImagesReference.child(ItemType.MAIN_COURSE.toString()).child(mainCourse.getName());
                if(hasImage) {
                    database.child("Users").child(UID).child("Menu").child(ItemType.MAIN_COURSE.toString()).child(mainCourse.getName())
                            .child("hasImage").setValue(true);
                    uploadImage(mainCourseReference);
                } else {
                    database.child("Users").child(UID).child("Menu").child(ItemType.MAIN_COURSE.toString()).child(mainCourse.getName())
                            .child("hasImage").setValue(false);
                }
                break;
            case DESERT:
                Deserts desert = new Deserts(price,itemName,((RadioButton)findViewById(R.id.isStoreBoughtRadio)).isChecked());
                database.child("Users").child(UID).child("Menu").child(ItemType.DESERT.toString()).child(desert.getName())
                        .setValue(desert.getDesertValues());
                StorageReference desertsReference = userImagesReference.child(ItemType.DESERT.toString()).child(desert.getName());
                if (hasImage) {
                    database.child("Users").child(UID).child("Menu").child(ItemType.DESERT.toString()).child(desert.getName())
                            .child("hasImage").setValue(true);
                    uploadImage(desertsReference);
                } else {
                    database.child("Users").child(UID).child("Menu").child(ItemType.DESERT.toString()).child(desert.getName())
                            .child("hasImage").setValue(false);
                }

                break;
            case DRINKS:
                Drink drink = new Drink(price, itemName);
                database.child("Users").child(UID).child("Menu").child(ItemType.DRINKS.toString()).child(drink.getName())
                        .setValue(drink.getDrinkValues());
                StorageReference drinksReference = userImagesReference.child(ItemType.DRINKS.toString()).child(drink.getName());
                if (hasImage) {
                    database.child("Users").child(UID).child("Menu").child(ItemType.DRINKS.toString()).child(drink.getName())
                            .child("hasImage").setValue(true);
                    uploadImage(drinksReference);
                } else {
                    database.child("Users").child(UID).child("Menu").child(ItemType.DRINKS.toString()).child(drink.getName())
                            .child("hasImage").setValue(false);
                }
                break;
        }
        updateProgressBar(100);


    }

    public void uploadImage(StorageReference typeFolderRef) {
        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setDrawingCacheEnabled(true);
        imageButton.buildDrawingCache();
        Bitmap bitmap = imageButton.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = typeFolderRef.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("onFailure","Failed to upload");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("onSuccess","Uploaded");
            }
        });
    }

    public void createEntity(DatabaseReference database, String UID) {
        database.child("Users").child(UID).setValue("Menu");

    }

    public void updateProgressBar(final int value) {
        AddMenuItemActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(value);
            }
        });
    }

    public void onAddPhotoClick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent,"Select Display Photo"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK)
                if (data != null) {
                    try {
                        Bitmap image = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                        ((ImageButton)findViewById(R.id.imageButton)).setImageBitmap(image);
                        hasImage = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}
