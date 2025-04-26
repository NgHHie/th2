package com.example.th2.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.th2.R;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private Context context;
    private List<Book> list;
    private BookItemListener listener;

    public BookAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    public void setList(List<Book> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public Book getItem(int pos) {
        return list.get(pos);
    }

    public void setListener(BookItemListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = list.get(position);

        holder.txtTitle.setText(book.getTitle());
        holder.txtAuthor.setText(book.getAuthor());
        holder.txtDate.setText(book.getDate());
        holder.txtTypes.setText(book.getTypesString());

        // Set click listeners for edit and delete buttons
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(holder.getAdapterPosition());
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(Book book) {
        list.add(book);
        notifyDataSetChanged();
    }

    public void update(int pos, Book book) {
        list.set(pos, book);
        notifyDataSetChanged();
    }

    public void remove(int pos) {
        list.remove(pos);
        notifyDataSetChanged();
    }

    public interface BookItemListener {
        void onEditClick(int pos);
        void onDeleteClick(int pos);
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle, txtAuthor, txtDate, txtTypes;
        private Button btnEdit, btnDelete;

        public BookViewHolder(@NonNull View view) {
            super(view);
            txtTitle = view.findViewById(R.id.txtTitle);
            txtAuthor = view.findViewById(R.id.txtAuthor);
            txtDate = view.findViewById(R.id.txtDate);
            txtTypes = view.findViewById(R.id.txtTypes);
            btnEdit = view.findViewById(R.id.btnEdit);
            btnDelete = view.findViewById(R.id.btnDelete);
        }
    }
}