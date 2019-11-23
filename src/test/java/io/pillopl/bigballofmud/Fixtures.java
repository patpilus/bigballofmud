package io.pillopl.bigballofmud;

import io.pillopl.bigballofmud.entities.BookEntity;
import io.pillopl.bigballofmud.entities.BookHolderEntity;
import io.pillopl.bigballofmud.repositories.BookHolderRepository;
import io.pillopl.bigballofmud.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static io.pillopl.bigballofmud.entities.BookEntity.BookLendingState.Available;
import static io.pillopl.bigballofmud.entities.BookEntity.BookState.InLending;
import static io.pillopl.bigballofmud.entities.BookEntity.BookType.Circulating;
import static io.pillopl.bigballofmud.entities.BookEntity.BookType.Restricted;

@Component
class Fixtures {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookHolderRepository bookHolderRepository;

    BookHolderEntity aPatron(BookHolderEntity.HolderType type) {
        BookHolderEntity holder = new BookHolderEntity();
        holder.setType(type);
        holder.setHolderName("name");
        holder = bookHolderRepository.save(holder);
        return holder;
    }

    BookHolderEntity aRegularPatron() {
        return aPatron(BookHolderEntity.HolderType.Regular);
    }

    BookHolderEntity aResearcherPatron() {
        return aPatron(BookHolderEntity.HolderType.Researcher);
    }


    BookEntity aCirculatingBookAvailableForLending() {
        BookEntity book = new BookEntity();
        book.setIsbn("0198526636");
        book.setAuthor("author");
        book.setTitle("title");
        book.setState(InLending);
        book.setLendingState(Available);
        book.setType(Circulating);
        book.setLendingCostPerDay(BigDecimal.ZERO);
        book = bookRepository.save(book);
        return book;
    }


    public BookEntity aRestrictedBookAvailableForLending() {
        BookEntity book = new BookEntity();
        book.setIsbn("0198526636");
        book.setAuthor("author");
        book.setTitle("title");
        book.setState(InLending);
        book.setLendingState(Available);
        book.setType(Restricted);
        book.setLendingCostPerDay(BigDecimal.ZERO);
        book = bookRepository.save(book);
        return book;
    }
}
