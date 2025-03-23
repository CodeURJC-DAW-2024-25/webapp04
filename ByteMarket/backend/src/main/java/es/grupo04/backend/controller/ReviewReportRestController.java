package es.grupo04.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.grupo04.backend.dto.NewReportDTO;
import es.grupo04.backend.dto.NewReviewDTO;
import es.grupo04.backend.dto.ReportDTO;
import es.grupo04.backend.dto.ReviewDTO;
import es.grupo04.backend.dto.UserBasicDTO;
import es.grupo04.backend.service.ReportService;
import es.grupo04.backend.service.ReviewService;
import es.grupo04.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;





@RestController
@RequestMapping("/api/v1")
public class ReviewReportRestController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReportService reportService;


    //REVIEWS

    @GetMapping("/users/{userId}/reviews")
    public ResponseEntity<List<ReviewDTO>> getReviews(@PathVariable Long userId) {
        Optional<UserBasicDTO> user = userService.findById(userId);
        if (user.isEmpty()) {
            throw new NoSuchElementException();
        }

        List<ReviewDTO> reviews = reviewService.getAllReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);

    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable Long reviewId) {

        ReviewDTO review = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(review);

    }

    @PostMapping("/users/{userId}/reviews")
    public ResponseEntity<ReviewDTO> postReview(HttpServletRequest request, @ModelAttribute NewReviewDTO reviewDTO, @PathVariable Long userId) {
        Principal principal = request.getUserPrincipal();
        UserBasicDTO user = userService.findByMail(principal.getName()).get();

        if(reviewDTO.rating() < 1 || reviewDTO.rating() > 5) {
            return ResponseEntity.badRequest().build();
        }

        NewReviewDTO fullReview = new NewReviewDTO(reviewDTO.rating(), reviewDTO.description(), userId);
        Optional<ReviewDTO> review = reviewService.saveReview(fullReview, user.id());
        if(review.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String location = String.format("/api/v1/reviews/%d", review.get().id());

        return ResponseEntity.created(URI.create(location)).body(review.get());
    }
    
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        reviewService.delete(reviewId);
        return ResponseEntity.ok("Review deleted successfully");
    }


    //REPORTS

    @GetMapping("/reports")
    public ResponseEntity<List<ReportDTO>> getReports() {
        List<ReportDTO> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    @PostMapping("/reports/products/{productId}")
    public ResponseEntity<ReportDTO> postReport(HttpServletRequest request, @ModelAttribute NewReportDTO reportDTO, @PathVariable Long productId) {
        Principal principal = request.getUserPrincipal();
        UserBasicDTO user = userService.findByMail(principal.getName()).get();

        NewReportDTO fullReport = new NewReportDTO(reportDTO.reason(), reportDTO.description(), productId, user.id());
        Optional<ReportDTO> report = reportService.saveReport(fullReport);
        if(report.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(report.get());
    }
    
    @DeleteMapping("/reports/{reportId}")
    public ResponseEntity<String> deleteReport(@PathVariable Long reportId) {
        reportService.delete(reportId);
        return ResponseEntity.ok("Report deleted successfully");
    }

}
