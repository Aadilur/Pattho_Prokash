package com.pattho.prokash.patthoprokash.Activity.E_Library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pattho.prokash.patthoprokash.Activity.ChatTalkTo;
import com.pattho.prokash.patthoprokash.Activity.LandingActivity;
import com.pattho.prokash.patthoprokash.Activity.SignIn;
import com.pattho.prokash.patthoprokash.Activity.UserDetails;
import com.pattho.prokash.patthoprokash.Adapter.CategoryListView_Adapter;
import com.pattho.prokash.patthoprokash.Adapter.LibraryBookListAdapter;
import com.pattho.prokash.patthoprokash.Adapter.StoreSliderAdapter;
import com.pattho.prokash.patthoprokash.Model.AllBook_Model;
import com.pattho.prokash.patthoprokash.R;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LibraryHome extends AppCompatActivity {
    String phone = "+8801688901225";
    MaterialToolbar materialToolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;


    SliderView sliderView;
    StoreSliderAdapter sliderAdapter;
    List<String> sliderList;
    DatabaseReference sliderRef;

    LibraryBookListAdapter libraryBookListAdapter;
    RecyclerView recyclerView;
    List<AllBook_Model> booksModelList;
    DatabaseReference allBookRef;
    DatabaseReference secBook;


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    RecyclerView recyclerView1;
    List<AllBook_Model> list;
    int i;
    List<AllBook_Model> list1,list2,list3,list4,list5,list6,list7,list8;



    ExpandableListView expandableListView;
    List<String> categoryGroupList;
    HashMap<String, List<String>> categoryListItem;

    CategoryListView_Adapter categoryListViewAdapter;


    TextView title1, title2, title3, title4, title5, title6, title7, title8, titleAll;
    TextView view1, view2, view3, view4, view5, view6, view7, view8, viewAll;
    RecyclerView rv1, rv2, rv3, rv4, rv5, rv6, rv7, rv8;

    LinearLayout ln1, ln2, ln3, ln4, ln5, ln6, ln7, ln8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_home);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        UsesPermission();
        initData();


        expandableListView = findViewById(R.id.ExpandCategory);

        categoryGroupList = new ArrayList<>();
        categoryListItem = new HashMap<>();
        initExpandableListData();

        categoryListViewAdapter = new CategoryListView_Adapter(LibraryHome.this, categoryGroupList, categoryListItem);
        expandableListView.setAdapter(categoryListViewAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight(parent, groupPosition);

                return false;
            }
        });



        UserAuth();
        onClickData();
        databaseFunction();

    }

    private void setListViewHeight(ExpandableListView listView, int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight()+2;

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void initExpandableListData() {



        categoryGroupList.add(getString(R.string.category_expandable_list_view));

        String[] listArray;
        listArray = getResources().getStringArray(R.array.category_expandable_list_view);
        List<String> dataList = new ArrayList<>(Arrays.asList(listArray));

        categoryListItem.put(categoryGroupList.get(0), dataList);

    }


    private void databaseFunction() {

        sliderRef = FirebaseDatabase.getInstance().getReference().child("StoreSlider");
        sliderList = new ArrayList<>();

        sliderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    sliderList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        sliderList.add(dataSnapshot.getValue().toString());
                    }

                    if (sliderList.size() == 0) {
                        sliderView.setVisibility(View.GONE);
                    } else sliderView.setVisibility(View.VISIBLE);

                    sliderAdapter = new StoreSliderAdapter(LibraryHome.this, sliderList);
                    sliderView.setSliderAdapter(sliderAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//---------------------------------------------------------------------------------

        allBookRef = FirebaseDatabase.getInstance().getReference().child("Books");
        recyclerView = findViewById(R.id.AllBook_RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        int spanCount = 3; // 3 columns
        int spacing = 30; // 50px
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new RecyclerView_Deco(spanCount, spacing, includeEdge));

        recyclerView.setLayoutManager(gridLayoutManager);
        booksModelList = new ArrayList<>();

        allBookRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                booksModelList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.child("inLibrary").getValue().equals("yes")) {
                            AllBook_Model allBook_model = dataSnapshot.getValue(AllBook_Model.class);

                            booksModelList.add(allBook_model);
                        }
                    }
                }

                libraryBookListAdapter = new LibraryBookListAdapter(LibraryHome.this, booksModelList);
                recyclerView.setAdapter(libraryBookListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//---------------------------------------------------------------------------------

        secBook = FirebaseDatabase.getInstance().getReference().child("Library").child("sec1");
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 3);

        int spanCount1 = 3; // 3 columns
        int spacing1 = 30; // 30px
        boolean includeEdge1 = true;
        rv1.addItemDecoration(new RecyclerView_Deco(spanCount1, spacing1, includeEdge1));
        rv1.setLayoutManager(gridLayoutManager1);

        List<String> bid = new ArrayList<>();
        list1 = new ArrayList<>();

        secBook.child("bid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1.clear();
                bid.clear();
                if (snapshot.exists()) {
                    ln1.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        bid.add(dataSnapshot.getValue().toString());
                    }
                    allBookRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list1.clear();
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    System.out.println(bid.contains(dataSnapshot.child("id").getValue().toString()));
                                    if (bid.contains(dataSnapshot.child("id").getValue().toString())) {
                                        AllBook_Model allBook_model = dataSnapshot.getValue(AllBook_Model.class);
                                        list1.add(allBook_model);
                                    }
                                }

                                libraryBookListAdapter = new LibraryBookListAdapter(LibraryHome.this, list1);
                                rv1.setAdapter(libraryBookListAdapter);

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }else {
                    ln1.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

//---------------------------------------------------------------------------------

//---------------------------------------------------------------------------------

        secBook = FirebaseDatabase.getInstance().getReference().child("Library").child("sec2");
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 3);


        rv2.addItemDecoration(new RecyclerView_Deco(spanCount1, spacing1, includeEdge1));
        rv2.setLayoutManager(gridLayoutManager2);

        List<String> bid2 = new ArrayList<>();
        list2 = new ArrayList<>();

        secBook.child("bid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bid2.clear();
                if (snapshot.exists()) {
                    ln2.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        bid2.add(dataSnapshot.getValue().toString());
                        System.out.println("-----------------------------"+bid2);
                    }
                    allBookRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list2.clear();
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    System.out.println(bid2.contains(dataSnapshot.child("id").getValue().toString()));
                                    if (bid2.contains(dataSnapshot.child("id").getValue().toString())) {
                                        AllBook_Model allBook_model = dataSnapshot.getValue(AllBook_Model.class);
                                        list2.add(allBook_model);
                                    }
                                }

                                libraryBookListAdapter = new LibraryBookListAdapter(LibraryHome.this, list2);
                                rv2.setAdapter(libraryBookListAdapter);

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }else {
                    ln2.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

//---------------------------------------------------------------------------------
//---------------------------------------------------------------------------------

        secBook = FirebaseDatabase.getInstance().getReference().child("Library").child("sec3");
        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(this, 3);


        rv3.addItemDecoration(new RecyclerView_Deco(spanCount1, spacing1, includeEdge1));
        rv3.setLayoutManager(gridLayoutManager3);

        List<String> bid3 = new ArrayList<>();
        list3 = new ArrayList<>();

        secBook.child("bid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bid3.clear();
                if (snapshot.exists()) {
                    ln3.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        bid3.add(dataSnapshot.getValue().toString());
                        System.out.println("-----------------------------"+bid3);
                    }
                    allBookRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list3.clear();
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    System.out.println(bid3.contains(dataSnapshot.child("id").getValue().toString()));
                                    if (bid3.contains(dataSnapshot.child("id").getValue().toString())) {
                                        AllBook_Model allBook_model = dataSnapshot.getValue(AllBook_Model.class);
                                        list3.add(allBook_model);
                                    }
                                }

                                libraryBookListAdapter = new LibraryBookListAdapter(LibraryHome.this, list3);
                                rv3.setAdapter(libraryBookListAdapter);

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }else {
                    ln3.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
//---------------------------------------------------------------------------------
//---------------------------------------------------------------------------------

        secBook = FirebaseDatabase.getInstance().getReference().child("Library").child("sec4");
        GridLayoutManager gridLayoutManager4 = new GridLayoutManager(this, 3);


        rv4.addItemDecoration(new RecyclerView_Deco(spanCount1, spacing1, includeEdge1));
        rv4.setLayoutManager(gridLayoutManager4);

        List<String> bid4 = new ArrayList<>();
        list4 = new ArrayList<>();

        secBook.child("bid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bid4.clear();
                if (snapshot.exists()) {
                    ln4.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        bid4.add(dataSnapshot.getValue().toString());
                        System.out.println("-----------------------------"+bid4);
                    }
                    allBookRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list4.clear();
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    System.out.println(bid4.contains(dataSnapshot.child("id").getValue().toString()));
                                    if (bid4.contains(dataSnapshot.child("id").getValue().toString())) {
                                        AllBook_Model allBook_model = dataSnapshot.getValue(AllBook_Model.class);
                                        list4.add(allBook_model);
                                    }
                                }

                                libraryBookListAdapter = new LibraryBookListAdapter(LibraryHome.this, list4);
                                rv4.setAdapter(libraryBookListAdapter);

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }else {
                    ln4.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
//---------------------------------------------------------------------------------
//---------------------------------------------------------------------------------




    }


    private void onClickData() {





        //------------------------------------------------------------------------------------------------------------
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });


        //------------------------------------------------------------------------------------------------------------

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LibraryViewAllBooks.class);
                intent.putExtra("name","e-Library Section 1");
                startActivity(intent);
            }
        });


        //------------------------------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------------------------------

        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LibraryViewAllBooks.class);
                intent.putExtra("name","e-Library Section 2");
                startActivity(intent);
            }
        });


        //------------------------------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------------------------------

        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LibraryViewAllBooks.class);
                intent.putExtra("name","e-Library Section 3");
                startActivity(intent);
            }
        });


        //------------------------------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------------------------------

        view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LibraryViewAllBooks.class);
                intent.putExtra("name","e-Library Section 4");
                startActivity(intent);
            }
        });




        //------------------------------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------------------------------

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LibraryViewAllBooks.class);
                intent.putExtra("name","e-Library Section All");
                startActivity(intent);
            }
        });


        //------------------------------------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------------------------
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });

        //------------------------------------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------------------------

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.store_toolbar,menu);

        View actionView = menu.findItem(R.id.store_cart_icon).getActionView();
        TextView tv = actionView.findViewById(R.id.itemCount);
        if (firebaseUser!=null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("LibraryCart").child(firebaseUser.getUid()).child("books");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int tBookCount = (int) snapshot.getChildrenCount();
                    if (tBookCount > 0 && tBookCount < 10) {
                        tv.setText(tBookCount + "");
                    } else if (tBookCount > 9) {
                        tv.setText("9+");
                    } else tv.setText("0");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LibraryHome.this, LibraryCart.class);
                startActivity(intent);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.user_icon) {
            drawerLayout.openDrawer(GravityCompat.END);
            return true;
        }  if (item.getItemId() == R.id.store_cart_icon) {
            Intent intent = new Intent(LibraryHome.this, LibraryCart.class);
            startActivity(intent);
            return true;
        }
        return true;
    }

    @SuppressLint("SetTextI18n")
    private void initData() {

        //------------------------------------------------------------------------------------

        titleAll = findViewById(R.id.titleAll);
        title1 = findViewById(R.id.sec1);
        title2 = findViewById(R.id.sec2);
        title3 = findViewById(R.id.sec3);
        title4 = findViewById(R.id.sec4);

        viewAll = findViewById(R.id.viewAll);
        view1 = findViewById(R.id.viewAll1);
        view2 = findViewById(R.id.viewAll2);
        view3 = findViewById(R.id.viewAll3);
        view4 = findViewById(R.id.viewAll4);

        rv1 = findViewById(R.id.sec1_rv);
        rv2 = findViewById(R.id.sec2_rv);
        rv3 = findViewById(R.id.sec3_rv);
        rv4 = findViewById(R.id.sec4_rv);

        ln1 = findViewById(R.id.ln1);
        ln2 = findViewById(R.id.ln2);
        ln3 = findViewById(R.id.ln3);
        ln4 = findViewById(R.id.ln4);


        //------------------------------------------------------------------------------------



        sliderView = findViewById(R.id.imageSlider);


        sliderAdapter = new StoreSliderAdapter(this);
        sliderView.setSliderAdapter(sliderAdapter);

        sliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);

        sliderView.setScrollTimeInSec(6);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        TextView textView = findViewById(R.id.verifyText2);
        if (firebaseUser != null ) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText("Please sign in to order books.");
        }

        materialToolbar = findViewById(R.id.materialToolbar);
        materialToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(materialToolbar);
        getSupportActionBar().setTitle("পাঠ্য ই-লাইব্রের");

        drawerLayout = findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, materialToolbar, R.string.app_name, R.string.app_name);
        toggle.getDrawerArrowDrawable().setColor(Color.parseColor("#ffffff"));
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view2);
        navigationView.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#ffffff")));


        if (firebaseUser != null ) {
            navigationView.getMenu().setGroupVisible(R.id.drawer_menu_anonimas, false);
            navigationView.getMenu().setGroupVisible(R.id.drawer_menu_loggedIn, true);
        } else {

            navigationView.getMenu().setGroupVisible(R.id.drawer_menu_anonimas, true);
            navigationView.getMenu().setGroupVisible(R.id.drawer_menu_loggedIn, false);
        }

    }


    private void UserAuth() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.store_menu_login_0:

                        Toast.makeText(LibraryHome.this, "hola", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LibraryHome.this, SignIn.class);
                        startActivity(intent);
                        return true;

                    case R.id.store_menu_logout_1:
                        new AlertDialog.Builder(LibraryHome.this,R.style.AlertDialogStyle)
                                .setTitle("Log Out!")
                                .setMessage(Html.fromHtml("</br><p style=\"color:#cfd8dc\">Are you sure you want to Log Out?</p>"))
                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        firebaseAuth.signOut();
                                        Intent intent2 = new Intent(LibraryHome.this, SignIn.class);
                                        startActivity(intent2);
                                        finish();


                                    }
                                })
                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton("No", null)
                                .show();



                        return true;

                    case R.id.store_menu_about_us_0:
                    case R.id.store_menu_my_order_1:
//                        Intent intent3 = new Intent(StoreHome.this, Author.class);
//                        startActivity(intent3);
//                        finish();
                        return true;
                    case R.id.store_menu_my_account_1:

                        Intent intent4 = new Intent(LibraryHome.this, UserDetails.class);
                        startActivity(intent4);
                        return true;

                }
                return true;
            }
        });

    }
    public  void toPhone(View view){
        try {

//            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
//            startActivity(intent);

            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(intent);

        }catch (Exception ignored){

        }
    }

    public void toMessenger(View view) {

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("fb-messenger://user-thread/103807988159973"));
//        i.setPackage("com.facebook.orca");



        try {
            startActivity(i);
        } catch (Exception ex) {
            Toast.makeText(this, "Please Install Facebook Messenger",    Toast.LENGTH_LONG).show();
        }
    }

    public void toWhatapp(View view) {

        try {
            Intent in = new Intent(Intent.ACTION_VIEW);
            in.setData(Uri.parse("https://api.whatsapp.com/send?phone=8801688901225&text=Hello%20*Pattho Prokash*, I want info."));
            in.setPackage("com.whatsapp");
            startActivity(in);
        } catch (Exception e) {
            Toast.makeText(this, "Whatsapp Not Installed", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LandingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void UsesPermission() {
        Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.CALL_PHONE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                }
                if (report.isAnyPermissionPermanentlyDenied()) {
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }


    public void allBook(View view) {
        Intent intent = new Intent(getApplicationContext(), LibraryViewAllBooks.class);
        intent.putExtra("name","e-Library Section All");
        startActivity(intent);
    }

    public void chatWebView(View view) {
        startActivity(new Intent(this, ChatTalkTo.class));
    }
}



















class RecyclerView_Deco extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public RecyclerView_Deco(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
    }

}