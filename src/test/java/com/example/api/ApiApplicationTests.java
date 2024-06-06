package com.example.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ApiApplicationTests {
	@Getter
	@AllArgsConstructor
	private static class Engine {
		private int id;
		private String engineCode;
		private String make;

		public static void insert(Connection connection, Engine engine) {
			if (connection == null) {
				throw new RuntimeException("Your connection can't be null!");
			}
			try {
				PreparedStatement statement = connection.prepareStatement("insert into engine (id, engine_code, make) values (?, ?, ?);");
				statement.setLong(1, engine.id);
				statement.setString(2, engine.engineCode);
				statement.setString(3, engine.make);
				statement.executeUpdate();
			}
			catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

		public static List<Engine> parseFromResultSet(ResultSet resultSet) {
			List<Engine> engines = new ArrayList<>();
			try {
				while (resultSet.next()) {
					engines.add(new Engine(resultSet.getInt("id"), resultSet.getString("engine_code"), resultSet.getString("make")));
				}
			}
			catch (SQLException e) {
				throw new RuntimeException(e);
			}
			return engines;
		}

		public static Engine getById(Connection connection, int id) {
			if (connection == null) {
				throw new RuntimeException("Your connection can't be null!");
			}
			try {
				PreparedStatement statement = connection.prepareStatement("select * from engine where id = ?;");
				statement.setLong(1, id);
				return Engine.parseFromResultSet(statement.executeQuery()).getFirst();
			}
			catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Getter
	@AllArgsConstructor
	private static class Car {
		private int id;
		private String make;
		private String model;
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

		public static List<Car> parseFromResultSet(Connection connection, ResultSet resultSet) {
			List<Car> cars = new ArrayList<>();
			try {
				while (resultSet.next()) {
					//lazy bool parameter addition + using it for lazy load or not
					Engine engine = Engine.getById(connection, resultSet.getInt("engine_id"));
					cars.add(new Car(resultSet.getInt("id"), resultSet.getString("make"), resultSet.getString("model"), engine));
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

			connection.prepareStatement("create table if not exists engine (id serial primary key, engine_code text not null, make text not null);").executeUpdate();
			connection.prepareStatement("create table if not exists car (id serial primary key, make text not null, model text not null, engine_id int references engine(id) not null);").executeUpdate();

			Engine engine = new Engine(1, "VW150hp", "VW-Engines");
			Engine.insert(connection, engine);
			Car.insert(connection, new Car(1, "VW", "Tiguan", engine));

			ResultSet resultSet = connection.prepareStatement("select * from car join engine on car.engine_id = engine.id;").executeQuery();
            for (Car car : Car.parseFromResultSet(connection, resultSet)) {
				System.out.printf("%d %s %s | %d %s %s\n", car.getId(), car.getMake(), car.getModel(), car.getEngine().getId(), car.getEngine().getEngineCode(), car.getEngine().getMake());
			}

//			resultSet = connection.prepareStatement("select * from engine;").executeQuery();
//            for (Engine myEngine : Engine.parseFromResultSet(resultSet)) {
//				System.out.printf("%d %s %s%n", myEngine.getId(), myEngine.getEngineCode(), myEngine.getMake());
//			}

			connection.close();
		}
		catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
