package Yousof.HollowKnight.Model.contacts;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameContactListener implements ContactListener {

    public ArrayList<ContactListener> listeners;

    public GameContactListener(){
        listeners = new ArrayList<>();
    }

    @Override
    public void beginContact(Contact contact) {
        for(ContactListener listener : listeners){
            listener.beginContact(contact);
        }
    }

    @Override
    public void endContact(Contact contact) {
        for(ContactListener listener : listeners){
            listener.endContact(contact);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}



    public ArrayList<ContactListener> getListeners() {
        return listeners;
    }

    public void addListeners(ContactListener listener) {
        this.listeners.add(listener);
    }
}