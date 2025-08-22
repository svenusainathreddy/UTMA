package hospital.HospitalWebApp2.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.HospitalWebApp2.model.Appointment;
import hospital.HospitalWebApp2.model.User;
import hospital.HospitalWebApp2.repo.AppointmentRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    DoctorAppointmentService doctorService;

    public boolean isSlotAvailable(String doctor, LocalDate date, LocalTime time) {
        Appointment appointment = appointmentRepository.findByDoctorAndAppointmentDateAndAppointmentTime(doctor, date, time);
        return appointment == null;
    }

    public void bookAppointment(Appointment appointment) {
        if (!isSlotAvailable(appointment.getDoctor(), appointment.getAppointmentDate(), appointment.getAppointmentTime())) {
            throw new RuntimeException("Slot is already booked, please choose a different time.");
        }
        appointment.setSlotAvailable(true);
        appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointmentsByDoctorId(String username) {
        return appointmentRepository.findByDoctor(username);
    }
    public List<User> getAllDoctors(){
    	return doctorService.getAll();
    }
}
