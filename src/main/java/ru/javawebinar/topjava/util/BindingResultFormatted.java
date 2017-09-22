package ru.javawebinar.topjava.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.StringJoiner;

public class BindingResultFormatted {
    private final boolean ok;
    private final ResponseEntity<String> response;

    public BindingResultFormatted(BindingResult result) {
        this.ok = !result.hasErrors();
        if(this.ok) {
            this.response = new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            StringJoiner joiner = new StringJoiner("<br>");
            result.getFieldErrors().forEach(
                    fe -> {
                        String msg = fe.getDefaultMessage();
                        if (!msg.startsWith(fe.getField())) {
                            msg = fe.getField() + ' ' + msg;
                        }
                        joiner.add(msg);
                    });
            this.response = new ResponseEntity<>(joiner.toString(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public boolean isOk() {
        return ok;
    }

    public ResponseEntity<String> getResultFormatted() {
        return response;
    }
}
