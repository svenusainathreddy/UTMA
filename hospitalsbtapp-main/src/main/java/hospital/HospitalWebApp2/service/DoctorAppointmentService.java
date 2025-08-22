package hospital.HospitalWebApp2.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import hospital.HospitalWebApp2.model.Appointment;
import hospital.HospitalWebApp2.model.Report;
import hospital.HospitalWebApp2.model.User;
import hospital.HospitalWebApp2.repo.AppointmentRepository;
import hospital.HospitalWebApp2.repo.DoctorRepo;
import hospital.HospitalWebApp2.repo.ReportRepo;
import jakarta.servlet.http.HttpSession;

@Service
public class DoctorAppointmentService {

	@Autowired
	private AppointmentRepository appointmentRepository;
	@Autowired
	private DoctorRepo docRepo;
	@Autowired
	private ReportRepo recRepo;

	public List<Appointment> getAllAppointments(String username) {
		return appointmentRepository.findByDoctor(username);
	}

	public void denyAppointment(long id) {
		docRepo.deleteById(id);
	}

	public List<User> getAll() {
		return docRepo.findByStaffMemberTrue();
	}

	public void denyAppointment(Long id) {
		Appointment appointment = appointmentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Appointment not found"));
		appointment.setStatus("DENIED");
		appointmentRepository.save(appointment);
	}

	public User checkPatient(String username) {
		User patient = docRepo.findByUsername(username);
		if (patient != null)
			return patient;
		else
			throw new RuntimeException("Patient not Existed !!");
	}

	public void addReport(String reportName, MultipartFile file, HttpSession ses) throws IOException {
		final String baseDir = "C:/MedicalReports/";
		Path patientFolder = Paths.get(baseDir, (String) ses.getAttribute("patientname"));
		Files.createDirectories(patientFolder);

		String storedFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		Path destination = patientFolder.resolve(storedFileName);
		Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

		Report report = new Report();
		report.setPatientName((String) ses.getAttribute("patientname"));
		report.setReportName(reportName);
		report.setFileName(file.getOriginalFilename());
		report.setFilePath(destination.toString());
		report.setDoctorName((String) ses.getAttribute("docusername"));
		recRepo.save(report);
	}

	public List<Report> getAllReports() {

		return recRepo.findAll();
	}

//	emailService.sendAppointmentDeniedEmail(
//	        appt.getPatientEmail(),
//	        appt.getPatientName(),
//	        appt.getDoctor()
//	    );
}
