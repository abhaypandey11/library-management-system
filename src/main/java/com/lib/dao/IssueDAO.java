package com.lib.dao;



import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.lib.entity.IssueRecord;
import com.lib.repository.IssueRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class IssueDAO {

    private final IssueRepository issueRepository;

    public IssueRecord save(IssueRecord record) {
        return issueRepository.save(record);
    }

    public Optional<IssueRecord> findById(Long id) {
        return issueRepository.findById(id);
    }

    public List<IssueRecord> findAll() {
        return issueRepository.findAll();
    }

    public List<IssueRecord> findByMemberId(Long memberId) {
        return issueRepository.findByMemberId(memberId);
    }

    public List<IssueRecord> findByBookId(Long bookId) {
        return issueRepository.findByBookId(bookId);
    }

    public List<IssueRecord> findByStatus(String status) {
        return issueRepository.findByStatus(status);
    }
}
