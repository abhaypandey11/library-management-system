package com.lib.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

    // ✅ Factory method for success response
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    // ✅ Factory method for error response
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
/*

## 💡 Key Things to Understand

**What is `<T>` (Generic type)?**
```
T means any type — Book, Member, IssueRecord, List, Map
So one class handles ALL your API responses!

ApiResponse<Book>         → when returning single book
ApiResponse<List<Book>>   → when returning all books
ApiResponse<Map>          → when returning validation errors
ApiResponse<?>            → when you don't know the type yet
    
    */
}