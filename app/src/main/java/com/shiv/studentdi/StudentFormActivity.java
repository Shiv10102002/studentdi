package com.shiv.studentdi;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.shiv.studentmanlib.StudentFormView;
import com.shiv.studentmanlib.Student;

public class StudentFormActivity extends AppCompatActivity {

    private StudentFormView studentFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_form);

        studentFormView = findViewById(R.id.studentFormView);

        // Check if we received a student for updating
        Intent intent = getIntent();
        Student student = (Student) intent.getSerializableExtra("student");

        if (student != null) {
            // If student is not null, we are editing an existing student
            studentFormView.setStudentForUpdate(student);
        }

        // Set up the callback to listen for when the student is created
        studentFormView.setStudentCreateCallback(new StudentFormView.StudentCreateCallback() {
            @Override
            public void onStudentCreated(Student createdStudent) {
                // Pass the newly created student back to HomeScreenActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("newStudent", createdStudent);  // Assuming Student is Serializable
                setResult(RESULT_OK, resultIntent);
                finish();  // Close StudentFormActivity and return to HomeScreenActivity
            }
        });

        // Set up the callback to listen for when the student is updated
        studentFormView.setStudentUpdateCallback(new StudentFormView.StudentUpdateCallback() {
            @Override
            public void onStudentUpdated(Student updatedStudent) {
                // Pass the updated student back to HomeScreenActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("updatedStudent", updatedStudent);  // Assuming Student is Serializable
                setResult(RESULT_OK, resultIntent);
                finish();  // Close StudentFormActivity and return to HomeScreenActivity
            }
        });
    }
}
