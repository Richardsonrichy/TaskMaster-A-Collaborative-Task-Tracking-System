package com.taskmaster.repository;

import com.taskmaster.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> 

{

    
}