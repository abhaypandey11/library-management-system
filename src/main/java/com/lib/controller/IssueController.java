package com.lib.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lib.dto.IssueRequestDTO;
import com.lib.dto.IssueResponseDTO;
import com.lib.response.ApiResponse;
import com.lib.service.IssueService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class IssueController {

    private final IssueService issueService;

    // ✅ Issue book to member
    // POST http://localhost:8080/api/issues
    @PostMapping
    public ResponseEntity<ApiResponse<IssueResponseDTO>> issueBook(
            @Valid @RequestBody IssueRequestDTO dto) {

        IssueResponseDTO issuedRecord = issueService.issueBook(dto);
        return ResponseEntity.ok(ApiResponse.success("Book issued successfully", issuedRecord));
    }

    // ✅ Return book
    // PUT http://localhost:8080/api/issues/return/1
    @PutMapping("/return/{id}")
    public ResponseEntity<ApiResponse<IssueResponseDTO>> returnBook(
            @PathVariable Long id) {

        IssueResponseDTO returnedRecord = issueService.returnBook(id);
        return ResponseEntity.ok(ApiResponse.success("Book returned successfully", returnedRecord));
    }

    // ✅ Get all issue records
    // GET http://localhost:8080/api/issues
    @GetMapping
    public ResponseEntity<ApiResponse<List<IssueResponseDTO>>> getAllIssues() {

        List<IssueResponseDTO> issues = issueService.getAllIssues();
        return ResponseEntity.ok(ApiResponse.success("Issues fetched successfully", issues));
    }

    // ✅ Get issues by member ID
    // GET http://localhost:8080/api/issues/member/1
    @GetMapping("/member/{memberId}")
    public ResponseEntity<ApiResponse<List<IssueResponseDTO>>> getByMember(
            @PathVariable Long memberId) {

        List<IssueResponseDTO> issues = issueService.getIssuesByMember(memberId);
        return ResponseEntity.ok(ApiResponse.success("Member issues fetched successfully", issues));
    }

    // ✅ Get issues by book ID
    // GET http://localhost:8080/api/issues/book/1
    @GetMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse<List<IssueResponseDTO>>> getByBook(
            @PathVariable Long bookId) {

        List<IssueResponseDTO> issues = issueService.getIssuesByBook(bookId);
        return ResponseEntity.ok(ApiResponse.success("Book issues fetched successfully", issues));
    }

    // ✅ Get issues by status
    // GET http://localhost:8080/api/issues/status/ISSUED
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<IssueResponseDTO>>> getByStatus(
            @PathVariable String status) {

        List<IssueResponseDTO> issues = issueService.getIssuesByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Issues fetched by status successfully", issues));
    }
/*
```

---

## 💡 Key Things to Understand

**Annotations explained:**

| Annotation | What it does |
|---|---|
| `@RestController` | Marks class as REST API controller |
| `@RequestMapping` | Base URL for all methods in this controller |
| `@CrossOrigin` | Allows frontend JS to call this API |
| `@PostMapping` | Handles HTTP POST request |
| `@GetMapping` | Handles HTTP GET request |
| `@PutMapping` | Handles HTTP PUT request |
| `@DeleteMapping` | Handles HTTP DELETE request |
| `@RequestBody` | Reads JSON from request body |
| `@PathVariable` | Reads value from URL like `/books/1` |
| `@Valid` | Triggers DTO validation rules |

**Complete request flow:**
```
POST /api/books
      ↓
@Valid checks BookDTO    (validation)
      ↓
BookService.addBook()   (business logic)
      ↓
BookDAO.save()          (DB operation)
      ↓
ModelMapper converts     (Model → ResponseDTO)
      ↓
ApiResponse.success()   (wrap in response)
      ↓
JSON sent back to user
```

---

## 🎉 ALL 8 LAYERS COMPLETED!
```
✅ Layer 1 — Model
✅ Layer 2 — Repository
✅ Layer 3 — DAO
✅ Layer 4 — DTO + Response DTOs
✅ Layer 5 — Exception
✅ Layer 6 — Response
✅ Layer 7 — Service
✅ Layer 8 — Controller
    
    */
}