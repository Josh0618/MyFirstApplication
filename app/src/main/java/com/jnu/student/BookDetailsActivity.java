package com.jnu.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class BookDetailsActivity extends AppCompatActivity {

    private int position = -1;
    private boolean isNewBook = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item_details);

        Intent intent = getIntent();
        if (intent != null) {
            // 获取从主界面传递过来的数据
            String name = intent.getStringExtra("name");
            position = intent.getIntExtra("position", -1);

            // 判断是否为新建操作
            isNewBook = position == -1;

            EditText editTextBookTitle = findViewById(R.id.edit_text_book_title);
            if (name != null) {
                editTextBookTitle.setText(name);
            }
        }
    }

    private void saveBookData() {
        EditText editTextBookTitle = findViewById(R.id.edit_text_book_title);
        String newBookTitle = editTextBookTitle.getText().toString();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("name", newBookTitle);
        resultIntent.putExtra("position", position);

        if (isNewBook) {
            setResult(Activity.RESULT_OK, resultIntent);
        } else {
            setResult(Activity.RESULT_OK, resultIntent);
        }

        finish();
    }

    private void cancelOperation() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
