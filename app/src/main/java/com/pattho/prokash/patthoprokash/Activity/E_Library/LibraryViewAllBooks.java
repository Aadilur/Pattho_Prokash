package com.pattho.prokash.patthoprokash.Activity.E_Library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pattho.prokash.patthoprokash.Adapter.LibraryViewAll_Adapter;
import com.pattho.prokash.patthoprokash.Model.AllBook_Model;
import com.pattho.prokash.patthoprokash.R;

import java.util.ArrayList;
import java.util.List;

public class LibraryViewAllBooks extends AppCompatActivity {


    SearchView searchView;
    RecyclerView recyclerView;

    String dir, dir2, dir3,dir4;
    DatabaseReference allBookRef;
    DatabaseReference secBook;

    LibraryViewAll_Adapter adapter;

    List<AllBook_Model> booksModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_view_all_books);

        initData();

        databaseFunction();


    }





    private void databaseFunction() {



        allBookRef = FirebaseDatabase.getInstance().getReference().child("Books");

        LinearLayoutManager layoutManager = new LinearLayoutManager(LibraryViewAllBooks.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(LibraryViewAllBooks.this, 3);

        recyclerView.addItemDecoration(new LibraryViewAllBooks_RecyclerViewDeco(3, -30, true));
        int spanCount = 3; // 3 columns
        int spacing = 30; // 50px
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new LibraryViewAllBooks_RecyclerViewDeco(spanCount, spacing, includeEdge));

        recyclerView.setLayoutManager(gridLayoutManager);

        List<String> bid = new ArrayList<>();
        booksModelList = new ArrayList<>();

        if (dir !=null || dir2 !=null) {

            if (dir != null) {
                secBook = FirebaseDatabase.getInstance().getReference().child("Store").child(dir).child("bid");
            } else if (dir2 != null) {
                secBook = FirebaseDatabase.getInstance().getReference().child("Library").child(dir2).child("bid");
            }
            secBook.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    booksModelList.clear();
                    bid.clear();
                    if (snapshot.exists()) {
                        recyclerView.setVisibility(View.VISIBLE);
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            bid.add(dataSnapshot.getValue().toString());
                        }
                        allBookRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                booksModelList.clear();
                                if (snapshot.exists()) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        if (bid.contains(dataSnapshot.child("id").getValue().toString())) {
                                            AllBook_Model allBook_model = dataSnapshot.getValue(AllBook_Model.class);
                                            booksModelList.add(allBook_model);
                                        }

                                    }

                                    adapter = new LibraryViewAll_Adapter(LibraryViewAllBooks.this, booksModelList);
                                    recyclerView.setAdapter(adapter);

                                    searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                                        @Override
                                        public boolean onQueryTextSubmit(String query) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onQueryTextChange(String newText) {

                                            adapter.getFilter().filter(newText.toString());

                                            return false;
                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }if (dir3 !=null){

            System.out.println(dir3 + "-------------------------------------");
            allBookRef = FirebaseDatabase.getInstance().getReference().child("Books");


            allBookRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    booksModelList.clear();
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.child(dir3).getValue().equals("yes")) {
                                AllBook_Model allBook_model = dataSnapshot.getValue(AllBook_Model.class);

                                booksModelList.add(allBook_model);
                            }
                        }
                    }

                    adapter = new LibraryViewAll_Adapter(LibraryViewAllBooks.this, booksModelList);
                    recyclerView.setAdapter(adapter);
                    searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {

                            adapter.getFilter().filter(newText.toString());

                            return false;
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }if (dir4 !=null){

            System.out.println(dir4+ "-------------------------------------");
            allBookRef = FirebaseDatabase.getInstance().getReference().child("Books");


            allBookRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    booksModelList.clear();
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.child("category").getValue().toString().contains(dir4)) {
                                AllBook_Model allBook_model = dataSnapshot.getValue(AllBook_Model.class);

                                booksModelList.add(allBook_model);
                            }
                        }
                    }

                    adapter = new LibraryViewAll_Adapter(LibraryViewAllBooks.this, booksModelList);
                    recyclerView.setAdapter(adapter);
                    searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {

                            adapter.getFilter().filter(newText.toString());

                            return false;
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
    }



    private void initData() {
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerview);



        Intent intent = getIntent();
        String temp = intent.getStringExtra("name");

        switch (temp) {
            case "Store Section 1":
                dir = "sec1";
                break;
            case "Store Section 2":
                dir = "sec2";
                break;
            case "Store Section 3":
                dir = "sec3";
                break;
            case "Store Section 4":
                dir = "sec4";
                break;
            case "Store Section 5":
                dir = "sec5";
                break;
            case "Store Section 6":
                dir = "sec6";
                break;
            case "Store Section 7":
                dir = "sec7";
                break;
            case "Store Section 8":
                dir = "sec8";
                break;
        }
        switch (temp) {
            case "e-Library Section 1":
                dir2 = "sec1";
                break;
            case "e-Library Section 2":
                dir2 = "sec2";
                break;
            case "e-Library Section 3":
                dir2 = "sec3";
                break;
            case "e-Library Section 4":
                dir2 = "sec4";
                break;
        }
        switch (temp) {
            case "Store Section All":
                dir3 = "inStore";
                break;
            case "e-Library Section All":
                dir3 = "inLibrary";
                break;
            default:
                dir4 = temp;
                System.out.println(dir4);
        }

    }

}












class LibraryViewAllBooks_RecyclerViewDeco extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public LibraryViewAllBooks_RecyclerViewDeco(int spanCount, int spacing, boolean includeEdge) {
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