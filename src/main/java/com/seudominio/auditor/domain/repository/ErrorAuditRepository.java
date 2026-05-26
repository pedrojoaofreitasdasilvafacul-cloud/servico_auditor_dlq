package com.seudominio.auditor.domain.repository;

import com.seudominio.auditor.domain.model.ErrorAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorAuditRepository extends JpaRepository<ErrorAudit, String> {
}
