package hospital.HospitalWebApp2.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import hospital.HospitalWebApp2.model.Appointment;
import hospital.HospitalWebApp2.model.Report;
import hospital.HospitalWebApp2.repo.ReportRepo;
import hospital.HospitalWebApp2.service.PatientAppointmentService;
import jakarta.servlet.http.HttpSession;

@Controller

public class PatientAppointmentController {

    @Autowired
    private PatientAppointmentService patientAppointmentService;
    @Autowired
    private ReportRepo repRepo;

    // Display all appointments for the logged-in patient
    @GetMapping("/viewall")
    public String viewAppointments(Model model,HttpSession ses) {
    	String username=(String) ses.getAttribute("username");
        List<Appointment> appointments = patientAppointmentService.getPatientAppointments(username);
        model.addAttribute("appointments", appointments);
        return "PatientAppointments"; // This is your Thymeleaf template
    }

    // Cancel an appointment
    @PostMapping("/cancel/{id}")
    public String cancelAppointment(@PathVariable Long id,Model m) {
        patientAppointmentService.cancelAppointment(id);
        m.addAttribute("successMessage", "Appointment Cancelled Successfully");
        return "patientHome";
    }

    // Reschedule an appointment - load the current details into the form
    @GetMapping("/reschedule/{id}")
    public String rescheduleAppointment(@PathVariable Long id, Model model) {
        Appointment appointment = patientAppointmentService.getAppointmentById(id);
        model.addAttribute("appointment", appointment);
        return "appointment-reschedule"; // This is your Thymeleaf template
    }

    // Update the appointment after rescheduling
    @PostMapping("/reschedule")
    public String updateRescheduledAppointment(@ModelAttribute Appointment appointment,Model m) {
    	try {
        patientAppointmentService.rescheduleAppointment(appointment);
        m.addAttribute("successMessage","Appointment Rescheduled Successfully");
        return "patientHome";
    	}
    	catch(RuntimeException e) {
    		m.addAttribute("errorMessage", e.getMessage());
            m.addAttribute("appointment", appointment); // re-bind the form with old values
            return "appointment-reschedule";    		
    	}
       
    }
    @GetMapping("/view-my-reports")
    public String getReports(HttpSession ses,Model m) {
    	String username=(String)ses.getAttribute("username");
    	System.out.println(username);
    	List<Report> reports=patientAppointmentService.getMyReports(username);
    	m.addAttribute("reports",reports);
    	return "Patient-reports";
    }
    @GetMapping("/download-report/{id}")
    public ResponseEntity<?> downloadReport(@PathVariable Long id) throws IOException {
        Optional<Report> optionalReport = repRepo.findById(id);

        if (optionalReport.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Report report = optionalReport.get();
        File file = new File(report.getFilePath());

        if (!file.exists()) {
            return ResponseEntity.status(404).body("File not found on server.");
        }
        
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + report.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
        
    }

}
