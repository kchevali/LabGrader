import java.util.*;

class MyClient {
    public static void main(String[] args) {
        System.out.println(new Client("Kevin", "kchevali@my.hpu.edu", "(808) 123-1234", 123, true));
        System.out.println(new Client("Elizabeth", "efisch2@my.hpu.edu", "(808) 321-4321", 321, false));

    }
}

class Client extends Person {
    int clientNumber;
    boolean isInMailingList;

    public Client() {
        super();
        clientNumber = 0;
        isInMailingList = false;
    }

    public Client(String name, String email, String telephone, int clientNumber, boolean isInMailingList) {
        super(name, email, telephone);
        this.clientNumber = clientNumber;
        this.isInMailingList = isInMailingList;
    }

    public int getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(int clientNumber) {
        this.clientNumber = clientNumber;
    }

    public boolean isInMailingList() {
        return isInMailingList;
    }

    public void setInMailingList(boolean isInMailingList) {
        this.isInMailingList = isInMailingList;
    }

    @Override
    public String toString() {
        return super.toString() + "\nNo: " + clientNumber + "\nMailing List: " + (isInMailingList ? "Yes" : " No")
                + "\n";
    }

}

class Person {
    String name, email, telephone;

    public Person() {
        name = "";
        email = "";
        telephone = "";
    }

    public Person(String name, String email, String telephone) {
        this.name = name;
        this.email = email;
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String toString() {
        return "Name: " + name + "\nEmail: " + email + "\nPhone: " + telephone;
    }
}