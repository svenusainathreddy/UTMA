package hospital.HospitalWebApp2.repo;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hospital.HospitalWebApp2.model.Appointment;

@Repository
public interface PatientAppointmentRepo extends JpaRepository<Appointment, Long> {

    List<Appointment> findByUsername(String username);
}

