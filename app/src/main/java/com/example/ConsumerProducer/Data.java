package com.example.ConsumerProducer;

class Data<E>
{
    private E[] items;
    private int counter = 0;


    E[] getItems()
    {
        return items;
    }

    void setItems(E[] items)
    {
        this.items = items;
    }

    int getCounter()
    {
        return counter;
    }

    void setCounter(int counter)
    {
        this.counter = counter;
    }
}
