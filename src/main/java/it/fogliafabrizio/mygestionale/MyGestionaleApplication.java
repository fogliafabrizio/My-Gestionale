package it.fogliafabrizio.mygestionale;

import it.fogliafabrizio.mygestionale.model.*;
import it.fogliafabrizio.mygestionale.repository.EventsRepository;
import it.fogliafabrizio.mygestionale.repository.UserGroupsRepository;
import it.fogliafabrizio.mygestionale.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SpringBootApplication
public class MyGestionaleApplication implements CommandLineRunner {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private UserGroupsRepository groupsRepository;

	@Autowired
	private EventsRepository eventsRepository;

	public static void main(String[] args) {
		SpringApplication.run(MyGestionaleApplication.class, args);
	}

	@Override
	//@Transactional
	public void run(String... args) throws Exception {
		/*BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			//CREAZIONE UTENTE ADMIN
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

		for(int i=0; i<10; i++){
			Users userProva = new Users();
			userProva.setFirstName("Prova " + i);
			userProva.setLastName("Prova " + i);
			userProva.setEmail("prova" + i);
			userProva.setPassword(passwordEncoder.encode("prova" + i));
			userProva.setRole(Role.USER);
			userProva.setEnabled(true);
			Calendar birthdayProva = Calendar.getInstance();
			birthdayProva.set(Calendar.DAY_OF_MONTH, i);
			birthdayProva.set(Calendar.MONTH, Calendar.OCTOBER);
			birthdayProva.set(Calendar.YEAR, 1997);
			user.setDateOfBirthday(birthdayProva);
			usersRepository.save(userProva);
		}

		UserGroups groups = new UserGroups();
		groups.setName("Prova22");
		groups.setVisibility(Visibility.PUBLIC);
		groups.setUserAdmin(usersRepository.findById(2L).orElseThrow());
		List<Users> list = new ArrayList<>();
		list.add(usersRepository.findById(1L).orElseThrow());
		list.add(usersRepository.findById(2L).orElseThrow());
		groups.setUserMembers(list);
		groupsRepository.save(groups);

		 //FESTIVITA

		for(int i=2023; i<2043; i++){
			Events events = new Events();
			events.setName("Natale");
			events.setAllDay(true);
			Calendar data = Calendar.getInstance();
			data.set(Calendar.DAY_OF_MONTH, 25);
			data.set(Calendar.MONTH, Calendar.DECEMBER);
			data.set(Calendar.YEAR, i);
			events.setDate(data);
			events.setDescription("Buon natale");
			events.setAllUserInvitated(true);
			events.setVisibility(Visibility.PUBLIC);
			events.setFestivity(true);
			eventsRepository.save(events);
		}

		for(int i=2023; i<2043; i++){
			Events events = new Events();
			events.setName("Santo Stefano");
			events.setAllDay(true);
			Calendar data = Calendar.getInstance();
			data.set(Calendar.DAY_OF_MONTH, 26);
			data.set(Calendar.MONTH, Calendar.DECEMBER);
			data.set(Calendar.YEAR, i);
			events.setDate(data);
			events.setDescription("Buon natale");
			events.setAllUserInvitated(true);
			events.setVisibility(Visibility.PUBLIC);
			events.setFestivity(true);
			eventsRepository.save(events);
		}

		for(int i=2023; i<2043; i++){
			Events events = new Events();
			events.setName("Santo Stefano");
			events.setAllDay(true);
			Calendar data = Calendar.getInstance();
			data.set(Calendar.DAY_OF_MONTH, 26);
			data.set(Calendar.MONTH, Calendar.DECEMBER);
			data.set(Calendar.YEAR, i);
			events.setDate(data);
			events.setDescription("Buon Santo Stefano a tutti!");
			events.setAllUserInvitated(true);
			events.setVisibility(Visibility.PUBLIC);
			events.setFestivity(true);
			eventsRepository.save(events);
		}

		for(int i=2023; i<2043; i++){
			Events events = new Events();
			events.setName("Capodanno");
			events.setAllDay(true);
			Calendar data = Calendar.getInstance();
			data.set(Calendar.DAY_OF_MONTH, 1);
			data.set(Calendar.MONTH, Calendar.JANUARY);
			data.set(Calendar.YEAR, i);
			events.setDate(data);
			events.setDescription("Buon Anno Nuovo a tutti!");
			events.setAllUserInvitated(true);
			events.setVisibility(Visibility.PUBLIC);
			events.setFestivity(true);
			eventsRepository.save(events);
		}

		for(int i=2023; i<2043; i++){
			Events events = new Events();
			events.setName("Epifania");
			events.setAllDay(true);
			Calendar data = Calendar.getInstance();
			data.set(Calendar.DAY_OF_MONTH, 6);
			data.set(Calendar.MONTH, Calendar.JANUARY);
			data.set(Calendar.YEAR, i);
			events.setDate(data);
			events.setDescription("Buona Epifania a tutti!");
			events.setAllUserInvitated(true);
			events.setVisibility(Visibility.PUBLIC);
			events.setFestivity(true);
			eventsRepository.save(events);
		}

		for(int i=2023; i<2043; i++){
			Events events = new Events();
			events.setName("Anniversario della Liberazione");
			events.setAllDay(true);
			Calendar data = Calendar.getInstance();
			data.set(Calendar.DAY_OF_MONTH, 25);
			data.set(Calendar.MONTH, Calendar.APRIL);
			data.set(Calendar.YEAR, i);
			events.setDate(data);
			events.setDescription("Festa della Liberazione");
			events.setAllUserInvitated(true);
			events.setVisibility(Visibility.PUBLIC);
			events.setFestivity(true);
			eventsRepository.save(events);
		}

		for(int i=2023; i<2043; i++){
			Events events = new Events();
			events.setName("Festa del Lavoro");
			events.setAllDay(true);
			Calendar data = Calendar.getInstance();
			data.set(Calendar.DAY_OF_MONTH, 1);
			data.set(Calendar.MONTH, Calendar.MAY);
			data.set(Calendar.YEAR, i);
			events.setDate(data);
			events.setDescription("Buona Festa del Lavoro a tutti");
			events.setAllUserInvitated(true);
			events.setVisibility(Visibility.PUBLIC);
			events.setFestivity(true);
			eventsRepository.save(events);
		}

		for(int i=2023; i<2043; i++){
			Events events = new Events();
			events.setName("Festa della Repubblica");
			events.setAllDay(true);
			Calendar data = Calendar.getInstance();
			data.set(Calendar.DAY_OF_MONTH, 2);
			data.set(Calendar.MONTH, Calendar.JUNE);
			data.set(Calendar.YEAR, i);
			events.setDate(data);
			events.setDescription("Buona Festa della Repubblica a tutti!");
			events.setAllUserInvitated(true);
			events.setVisibility(Visibility.PUBLIC);
			events.setFestivity(true);
			eventsRepository.save(events);
		}

		for(int i=2023; i<2043; i++){
			Events events = new Events();
			events.setName("Ognissanti");
			events.setAllDay(true);
			Calendar data = Calendar.getInstance();
			data.set(Calendar.DAY_OF_MONTH, 1);
			data.set(Calendar.MONTH, Calendar.NOVEMBER);
			data.set(Calendar.YEAR, i);
			events.setDate(data);
			events.setDescription("Buon onomastico a tutti!");
			events.setAllUserInvitated(true);
			events.setVisibility(Visibility.PUBLIC);
			events.setFestivity(true);
			eventsRepository.save(events);
		}

		for(int i=2023; i<2043; i++){
			Events events = new Events();
			events.setName("Immacolata Concezione");
			events.setAllDay(true);
			Calendar data = Calendar.getInstance();
			data.set(Calendar.DAY_OF_MONTH, 8);
			data.set(Calendar.MONTH, Calendar.DECEMBER);
			data.set(Calendar.YEAR, i);
			events.setDate(data);
			events.setDescription("Buona giornata dell'Immacolata a tutti!");
			events.setAllUserInvitated(true);
			events.setVisibility(Visibility.PUBLIC);
			events.setFestivity(true);
			eventsRepository.save(events);
		}

*/
	}

}
