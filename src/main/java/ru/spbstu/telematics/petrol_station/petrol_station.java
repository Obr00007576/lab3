package ru.spbstu.telematics.petrol_station;

import java.util.ArrayList;

public class petrol_station
{

    public static class Assistant
    {
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
        public void release()
        {
            occupied_pump--;
        }
        public boolean is_all_occupied()
        {
            return num_pump<=occupied_pump;
        }
    }
    public static class Consumer extends Thread
    {
        public static final Object lock = new Object();
        private int capacity;
        private final int applied_quality=1;
        private final Assistant assistant;
        Consumer(Assistant assistant)
        {
            capacity=0;
            this.assistant=assistant;
        }
        public void refuel() throws InterruptedException
        {
            if(assistant.left_capacity()>=applied_quality)
                {
                    capacity += applied_quality;
                    assistant.use(applied_quality);
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
                        Consumer.lock.wait();
                    }
                    assistant.occupy();
                }
                sleep(Math.round(applied_quality*10));
                synchronized(Consumer.class)
                {
                    refuel();
                }
                synchronized (Consumer.lock)
                {
                    assistant.release();
                    Consumer.lock.notify();
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
    public static void main( String[] args ) throws InterruptedException
    {
        Assistant a=new Assistant();
        ArrayList<Consumer>carr=new  ArrayList<Consumer>();
        for(int i=0;i<100;i++)
        {
            Consumer consumer=new Consumer(a);
            consumer.start();
            carr.add(consumer);
        }
        for (Consumer i:carr)
        {
            i.join();
        }
        int sum=0;
        System.out.println(a.left_capacity());
        for (Consumer i:carr)
        {
            sum+=i.getCapacity();
        }
        System.out.println(sum);
    }
}
