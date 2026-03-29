package com.lib.dao;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.lib.entity.Member;
import com.lib.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberDAO {

    private final MemberRepository memberRepository;

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public void delete(Member member) {
        memberRepository.delete(member);
    }

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean existsByEmailAndIdNot(String email, Long id) {
        return memberRepository.existsByEmailAndIdNot(email, id);
    }
}
