package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			// save a couple of customers

			Client client1 = new Client("Melba", "Morel", "melbamorel@gmail.com", passwordEncoder.encode("123"));
			Client client2= new Client("Adriano", "Balbo", "adriano.balbo00@gmail.com", passwordEncoder.encode("123"));
			Client client3= new Client("Irina", "Saccani", "iri@gmail.com", passwordEncoder.encode("123"));
			Client client4= new Client("admin", "admin", "admin@mindbank.com", passwordEncoder.encode("123"));

			Account account1 = new Account("VIN001", LocalDateTime.now(), 5000.00, client1, AccountType.CURRENT);
			Account account2 = new Account("VIN002", LocalDateTime.now().plusHours(24), 7500.00, client1, AccountType.CURRENT);
			Account account3 = new Account("VIN003", LocalDateTime.now(), 489, client2, AccountType.CURRENT);
			Account account4 = new Account("VIN004", LocalDateTime.now(), 10000000, client3, AccountType.CURRENT);


			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 100.00, "Para la cocucha", LocalDateTime.now());
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, -5000, "por los 10 gramos de polenta", LocalDateTime.now());
			Transaction transaction3 = new Transaction(TransactionType.DEBIT, -50, "Puchito suelto", LocalDateTime.now());


			Loan mortgageLoan = new Loan("Mortgage", 500000.00, Arrays.asList(12,24,36,48,60));
			Loan personalLoan = new Loan("Personal", 100000.00, Arrays.asList(6,12,24));
			Loan carLoan = new Loan("Car", 300000.00, Arrays.asList(6,12,24,36));


			ClientLoan clientLoan1 = new ClientLoan(400000.00, mortgageLoan.getPayments().get(4), client1, mortgageLoan);
			ClientLoan clientLoan2 = new ClientLoan(50000.00, personalLoan.getPayments().get(1), client1, personalLoan);
			ClientLoan clientLoan3 = new ClientLoan(100000.00, personalLoan.getPayments().get(2), client2, personalLoan);
			ClientLoan clientLoan4 = new ClientLoan(200000.00, carLoan.getPayments().get(3), client2, carLoan);

			Card card1 = new Card(client1, CardType.DEBIT, CardColor.GOLD, "4732 2615 9123 7701", 333, LocalDate.now().plusYears(5), LocalDate.now());
			Card card2 = new Card(client1, CardType.CREDIT, CardColor.TITANIUM, "8398 2615 1279 3298", 690, LocalDate.now().plusYears(5), LocalDate.now());
			Card card3 = new Card(client2, CardType.CREDIT, CardColor.SILVER, "4200 6969 1234 5678", 420, LocalDate.now().plusYears(7), LocalDate.now());
			Card card4 = new Card(client3, CardType.DEBIT, CardColor.TITANIUM, "9081 2378 0193 1298", 612, LocalDate.now().plusYears(2), LocalDate.now());

			account1.addTransaction(transaction1);
			account2.addTransaction(transaction2);
			account3.addTransaction(transaction3);

			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);
			client3.addCard(card4);

			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);
			clientRepository.save(client4);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);

			loanRepository.save(mortgageLoan);
			loanRepository.save(personalLoan);
			loanRepository.save(carLoan);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
			cardRepository.save(card4);

		};

	}
	@Autowired
	private PasswordEncoder passwordEncoder;

}