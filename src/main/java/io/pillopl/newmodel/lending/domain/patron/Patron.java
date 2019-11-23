package io.pillopl.newmodel.lending.domain.patron;

import io.pillopl.newmodel.catalogue.BookId;
import io.pillopl.newmodel.lending.domain.book.AvailableBook;
import io.pillopl.newmodel.lending.domain.book.Book;
import io.pillopl.newmodel.lending.domain.patron.events.BookCollected;
import io.pillopl.newmodel.lending.domain.patron.events.BookPlacedOnHold;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.Optional;
import java.util.Set;


/**
 * Task #2: Implement this aggregate
 * Make PlacingOnHoldBookScenarios and CollectingPlacedOnHoldBookScenarios pass!
 * Remember that Fixtures, for instance aRegularPatron() should be changed so that it returns a regular patron in the meaning of your new model.
 * There is one scenario to implement in CollectingPlacedOnHoldBookScenarios too. Implement it, make sure it passes.
 * Think about why we pass AvailableBook instead of Book to placeOnHold() method. Does it have any benefit?
 */
@AllArgsConstructor
public class Patron {

    @NonNull @Getter
    final PatronId patronId;

    @NonNull @Getter
    final PatronType patronType;

    int holdSize;

    int overDueBooks;

    public Optional<BookPlacedOnHold> placeOnHold(AvailableBook book, HoldDuration holdDuration) {
        if (isRegular() && book.isRestricted()) {
            return Optional.empty();
        }

        if (isRegular() && holdDuration.isOpenEnded()) {
            return Optional.empty();
        }

        if (holdLimitReached()) {
            return Optional.empty();
        }

        if (overdueBooksLimitReached()) {
            return Optional.empty();
        }

        return Optional.of(new BookPlacedOnHold(book.getBookId(), patronId, holdDuration.getDays().get()));
    }

    private boolean overdueBooksLimitReached() {
        return overDueBooks > 1;
    }

    private boolean holdLimitReached() {
        return holdSize >= 5;
    }

    public Optional<BookCollected> collect(BookId bookId, CollectDuration collectDuration) {
        if (collectDuration.getPeriod().getDays() > 60) {
            return Optional.empty();
        }

        return Optional.of(new BookCollected(bookId, patronId, collectDuration.getPeriod().getDays()));
    }

    private boolean isRegular() {
        return patronType.equals(PatronType.Regular);
    }
}
