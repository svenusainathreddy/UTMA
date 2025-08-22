package hospital.HospitalWebApp2.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.HospitalWebApp2.model.User;

public interface RegisterRepo extends JpaRepository<User, Long> {
	User findByUsername(String username);

	boolean existsByUsername(String username);
}
