package com.jnu.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class BookDetailsActivity extends AppCompatActivity {

    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item_details);

        Intent intent = getIntent();
        if (intent != null) {
            // 获取从主界面传递过来的数据
            String name = intent.getStringExtra("name");

            if (name != null) {
                position = intent.getIntExtra("position", -1);
                EditText editTextBookTitle = findViewById(R.id.edit_text_book_title);
                editTextBookTitle.setText(name);
            }
        }
        Button buttonOk = findViewById(R.id.button_item_details_ok);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                EditText editTextItemName = findViewById(R.id.edit_text_book_title);
                intent.putExtra("name", editTextItemName.getText().toString());
                intent.putExtra("position", position);
                setResult(Activity.RESULT_OK, intent);
                BookDetailsActivity.this.finish();
            }
        });
    }
}
