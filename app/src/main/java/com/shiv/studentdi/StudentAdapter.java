package com.shiv.studentdi;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shiv.studentmanlib.Student;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private final List<Student> studentList;
    private final OnStudentActionListener listener;
    private final Context context;
    public interface OnStudentActionListener {
        void onEditStudent(Student student);
        void onDeleteStudent(Student student);


    }

    public StudentAdapter(List<Student> studentList, OnStudentActionListener listener,Context context) {
        this.studentList = studentList;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        Context context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.firstName.setText(student.getFirstName());
        holder.lastName.setText(student.getLastName());
        holder.email.setText(student.getEmail());
        holder.dateOfBirth.setText(student.getDateOfBirth());
// Set background color to black
        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));


        holder.editButton.setOnClickListener(v -> listener.onEditStudent(student));
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteStudent(student));
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView firstName, lastName, email, dateOfBirth;
        Button editButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.firstName);
            lastName = itemView.findViewById(R.id.lastName);
            email = itemView.findViewById(R.id.email);
            dateOfBirth = itemView.findViewById(R.id.dateOfBirth);
            editButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton); // Ensure this line is uncommented
        }
    }
}
