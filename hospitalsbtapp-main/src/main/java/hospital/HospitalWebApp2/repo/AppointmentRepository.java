package hospital.HospitalWebApp2.repo;

import hospital.HospitalWebApp2.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
	List<Appointment> findByDoctor(String username);

	List<Appointment> findByDoctorAndAppointmentDate(String doctor, LocalDate appointmentDate);

	Appointment findByDoctorAndAppointmentDateAndAppointmentTime(String doctor, LocalDate appointmentDate,
			LocalTime appointmentTime);
}
