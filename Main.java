package baitapGK;
import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.security.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.math.BigInteger;

class Student {
    int id;
    String name;
    String address;
    String dateOfBirth;
    int age;
    int sum;
    boolean isDigit;
    public Student(int id, String name, String address, String dateOfBirth) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }
}

class Thread1 implements Runnable {
    List<Student> students;

    public Thread1(List<Student> students) {
        this.students = students;
    }

    public void run() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse("student.xml");

            NodeList studentList = doc.getElementsByTagName("student");
            for (int i = 0; i < studentList.getLength(); i++) {
                Node studentNode = studentList.item(i);
                if (studentNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element studentElement = (Element) studentNode;

                    int id = Integer.parseInt(studentElement.getElementsByTagName("id").item(0).getTextContent());
                    String name = studentElement.getElementsByTagName("name").item(0).getTextContent();
                    String address = studentElement.getElementsByTagName("address").item(0).getTextContent();
                    String dateOfBirth = studentElement.getElementsByTagName("dateOfBirth").item(0).getTextContent();

                    students.add(new Student(id, name, address, dateOfBirth));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Thread2 implements Runnable {
    List<Student> students;

    public Thread2(List<Student> students) {
        this.students = students;
    }

    public void run() {
        for (Student student : students) {
            Calendar dob = Calendar.getInstance();
            try {
				dob.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(student.dateOfBirth));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            int age = Calendar.getInstance().get(Calendar.YEAR) - dob.get(Calendar.YEAR);

            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            byte[] encodedAge = md.digest(Integer.toString(age).getBytes());

            student.age = age;
            student.isDigit = true;
            for (byte b : encodedAge) {
                if (b < 0) {
                    student.isDigit = false;
                    break;
                }
            }
            student.sum = 0;
            String dobStr = student.dateOfBirth.substring(0, 4);
            for (char c : dobStr.toCharArray()) {
                student.sum += Character.getNumericValue(c);
            }
        }
    }
}

class Thread3 implements Runnable {
    List<Student> students;

    public Thread3(List<Student> students) {
        this.students = students;
    }

    public void run() {
        for (Student student : students) {
            boolean isPrime = true;
            for (int i = 2; i <= Math.sqrt(student.sum); i++) {
                if (student.sum % i == 0) {
                    isPrime = false;
                    break;
                }
            }
            student.isDigit = isPrime;
        }
    }
}

class Thread4 implements Runnable {
    List<Student> students;

    public Thread4(List<Student> students) {
        this.students = students;
    }

    public void run() {
        for (Student student : students) {
            System.out.println("Student " + student.id + " is digit: " + (student.isDigit ? "Yes" : "No"));
            System.out.println("Student " + student.id + " age: " + student.age);
            System.out.println("Student " + student.id + " sum: " + student.sum);
            System.out.println("Student " + student.id + " name: " + student.name);
            System.out.println("Student " + student.id + " address: " + student.address);
            System.out.println("Student " + student.id + " dateOfBirth: " + student.dateOfBirth);
            System.out.println();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();

        Thread t1 = new Thread(new Thread1(students));
        Thread t2 = new Thread(new Thread2(students));
        Thread t3 = new Thread(new Thread3(students));
        Thread t4 = new Thread(new Thread4(students));

        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t2.start();
        t3.start();

        try {
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t4.start();
    }
}