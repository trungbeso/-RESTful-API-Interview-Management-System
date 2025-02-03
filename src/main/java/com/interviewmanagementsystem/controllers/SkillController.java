package com.interviewmanagementsystem.controllers;

import com.ninja_in_pyjamas.dtos.skills.SkillDTO;
import com.ninja_in_pyjamas.services.skill.SkillService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

     private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public List<SkillDTO> getAllSkills() {
        return skillService.getAllSkills();
    }

    @PostMapping
    public SkillDTO createSkill(@RequestBody SkillDTO skillDTO) {
        return skillService.saveSkill(skillDTO);
    }

}
