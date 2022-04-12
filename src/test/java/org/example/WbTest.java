package org.example;

import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.Validator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class WbTest {
    private final String VALID_ID = "1";
    private final String INVALID_ID = "";
    private final String VALID_DESC = "some_description";
    private final int VALID_DEADLINE = 2;
    private final int VALID_STARTLINE = 1;

    private Service service;

    @Before
    public void setup() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Tema> temaValidator = new TemaValidator();
        Validator<Nota> notaValidator = new NotaValidator();

        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, "studenti.xml");
        TemaXMLRepository fileRepository2 = new TemaXMLRepository(temaValidator, "teme.xml");
        NotaXMLRepository fileRepository3 = new NotaXMLRepository(notaValidator, "note.xml");

        service = new Service(fileRepository1, fileRepository2, fileRepository3);
        List<Tema> assign = StreamSupport
                .stream(service.findAllTeme().spliterator(), false)
                .collect(Collectors.toList());

        for (Tema s: assign) {
            service.deleteTema(s.getID());
        }
    }

    @Test
    public void addAssignment_validId_returnCode1() {
        int returnCode = service.saveTema(VALID_ID, VALID_DESC, VALID_DEADLINE, VALID_STARTLINE);

        Assert.assertEquals(1, returnCode);
    }

    @Test
    public void addAssignment_invalidId_returnCode0() {
        // TODO, validation exception same effect as when adding an entity with is not in the store already (return null)
        // TODO catch in service and return 0 only on validation exceptions
        int returnCode = service.saveTema(INVALID_ID, VALID_DESC, VALID_DEADLINE, VALID_STARTLINE);

        Assert.assertEquals(0, returnCode);
    }

    @Test
    public void addAssignment_validFields_twice_returnCode1() {
        int returnCode = service.saveTema(VALID_ID, VALID_DESC, VALID_DEADLINE, VALID_STARTLINE);
        Assert.assertEquals(1, returnCode);

        returnCode = service.saveTema(VALID_ID, VALID_DESC, VALID_DEADLINE, VALID_STARTLINE);
        Assert.assertEquals(1, returnCode);

        // check that only one assignment with this ID was added
        long assigCount = StreamSupport.stream(service.findAllTeme().spliterator(), false)
                .filter(assign -> assign.getID() == VALID_ID)
                .count();
        Assert.assertEquals(1, assigCount);
    }

    @Test
    public void addAssignment_validFields_twice_addedOnce() {
        service.saveTema(VALID_ID, VALID_DESC, VALID_DEADLINE, VALID_STARTLINE);
        service.saveTema(VALID_ID, VALID_DESC, VALID_DEADLINE, VALID_STARTLINE);

        // check that only one assignment with this ID was added
        long assigCount = StreamSupport.stream(service.findAllTeme().spliterator(), false)
                .filter(assign -> assign.getID() == VALID_ID)
                .count();
        Assert.assertEquals(1, assigCount);
    }
}
