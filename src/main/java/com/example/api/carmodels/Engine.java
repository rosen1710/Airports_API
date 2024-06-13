package com.example.api.carmodels;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Engine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(nullable = false)
    private String engineCode;

    @Column(nullable = false)
    private String make;

    @OneToOne
    private Piston piston;

    @OneToOne
    private Rod rod;

    public static void insert(Connection connection, Engine engine) {
        if (connection == null) {
            throw new RuntimeException("Your connection can't be null!");
        }
        try {
            PreparedStatement statement = connection.prepareStatement("insert into engine (id, engine_code, make, piston_id, rod_id) values (?, ?, ?, ?, ?);");
            statement.setLong(1, engine.id);
            statement.setString(2, engine.engineCode);
            statement.setString(3, engine.make);
            statement.setInt(4, engine.piston.getId());
            statement.setInt(5, engine.rod.getId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Engine> parseFromResultSet(Connection connection, ResultSet resultSet, int depth) {
        List<Engine> engines = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Piston piston = null;
                Rod rod = null;
                if (depth >= 1) {
                    piston = Piston.getById(connection, resultSet.getInt("piston_id"));
                    rod = Rod.getById(connection, resultSet.getInt("rod_id"));
                }
                engines.add(new Engine(resultSet.getInt("id"), resultSet.getString("engine_code"), resultSet.getString("make"), piston, rod));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return engines;
    }

    public static Engine getById(Connection connection, int id, int depth) {
        if (connection == null) {
            throw new RuntimeException("Your connection can't be null!");
        }
        try {
            PreparedStatement statement = connection.prepareStatement("select * from engine where id = ?;");
            statement.setLong(1, id);
            return Engine.parseFromResultSet(connection, statement.executeQuery(), depth).getFirst();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Engine{" +
                "id=" + id +
                ", engineCode='" + engineCode + '\'' +
                ", make='" + make + '\'' +
                ",\n\t\tpiston=" + piston +
                ",\n\t\trod=" + rod +
                "\n\t}";
    }
}
