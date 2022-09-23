package com.mindhub.homebanking.controllers;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mindhub.homebanking.dtos.FilteredTransactionDTO;
import com.mindhub.homebanking.dtos.PaymentDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class TransactionController {
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private ClientRepository clientRepository;
//
//    @Autowired
//    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CardService cardService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(
            @RequestParam Double amount, @RequestParam String description,
            @RequestParam String numberAccountFrom, @RequestParam String numberAccountTo,
            Authentication authentication){
        Account accountFrom = accountService.findByNumber(numberAccountFrom);
        Account accountTo = accountService.findByNumber(numberAccountTo);

        if (amount.toString().isEmpty()){
            return new ResponseEntity<>("Please fill the amount.", HttpStatus.FORBIDDEN);
        }
        if (amount<=0){
            return new ResponseEntity<>("The amount to transfer can't be negative",HttpStatus.FORBIDDEN);
        }
        if (description.isEmpty()){
            return new ResponseEntity<>("Please fill the description!",HttpStatus.FORBIDDEN);
        }
        if (numberAccountFrom.isEmpty()){
            return new ResponseEntity<>("Please select an account of origin.",HttpStatus.FORBIDDEN);
        }
        if ( numberAccountTo.isEmpty()){
            return new ResponseEntity<>("Please select an account to make the transfer.",HttpStatus.FORBIDDEN);
        }
        if(!accountTo.isAccountActive()){
            return new ResponseEntity<>("The account you're trying to reach is actually disabled.", HttpStatus.FORBIDDEN);
        }
        if (!accountFrom.isAccountActive()){
            return new ResponseEntity<>("You can't make a transaction from a disabled account. Please try with another one.", HttpStatus.FORBIDDEN);
        }
        if (numberAccountFrom.equals(numberAccountTo)){
            return new ResponseEntity<>("You cannot make a transaction to this account, try with another one.", HttpStatus.FORBIDDEN);
        }
        if (accountFrom == null){
            return new ResponseEntity<>("This account does not exist.", HttpStatus.FORBIDDEN);
        }
        if (!clientService.findClientByEmail(authentication.getName()).getAccounts().contains(accountFrom)){
            return new ResponseEntity<>("This account does not belong to you.", HttpStatus.FORBIDDEN);
        }
        if (accountTo == null){
            return new ResponseEntity<>("The account you're trying to reach does not exists. Make sure the number is correct.", HttpStatus.FORBIDDEN);
        }
        if (accountFrom.getBalance()<amount){
            return new ResponseEntity<>("You have not enough capital for this transaction.", HttpStatus.FORBIDDEN);
        }
        Transaction transactionDebit = new Transaction(accountFrom, TransactionType.DEBIT,- amount, description, LocalDateTime.now());
        transactionService.saveTransaction(transactionDebit);
        Transaction transactionCredit = new Transaction(accountTo, TransactionType.CREDIT, amount, description, LocalDateTime.now());
        transactionService.saveTransaction(transactionCredit);

        accountFrom.setBalance(accountFrom.getBalance() - amount);
        accountService.saveAccount(accountFrom);
        accountTo.setBalance(accountTo.getBalance() + amount);
        accountService.saveAccount(accountTo);

        return new ResponseEntity<>("The transaction was successful.", HttpStatus.CREATED);

    }
    @PostMapping("/transactions/filtered")
    public ResponseEntity<Object> getFilteredTransaction(
            @RequestBody FilteredTransactionDTO filteredTransactionDTO, Authentication authentication) throws DocumentException, FileNotFoundException {
        Client client = clientService.findClientByEmail(authentication.getName());
        Account account = accountService.findByNumber(filteredTransactionDTO.getAccountNumber());


        if (filteredTransactionDTO.accountNumber.isEmpty()||filteredTransactionDTO.fromDate==null||filteredTransactionDTO.thruDate==null){
            return new ResponseEntity<>("Missing data, please complete all the fields.", HttpStatus.FORBIDDEN);
        }
        if (!account.isAccountActive()){
            return new ResponseEntity<>("That account is disabled, try with another one.",HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("This account is not yours, you cant request this data.", HttpStatus.FORBIDDEN);
        }
        if (account.getTransactions()==null){
            return new ResponseEntity<>("There are not transactions in this account.",HttpStatus.FORBIDDEN);
        }

        Set<Transaction> transactions = transactionService.filterTransactionWithDate(filteredTransactionDTO.fromDate, filteredTransactionDTO.thruDate,account);

        if (transactions.isEmpty()){
            return new ResponseEntity<>("There are not transactions between the dates selected.", HttpStatus.FORBIDDEN);
        }
        createTable(transactions, account);

        return new ResponseEntity<>("Data downloaded",HttpStatus.ACCEPTED);
    }

    public void createTable(Set<Transaction> transactions, Account account) throws FileNotFoundException, DocumentException {

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18,
                Font.BOLD);
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 14,
                Font.BOLD, BaseColor.WHITE);

        Font subFont = new Font(Font.FontFamily.HELVETICA, 12,
                Font.NORMAL);
        try {
            Document document = new Document(PageSize.A4);
            String route = System.getProperty("user.home");
            PdfWriter.getInstance(document, new FileOutputStream(route + "/Desktop/Transaction_Report.pdf"));


            document.open();
            document.setMargins(2,2,2,2);



            /*TITLES*/
            Paragraph title = new Paragraph("MindBank", titleFont);
            title.setSpacingAfter(3);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingBefore(-2);

            Paragraph subTitle = new Paragraph("Account number: " + account.getNumber(), subFont);
            subTitle.setAlignment(Element.ALIGN_CENTER);
            subTitle.setSpacingAfter(1);

            Paragraph date = new Paragraph("Current date: " + LocalDate.now(), subFont);
            date.setSpacingAfter(6);
            date.setAlignment(Element.ALIGN_CENTER);




            /*LOGO*/
            Image img = Image.getInstance("./src/main/resources/static/assets/img/leaf.png");
            img.scaleAbsoluteWidth(80);
            img.scaleAbsoluteHeight(80);
            img.setAlignment(Element.ALIGN_CENTER);

            /*HEADERS*/
            PdfPTable pdfPTable = new PdfPTable(4);
            PdfPCell pdfPCell1 = new PdfPCell(new Paragraph("Description", headerFont));
            PdfPCell pdfPCell2 = new PdfPCell(new Paragraph("Date", headerFont));
            PdfPCell pdfPCell3 = new PdfPCell(new Paragraph("Type", headerFont));
            PdfPCell pdfPCell4 = new PdfPCell(new Paragraph("Amount", headerFont));
            pdfPCell1.setBackgroundColor(new BaseColor(21, 100, 200));
            pdfPCell2.setBackgroundColor(new BaseColor(21, 100, 200));
            pdfPCell3.setBackgroundColor(new BaseColor(21, 100, 200));
            pdfPCell4.setBackgroundColor(new BaseColor(21, 100, 200));
            pdfPCell1.setBorder(0);
            pdfPCell2.setBorder(0);
            pdfPCell3.setBorder(0);
            pdfPCell4.setBorder(0);
            pdfPTable.addCell(pdfPCell1);
            pdfPTable.addCell(pdfPCell2);
            pdfPTable.addCell(pdfPCell3);
            pdfPTable.addCell(pdfPCell4);

            /*TABLE OF TRANSACTIONS*/
            transactions.forEach(transaction -> {

                PdfPCell pdfPCell5 = new PdfPCell(new Paragraph(transaction.getDescription(), subFont));
                PdfPCell pdfPCell6 = new PdfPCell(new Paragraph(transaction.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), subFont));
                PdfPCell pdfPCell7 = new PdfPCell(new Paragraph(String.valueOf(transaction.getType()), subFont));
                PdfPCell pdfPCell8 = new PdfPCell(new Paragraph("$" + String.valueOf(transaction.getAmount()), subFont));
                pdfPCell5.setBorder(1);
                pdfPCell6.setBorder(1);
                pdfPCell7.setBorder(1);
                pdfPCell8.setBorder(1);

                pdfPTable.addCell(pdfPCell5);
                pdfPTable.addCell(pdfPCell6);
                pdfPTable.addCell(pdfPCell7);
                pdfPTable.addCell(pdfPCell8);
            });

            document.add(img);
            document.add(title);
            document.add(subTitle);
            document.add(date);
            document.add(pdfPTable);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    @PostMapping("/transactions/payment")
        ResponseEntity<Object> paymentApp(
                @RequestBody PaymentDTO paymentDTO, Authentication authentication){
        Client client = clientService.findClientByEmail(authentication.getName());
        Account account = accountService.findByNumber(paymentDTO.getAccountNumber());
        Card card = cardService.findCardByNumber(paymentDTO.getCardNumber());


        if (!card.isCardActive()){
            return new ResponseEntity<>("You cant make a payment with a disabled card.", HttpStatus.FORBIDDEN);
        }
        if (card.getThruDate().isBefore(LocalDate.now())){
            return new ResponseEntity<>("This card is expired, try with another one.", HttpStatus.FORBIDDEN);
        }
        if (!client.getCards().contains(card)){
            return new ResponseEntity<>("This card does not belong to you, try with another one.", HttpStatus.FORBIDDEN);
        }
        if (account.getBalance()<paymentDTO.getAmount()){
            return new ResponseEntity<>("This account does not have founds, try with another one.",HttpStatus.FORBIDDEN);
        }
        if (!account.isAccountActive()){
            return new ResponseEntity<>("You cant make a payment with a disabled account, please try with another one.", HttpStatus.FORBIDDEN);
        }
        if (!card.getCvv().equals(paymentDTO.getCvv())){
            return new ResponseEntity<>("Wrong CVV, try again.", HttpStatus.FORBIDDEN);
        }


        Transaction transactionPayment = new Transaction(account, TransactionType.DEBIT,-paymentDTO.getAmount(), paymentDTO.getDescription(),LocalDateTime.now());
        transactionService.saveTransaction(transactionPayment);
        account.setBalance(account.getBalance()-paymentDTO.getAmount());
        accountService.saveAccount(account);
        return new ResponseEntity<>("The payment was successful!",HttpStatus.ACCEPTED);
    }


}
