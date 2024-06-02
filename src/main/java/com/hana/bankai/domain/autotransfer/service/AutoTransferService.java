package com.hana.bankai.domain.autotransfer.service;

import com.hana.bankai.domain.account.dto.AccountRequestDto;
import com.hana.bankai.domain.account.entity.Account;
import com.hana.bankai.domain.account.service.AccountService;
import com.hana.bankai.domain.autotransfer.entity.AutoTransfer;
import com.hana.bankai.domain.autotransfer.repository.AutoTransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AutoTransferService {

    private final AutoTransferRepository autoTransferRepository;
    private final AccountService accountService;

    public void autoTransfer() {
        // 자동이체 DB Table 에서 모든 데이터를 조회
        List<AutoTransfer> autoTransferList = autoTransferRepository.findAll();
        for(AutoTransfer autoTransfer : autoTransferList) {
            // 자동이체 실행
            setAutoTransfer(autoTransfer);
        }
    }

    public void setAutoTransfer(AutoTransfer autoTransfer) {
        // 오늘이 자동이체 날이면 자동이체 실행
        if(autoTransfer.getAutoTransferId().getAtDate() != LocalDate.now().getDayOfMonth()) return;

        // 출금 계좌 user ID 조회
        Account outAccount = autoTransfer.getAccount();
        String userId = outAccount.getUser().getUserId();

        // 이체(Transfer) DTO 생성
        AccountRequestDto.Transfer transfer = AccountRequestDto.Transfer.from(autoTransfer);

        // 이체 Service 호출
        accountService.transfer(transfer, userId);
    }

}

