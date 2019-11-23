package io.pillopl.acl;

import io.pillopl.acl.reconciliation.Reconciliation;
import io.pillopl.acl.toggles.NewModelToggles;
import io.pillopl.bigballofmud.dtos.BookDto;
import io.pillopl.bigballofmud.dtos.BookRequest;
import io.pillopl.newmodel.catalogue.BookId;
import io.pillopl.newmodel.lending.application.CollectCommand;
import io.pillopl.newmodel.lending.application.LendingFacade;
import io.pillopl.newmodel.lending.application.PlaceOnHoldCommand;
import io.pillopl.newmodel.lending.domain.patron.CollectDuration;
import io.pillopl.newmodel.lending.domain.patron.HoldDuration;
import io.pillopl.newmodel.lending.domain.patron.Patron;
import io.pillopl.newmodel.lending.domain.patron.PatronId;

import java.util.*;

public class LendingACL {

    private final Reconciliation<BookDto> reconciliation;
    private final LendingFacade lendingFacade;

    public LendingACL(Reconciliation<BookDto> reconciliation, LendingFacade lendingFacade) {
        this.reconciliation = reconciliation;
        this.lendingFacade = lendingFacade;
    }

    public List<BookDto> booksPlacedOnHoldBy(UUID patronId, List<BookDto> oldModelResult) {
        if (NewModelToggles.RECONCILE_AND_USE_NEW_MODEL.isActive()) {
            List<BookDto> newModelResult = callNewModelPlacedOnHoldBy(new PatronId(patronId));
            reconciliation.compare(toSet(oldModelResult), toSet(newModelResult));
            return newModelResult;
        }
        if (NewModelToggles.RECONCILE_NEW_MODEL.isActive()) {
            List<BookDto> newModelResult = callNewModelPlacedOnHoldBy(new PatronId(patronId));
            reconciliation.compare(toSet(oldModelResult), toSet(newModelResult));
            return oldModelResult;
        }
        return oldModelResult;
    }

    /**
     * Task #3:
     * Implement this. Make sure tests in LendingACL pass.
     */
    public List<BookDto> booksCurrentlyCollectedBy(UUID patronId, List<BookDto> oldModelResult) {
        if (NewModelToggles.RECONCILE_AND_USE_NEW_MODEL.isActive()) {
            List<BookDto> newModelResult = callNewModelCollectedBy(new PatronId(patronId));
            reconciliation.compare(toSet(oldModelResult), toSet(newModelResult));
            return newModelResult;
        }
        if (NewModelToggles.RECONCILE_NEW_MODEL.isActive()) {
            List<BookDto> newModelResult = callNewModelCollectedBy(new PatronId(patronId));
            reconciliation.compare(toSet(oldModelResult), toSet(newModelResult));
            return oldModelResult;
        }
        return oldModelResult;
    }

    private Set<BookDto> toSet(List<BookDto> books) {
        return new HashSet<>(books);
    }

    public void placeOnHold(BookRequest bookRequest) {
        if (bookRequest.getDays() != null) {
            lendingFacade.execute(new PlaceOnHoldCommand(
                    new BookId(bookRequest.getBookId()),
                    new PatronId(bookRequest.getHolderId()),
                    HoldDuration.forDays(bookRequest.getDays())
            ));
        } else {
            lendingFacade.execute(new PlaceOnHoldCommand(
                    new BookId(bookRequest.getBookId()),
                    new PatronId(bookRequest.getHolderId()),
                    HoldDuration.openEnded()
            ));
        }


    }

    private List<BookDto> callNewModelPlacedOnHoldBy(PatronId patronId) {
        return BookDto.translateFrom(lendingFacade.booksPlacedOnHoldBy(patronId));
    }

    private List<BookDto> callNewModelCollectedBy(PatronId patronId) {
        return BookDto.translateFrom(lendingFacade.booksCollectedBy(patronId));
    }

    public void collect(BookRequest bookRequest) {
        lendingFacade.execute(new CollectCommand(
                new CollectDuration(bookRequest.getDays()
        ));
    }
}
