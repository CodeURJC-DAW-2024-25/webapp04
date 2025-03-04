package es.grupo04.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import es.grupo04.backend.model.Report;
import es.grupo04.backend.model.User;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByUser(User user);
}
