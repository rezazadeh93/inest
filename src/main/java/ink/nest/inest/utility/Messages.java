package ink.nest.inest.utility;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Messages {
    private final MessageSource messages;

    public Messages(MessageSource messages) {
        this.messages = messages;
    }

    public String getExceptionMessage(String code, Object arg) {
        return messages.getMessage(code,
                List.of(arg).toArray(),
                LocaleContextHolder.getLocale());
    }

    public String getExceptionMessage(String code, List<Object> arg) {
        return messages.getMessage(code,
                arg.toArray(),
                LocaleContextHolder.getLocale());
    }

    public String getExceptionMessage(String code) {
        return messages.getMessage(code,
                null,
                LocaleContextHolder.getLocale());
    }
}
