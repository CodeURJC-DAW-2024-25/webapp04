package es.grupo04.backend.controller;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;


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
    @Operation (summary= "Retrieve reviews recieved by a user by user ID")
    @GetMapping("/users/{userId}/reviews")
    public ResponseEntity<List<ReviewDTO>> getReviews(@PathVariable Long userId) {
        Optional<UserBasicDTO> user = userService.findById(userId);
        if (user.isEmpty()) {
            throw new NoSuchElementException();
        }

        List<ReviewDTO> reviews = reviewService.getAllReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);

    }

    @Operation (summary= "Retrieve a review by its ID")
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable Long reviewId) {

        ReviewDTO review = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(review);

    }

    @Operation (summary= "Create (post) a review for the user indicated by user ID")
    @PostMapping("/users/{userId}/reviews")
    public ResponseEntity<ReviewDTO> postReview(HttpServletRequest request, @RequestBody NewReviewDTO reviewDTO, @PathVariable Long userId) {
        Principal principal = request.getUserPrincipal();
        Optional<UserBasicDTO> user = userService.findByMail(principal.getName());

        if (user.isEmpty()) {
            return ResponseEntity.status(403).build();
        }

        if(reviewDTO.rating() < 1 || reviewDTO.rating() > 5) {
            return ResponseEntity.badRequest().build();
        }

        NewReviewDTO fullReview = new NewReviewDTO(reviewDTO.rating(), reviewDTO.description(), userId);
        Optional<ReviewDTO> review = reviewService.saveReview(fullReview, user.get().id());
        if(review.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String location = String.format("/api/v1/reviews/%d", review.get().id());

        return ResponseEntity.created(URI.create(location)).body(review.get());
    }
    
    @Operation (summary= "Delete review by its ID")
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        reviewService.delete(reviewId);
        return ResponseEntity.ok("Review deleted successfully");
    }


    //REPORTS
    @Operation (summary= "Retrieve a list of all reports")
    @GetMapping("/reports")
    public ResponseEntity<List<ReportDTO>> getReports() {
        List<ReportDTO> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    @Operation (summary= "Crate (post) a report to the product indicated by product ID")
    @PostMapping("/reports")
    public ResponseEntity<ReportDTO> postReport(HttpServletRequest request, @RequestBody NewReportDTO reportDTO) {
        Principal principal = request.getUserPrincipal();
        UserBasicDTO user = userService.findByMail(principal.getName()).get();

        NewReportDTO fullReport = new NewReportDTO(reportDTO.reason(), reportDTO.description(), reportDTO.productId(), user.id());
        Optional<ReportDTO> report = reportService.saveReport(fullReport);
        if(report.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(report.get());
    }
    
    @Operation (summary= "Delete report by its ID")
    @DeleteMapping("/reports/{reportId}")
    public ResponseEntity<String> deleteReport(@PathVariable Long reportId) {
        reportService.delete(reportId);
        return ResponseEntity.ok("Report deleted successfully");
    }

}
