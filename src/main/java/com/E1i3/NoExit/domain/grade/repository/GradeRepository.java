package com.E1i3.NoExit.domain.grade.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.E1i3.NoExit.domain.grade.domain.Grade;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

}
