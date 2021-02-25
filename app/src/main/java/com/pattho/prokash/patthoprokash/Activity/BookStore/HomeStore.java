package com.pattho.prokash.patthoprokash.Activity.BookStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.pattho.prokash.patthoprokash.Activity.LandingActivity;
import com.pattho.prokash.patthoprokash.Activity.SignIn;
import com.pattho.prokash.patthoprokash.Activity.UserDetails;
import com.pattho.prokash.patthoprokash.Adapter.CategoryListView_Adapter;
import com.pattho.prokash.patthoprokash.Adapter.StoreBookListAdapter;
import com.pattho.prokash.patthoprokash.Adapter.StoreSliderAdapter;
import com.pattho.prokash.patthoprokash.Model.AllBook_Model;
import com.pattho.prokash.patthoprokash.R;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HomeStore extends AppCompatActivity {


    MaterialToolbar materialToolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;


    SliderView sliderView;
    StoreSliderAdapter sliderAdapter;
    List<String> sliderList;
    DatabaseReference sliderRef;

    StoreBookListAdapter storeBookListAdapter;
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


    private static final String[] paths = {"item 1", "item 2", "item 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_store);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        UsesPermission();
        initData();


        expandableListView = findViewById(R.id.ExpandCategory);

        categoryGroupList = new ArrayList<>();
        categoryListItem = new HashMap<>();
        initExpandableListData();

        categoryListViewAdapter = new CategoryListView_Adapter(HomeStore.this, categoryGroupList, categoryListItem);
        expandableListView.setAdapter(categoryListViewAdapter);


        UserAuth();
        onClickData();
        databaseFunction();

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

                    sliderAdapter = new StoreSliderAdapter(HomeStore.this, sliderList);
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
                        if (dataSnapshot.child("inStore").getValue().equals("yes")) {
                            AllBook_Model allBook_model = dataSnapshot.getValue(AllBook_Model.class);

                            booksModelList.add(allBook_model);
                        }
                    }
                }

                storeBookListAdapter = new StoreBookListAdapter(HomeStore.this, booksModelList);
                recyclerView.setAdapter(storeBookListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//---------------------------------------------------------------------------------

            secBook = FirebaseDatabase.getInstance().getReference().child("Store").child("sec1");
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

                                    storeBookListAdapter = new StoreBookListAdapter(HomeStore.this, list1);
                                    rv1.setAdapter(storeBookListAdapter);

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

        secBook = FirebaseDatabase.getInstance().getReference().child("Store").child("sec2");
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

                                storeBookListAdapter = new StoreBookListAdapter(HomeStore.this, list2);
                                rv2.setAdapter(storeBookListAdapter);

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

        secBook = FirebaseDatabase.getInstance().getReference().child("Store").child("sec3");
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

                                storeBookListAdapter = new StoreBookListAdapter(HomeStore.this, list3);
                                rv3.setAdapter(storeBookListAdapter);

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

        secBook = FirebaseDatabase.getInstance().getReference().child("Store").child("sec4");
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

                                storeBookListAdapter = new StoreBookListAdapter(HomeStore.this, list4);
                                rv4.setAdapter(storeBookListAdapter);

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

        secBook = FirebaseDatabase.getInstance().getReference().child("Store").child("sec5");
        GridLayoutManager gridLayoutManager5 = new GridLayoutManager(this, 3);


        rv5.addItemDecoration(new RecyclerView_Deco(spanCount1, spacing1, includeEdge1));
        rv5.setLayoutManager(gridLayoutManager5);

        List<String> bid5 = new ArrayList<>();
        list5 = new ArrayList<>();

        secBook.child("bid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bid5.clear();
                if (snapshot.exists()) {
                    ln5.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        bid5.add(dataSnapshot.getValue().toString());
                        System.out.println("-----------------------------"+bid5);
                    }
                    allBookRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list5.clear();
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    System.out.println(bid5.contains(dataSnapshot.child("id").getValue().toString()));
                                    if (bid5.contains(dataSnapshot.child("id").getValue().toString())) {
                                        AllBook_Model allBook_model = dataSnapshot.getValue(AllBook_Model.class);
                                        list5.add(allBook_model);
                                    }
                                }

                                storeBookListAdapter = new StoreBookListAdapter(HomeStore.this, list5);
                                rv5.setAdapter(storeBookListAdapter);

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }else {
                    ln5.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
//---------------------------------------------------------------------------------
//---------------------------------------------------------------------------------

        secBook = FirebaseDatabase.getInstance().getReference().child("Store").child("sec6");
        GridLayoutManager gridLayoutManager6 = new GridLayoutManager(this, 3);


        rv6.addItemDecoration(new RecyclerView_Deco(spanCount1, spacing1, includeEdge1));
        rv6.setLayoutManager(gridLayoutManager6);

        List<String> bid6 = new ArrayList<>();
        list6 = new ArrayList<>();

        secBook.child("bid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bid6.clear();
                if (snapshot.exists()) {
                    ln6.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        bid6.add(dataSnapshot.getValue().toString());
                        System.out.println("-----------------------------"+bid6);
                    }
                    allBookRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list6.clear();
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    System.out.println(bid6.contains(dataSnapshot.child("id").getValue().toString()));
                                    if (bid6.contains(dataSnapshot.child("id").getValue().toString())) {
                                        AllBook_Model allBook_model = dataSnapshot.getValue(AllBook_Model.class);
                                        list6.add(allBook_model);
                                    }
                                }

                                storeBookListAdapter = new StoreBookListAdapter(HomeStore.this, list6);
                                rv6.setAdapter(storeBookListAdapter);

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }else {
                    ln6.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
//---------------------------------------------------------------------------------
//---------------------------------------------------------------------------------

        secBook = FirebaseDatabase.getInstance().getReference().child("Store").child("sec7");
        GridLayoutManager gridLayoutManager7 = new GridLayoutManager(this, 3);


        rv7.addItemDecoration(new RecyclerView_Deco(spanCount1, spacing1, includeEdge1));
        rv7.setLayoutManager(gridLayoutManager7);

        List<String> bid7 = new ArrayList<>();
        list7 = new ArrayList<>();

        secBook.child("bid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bid7.clear();
                if (snapshot.exists()) {

                    ln7.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        bid7.add(dataSnapshot.getValue().toString());
                        System.out.println("-----------------------------"+bid7);
                    }
                    allBookRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list7.clear();
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    System.out.println(bid7.contains(dataSnapshot.child("id").getValue().toString()));
                                    if (bid7.contains(dataSnapshot.child("id").getValue().toString())) {
                                        AllBook_Model allBook_model = dataSnapshot.getValue(AllBook_Model.class);
                                        list7.add(allBook_model);
                                    }
                                }

                                storeBookListAdapter = new StoreBookListAdapter(HomeStore.this, list7);
                                rv7.setAdapter(storeBookListAdapter);

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }else {
                    ln7.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
//---------------------------------------------------------------------------------
//---------------------------------------------------------------------------------

        secBook = FirebaseDatabase.getInstance().getReference().child("Store").child("sec8");
        GridLayoutManager gridLayoutManager8 = new GridLayoutManager(this, 3);


        rv8.addItemDecoration(new RecyclerView_Deco(spanCount1, spacing1, includeEdge1));
        rv8.setLayoutManager(gridLayoutManager8);

        List<String> bid8 = new ArrayList<>();
        list8 = new ArrayList<>();

        secBook.child("bid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bid8.clear();
                if (snapshot.exists()) {
                    ln8.setVisibility(View.VISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        bid8.add(dataSnapshot.getValue().toString());
                        System.out.println("-----------------------------"+bid8);
                    }
                    allBookRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list8.clear();
                            if (snapshot.exists()) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    System.out.println(bid8.contains(dataSnapshot.child("id").getValue().toString()));
                                    if (bid8.contains(dataSnapshot.child("id").getValue().toString())) {
                                        AllBook_Model allBook_model = dataSnapshot.getValue(AllBook_Model.class);
                                        list8.add(allBook_model);
                                    }
                                }

                                storeBookListAdapter = new StoreBookListAdapter(HomeStore.this, list8);
                                rv8.setAdapter(storeBookListAdapter);

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }else {
                    ln8.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        //------------------------------------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------------------------





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
                Intent intent = new Intent(getApplicationContext(),ViewAllBooks.class);
                intent.putExtra("name","Store Section 1");
                startActivity(intent);
            }
        });


        //------------------------------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------------------------------

        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ViewAllBooks.class);
                intent.putExtra("name","Store Section 2");
                startActivity(intent);
            }
        });


        //------------------------------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------------------------------

        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ViewAllBooks.class);
                intent.putExtra("name","Store Section 3");
                startActivity(intent);
            }
        });


        //------------------------------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------------------------------

        view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ViewAllBooks.class);
                intent.putExtra("name","Store Section 4");
                startActivity(intent);
            }
        });


        //------------------------------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------------------------------

        view5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ViewAllBooks.class);
                intent.putExtra("name","Store Section 5");
                startActivity(intent);
            }
        });


        //------------------------------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------------------------------

        view6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ViewAllBooks.class);
                intent.putExtra("name","Store Section 6");
                startActivity(intent);
            }
        });


        //------------------------------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------------------------------

        view7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ViewAllBooks.class);
                intent.putExtra("name","Store Section 7");
                startActivity(intent);
            }
        });


        //------------------------------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------------------------------

        view8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ViewAllBooks.class);
                intent.putExtra("name","Store Section 8");
                startActivity(intent);
            }
        });


        //------------------------------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------------------------------

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ViewAllBooks.class);
                intent.putExtra("name","Store Section All");
                startActivity(intent);
            }
        });


        //------------------------------------------------------------------------------------------------------------

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.store_toolbar,menu);

        View actionView = menu.findItem(R.id.cart_icon).getActionView();
        TextView tv = actionView.findViewById(R.id.itemCount);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart").child(firebaseUser.getUid()).child("books");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int tBookCount = (int) snapshot.getChildrenCount();
                if (tBookCount>0 && tBookCount<10) {
                    tv.setText(tBookCount + "");
                }else if (tBookCount >9){
                    tv.setText("9+");
                }else tv.setText("0");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeStore.this,ViewStoreCart.class);
                startActivity(intent);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings_icon) {
            drawerLayout.openDrawer(GravityCompat.END);
            return true;
        }  if (item.getItemId() == R.id.cart_icon) {
            Intent intent = new Intent(HomeStore.this,ViewStoreCart.class);
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
        title5 = findViewById(R.id.sec5);
        title6 = findViewById(R.id.sec6);
        title7 = findViewById(R.id.sec7);
        title8 = findViewById(R.id.sec8);

        viewAll = findViewById(R.id.viewAll);
        view1 = findViewById(R.id.viewAll1);
        view2 = findViewById(R.id.viewAll2);
        view3 = findViewById(R.id.viewAll3);
        view4 = findViewById(R.id.viewAll4);
        view5 = findViewById(R.id.viewAll5);
        view6 = findViewById(R.id.viewAll6);
        view7 = findViewById(R.id.viewAll7);
        view8 = findViewById(R.id.viewAll8);

        rv1 = findViewById(R.id.sec1_rv);
        rv2 = findViewById(R.id.sec2_rv);
        rv3 = findViewById(R.id.sec3_rv);
        rv4 = findViewById(R.id.sec4_rv);
        rv5 = findViewById(R.id.sec5_rv);
        rv6 = findViewById(R.id.sec6_rv);
        rv7 = findViewById(R.id.sec7_rv);
        rv8 = findViewById(R.id.sec8_rv);

        ln1 = findViewById(R.id.ln1);
        ln2 = findViewById(R.id.ln2);
        ln3 = findViewById(R.id.ln3);
        ln4 = findViewById(R.id.ln4);
        ln5 = findViewById(R.id.ln5);
        ln6 = findViewById(R.id.ln6);
        ln7 = findViewById(R.id.ln7);
        ln8 = findViewById(R.id.ln8);


        //------------------------------------------------------------------------------------



        sliderView = findViewById(R.id.imageSlider);


        sliderAdapter = new StoreSliderAdapter(this);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
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
        getSupportActionBar().setTitle("পাঠ্য প্রকাশ");

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
                    case R.id.drawer_menu_login:

                        Toast.makeText(HomeStore.this, "hola", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(HomeStore.this, SignIn.class);
                        startActivity(intent);
                        return true;

                    case R.id.drawer_menu_logout:
                        Toast.makeText(HomeStore.this, "hola", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        Intent intent2 = new Intent(HomeStore.this, SignIn.class);
                        startActivity(intent2);
                        finish();
                        return true;

                    case R.id.drawer_menu_author_d1:
                    case R.id.drawer_menu_author_d2:
//                        Intent intent3 = new Intent(HomeStore.this, Author.class);
//                        startActivity(intent3);
//                        finish();
                        return true;
                    case R.id.drawer_menu_user:

                        Intent intent4 = new Intent(HomeStore.this, UserDetails.class);
                        startActivity(intent4);
                        return true;

                }
                return true;
            }
        });

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
        Intent intent = new Intent(getApplicationContext(),ViewAllBooks.class);
        intent.putExtra("name","Store Section All");
        startActivity(intent);
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