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
public class Rod {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(nullable = false)
    private double length;

    @Column(nullable = false)
    private String material;

    public static void insert(Connection connection, Rod rod) {
        if (connection == null) {
            throw new RuntimeException("Your connection can't be null!");
        }
        try {
            PreparedStatement statement = connection.prepareStatement("insert into rod (id, length, material) values (?, ?, ?);");
            statement.setLong(1, rod.id);
            statement.setDouble(2, rod.length);
            statement.setString(3, rod.material);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Rod> parseFromResultSet(ResultSet resultSet) {
        List<Rod> rods = new ArrayList<>();
        try {
            while (resultSet.next()) {
                rods.add(new Rod(resultSet.getInt("id"), resultSet.getDouble("length"), resultSet.getString("material")));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rods;
    }

    public static Rod getById(Connection connection, int id) {
        if (connection == null) {
            throw new RuntimeException("Your connection can't be null!");
        }
        try {
            PreparedStatement statement = connection.prepareStatement("select * from rod where id = ?;");
            statement.setLong(1, id);
            return Rod.parseFromResultSet(statement.executeQuery()).getFirst();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Rod{" +
                "id=" + id +
                ", length=" + length +
                ", material='" + material + '\'' +
                '}';
    }
}
