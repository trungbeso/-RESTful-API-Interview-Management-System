package com.interviewmanagementsystem.services.skill;

import com.interviewmanagementsystem.dtos.skills.SkillDTO;
import com.interviewmanagementsystem.entities.Skill;
import com.interviewmanagementsystem.mapper.SkillMapper;
import com.interviewmanagementsystem.repositories.ISkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillService {
    private final ISkillRepository skillRepository;
    private final SkillMapper skillMapper;

     public List<SkillDTO> getAllSkills() {
        List<Skill> skills = skillRepository.findAll();
        return skillMapper.toDTOList(skills); // Chuyển từ entity sang DTO
    }

    public SkillDTO saveSkill(SkillDTO skillDTO) {
        Skill skill = skillMapper.toEntity(skillDTO); // Chuyển từ DTO sang entity
        Skill savedSkill = skillRepository.save(skill);
        return skillMapper.toDTO(savedSkill); // Chuyển entity đã lưu sang DTO
    }
}
