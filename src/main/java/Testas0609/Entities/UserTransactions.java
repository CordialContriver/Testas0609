package Testas0609.Entities;

import java.time.LocalDateTime;

public class UserTransactions {
    User sender;
    User reciever;
    Double ammount;
    LocalDateTime time;
    Boolean isComplete;

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReciever() {
        return reciever;
    }

    public void setReciever(User reciever) {
        this.reciever = reciever;
    }

    public Double getAmmount() {
        return ammount;
    }

    public void setAmmount(Double ammount) {
        this.ammount = ammount;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Boolean isComplete) {
        this.isComplete = isComplete;
    }

    public UserTransactions(User sender, User reciever, Double ammount, LocalDateTime time, Boolean isComplete) {
        this.sender = sender;
        this.reciever = reciever;
        this.ammount = ammount;
        this.time = time;
        this.isComplete = isComplete;
    }
}
