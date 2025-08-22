package hospital.HospitalWebApp2.service;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import hospital.HospitalWebApp2.model.Appointment;
import hospital.HospitalWebApp2.model.Report;
import hospital.HospitalWebApp2.repo.PatientAppointmentRepo;
import hospital.HospitalWebApp2.repo.ReportRepo;

@Service
public class PatientAppointmentService {

    @Autowired
    private PatientAppointmentRepo appointmentRepository;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    ReportRepo repRepo;

    // Fetch all appointments for the logged-in patient (to be replaced with actual patient filtering logic)
    public List<Appointment> getPatientAppointments(String username) {
        // TODO: Replace with actual patient filtering based on logged-in user
        return appointmentRepository.findByUsername(username); 
    }

    // Cancel an appointment
    public void cancelAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    // Get appointment details by ID
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    // Reschedule the appointment
    public void rescheduleAppointment(Appointment updatedAppointment) {
        Appointment existingAppointment = appointmentRepository.findById(updatedAppointment.getId()).orElse(null);
        if (existingAppointment != null) {
            LocalDate newDate = updatedAppointment.getAppointmentDate();
            LocalTime newTime = updatedAppointment.getAppointmentTime();
            String doctor = existingAppointment.getDoctor();

            // Check if the new slot is available for the same doctor
            if (!appointmentService.isSlotAvailable(doctor, newDate, newTime)) {
                throw new RuntimeException("Slot is already booked, please choose a different time.");
            }

            // Update details
            existingAppointment.setAppointmentDate(newDate);
            existingAppointment.setAppointmentTime(newTime);
            appointmentRepository.save(existingAppointment);
        }
    }

	public List<Report> getMyReports(String username) {
		
		return repRepo.findByPatientName(username);
	}
	
    
}
