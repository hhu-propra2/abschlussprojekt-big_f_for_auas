package mops.applicationServices;

import mops.domain.models.DatePoll;
import mops.domain.models.DatePollID;
import mops.domain.models.User;
import org.springframework.stereotype.Service;

/**
 * TODO Implementierung des Services, der die Daten zu einer Terminfindung (anhand eines Links einer ID ...) ausliest.
 * Die Daten der Termine gehen an den Controller.
 * Termine werden in Form von DatePoll Objekten bereitgestellt, in denen sich die einzelnen Termine befinden: datePollOptions.
 *
 * TODO Wo werden die Stimmen fuer eine datePollOption gesammelt? - In dem Objekt selbst? - Absprache mit QuestionPoll.
 */
@Service
public class DatePollReadService {

    /*public DatePoll readDatePoll(DatePollID datePollID) {
        //return getDatePollByID(datePollID);
    }*/
}