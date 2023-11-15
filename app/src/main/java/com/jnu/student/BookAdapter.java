package com.jnu.student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> bookList;

    public BookAdapter(List<Book> bookList) {
        this.bookList = bookList;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookCoverImageView;
        TextView bookTitleTextView;

        public BookViewHolder(View itemView) {
            super(itemView);
            bookCoverImageView = itemView.findViewById(R.id.image_view_book_cover);
            bookTitleTextView = itemView.findViewById(R.id.text_view_book_title);
        }
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.bookTitleTextView.setText(book.getTitle());
        holder.bookCoverImageView.setImageResource(book.getCoverResourceId());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
