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
public class Piston {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(nullable = false)
    private double bore;

    @Column(nullable = false)
    private String material;

    public static void insert(Connection connection, Piston piston) {
        if (connection == null) {
            throw new RuntimeException("Your connection can't be null!");
        }
        try {
            PreparedStatement statement = connection.prepareStatement("insert into piston (id, bore, material) values (?, ?, ?);");
            statement.setLong(1, piston.id);
            statement.setDouble(2, piston.bore);
            statement.setString(3, piston.material);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Piston> parseFromResultSet(ResultSet resultSet) {
        List<Piston> pistons = new ArrayList<>();
        try {
            while (resultSet.next()) {
                pistons.add(new Piston(resultSet.getInt("id"), resultSet.getDouble("bore"), resultSet.getString("material")));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pistons;
    }

    public static Piston getById(Connection connection, int id) {
        if (connection == null) {
            throw new RuntimeException("Your connection can't be null!");
        }
        try {
            PreparedStatement statement = connection.prepareStatement("select * from piston where id = ?;");
            statement.setLong(1, id);
            return Piston.parseFromResultSet(statement.executeQuery()).getFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Piston{" +
                "id=" + id +
                ", bore=" + bore +
                ", material='" + material + '\'' +
                '}';
    }
}
