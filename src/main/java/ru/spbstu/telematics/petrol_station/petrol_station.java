package ru.spbstu.telematics.petrol_station;

import java.util.ArrayList;
import java.util.Random;

public class petrol_station
{
    static Random random = new Random();
    public static class Assistant
    {
        private int profit=0;
        private final int num_pump=3;
        private int gas_capacity=500;
        public int occupied_pump=0;
        public int left_capacity()
        {
            return gas_capacity;
        }
        public void use(int value)
        {
            gas_capacity-=value;
        }
        public void occupy() {
            occupied_pump++;
        }
        Assistant(int gas_capacity)
        {
            this.gas_capacity=gas_capacity;
        }
        public void release()
        {
            occupied_pump--;
        }
        public int getProfit()
        {
            return profit;
        }
        public boolean is_all_occupied()
        {
            return num_pump<=occupied_pump;
        }
        public void payment(int quality)
        {
            profit+=quality*10;
        }
        public int getPrice(int quality)
        {
            return 10*quality;
        }
    }
    public static class Consumer extends Thread
    {
        private static int client=0;
        private Integer client_num;
        public static final Object lock = new Object();
        private int capacity;
        private int applied_quality=1;
        private final Assistant assistant;
        Consumer(Assistant assistant)
        {
            client++;
            client_num =client;
            capacity=0;
            applied_quality=random.nextInt(10)+1;
            this.assistant=assistant;
        }
        public void refuel() throws InterruptedException
        {
            try
            {
                if (assistant.left_capacity() >= applied_quality)
                {
                    assistant.payment(applied_quality);
                    capacity += applied_quality;
                    assistant.use(applied_quality);
                }
                else
                {
                    throw new Exception();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        int getCapacity()
        {
            return capacity;
        }
        @Override
        public void run()
        {
            try
            {
                synchronized (Consumer.lock)
                {
                    while(assistant.is_all_occupied())
                    {
                        System.out.println("Client"+ client_num.toString()+":All the pumps are occupied, going to be waiting.");
                        Consumer.lock.wait();
                    }
                    System.out.println("Client"+ client_num.toString()+":There is an unoccupied pump, and my car will be assigned at the pump.");
                    if(assistant.left_capacity()<applied_quality)
                    {
                        System.out.println("Client"+ client_num.toString()+":Not enough petrol left, I'm leaving.");
                        Consumer.lock.notify();
                        return;
                    }
                    assistant.occupy();
                    System.out.println("Client"+ client_num.toString()+": I will pay "+String.valueOf(assistant.getPrice(applied_quality))+" dollars for "+String.valueOf(applied_quality)+ " pounds of petrol and wait to get the car refueled.");
                }
                synchronized (Consumer.class)
                {
                    refuel();
                }
                sleep(Math.round(applied_quality*30));
                synchronized (Consumer.lock)
                {
                    assistant.release();
                    System.out.println("Client"+ client_num.toString()+": My car has been refueled and I'm leaving now.");
                    Consumer.lock.notify();
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
