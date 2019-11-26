package io.pillopl.newmodel.lending.domain.patron;


import io.pillopl.newmodel.catalogue.BookId;
import io.pillopl.newmodel.lending.domain.book.AvailableBook;
import io.pillopl.newmodel.lending.domain.book.BookType;

import java.util.UUID;


public class Fixtures {

    /**
     *
     * TODO: Fixture needs to be completed here.
     *
     */
    public static Patron aRegularPatron() {
        return new Patron(new PatronId(UUID.randomUUID()), PatronType.Regular);
    }

    /**
     *
     * TODO: Fixture needs to be completed here.
     *
     */
    public static Patron aResearcherPatron() {
        return new Patron(new PatronId(UUID.randomUUID()), PatronType.Researcher);
    }

    /**
     *
     * TODO: Fixture needs to be completed here.
     *
     */
    public static Patron aResearcherPatronWithOverdueBooks(int overdueBooks) {
        return new Patron(new PatronId(UUID.randomUUID()), PatronType.Researcher, overdueBooks);
    }

    public static BookId anyBookId() {
        return new BookId(UUID.randomUUID());
    }

    public static AvailableBook restrictedBook() {
        return new AvailableBook(anyBookId(), BookType.Restricted);
    }

    public static AvailableBook circulatingBook() {
        return new AvailableBook(anyBookId(), BookType.Circulating);
    }
}

