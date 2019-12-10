/*
Michael Jee
Main.java, Frame1.java, customer.java
This file contains the customer class, including all of its variables 
   and functions
*/

package group6;

public class Customer {

    static private int currentID = 1000000000;
    private String id;
    private String first; //first name
    private String last; //last name
    private String middle;
    private String address;
    private String zip;
    private String homePhone;
    private String workPhone;
    private String email;
    private String social; //social security number
    public enum ownStatus {leased, financed, purchased}
    private ownStatus OS;
    private String date; //the date in YYYYMMDD form
    private String income; //monthly income
    private String lastDate; //date of last payment if all payments made and 
    //on time at the first of the month
    
    /*private float payment = 0; //monthly payment
    private float carCost = 0; //cost of the car
    private float tradeIn = 0; //value of the trade in
    private float downPayment = 0; //up front payment of the car
    private int numberYears = 0; //number of years until loan paid off
    private int credit = 0; //credit score
    private float incomes; //combined monthly incomes
    private float payments; //combined monthly debt payments
    private float paymentIncome; //ratio of monthly debt payments to income
    private float interest; //interest rate of new loan
    private float loanAmount; //total amount user will owe
    private float monthlyPayment; //monthly payment of new loan*/
    
    public Customer() { ++currentID; id = Integer.toString(currentID); first = last = middle = address = zip = homePhone = workPhone = email = social = date = income = lastDate; OS = ownStatus.purchased;}
    
    //accessors
    public String getId() {return id;}
    public String getFirst(){return first;}
    public String getLast() {return last;}
    public String getMiddle() {return middle;}
    public String getAddress() {return address;}
    public String getZip() {return zip;}
    public String getHome() {return homePhone;}
    public String getWork() {return workPhone;}
    public String getEmail() {return email;}
    public String getSocial() {return social;}
    public ownStatus getStatus() {return OS;}
    public String getDate() {return date;}
    public String getIncome() {return income;}
    public String getLastDate() {return lastDate;}
    
    /*public float getPayment() {return payment;}
    public float getCarCost() {return carCost;}
    public float getTradeIn() {return tradeIn;}
    public float getDownPayment() {return downPayment;}
    public int getNumberYears() {return numberYears;}
    public int getCredit() {return credit;}
    public float getIncomes() {return incomes;}
    public float getPayments() {return payments;}
    public float getPaymentIncome() {return paymentIncome;}
    public float getInterest() {return interest;}
    public float getLoanAmount() {return loanAmount;}
    public float getMonthlyPayment() {return monthlyPayment;}*/
    
    //mutators
    public void setId(String temp) {id = temp;}
    public void setFirst(String temp) {first = temp;}
    public void setLast(String temp) {last = temp;}
    public void setMiddle(String temp) {middle = temp;}
    public void setAddress(String temp) {address = temp;}
    public void setZip(String temp) {zip = temp;}
    public void setHome(String temp) {homePhone = temp;}
    public void setWork(String temp) {workPhone = temp;}
    public void setEmail(String temp) {email = temp;}
    public void setSocial(String temp) {social = temp;}
    public void setOS(ownStatus temp) {OS = temp;}
    public void setDate(String temp) {date = temp;}
    public void setIncome(String temp) {income = temp;}
    public void setLastDate(String temp) {lastDate = temp;}
    
    /*public void setPayment(float temp) {payment = temp;}
    public void setCarCost(float temp) {carCost = temp;}
    public void setTradeIn(float temp) {tradeIn = temp;}
    public void setDownPayment(float temp) {downPayment = temp;}
    public void setNumberYears(int temp) {numberYears = temp;}
    public void setCredit(int temp) {credit = temp;}
    public void setIncomes(float temp) {incomes = temp;}
    public void setPayments(float temp) {payments = temp;}
    public void setPaymentIncome(float temp) {paymentIncome = temp;}
    public void setInterest(float temp) {interest = temp;}
    public void setLoanAmount(float temp) {loanAmount = temp;}
    public void setMonthlyPayment(float temp) {monthlyPayment = temp;}*/
    
    //checks if the applicant's credit is sufficient
    //precondition: the customer object exists and has credit
    //postcondition: function returns true or false
    //public boolean goodCredit() {return (credit > 500) ? true : false;}

    //calculates all of the output variables
    //precondition: two different customer objects have income & payment & exist
    //postcondition: incomes, payments, paymentIncome, interest, loanAmount, 
       //monthlyPayment, and lastDate have values for the calling object
    /*public void calculate(float income2, float payment2) 
    {
        incomes = income + income2;
        payments = payment + payment2;
        paymentIncome = payments / incomes;
        interest = paymentIncome / 10;
        double temp = Math.pow((1 + interest / 12), 12 * numberYears - 1 / 365 
            * numberYears);
        double temp2 = Math.pow((1 + interest / 12), 12 * numberYears);
        loanAmount = carCost - tradeIn - downPayment;
        monthlyPayment = (float) (loanAmount * interest * temp / (12 * temp2 - 
            12));
        lastDate = date + numberYears;
    }*/

    //checks if the int falls within the bounds
    //precondition: check, lower, and upper exist
    //postcondition: function returns true or false
    /*public boolean validInt(int check, int lower, int upper) 
    {
        if (check < lower || check > upper)
            return false;
        else
            return true;
    }*/

    //checks if the float falls within the bounds
    //precondition: check, lower, and upper exist
    //postcondition: function returns true or false
    /*public boolean validFloat(float check, float lower, float upper) 
    {
        if (check < lower || check > upper)
            return false;
        else
            return true;
    }*/
}
