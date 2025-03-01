package es.grupo04.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.grupo04.backend.model.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
