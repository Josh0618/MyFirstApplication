package com.jnu.student;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookListFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class BookListFragment extends Fragment {

    public BookListFragment() {
        // Required empty public constructor
    }
    public static BookListFragment newInstance() {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }
    private ArrayList<Book> books = new ArrayList<>();
    private BookAdapter bookAdapter;
    ActivityResultLauncher<Intent> addItemLauncher;
    ActivityResultLauncher<Intent> updateItemLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_book_list, container, false);

        RecyclerView mainRecyclerView = rootView.findViewById(R.id.recycle_view_books);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        books = new DataBank().LoadBooks(requireActivity());
        if(0 == books.size()) {
            books.add(new Book("软件项目管理案例教程（第4版）", R.drawable.book_2));
            books.add(new Book("创新工程实践", R.drawable.book_no_name));
            books.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1));
        }

        bookAdapter = new BookAdapter(books);
        mainRecyclerView.setAdapter(bookAdapter);

        registerForContextMenu(mainRecyclerView);

        addItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String name = data.getStringExtra("name"); // 获取返回的数据

                        books.add(new Book(name, R.drawable.book_1));
                        bookAdapter.notifyItemInserted(books.size());

                        new DataBank().SaveBooks(requireActivity(), books);

                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        // 处理取消操作
                    }
                }
        );

        updateItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        int position = data.getIntExtra("position",0);
                        String name = data.getStringExtra("name"); // 获取返回的数据

                        Book book = books.get(position);
                        book.setName(name);
                        bookAdapter.notifyItemChanged(position);

                        new DataBank().SaveBooks(requireActivity(), books);

                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        // 处理取消操作
                    }
                }
        );
        return rootView;
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(requireActivity(), BookDetailsActivity.class);
                addItemLauncher.launch(intent);
                startActivity(intent);
                break;
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("Delete Data");
                builder.setMessage("Are you sure you want to delete this data?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        books.remove(item.getOrder());
                        bookAdapter.notifyItemRemoved(item.getOrder());

                        new DataBank().SaveBooks(requireActivity(), books);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
                break;
            case 2:
                Intent intentUpdate = new Intent(requireActivity(), BookDetailsActivity.class);
                Book book = books.get(item.getOrder());
                intentUpdate.putExtra("name", book.getName());
                intentUpdate.putExtra("position", item.getOrder());
                updateItemLauncher.launch(intentUpdate);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
        private ArrayList<Book> bookArrayList;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            private final TextView textViewName;
            private final ImageView imageViewItem;

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("具体操作");

                menu.add(0, 0, this.getAdapterPosition(), "添加" + this.getAdapterPosition());
                menu.add(0, 1, this.getAdapterPosition(), "删除" + this.getAdapterPosition());
                menu.add(0, 2, this.getAdapterPosition(), "修改" + this.getAdapterPosition());
            }
            public ViewHolder(View bookView) {
                super(bookView);

                textViewName = bookView.findViewById(R.id.text_view_book_title);
                imageViewItem = bookView.findViewById(R.id.image_view_book_cover);

                bookView.setOnCreateContextMenuListener(this);
            }

            public TextView getTextViewName() {
                return textViewName;
            }

            public ImageView getImageViewItem() {
                return imageViewItem;
            }


        }
        public BookAdapter(ArrayList<Book> books) {
            bookArrayList = books;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_book, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            viewHolder.getTextViewName().setText(books.get(position).getName());
            viewHolder.getImageViewItem().setImageResource(books.get(position).getCoverResourceId());
        }

        @Override
        public int getItemCount() {
            return bookArrayList.size();
        }

    }
}

class Book implements Serializable {
    public int getCoverResourceId() {
        return imageResourceId;
    }

    private int imageResourceId;

    public String getName() {
        return name;
    }

    private String name;

    public Book(String name_, int imageResourceId_) {
        this.name=name_;
        this.imageResourceId =imageResourceId_;
    }

    public void setName(String name) {
        this.name = name;
    }
}