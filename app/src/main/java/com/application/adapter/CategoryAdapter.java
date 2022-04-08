package com.application.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.R;
import com.application.listeners.OnCategorySelectedListener;
import com.application.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categories;
    private OnCategorySelectedListener mOnCategorySelectedListener;

    public CategoryAdapter(List<Category> categories, OnCategorySelectedListener mOnCategorySelectedListener){
        this.categories = categories;
        this.mOnCategorySelectedListener = mOnCategorySelectedListener;
    }


    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View categoryView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_category_item, parent, false);

        return new CategoryViewHolder(categoryView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        Category cat = this.categories.get(position);
        holder.setData(cat);
    }

    @Override
    public int getItemCount() {
        return this.categories.size();
    }

    public void clearList() {
        this.categories.clear();
        notifyDataSetChanged();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView title, tasks;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.categoryImage);
            title = itemView.findViewById(R.id.categoryTitle);
            tasks = itemView.findViewById(R.id.categoryTasks);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Category category = categories.get(getAdapterPosition());
            try {
                mOnCategorySelectedListener.categorySelected(category);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void setData(Category cat) {
            this.image.setImageResource(cat.getImageName());
            this.title.setText(cat.getTitle());
            this.tasks.setText(cat.getTasks() + " Task" + (cat.getTasks() == 1 ? "" : "s"));
        }

    }
}
