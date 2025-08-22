package hospital.HospitalWebApp2.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import hospital.HospitalWebApp2.model.Appointment;
import hospital.HospitalWebApp2.model.Report;
import hospital.HospitalWebApp2.repo.ReportRepo;
import hospital.HospitalWebApp2.service.DoctorService;
import jakarta.servlet.http.HttpSession;

@Controller
public class DoctorController {
	@Autowired
	DoctorService docService;
	@Autowired
	ReportRepo repRepo;

	// Displays All Appointments
	@GetMapping("/myappointments")
	public String getAllAppointments(Model m, HttpSession ses) {
		String username = (String) ses.getAttribute("docusername");
		System.out.println(username);
		List<Appointment> appointments = docService.getAllAppointments(username);
		m.addAttribute("appointments", appointments);
		return "doctor-appointments";
	}

	// Cancel The Appointment
	@PostMapping("/deny/{id}")
	public String denyAppointment(@PathVariable long id, Model m,HttpSession ses) {
		docService.denyAppointment(id,ses);
		m.addAttribute("successMessage", "Appointment Cancelled Successfully");
		return "doctor-home";

	}

	// Adds The Report
	@GetMapping("/addreport")
	public String addReport() {
		return "check-patient";
	}

	// Checks Patients Existed or Not
	@GetMapping("/checkpatient")
	public String checkPatientExisted(@RequestParam String username, Model m, HttpSession ses) {
		try {
			docService.checkPatient(username);
			ses.setAttribute("patientname", username);
			return "upload-report";
		} catch (RuntimeException e) {
			m.addAttribute("errorMessage", e.getMessage());
			return "check-patient";
		}
	}

	// Uploads a report for Patient
	@PostMapping("/uploadreport")
	public String addReport(@RequestParam String reportName, @RequestParam MultipartFile file, Model model,
			HttpSession ses) {
		try {
			docService.addReport(reportName, file, ses);
			model.addAttribute("successMessage", "Report uploaded successfully.");
			return "doctor-home";

		} catch (Exception e) {
			model.addAttribute("message", "Upload failed: " + e.getMessage());
			return "upload-report";
		}
	}

	// Fetches All Reports
	@GetMapping("/view-reports")
	public String viewReports(Model m) {
		List<Report> reports = docService.getAllReports();
		m.addAttribute("reports", reports);
		return "view-reports";
	}
	
	@GetMapping("/view-report/{id}")
    public ResponseEntity<FileSystemResource> viewReport(@PathVariable("id") Long id) throws IOException {
        // Fetch the report based on the ID
        Report report = repRepo.findById(id).orElseThrow(() -> new RuntimeException("Report not found"));
        
        File reportFile = new File(report.getFilePath());

        // Set appropriate media type based on the file extension (for example, PDF or image)
        String contentType = getContentTypeForFile(reportFile);
        
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + reportFile.getName() + "\"")
            .body(new FileSystemResource(reportFile));
    }
	private String getContentTypeForFile(File file) {
        // Simple example: You can add more file types based on your needs
        if (file.getName().endsWith(".pdf")) {
            return "application/pdf";
        } else if (file.getName().endsWith(".docx")) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        } else if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (file.getName().endsWith(".png")) {
            return "image/png";
        } else if (file.getName().endsWith(".txt")) {
            return "text/plain";
        }
        return "application/octet-stream"; // Default for other types
    }

	// Logouts The User
	@GetMapping("/logout")
	public String logout(HttpSession ses) {
		ses.invalidate();
		return "index";
	}

}
