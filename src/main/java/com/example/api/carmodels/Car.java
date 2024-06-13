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
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String model;

    @OneToOne
    private Engine engine;

    public static void insert(Connection connection, Car car) {
        if (connection == null) {
            throw new RuntimeException("Your connection can't be null!");
        }
        try {
            PreparedStatement statement = connection.prepareStatement("insert into car (id, make, model, engine_id) values (?, ?, ?, ?);");
            statement.setLong(1, car.id);
            statement.setString(2, car.make);
            statement.setString(3, car.model);
            statement.setInt(4, car.engine.getId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Car> parseFromResultSet(Connection connection, ResultSet resultSet, int depth) {
        List<Car> cars = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Engine engine = null;
                if (depth >= 1) {
                    engine = Engine.getById(connection, resultSet.getInt("engine_id"), depth - 1);
                }
                cars.add(new Car(resultSet.getInt("id"), resultSet.getString("make"), resultSet.getString("model"), engine));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cars;
    }

    public static Car getById(Connection connection, int id, int depth) {
        if (connection == null) {
            throw new RuntimeException("Your connection can't be null!");
        }
        try {
            PreparedStatement statement = connection.prepareStatement("select * from car where id = ?;");
            statement.setLong(1, id);
            return Car.parseFromResultSet(connection, statement.executeQuery(), depth).getFirst();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ",\n\tengine=" + engine +
                "\n}";
    }
}
