package com.example.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ApiApplicationTests {
	@Getter
	@AllArgsConstructor
	private static class Car {
		String make;
		String model;

		public static void insert(Connection connection, Car car) {
			if (connection == null) {
				throw new RuntimeException("Your connection can't be null!");
			}
            try {
                connection.prepareStatement("insert into car (make, model) values ('%s', '%s');".formatted(car.make, car.model)).executeUpdate();
            }
			catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

		public static List<Car> parseFromResultSet(ResultSet resultSet) {
			List<Car> cars = new ArrayList<>();
			try {
				while (resultSet.next()) {
					cars.add(new Car(resultSet.getString("make"), resultSet.getString("model")));
				}
			}
			catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return cars;
		}
	}

	@Test
	void contextLoads() {
		PGSimpleDataSource dataSource = new PGSimpleDataSource();
		dataSource.setUser("airports_api_user");
		dataSource.setPassword("MyUserPassword123");
		dataSource.setDatabaseName("airports_db");
		dataSource.setServerNames(new String[]{"localhost"});
		dataSource.setPortNumbers(new int[]{6543});

		try {
			Connection connection = dataSource.getConnection();

			connection.prepareStatement("create table if not exists car (id serial primary key, make text not null, model text not null);").executeUpdate();

			Car.insert(connection, new Car("VW", "Golf"));

			ResultSet resultSet = connection.prepareStatement("select * from car;").executeQuery();
            for (Car car : Car.parseFromResultSet(resultSet)) {
				System.out.printf("%s %s%n", car.getMake(), car.getModel());
			}

			connection.close();
		}
		catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
