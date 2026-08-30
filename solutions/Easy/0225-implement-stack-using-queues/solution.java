// ──────────────────────────────────────────────────
// Problem  : 225. Implement Stack using Queues
// Difficulty: Easy
// Tags     : Stack, Design, Queue
// Link     : https://leetcode.com/problems/implement-stack-using-queues/
// Runtime  : 0 ms (beats 100%)
// Memory   : 42740000 (beats 72%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class MyStack 
{
    Queue<Integer> o;
    Queue<Integer> p;
    public MyStack() 
    {
        o = new ArrayDeque<>();
        p = new ArrayDeque<>();
    }
    
    public void push(int x) 
    {
        o.add(x);
    }
    
    public int pop() 
    {
        if(o.isEmpty())
        {
            return -1;
        }
        while (o.size() > 1) 
        {
            p.add(o.remove());
        }
        int f = o.remove();
        while(!p.isEmpty())
        {
            o.add(p.remove());
        }
        return f;
    }
    
    public int top() 
    {
        if(o.isEmpty())
        {
            return -1;
        }
        while (o.size() > 1) 
        {
            p.add(o.remove());
        }
        int f = o.remove();
        p.add(f);
        while(!p.isEmpty())
        {
            o.add(p.remove());
        }
        return f;
    }
    
    public boolean empty() 
    {
        if(o.isEmpty())
        {
            return true;
        }
        return false;
    }
}

/**
 * Your MyStack object will be instantiated and called as such:
 * MyStack obj = new MyStack();
 * obj.push(x);
 * int param_2 = obj.pop();
 * int param_3 = obj.top();
 * boolean param_4 = obj.empty();
 */