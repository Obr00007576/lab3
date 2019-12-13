package ru.spbstu.telematics.petrol_station;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;

public class petrol_stationTest
{
    @Test
    public void test_sum() throws Exception
    {
        int num_customers=120;
        int capacity=700;
        petrol_station.Assistant a=new petrol_station.Assistant(capacity);
        ArrayList<petrol_station.Consumer>carr=new  ArrayList<petrol_station.Consumer>();
        for(int i=0;i<100;i++)
        {
            petrol_station.Consumer consumer=new petrol_station.Consumer(a);
            consumer.start();
            carr.add(consumer);
        }
        for (petrol_station.Consumer i:carr)
        {
            i.join();
        }
        int sum=0;
        System.out.println("Summary: ");
        System.out.println("Price for 1 pound of petrol: " +String.valueOf(a.getPrice(1))+"$");
        System.out.println("Number of customers: "+String.valueOf(num_customers));
        System.out.println("Total capacity of petrol: "+String.valueOf(capacity)+" pounds");
        System.out.println("Quality of the rest of petrol: "+String.valueOf(a.left_capacity())+" pounds");
        for (petrol_station.Consumer i:carr)
        {
            sum+=i.getCapacity();
        }
        System.out.println("Quality of all petrol sold: "+String.valueOf(sum)+" pounds");
        System.out.println("The profit: "+String.valueOf(a.getProfit())+"$");
        Assert.assertEquals(capacity, sum+a.left_capacity());
    }
    @Test
    public void test_profit() throws Exception
    {
        int num_customers=100;
        int capacity=500;
        petrol_station.Assistant a=new petrol_station.Assistant(capacity);
        ArrayList<petrol_station.Consumer>carr=new  ArrayList<petrol_station.Consumer>();
        for(int i=0;i<num_customers;i++)
        {
            petrol_station.Consumer consumer=new petrol_station.Consumer(a);
            consumer.start();
            carr.add(consumer);
        }
        for (petrol_station.Consumer i:carr)
        {
            i.join();
        }
        int sum=0;
        System.out.println("Summary: ");
        System.out.println("Price for 1 pound of petrol: " +String.valueOf(a.getPrice(1))+"$");
        System.out.println("Number of customers: "+String.valueOf(num_customers));
        System.out.println("Total capacity of petrol: "+String.valueOf(capacity)+" pounds");
        System.out.println("Quality of the rest of petrol: "+String.valueOf(a.left_capacity())+" pounds");
        for (petrol_station.Consumer i:carr)
        {
            sum+=i.getCapacity();
        }
        System.out.println("Quality of all petrol sold: "+String.valueOf(sum)+" pounds");
        System.out.println("The profit: "+String.valueOf(a.getProfit())+"$");
        Assert.assertEquals(a.getPrice(sum), a.getProfit());
    }
}