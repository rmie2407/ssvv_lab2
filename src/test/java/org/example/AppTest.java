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
import validation.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AppTest {
    private static final String VALID_ID = "student#1234";
    private static final String INVALID_ID = "";
    private static final String VALID_NAME = "name123";
    private static final String INVALID_NAME = "";

    private static final int VALID_GROUP = 933;
    private static final int VALID_GROUP_UPPER_BOUNDARY = 937;
    private static final int VALID_GROUP_LOWER_BOUNDARY = 111;
    private static final int INVALID_GROUP_1 = 90;
    private static final int INVALID_GROUP_2 = -10;
    private static final int INVALID_GROUP_UPPER_BOUNDARY_1 = 110;
    private static final int INVALID_GROUP_LOWER_BOUNDARY_1 = 938;
    private static final int INVALID_GROUP_3 = 1000;

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
        List<Student> students = StreamSupport
                .stream(service.findAllStudents().spliterator(), false)
                .collect(Collectors.toList());
        for (Student s: students) {
            service.deleteStudent(s.getID());
        }
    }

    // EC for studentId: {null, ""} = invalid, {any text}=valid

    @Test
    public void saveStudent_validId_studentSaved() {
        service.saveStudent(VALID_ID, VALID_NAME, VALID_GROUP);

        List<Student> students = StreamSupport
                .stream(service.findAllStudents().spliterator(), false)
                .collect(Collectors.toList());

        Assert.assertEquals(1, students.stream().filter(s -> s.getID() == VALID_ID).count());
    }

    @Test
    public void saveStudent_invalidId_studentNotSaved() {

        service.saveStudent(INVALID_ID, VALID_NAME, VALID_GROUP);

        List<Student> students = StreamSupport
                .stream(service.findAllStudents().spliterator(), false)
                .collect(Collectors.toList());

        Assert.assertEquals(0, students.stream().filter(s -> s.getID() == INVALID_ID).count());
    }

    // EC for student name: {null, ""} = invalid, {any text}=valid

    @Test
    public void saveStudent_validName_studentSaved() {
        service.saveStudent(VALID_ID, VALID_NAME, VALID_GROUP);

        List<Student> students = StreamSupport
                .stream(service.findAllStudents().spliterator(), false)
                .collect(Collectors.toList());

        Assert.assertEquals(1, students.stream().filter(s -> s.getID() == VALID_ID).count());
    }

    @Test
    public void saveStudent_invalidName_studentNotSaved() {

        service.saveStudent(INVALID_ID, INVALID_NAME, VALID_GROUP);

        List<Student> students = StreamSupport
                .stream(service.findAllStudents().spliterator(), false)
                .collect(Collectors.toList());

        Assert.assertEquals(0, students.stream().filter(s -> s.getID() == INVALID_ID).count());
    }

    // EC for student group: {110 <= group <=938} = valid, {-inf <= group <110 OR 938 < group < +inf}=valid

    @Test
    public void saveStudent_validGroup_studentSaved() {
        service.saveStudent(VALID_ID, VALID_NAME, VALID_GROUP);

        List<Student> students = StreamSupport
                .stream(service.findAllStudents().spliterator(), false)
                .collect(Collectors.toList());

        Assert.assertEquals(1, students.stream().filter(s -> s.getID() == VALID_ID).count());
    }

    @Test
    public void saveStudent_invalidGroup_studentNotSaved() {

        service.saveStudent(INVALID_ID, INVALID_NAME, VALID_GROUP);

        List<Student> students = StreamSupport
                .stream(service.findAllStudents().spliterator(), false)
                .collect(Collectors.toList());

        Assert.assertEquals(0, students.stream().filter(s -> s.getID() == INVALID_ID).count());
    }

    // BVA
    @Test
    public void saveStudent_validGroupLowerBoundary_studentSaved() {
        service.saveStudent(VALID_ID, VALID_NAME, VALID_GROUP_LOWER_BOUNDARY);

        List<Student> students = StreamSupport
                .stream(service.findAllStudents().spliterator(), false)
                .collect(Collectors.toList());

        Assert.assertEquals(1, students.stream().filter(s -> s.getID() == VALID_ID).count());
    }

    @Test
    public void saveStudent_validGroupUpperBoundary_studentSaved() {
        service.saveStudent(VALID_ID, VALID_NAME, VALID_GROUP_UPPER_BOUNDARY);

        List<Student> students = StreamSupport
                .stream(service.findAllStudents().spliterator(), false)
                .collect(Collectors.toList());

        Assert.assertEquals(1, students.stream().filter(s -> s.getID() == VALID_ID).count());
    }

    @Test
    public void saveStudent_invalidGroupLowerInterval1_studentNotSaved() {
        service.saveStudent(VALID_ID, VALID_NAME, INVALID_GROUP_1);

        List<Student> students = StreamSupport
                .stream(service.findAllStudents().spliterator(), false)
                .collect(Collectors.toList());

        Assert.assertEquals(0, students.stream().filter(s -> s.getID() == VALID_ID).count());
    }

    @Test
    public void saveStudent_invalidGroupLowerInterval2_studentNotSaved() {
        service.saveStudent(VALID_ID, VALID_NAME, INVALID_GROUP_2);

        List<Student> students = StreamSupport
                .stream(service.findAllStudents().spliterator(), false)
                .collect(Collectors.toList());

        Assert.assertEquals(0, students.stream().filter(s -> s.getID() == VALID_ID).count());
    }

    @Test
    public void saveStudent_invalidGroupUpperBoundary_studentNotSaved() {
        service.saveStudent(VALID_ID, VALID_NAME, INVALID_GROUP_UPPER_BOUNDARY_1);

        List<Student> students = StreamSupport
                .stream(service.findAllStudents().spliterator(), false)
                .collect(Collectors.toList());

        Assert.assertEquals(0, students.stream().filter(s -> s.getID() == VALID_ID).count());
    }

    @Test
    public void saveStudent_invalidGroupLowerBoundary_studentNotSaved() {
        service.saveStudent(VALID_ID, VALID_NAME, INVALID_GROUP_LOWER_BOUNDARY_1);

        List<Student> students = StreamSupport
                .stream(service.findAllStudents().spliterator(), false)
                .collect(Collectors.toList());

        Assert.assertEquals(0, students.stream().filter(s -> s.getID() == VALID_ID).count());
    }

    @Test
    public void saveStudent_invalidGroupUpperInterval_studentNotSaved() {
        service.saveStudent(VALID_ID, VALID_NAME, INVALID_GROUP_3);

        List<Student> students = StreamSupport
                .stream(service.findAllStudents().spliterator(), false)
                .collect(Collectors.toList());

        Assert.assertEquals(0, students.stream().filter(s -> s.getID() == VALID_ID).count());
    }
}