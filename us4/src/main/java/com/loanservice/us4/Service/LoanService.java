package com.loanservice.us4.Service;

import com.loanservice.us4.Dto.LoanDTO;
import com.loanservice.us4.Dto.LoanReturnDTO;
import com.loanservice.us4.Entity.*;
import com.loanservice.us4.Exception.*;
import com.loanservice.us4.Repository.BookRepository;
import com.loanservice.us4.Repository.LoanRepository;
import com.loanservice.us4.Repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.loanservice.us4.Entity.BookStatus.AVAILABLE;
import static com.loanservice.us4.Entity.BookStatus.LOANED;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class LoanService {


    private static final BigDecimal LATE_FEE_PER_DAY = BigDecimal.valueOf(5);
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    public LoanRecord issueBook(LoanDTO loan) {
        Long userId = loan.getUserId();
        Long bookId = loan.getBookId();

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotAvailableException("Book not found"));


        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new BookNotAvailableException("Book is not available for loan");
        }

        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<LoanRecord> loanRecords = loanRepository.findByUserId(user.getId());
        int limit = 4;
        if (loanRecords.size() >= 4) {
            throw new ExceededLoanLimitException("The user has exceeded the maximum loan limit");
        }


        LoanRecord loanRecord = new LoanRecord();
        loanRecord.setUserId(userId);
        loanRecord.setBookId(bookId);
        loanRecord.setIssueDate(LocalDate.now());
        loanRecord.setDueDate(LocalDate.now().plusDays(14));


        loanRecord = loanRepository.save(loanRecord);

        book.setStatus(LOANED);
        bookRepository.save(book);
        userRepository.save(user);
        return loanRecord;
    }

    public LoanRecord returnBook(LoanReturnDTO loanReturn) {
        LoanRecord loanRecord = loanRepository.findById(loanReturn.getId())
                .orElseThrow(() -> new LoanNotFoundException("LoanRecord Not Found"));
        Book book = bookRepository.findById(loanRecord.getBookId())
                .orElseThrow(() -> new BookNotAvailableException("Book Not Available"));
        if (loanRecord != null && LOANED == book.getStatus()) {
            LocalDate returnDate = loanReturn.getReturnDate();

            // Set the return date to the custom return date if provided, otherwise use the current date
            if (returnDate != null) {
                loanRecord.setReturnDate(returnDate);
            } else {
                loanRecord.setReturnDate(LocalDate.now());
            }
            book.setStatus(AVAILABLE);
            loanRepository.save(loanRecord);
            bookRepository.save(book);
            return loanRecord;
        } else {
            throw new LoanNotFoundException(("Loan Not Found"));
        }
    }

    public ResponseEntity<?> returnBook(Long loanId) {

        LoanRecord loanRecord = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        LocalDate dueDate = loanRecord.getDueDate();
        LocalDate today = LocalDate.now();
        BigDecimal lateFee = BigDecimal.ZERO;

        if (today.isAfter(dueDate)) {
            long daysLate = DAYS.between(dueDate, today);
            lateFee = new BigDecimal(daysLate).multiply(LATE_FEE_PER_DAY);

            loanRecord.setLateFee(lateFee);
            loanRepository.save(loanRecord);
            UserAccount user = userRepository.findById(loanRecord.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            user.setTotalLateFees(user.getTotalLateFees().add(lateFee));
            userRepository.save(user);
        }
            return new ResponseEntity<>(loanRecord, HttpStatus.CREATED);
        }






    public LoanRecord clearLateFee(Long loanId) {
        LoanRecord loanRecord = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        BigDecimal lateFee = loanRecord.getLateFee();

        if (lateFee != null && lateFee.compareTo(BigDecimal.ZERO) > 0) {
            loanRecord.setLateFee(BigDecimal.ZERO);
            loanRepository.save(loanRecord);

            UserAccount user = userRepository.findById(loanRecord.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            user.setTotalLateFees(user.getTotalLateFees().subtract(lateFee));
            userRepository.save(user);
        } else {
            throw new NoLateFeesException("No late fees to clear for this loan.");
        }
        return loanRecord;
    }


    public LoanRecord updateLateFee(Long loanId, BigDecimal lateFee) {
        LoanRecord loanRecord = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found"));

        // Update the lateFee for the LoanRecord
        loanRecord.setLateFee(lateFee);
        loanRepository.save(loanRecord);

        // Update the UserAccount's totalLateFees
        UserAccount user = userRepository.findById(loanRecord.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Update the totalLateFees with the new lateFee
        user.setTotalLateFees(lateFee);
        userRepository.save(user);

        return loanRecord;
    }
}





















