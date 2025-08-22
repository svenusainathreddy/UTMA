package hospital.HospitalWebApp2.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hospital.HospitalWebApp2.model.User;
import hospital.HospitalWebApp2.repo.RegisterRepo;

@Service
public class RegisterService {
	@Autowired
	private RegisterRepo userRepository;

	private static final String BASE_PATH = "C:/HospitalRecords/records/";

	public User registerUser(User user) {
		if (userRepository.existsByUsername(user.getUsername())) {
			throw new RuntimeException("Username already exists. Please choose a different one.");
		}

		String userFolder = BASE_PATH + user.getUsername();
		File folder = new File(userFolder);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		user.setRecordPath(userFolder);
		return userRepository.save(user);
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
}
