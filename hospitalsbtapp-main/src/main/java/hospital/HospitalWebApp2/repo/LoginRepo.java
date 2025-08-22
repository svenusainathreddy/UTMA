package hospital.HospitalWebApp2.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hospital.HospitalWebApp2.model.User;

public interface LoginRepo extends JpaRepository<User, Long> {
	public User findByUsername(String username);
}
