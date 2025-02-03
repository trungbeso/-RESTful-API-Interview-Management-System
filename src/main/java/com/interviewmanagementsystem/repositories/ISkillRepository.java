package com.interviewmanagementsystem.repositories;

import com.ninja_in_pyjamas.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ISkillRepository extends JpaRepository<Skill, UUID> {
    // // Trong SkillRepository
    // @Query("SELECT s FROM Skill s WHERE s.name IN :names")
    // Set<Skill> findByNameIn(@Param("names") Set<String> names);

    Set<Skill> findByNameIn(Set<String> names);

    List<Skill> findAllById(Iterable<UUID> ids);
}
