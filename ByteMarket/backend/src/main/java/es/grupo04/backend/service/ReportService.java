package es.grupo04.backend.service;

import java.util.List;
import java.util.NoSuchElementException;
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

    @Autowired
    private PurchaseService purchaseService;

    public List<ReportDTO> getAllReports() {
        List<Report> reports = reportRepository.findAll();
        return reportMapper.toDTOs(reports);
    }

    public Optional<ReportDTO> saveReport(NewReportDTO newReportDTO) {
        User reportCreator = userRepository.findById(newReportDTO.reportCreatorId())
                .orElseThrow(() -> new NoSuchElementException());
        Product product = productRepository.findById(newReportDTO.productId())
                .orElseThrow(() -> new NoSuchElementException());

        if(!Constants.VALID_REASONS.contains(newReportDTO.reason())) {
            return Optional.empty();
        }

        if(!purchaseService.hasUserBoughtProduct(newReportDTO.reportCreatorId(), newReportDTO.productId())) {
            return Optional.empty();
        }
        
        Report report = new Report();
        report.setReason(newReportDTO.reason());
        report.setDescription(newReportDTO.description());
        report.setUser(reportCreator);
        report.setProduct(product);
        
        report = reportRepository.save(report);
        return Optional.of(reportMapper.toDTO(report));
    }

    public Optional<ReportDTO> findById(Long id) {
        return reportRepository.findById(id)
                .map(reportMapper::toDTO);
    }

    public void delete(Long id) {
        if (!reportRepository.existsById(id)) {
            throw new NoSuchElementException();
        }
        reportRepository.deleteById(id);
    }

    public void deleteAllReportsByUser(User user) {
        List<Report> reports = reportRepository.findByUser(user);
        reportRepository.deleteAll(reports);
    }
}
