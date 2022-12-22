package com.googleApp.shoppinglistapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googleApp.shoppinglistapplication.db.Category;
import com.googleApp.shoppinglistapplication.viewmodel.MainActivityViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryListAdapter.HandleCategoryClick {

    private MainActivityViewModel viewModel;
    private TextView noResulttextview;
    private RecyclerView recyclerView;
    private CategoryListAdapter categoryListAdapter;
    private Category categoryForEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Shopping List");
        noResulttextview = findViewById(R.id.noResult);
        recyclerView = findViewById(R.id.recyclerView);
        ImageView addnew = findViewById(R.id.addNewCategoryImageView);
        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCategoryDialog(false);


            }
        });
        initViewModel();
        initRecyclerView();
        viewModel.getAllCategoryList();
    }
    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryListAdapter = new CategoryListAdapter(this,this);
        recyclerView.setAdapter(categoryListAdapter);
    }

    private void initViewModel(){
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.getCategoryListObserbver().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                if(categories == null){
                    noResulttextview.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }else{
                    categoryListAdapter.setCategoryList(categories);
                    recyclerView.setVisibility(View.VISIBLE);
                    noResulttextview.setVisibility(View.GONE);

                }
            }
        });
    }

    private void showAddCategoryDialog(boolean isForEdit) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        View dialogView = getLayoutInflater().inflate(R.layout.add_category_layout,null);
        EditText enterCategoryInput = dialogView.findViewById(R.id.enterCategoryInput);
        TextView createButton = dialogView.findViewById(R.id.createButton);
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);

        if(isForEdit){
            createButton.setText("Update");
            enterCategoryInput.setText(categoryForEdit.categoryName);
        }
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = enterCategoryInput.getText().toString();
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(MainActivity.this,"Enter category name",Toast.LENGTH_SHORT);
                    return;
                }

                if(isForEdit){
                    categoryForEdit.categoryName = name;
                    viewModel.updateCategory(categoryForEdit);
                }
                else{
                    viewModel.insertCategory(name);
                }

                alertDialog.dismiss();
            }
        });
        alertDialog.setView(dialogView);
        alertDialog.show();

    }

    @Override
    public void itemClick(Category category) {
        Intent intent = new Intent(MainActivity.this,ShowItemListActivity.class);
        intent.putExtra("category_id",category.uid);
        intent.putExtra("category_name",category.categoryName);
        startActivity(intent);


    }

    @Override
    public void removeItem(Category category) {
        viewModel.deleteCategory(category);
    }

    @Override
    public void editItem(Category category) {
        this.categoryForEdit = category;
        showAddCategoryDialog(true);
    }
}