package pl.luncher.v3.luncher_core.common.persistence;


import jakarta.persistence.PersistenceException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.function.Function;

public class JpaExceptionsHandler {
    public static <ARG, T> T proxySave(SQLOperationWithArg<ARG, T> saveMethodRef, ARG arg, Function<String, ? extends RuntimeException> exceptionWithMessageSupplier) {
        return proxySave(() -> saveMethodRef.perform(arg), exceptionWithMessageSupplier);
    }

    public static <T> T proxySave(SQLOperation<T> saveMethodRef, Function<String, ? extends RuntimeException> exceptionWithMessageSupplier) {
        try {
            return saveMethodRef.perform();
        } catch (DataIntegrityViolationException exception) {
            //todo: check the exception type
            throw exceptionWithMessageSupplier.apply("Same entity already exists.");
        }
    }


    public interface SQLOperation<T> {
        T perform() throws PersistenceException;

    }

    public interface SQLOperationWithArg<ARG, T> {
        T perform(ARG arg) throws PersistenceException;

    }
}
