package com.shiv.studentdi;


import com.shiv.studentmanlib.Student;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface StudentApiService {
    @GET("student")
    Call<List<Student>> getAllStudents();

    @POST("student")
    Call<Student> createStudent(@Body Student student);

    @PUT("student/{id}")
    Call<Student> updateStudent(@Path("id") String id, @Body Student student);

    @DELETE("student/{id}")
    Call<Void> deleteStudent(@Path("id") String id);

    @GET("student/{email}")
    Call<Student> getStudentByEmail(@Path("email") String email);
}
