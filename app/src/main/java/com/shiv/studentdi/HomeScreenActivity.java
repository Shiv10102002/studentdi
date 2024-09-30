package com.shiv.studentdi;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shiv.studentmanlib.Student;
import com.shiv.studentmanlib.StudentFormView;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;





public class HomeScreenActivity extends AppCompatActivity {
    private final List<Student> studentList = new ArrayList<>();
    private StudentAdapter studentAdapter;
    private EditText searchStudentEmail;
    private StudentFormView studentFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_students);
        FloatingActionButton fab = findViewById(R.id.fab_create_student);
        searchStudentEmail = findViewById(R.id.search_student_email);
        // Set up RecyclerView with StudentAdapter
        studentAdapter = new StudentAdapter(studentList, new StudentAdapter.OnStudentActionListener() {
            @Override
            public void onEditStudent(Student student) {
                editStudent(student);
            }

            @Override
            public void onDeleteStudent(Student student) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenActivity.this).setTitle("Delete Student").setMessage("Are you sure want to delete?").setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteStudent(student);
                        dialog.dismiss();

                    }
                }).setNegativeButton("No",new DialogInterface.OnClickListener (){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        },this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(studentAdapter);

        // Floating Action Button for creating a new student
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(HomeScreenActivity.this, StudentFormActivity.class);

            startActivityForResult(intent, 1);

        });

        // Fetch all students when the activity starts
        fetchAllStudents();

        // Set up the search functionality
        searchStudentEmail.setOnEditorActionListener((v, actionId, event) -> {
            String email = searchStudentEmail.getText().toString().trim();
            if (!TextUtils.isEmpty(email)) {
                searchStudentByEmail(email);
            } else {
                Toast.makeText(HomeScreenActivity.this, "Please enter an email", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    // Method to search a student by email
    private void searchStudentByEmail(String email) {
        StudentApiService apiService = RetrofitClient.getRetrofitInstance().create(StudentApiService.class);
        apiService.getStudentByEmail(email).enqueue(new  Callback<Student>() {
            @Override
            public void onResponse(@NonNull Call<Student> call, @NonNull Response<Student> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Pass the found student to the ResultScreenActivity
                    Intent intent = new Intent(HomeScreenActivity.this, ResultScreenActivity.class);
                    intent.putExtra("student", response.body());
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenActivity.this).setTitle(" Student Search").setMessage("Student not found");
                    builder.show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<Student> call, @NonNull Throwable t) {
                Toast.makeText(HomeScreenActivity.this, "Failed to search student", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to fetch all students from API
    private void fetchAllStudents() {
        StudentApiService apiService = RetrofitClient.getRetrofitInstance().create(StudentApiService.class);
        apiService.getAllStudents().enqueue(new Callback<List<Student>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<Student>> call, @NonNull Response<List<Student>> response) {
                if (response.isSuccessful()) {
                    studentList.clear();
                    studentList.addAll(response.body());
                    studentAdapter.notifyDataSetChanged();  // Refresh the adapter with new data
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Student>> call, @NonNull Throwable t) {
                fetchAllStudents();
//                Toast.makeText(HomeScreenActivity.this, "Failed to fetch students", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to edit a student
    private void editStudent(Student student) {

        Intent intent = new Intent(HomeScreenActivity.this, StudentFormActivity.class);
        intent.putExtra("student", student);
        startActivityForResult(intent, 2);  // Request code 2 for editing a student
    }

    // Method to delete a student
    private void deleteStudent(Student student) {
        StudentApiService apiService = RetrofitClient.getRetrofitInstance().create(StudentApiService.class);
        apiService.deleteStudent(student.get_id()).enqueue(new Callback<Void>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    studentList.remove(student);  // Remove the deleted student from the list
                    studentAdapter.notifyDataSetChanged();  // Notify adapter about the change
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(HomeScreenActivity.this, "Failed to delete student", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Override onActivityResult to handle updates
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle student creation or update
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1) { // New student created
                Student newStudent = (Student) data.getSerializableExtra("newStudent");
                if (newStudent != null) {
                    addStudentToList(newStudent);  // Add the newly created student to the list
                }
            } else if (requestCode == 2) { // Updated student returned
                Student updatedStudent = (Student) data.getSerializableExtra("updatedStudent");
                if (updatedStudent != null) {
                    updateStudentInList(updatedStudent);  // Update the student in the list
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            // Handle case where no result or operation was canceled
            Toast.makeText(this, "Operation canceled", Toast.LENGTH_SHORT).show();
        }
    }


    // Method to update the student in the list based on _id
    @SuppressLint("NotifyDataSetChanged")
    private void updateStudentInList(Student updatedStudent) {
        for (int i = 0; i < studentList.size(); i++) {
            if (studentList.get(i).get_id().equals(updatedStudent.get_id())) {
                studentList.set(i, updatedStudent);
                studentAdapter.notifyItemChanged(i);  // Notify only the updated item
                break;
            }
        }
    }

    // Method to add the newly created student to the list
    @SuppressLint("NotifyDataSetChanged")
    private void addStudentToList(Student newStudent) {
        studentList.add(newStudent);  // Add the new student to the list
        studentAdapter.notifyItemInserted(studentList.size() - 1);  // Notify the adapter to add new item
    }

}