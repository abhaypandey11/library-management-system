package com.lib.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lib.entity.IssueRecord;

@Repository
public interface IssueRepository extends JpaRepository<IssueRecord, Long> {

    List<IssueRecord> findByMemberId(Long memberId);

    List<IssueRecord> findByBookId(Long bookId);

    List<IssueRecord> findByStatus(String status);
}
