package hospital.HospitalWebApp2.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.HospitalWebApp2.model.Report;

public interface ReportRepo extends JpaRepository<Report,Long> {
  List<Report> findByPatientName(String username);
}
