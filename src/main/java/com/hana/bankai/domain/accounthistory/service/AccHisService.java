package com.hana.bankai.domain.accounthistory.service;

import com.hana.bankai.domain.accounthistory.repository.AccHisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccHisService {

    final private AccHisRepository accHisRepository;
}
