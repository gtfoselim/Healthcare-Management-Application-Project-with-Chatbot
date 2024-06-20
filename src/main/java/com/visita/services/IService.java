package com.visita.services;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {


    public void ajouter(T t) throws SQLException;
    public void  supprimer(T t) throws SQLException;
    public void modifier(T t) throws SQLException;
    public void block(T t);
    public void unblock(T t);
    public void switchToUser(T t);
    public void switchToAdmin(T t);
    public List<T> afficher() throws SQLException;

}
