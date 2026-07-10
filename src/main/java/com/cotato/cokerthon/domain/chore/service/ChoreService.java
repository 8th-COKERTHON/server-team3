package com.cotato.cokerthon.domain.chore.service;

import com.cotato.cokerthon.domain.chore.dto.response.ChoreResponse;
import com.cotato.cokerthon.domain.chore.repository.ChoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChoreService {

    private final ChoreRepository choreRepository;

    /**
     * 미리 정의된 집안일(기본 집안일) 목록 조회
     */
    public List<ChoreResponse> getAllChores() {
        return choreRepository.findAll().stream()
                .map(ChoreResponse::from)
                .collect(Collectors.toList());
    }
}