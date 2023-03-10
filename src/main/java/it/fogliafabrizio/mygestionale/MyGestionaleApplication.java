package it.fogliafabrizio.mygestionale;

import it.fogliafabrizio.mygestionale.model.Role;
import it.fogliafabrizio.mygestionale.model.UserGroups;
import it.fogliafabrizio.mygestionale.model.Users;
import it.fogliafabrizio.mygestionale.model.Visibility;
import it.fogliafabrizio.mygestionale.repository.UserGroupsRepository;
import it.fogliafabrizio.mygestionale.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SpringBootApplication
public class MyGestionaleApplication implements CommandLineRunner {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private UserGroupsRepository groupsRepository;

	public static void main(String[] args) {
		SpringApplication.run(MyGestionaleApplication.class, args);
	}

	@Override
	//@Transactional
	public void run(String... args) throws Exception {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		/*	CREAZIONE UTENTE ADMIN */
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
		user.setDateOfBirthday(birthday);
		usersRepository.save(user);

		Users user2 = new Users();
		user2.setFirstName("Gabriele");
		user2.setLastName("Carlesso");
		user2.setEmail("prova");
		user2.setPassword(passwordEncoder.encode("prova"));
		user2.setRole(Role.USER);
		user2.setEnabled(true);
		Calendar birthday2 = Calendar.getInstance();
		birthday2.set(Calendar.DAY_OF_MONTH, 23);
		birthday2.set(Calendar.MONTH, Calendar.OCTOBER);
		birthday2.set(Calendar.YEAR, 1997);
		user.setDateOfBirthday(birthday2);
		usersRepository.save(user2);

		/*UserGroups groups = new UserGroups();
		groups.setName("Prova");
		groups.setVisibility(Visibility.PUBLIC);
		groups.setUserAdmin(user);
		List<Users> list = new ArrayList<>();
		list.add(user2);
		list.add(user);
		groups.setUserMembers(list);
		groupsRepository.save(groups);*/

		/* Prova
		Prova p = new Prova();
		p.setProva("CIAO");
		Users users = usersRepository.findByEmail(".");
		p.setUser(users);
		provaRepository.save(p);*/

		/*	EVENTS PROVA */
		/*Events events = new Events();

		events.setName("prova Evento");
		events.setVisibility(Visibility.PUBLIC);
		events.setDescription("Desrcizoine prova");
		events.setAllUserInvitated(false);

		Calendar date = Calendar.getInstance();
		date.set(Calendar.DAY_OF_MONTH, 10);
		date.set(Calendar.MONTH, Calendar.OCTOBER);
		date.set(Calendar.YEAR, 1997);
		events.setDate(date);
		Date bhour = new Date();
		events.setBeginHour(bhour);
		events.setEndHour(bhour);

		Users user = usersRepository.findByEmail(".");
		events.setUserOwner(user);

		eventsRepository.save(events);*/

	}

}
