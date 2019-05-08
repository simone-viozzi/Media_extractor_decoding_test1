package com.example.ConsumerProducer;

public class Data<E>
{
    private E[] items;
    private int counter = 0;


    public E[] getItems()
    {
        return items;
    }

    public void setItems(E[] items)
    {
        this.items = items;
    }

    public int getCounter()
    {
        return counter;
    }

    public void setCounter(int counter)
    {
        this.counter = counter;
    }
}
