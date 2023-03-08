package it.fogliafabrizio.mygestionale;

import it.fogliafabrizio.mygestionale.model.Role;
import it.fogliafabrizio.mygestionale.model.Users;
import it.fogliafabrizio.mygestionale.resporitory.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Calendar;
import java.util.Date;
import java.util.spi.CalendarDataProvider;

@SpringBootApplication
public class MyGestionaleApplication implements CommandLineRunner {

	@Autowired
	private UsersRepository usersRepository;

	public static void main(String[] args) {
		SpringApplication.run(MyGestionaleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		/*BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		Users user = new Users();
		user.setFirstName("Fabrizio");
		user.setLastName("Foglia");
		user.setEmail(".");
		user.setPassword(passwordEncoder.encode("."));
		user.setRole(Role.ADMIN);
		user.setEnabled(true);
		Calendar birthday = Calendar.getInstance();
		birthday.set(Calendar.DAY_OF_MONTH, 23);
		birthday.set(Calendar.MONTH, Calendar.OCTOBER);
		birthday.set(Calendar.YEAR, 1997);
		user.setDateOfBirhtday(birthday);

		usersRepository.save(user);*/
	}
}
