package com.example.th2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.th2.model.Book;
import com.example.th2.model.BookAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity3 extends AppCompatActivity implements BookAdapter.BookItemListener {
    private RecyclerView rvBooks;
    private Button btnAdd;
    private EditText edtSearch;
    private BookAdapter adapter;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private List<Book> bookList;
    private int currentId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        init();
        initRv();
        loadSampleData();
    }

    private void init() {
        rvBooks = findViewById(R.id.rvBooks);
        btnAdd = findViewById(R.id.btnAdd);
        edtSearch = findViewById(R.id.edtSearch);

        btnAdd.setOnClickListener(v -> showBookDialog(null, -1));

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initRv() {
        adapter = new BookAdapter(this);
        adapter.setListener(this);

        rvBooks.setLayoutManager(new LinearLayoutManager(this));
        rvBooks.setAdapter(adapter);

        bookList = new ArrayList<>();
    }

    private void filter(String text) {
        List<Book> filteredList = new ArrayList<>();

        for (Book book : bookList) {
            if (book.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(text.toLowerCase()) ||
                    book.getTypesString().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(book);
            }
        }

        adapter.setList(filteredList);
    }

    private void showBookDialog(Book book, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_book, null);
        builder.setView(view);

        TextView dialogTitle = view.findViewById(R.id.dialogTitle);
        EditText edtTitle = view.findViewById(R.id.edtTitle);
        EditText edtAuthor = view.findViewById(R.id.edtAuthor);
        TextView txtDate = view.findViewById(R.id.txtDate);
        CheckBox cbScience = view.findViewById(R.id.cbScience);
        CheckBox cbNovel = view.findViewById(R.id.cbNovel);
        CheckBox cbChildren = view.findViewById(R.id.cbChildren);

        TextView txtErrTitle = view.findViewById(R.id.txtErrTitle);
        TextView txtErrAuthor = view.findViewById(R.id.txtErrAuthor);
        TextView txtErrDate = view.findViewById(R.id.txtErrDate);

        // Set dialog title based on operation
        dialogTitle.setText(book == null ? "Thêm sách mới" : "Sửa thông tin sách");

        // Set date picker
        txtDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(selectedYear, selectedMonth, selectedDay);
                        txtDate.setText(sdf.format(selectedCalendar.getTime()));
                    },
                    year, month, day);

            datePickerDialog.show();
        });

        // Get button references
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnSave = view.findViewById(R.id.btnSave);

        // Set button text based on operation
        btnSave.setText(book == null ? "Thêm" : "Lưu");

        // Fill data if editing
        if (book != null) {
            edtTitle.setText(book.getTitle());
            edtAuthor.setText(book.getAuthor());
            txtDate.setText(book.getDate());

            List<String> types = book.getTypes();
            cbScience.setChecked(types.contains("Khoa học"));
            cbNovel.setChecked(types.contains("Tiểu thuyết"));
            cbChildren.setChecked(types.contains("Thiếu nhi"));
        }

        AlertDialog dialog = builder.create();
        dialog.show();

        // Set button click listeners
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            boolean isValid = true;

            String title = edtTitle.getText().toString().trim();
            String author = edtAuthor.getText().toString().trim();
            String date = txtDate.getText().toString().trim();

            // Validate
            if (title.isEmpty()) {
                txtErrTitle.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                txtErrTitle.setVisibility(View.GONE);
            }

            if (author.isEmpty()) {
                txtErrAuthor.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                txtErrAuthor.setVisibility(View.GONE);
            }

            if (date.isEmpty()) {
                txtErrDate.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                txtErrDate.setVisibility(View.GONE);
            }

            if (isValid) {
                Book newBook;
                if (book == null) {
                    // Creating new book
                    currentId++;
                    newBook = new Book(currentId, title, author, date);
                } else {
                    // Updating existing book
                    newBook = new Book(book.getId(), title, author, date);
                }

                // Add types
                if (cbScience.isChecked()) newBook.addType("Khoa học");
                if (cbNovel.isChecked()) newBook.addType("Tiểu thuyết");
                if (cbChildren.isChecked()) newBook.addType("Thiếu nhi");

                if (book == null) {
                    // Add new book
                    bookList.add(newBook);
                    adapter.add(newBook);
                    Toast.makeText(MainActivity3.this, "Thêm sách thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Update book
                    for (int i = 0; i < bookList.size(); i++) {
                        if (bookList.get(i).getId() == newBook.getId()) {
                            bookList.set(i, newBook);
                            break;
                        }
                    }
                    adapter.update(position, newBook);
                    Toast.makeText(MainActivity3.this, "Cập nhật sách thành công", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });
    }

    private void confirmDelete(int position) {
        Book book = adapter.getItem(position);

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sách \"" + book.getTitle() + "\"?")
                .setPositiveButton("Có", (dialog, which) -> {
                    // Remove from lists
                    bookList.remove(book);
                    adapter.remove(position);
                    Toast.makeText(MainActivity3.this, "Đã xóa sách", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void loadSampleData() {
        // Sample book 1
        Book book1 = new Book(++currentId, "Alice ở xứ sở diệu kỳ", "Lewis Carroll", "01/12/1865");
        book1.addType("Tiểu thuyết");
        book1.addType("Thiếu nhi");
        bookList.add(book1);

        // Sample book 2
        Book book2 = new Book(++currentId, "Thép đã tôi thế đấy", "Nikolai Ostrovsky", "15/08/1932");
        book2.addType("Tiểu thuyết");
        bookList.add(book2);

        // Sample book 3
        Book book3 = new Book(++currentId, "Vũ trụ trong vỏ hạt dẻ", "Stephen Hawking", "05/10/2001");
        book3.addType("Khoa học");
        bookList.add(book3);

        adapter.setList(bookList);
    }

    @Override
    public void onEditClick(int pos) {
        Book book = adapter.getItem(pos);
        showBookDialog(book, pos);
    }

    @Override
    public void onDeleteClick(int pos) {
        confirmDelete(pos);
    }
}