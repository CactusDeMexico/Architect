package com.pancarte.architecte.repository;

import com.pancarte.architecte.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("projectRepository")
public interface ProjectRepository extends JpaRepository <Project,Long> {

}
