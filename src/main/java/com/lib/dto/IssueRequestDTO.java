package com.lib.dto;



import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueRequestDTO {

    @NotNull(message = "Book ID is required")
    @Positive(message = "Book ID must be a positive number")
    private Long bookId;

    @NotNull(message = "Member ID is required")
    @Positive(message = "Member ID must be a positive number")
    private Long memberId;


/*## 💡 Key Things to Understand

**Why DTO and not directly Entity?**
```
❌ Bad  — Controller receives Book entity directly
            (exposes DB structure, security risk)

✅ Good — Controller receives BookDTO
            (you control exactly what user can send)*/
    
}