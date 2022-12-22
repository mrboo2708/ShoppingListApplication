package com.googleApp.shoppinglistapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.googleApp.shoppinglistapplication.db.Category;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {

    private android.content.Context context;
    private List<Category> categoryList;
    private HandleCategoryClick clickListener;
    public CategoryListAdapter(Context context,HandleCategoryClick clickListener){
        this.context = context;
        this.clickListener = clickListener;
    }
    public void setCategoryList(List<Category> categoryList){
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CategoryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.recycleview_row,parent,false);
       return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CategoryListAdapter.MyViewHolder holder, int position) {
        holder.tvCategoryName.setText(this.categoryList.get(position).categoryName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.itemClick(categoryList.get(holder.getAdapterPosition()));
            }
        });

        holder.editCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.editItem(categoryList.get(holder.getAdapterPosition()));
            }
        });

        holder.removeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.removeItem(categoryList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(categoryList == null ||categoryList.size() == 0){
            return 0;
        }
        else
            return categoryList.size();

    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        ImageView removeCategory;
        ImageView editCategory;

        public MyViewHolder(View view) {
            super(view);
            tvCategoryName = view.findViewById(R.id.tvCategoryName);
            removeCategory = view.findViewById(R.id.removeCategory);
            editCategory = view.findViewById(R.id.editCategory);
        }
    }
    public interface HandleCategoryClick {
        void itemClick(Category category);
        void removeItem(Category category);
        void editItem(Category category);

    }
}


