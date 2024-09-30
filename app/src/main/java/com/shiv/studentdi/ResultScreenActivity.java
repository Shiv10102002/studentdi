package com.shiv.studentdi;


import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


import com.shiv.studentmanlib.Student;

public class ResultScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);

        TextView studentDetails = findViewById(R.id.text_view_student_details);

        // Get the passed student object from the intent
        Student student = (Student) getIntent().getSerializableExtra("student");

        if (student != null) {
            // Display student details
            String details = "Name: " + student.getFirstName() + " " + student.getLastName() + "\n" +
                    "Email: " + student.getEmail() + "\n" +
                    "Date of Birth: " + student.getDateOfBirth();
            studentDetails.setText(details);
        }
    }
}
