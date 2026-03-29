package com.lib.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lib.dto.MemberDTO;
import com.lib.dto.MemberResponseDTO;
import com.lib.response.ApiResponse;
import com.lib.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MemberController {

    private final MemberService memberService;

    // ✅ Register new member
    // POST http://localhost:8080/api/members
    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponseDTO>> addMember(
            @Valid @RequestBody MemberDTO dto) {

        MemberResponseDTO savedMember = memberService.addMember(dto);
        return ResponseEntity.ok(ApiResponse.success("Member registered successfully", savedMember));
    }

    // ✅ Get all members
    // GET http://localhost:8080/api/members
    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberResponseDTO>>> getAllMembers() {

        List<MemberResponseDTO> members = memberService.getAllMembers();
        return ResponseEntity.ok(ApiResponse.success("Members fetched successfully", members));
    }

    // ✅ Get single member by ID
    // GET http://localhost:8080/api/members/1
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponseDTO>> getMemberById(
            @PathVariable Long id) {

        MemberResponseDTO member = memberService.getMemberById(id);
        return ResponseEntity.ok(ApiResponse.success("Member fetched successfully", member));
    }

    // ✅ Update member
    // PUT http://localhost:8080/api/members/1
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponseDTO>> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody MemberDTO dto) {

        MemberResponseDTO updatedMember = memberService.updateMember(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Member updated successfully", updatedMember));
    }

    // ✅ Delete member
    // DELETE http://localhost:8080/api/members/1
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(
            @PathVariable Long id) {

        memberService.deleteMember(id);
        return ResponseEntity.ok(ApiResponse.success("Member deleted successfully", null));
    }
}
