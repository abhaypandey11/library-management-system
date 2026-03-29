package com.lib.service;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.lib.dao.MemberDAO;
import com.lib.dto.MemberDTO;
import com.lib.dto.MemberResponseDTO;
import com.lib.entity.Member;
import com.lib.exception.DuplicateResourceException;
import com.lib.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDAO memberDAO;
    private final ModelMapper modelMapper;

    // ✅ Register new member
    public MemberResponseDTO addMember(MemberDTO dto) {

        // Check duplicate email
        if (memberDAO.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException(
                "Member with email " + dto.getEmail() + " already exists");
        }

        // DTO → Model
        Member member = modelMapper.map(dto, Member.class);

        Member savedMember = memberDAO.save(member);

        // Model → ResponseDTO
        return modelMapper.map(savedMember, MemberResponseDTO.class);
    }

    // ✅ Get all members
    public List<MemberResponseDTO> getAllMembers() {
        List<Member> members = memberDAO.findAll();
        List<MemberResponseDTO> responseDTOs = new ArrayList<>();
        for (Member member : members) {
            responseDTOs.add(modelMapper.map(member, MemberResponseDTO.class));
        }
        return responseDTOs;
    }

    // ✅ Get single member by ID
    public MemberResponseDTO getMemberById(Long id) {
        Optional<Member> member = memberDAO.findById(id);
        if (member.isPresent()) {
            return modelMapper.map(member.get(), MemberResponseDTO.class);
        } else {
            throw new ResourceNotFoundException("Member", id);
        }
    }

    // ✅ Update existing member
    public MemberResponseDTO updateMember(Long id, MemberDTO dto) {

        Optional<Member> existingMember = memberDAO.findById(id);
        if (!existingMember.isPresent()) {
            throw new ResourceNotFoundException("Member", id);
        }

        if (memberDAO.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new DuplicateResourceException(
                "Another member with email " + dto.getEmail() + " already exists");
        }

        Member member = existingMember.get();
        member.setName(dto.getName());
        member.setEmail(dto.getEmail());
        member.setPhone(dto.getPhone());
        member.setMemberType(dto.getMemberType());

        Member updatedMember = memberDAO.save(member);

        // Model → ResponseDTO
        return modelMapper.map(updatedMember, MemberResponseDTO.class);
    }

    // ✅ Delete member
    public void deleteMember(Long id) {
        Optional<Member> member = memberDAO.findById(id);
        if (!member.isPresent()) {
            throw new ResourceNotFoundException("Member", id);
        }
        memberDAO.delete(member.get());
    }
}