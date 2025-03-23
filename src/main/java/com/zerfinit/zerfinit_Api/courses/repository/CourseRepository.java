package com.zerfinit.zerfinit_Api.courses.repository;

import com.zerfinit.zerfinit_Api.courses.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}