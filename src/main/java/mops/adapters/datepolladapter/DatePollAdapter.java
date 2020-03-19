package mops.adapters.datepolladapter;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import mops.adapters.datepolladapter.dtos.ConfigDto;
import mops.adapters.datepolladapter.dtos.MetaInfDto;
import mops.domain.models.FieldErrorNames;
import mops.domain.models.Validation;
import mops.domain.models.datepoll.DatePollMetaInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.EnumSet;

@Service
@PropertySource(value = "classpath:errormappings/datepollmappings.properties", encoding = "UTF-8")
public final class DatePollAdapter {

    private final transient ConversionService conversionService;
    private final transient Environment errorEnvironment;

    @Autowired
    public DatePollAdapter(ConversionService conversionService, Environment env) {
        this.conversionService = conversionService;
        this.errorEnvironment = env;
    }

    /**
     * Validiert MetaInfDto, nachdem Titel und evtl. Beschreibung und Ort eingegeben wurden.
     *
     * @param metaInfDto ...
     * @param context    im MessageContext können die Fehlermeldungen angehängt werden
     * @return ob die Transition in den nächsten State stattfinden soll oder nicht
     */
    @SuppressWarnings({"PMD.LawOfDemeter"})
    @SuppressFBWarnings(
            value = "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE",
            justification = "Der eingesetzte Converter kann niemals eine null refrence zurückgeben, "
                    + "auch wenn das Interface es erlaubt")
    /*
     * Verletzung wird in Kauf genommen um in Validation die entscheidung zu Kapseln wann eine Validierung erfolgreich
     * war, aber die Validierung selbst kann nur das zu validierende Objekt selbst sinvoll lösen
     */
    public boolean validate(MetaInfDto metaInfDto, MessageContext context) {
        final DatePollMetaInf metaInf = conversionService.convert(metaInfDto, DatePollMetaInf.class);
        final Validation validation = metaInf.validate();
        mapErrors(validation.getErrorMessages(), context);
        return validation.hasNoErrors();
    }

    public boolean validate(ConfigDto configDto, MessageContext context) {
        return false;
    }

    private void mapErrors(EnumSet<FieldErrorNames> errors, MessageContext context) {
        errors.forEach(error -> context.addMessage(
                new MessageBuilder()
                        .error()
                        .code(error.toString())
                        .source(errorEnvironment.getProperty(error.toString(), "defaulterrors"))
                        .build()));
    }
}
