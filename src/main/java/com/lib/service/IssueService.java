package com.lib.service;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.lib.dao.BookDAO;
import com.lib.dao.IssueDAO;
import com.lib.dao.MemberDAO;
import com.lib.dto.IssueRequestDTO;
import com.lib.dto.IssueResponseDTO;
import com.lib.entity.Book;
import com.lib.entity.IssueRecord;
import com.lib.entity.Member;
import com.lib.exception.BookNotAvailableException;
import com.lib.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueDAO issueDAO;
    private final BookDAO bookDAO;
    private final MemberDAO memberDAO;
    private final ModelMapper modelMapper;

    private static final double FINE_PER_DAY = 5.0;
    private static final int LOAN_PERIOD_DAYS = 14;

    // ✅ Helper method — convert IssueRecord to IssueResponseDTO
    // Avoids repeating same mapping code in every method
    private IssueResponseDTO convertToResponseDTO(IssueRecord record) {
        IssueResponseDTO responseDTO = new IssueResponseDTO();
        responseDTO.setId(record.getId());
        responseDTO.setBookId(record.getBook().getId());
        responseDTO.setBookTitle(record.getBook().getTitle());
        responseDTO.setMemberId(record.getMember().getId());
        responseDTO.setMemberName(record.getMember().getName());
        responseDTO.setIssueDate(record.getIssueDate());
        responseDTO.setDueDate(record.getDueDate());
        responseDTO.setReturnDate(record.getReturnDate());
        responseDTO.setStatus(record.getStatus());
        responseDTO.setFine(record.getFine());
        return responseDTO;
    }

    // ✅ Issue book to member
    public IssueResponseDTO issueBook(IssueRequestDTO dto) {

        // Check book exists
        Optional<Book> optionalBook = bookDAO.findById(dto.getBookId());
        if (!optionalBook.isPresent()) {
            throw new ResourceNotFoundException("Book", dto.getBookId());
        }
        Book book = optionalBook.get();

        // Check copies available
        if (book.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException(
                "No copies available for book: " + book.getTitle());
        }

        // Check member exists
        Optional<Member> optionalMember = memberDAO.findById(dto.getMemberId());
        if (!optionalMember.isPresent()) {
            throw new ResourceNotFoundException("Member", dto.getMemberId());
        }
        Member member = optionalMember.get();

        // Reduce available copies by 1
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookDAO.save(book);

        // Create issue record
        IssueRecord record = new IssueRecord();
        record.setBook(book);
        record.setMember(member);
        record.setIssueDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(LOAN_PERIOD_DAYS));
        record.setStatus("ISSUED");
        record.setFine(0);

        IssueRecord savedRecord = issueDAO.save(record);

        return convertToResponseDTO(savedRecord);
    }

    // ✅ Return book + calculate fine
    public IssueResponseDTO returnBook(Long issueId) {

        // Check issue record exists
        Optional<IssueRecord> optionalRecord = issueDAO.findById(issueId);
        if (!optionalRecord.isPresent()) {
            throw new ResourceNotFoundException("Issue record", issueId);
        }
        IssueRecord record = optionalRecord.get();

        // Check not already returned
        if ("RETURNED".equals(record.getStatus())) {
            throw new BookNotAvailableException("This book is already returned");
        }

        LocalDate returnDate = LocalDate.now();
        record.setReturnDate(returnDate);
        record.setStatus("RETURNED");

        // Fine calculation
        if (returnDate.isAfter(record.getDueDate())) {
            long daysLate = ChronoUnit.DAYS.between(record.getDueDate(), returnDate);
            record.setFine(daysLate * FINE_PER_DAY);
        } else {
            record.setFine(0);
        }

        // Increase available copies by 1
        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookDAO.save(book);

        IssueRecord updatedRecord = issueDAO.save(record);

        return convertToResponseDTO(updatedRecord);
    }

    // ✅ Get all issues
    public List<IssueResponseDTO> getAllIssues() {
        List<IssueRecord> records = issueDAO.findAll();
        List<IssueResponseDTO> responseDTOs = new ArrayList<>();
        for (IssueRecord record : records) {
            responseDTOs.add(convertToResponseDTO(record));
        }
        return responseDTOs;
    }

    // ✅ Get issues by member
    public List<IssueResponseDTO> getIssuesByMember(Long memberId) {
        List<IssueRecord> records = issueDAO.findByMemberId(memberId);
        List<IssueResponseDTO> responseDTOs = new ArrayList<>();
        for (IssueRecord record : records) {
            responseDTOs.add(convertToResponseDTO(record));
        }
        return responseDTOs;
    }

    // ✅ Get issues by book
    public List<IssueResponseDTO> getIssuesByBook(Long bookId) {
        List<IssueRecord> records = issueDAO.findByBookId(bookId);
        List<IssueResponseDTO> responseDTOs = new ArrayList<>();
        for (IssueRecord record : records) {
            responseDTOs.add(convertToResponseDTO(record));
        }
        return responseDTOs;
    }

    // ✅ Get issues by status
    public List<IssueResponseDTO> getIssuesByStatus(String status) {
        List<IssueRecord> records = issueDAO.findByStatus(status);
        List<IssueResponseDTO> responseDTOs = new ArrayList<>();
        for (IssueRecord record : records) {
            responseDTOs.add(convertToResponseDTO(record));
        }
        return responseDTOs;
    }
}