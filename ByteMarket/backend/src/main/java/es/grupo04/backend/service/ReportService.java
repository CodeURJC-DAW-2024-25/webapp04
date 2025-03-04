package es.grupo04.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import es.grupo04.backend.model.Report;
import es.grupo04.backend.model.User;
import es.grupo04.backend.repository.ReportRepository;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }

    public Optional<Report> findById(Long id) {
        return reportRepository.findById(id);
    }

    public void delete(Long id) {
        reportRepository.deleteById(id);
    }
    public void deleteAllReportsByUser(User user) {
        List<Report> reports = reportRepository.findByUser(user);
        for (Report report : reports) {
            reportRepository.delete(report);   
        }
    }
}
