package ru.mephi.week2.lesson1.receCondition;

public class TicketBookingExample {

    public static class TicketBookingSystem {
        private int availableTickets = 1;

        public synchronized void bookTicket(String customerName) {
            if (availableTickets > 0) {
                System.out.println(customerName + " забронировал билет!");
                availableTickets--;
            } else {
                System.out.println(customerName + " не смог забронировать билет!");
            }
        }

    }

    public static void main(String[] args) {
        TicketBookingSystem system = new TicketBookingSystem();

        Thread customer1 = new Thread(() -> system.bookTicket("Клиент 1"));
        Thread customer2 = new Thread(() -> system.bookTicket("Клиент 2"));

        customer1.start();
        customer2.start();

    }

}
