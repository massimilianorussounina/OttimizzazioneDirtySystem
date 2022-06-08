package com.example.dirtsystemec;

import com.google.fpl.liquidfun.Contact;

/** An unordered pair of game objects.
 *
 */
public class Collision {
    PhysicsComponent a, b;
    Contact contact;

    public Collision(PhysicsComponent a, PhysicsComponent b, Contact contact) {
        this.a = a;
        this.b = b;
        this.contact=contact;
    }

    @Override
    public int hashCode() {
        return a.hashCode() ^ b.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Collision))
            return false;
        Collision otherCollision = (Collision) other;
        return (a.equals(otherCollision.a) && b.equals(otherCollision.b)) ||
               (a.equals(otherCollision.b) && b.equals(otherCollision.a));
    }
}
