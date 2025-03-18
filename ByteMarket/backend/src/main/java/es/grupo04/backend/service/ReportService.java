package es.grupo04.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.grupo04.backend.dto.NewReportDTO;
import es.grupo04.backend.dto.ReportDTO;
import es.grupo04.backend.dto.ReportMapper;
import es.grupo04.backend.model.Product;
import es.grupo04.backend.model.Report;
import es.grupo04.backend.model.User;
import es.grupo04.backend.repository.ProductRepository;
import es.grupo04.backend.repository.ReportRepository;
import es.grupo04.backend.repository.UserRepository;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReportMapper reportMapper;

    public List<ReportDTO> getAllReports() {
        List<Report> reports = reportRepository.findAll();
        return reportMapper.toDTOs(reports);
    }

    public ReportDTO saveReport(NewReportDTO newReportDTO) {
        User reportCreator = userRepository.findById(newReportDTO.reportCreatorId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Product product = productRepository.findById(newReportDTO.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        
        Report report = new Report();
        report.setReason(newReportDTO.reason());
        report.setDescription(newReportDTO.description());
        report.setUser(reportCreator);
        report.setProduct(product);
        
        report = reportRepository.save(report);
        return reportMapper.toDTO(report);
    }

    public Optional<ReportDTO> findById(Long id) {
        return reportRepository.findById(id)
                .map(reportMapper::toDTO);
    }

    public void delete(Long id) {
        reportRepository.deleteById(id);
    }

    public void deleteAllReportsByUser(User user) {
        List<Report> reports = reportRepository.findByUser(user);
        reportRepository.deleteAll(reports);
    }
}
