package com.hana.bankai.domain.autotransfer.repository;

import com.hana.bankai.domain.autotransfer.entity.AutoTransfer;
import com.hana.bankai.domain.autotransfer.entity.AutoTransferId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutoTransferRepository extends JpaRepository<AutoTransfer, AutoTransferId> {
}
