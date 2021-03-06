package mops.infrastructure.adapters.webflow.datepoll;

import mops.infrastructure.adapters.webflow.WebFlowAdapter;
import mops.infrastructure.adapters.webflow.datepoll.builderdtos.Entries;
import mops.infrastructure.adapters.webflow.datepoll.webflowdtos.EntriesDto;
import mops.infrastructure.adapters.webflow.datepoll.webflowdtos.EntryDto;
import org.springframework.binding.message.DefaultMessageContext;
import org.springframework.binding.message.MessageContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static mops.infrastructure.adapters.webflow.ErrorMessageHelper.addMessage;

@Service
@PropertySource(value = "classpath:flows/errormappings/datepollmappings.properties", encoding = "UTF-8")
public final class EntriesAdapter implements WebFlowAdapter<EntriesDto, Entries> {

    private final transient Environment errorEnvironment;
    private final transient EntryAdapter entryAdapter;

    public EntriesAdapter(Environment errorEnvironment, EntryAdapter entryAdapter) {
        this.errorEnvironment = errorEnvironment;
        this.entryAdapter = entryAdapter;
    }

    public boolean addEntry(EntriesDto entriesDto,
                            String addDate,
                            String addStartTime,
                            String addEndTime,
                            MessageContext context) {
        final boolean isvalid = entryAdapter.validateDto(new EntryDto(addDate, addStartTime, addEndTime), context);
        if (!isvalid) {
            return false;
        }
        entriesDto.getEntries().add(new EntryDto(addDate, addStartTime, addEndTime));
        // Neues vorgeschlagenes Datum auf den letzten hinzugefügten Eintrag setzen
        entriesDto.setProposedEntry(new EntryDto(addDate, addStartTime, addEndTime));
        return true;
    }

    public boolean deleteEntry(EntriesDto entriesDto,
                               String deleteDate,
                               String deleteStartTime,
                               String deleteEndTime) {
        entriesDto.getEntries().remove(new EntryDto(deleteDate, deleteStartTime, deleteEndTime));
        return true;
    }

    @Override
    public EntriesDto initializeDto() {
        return new EntriesDto(entryAdapter.initializeDto(), new TreeSet<>());
    }

    @Override
    @SuppressWarnings({"PMD.LawOfDemeter", "PMD.DataflowAnomalyAnalysis"})
    public boolean validateDto(EntriesDto entriesDto, MessageContext context) {
        // proposedEntry wird nicht abgefragt, weil es nicht im Entries-Objekt landet
        if (entriesDto.getEntries() == null || entriesDto.getEntries().isEmpty()) {
            addMessage("DATE_POLL_NO_ENTRIES", context, errorEnvironment);
            return false;
        }
        // DefaultMessageContext, weil wir nicht alle Fehler der Einträge im Kontext haben wollen
        final MessageContext emptyMessageContext = new DefaultMessageContext();
        if (entriesDto.getEntries().stream()
                .anyMatch(Predicate.not(entry -> entryAdapter.validateDto(entry, emptyMessageContext)))) {
            addMessage("DATE_POLL_INVALID_ENTRIES", context, errorEnvironment);
            return false;
        }
        return true;
    }

    @Override
    @SuppressWarnings("PMD.LawOfDemeter")
    public Entries build(EntriesDto entriesDto) {
        return new Entries(
                entriesDto.getEntries().stream().map(entryAdapter::build).collect(Collectors.toSet())
        );
    }
}
