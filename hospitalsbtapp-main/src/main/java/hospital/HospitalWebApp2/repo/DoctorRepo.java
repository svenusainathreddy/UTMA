package hospital.HospitalWebApp2.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.HospitalWebApp2.model.User;

public interface DoctorRepo extends JpaRepository<User, Long> {
	List<User> findByStaffMemberTrue();

	User findByUsername(String username);
}
