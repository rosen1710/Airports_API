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
	private static class Piston {
		private int id;
		private double bore;
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
			}
			catch (SQLException e) {
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

	@Getter
	@AllArgsConstructor
	private static class Rod {
		private int id;
		private double length;
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

	@Getter
	@AllArgsConstructor
	private static class Engine {
		private int id;
		private String engineCode;
		private String make;
		private Piston piston;
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
				statement.setInt(4, engine.piston.id);
				statement.setInt(5, engine.rod.id);
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

			connection.prepareStatement("create table if not exists piston (id serial primary key, bore float not null, material text not null);").executeUpdate();
			connection.prepareStatement("create table if not exists rod (id serial primary key, length float not null, material text not null);").executeUpdate();
			connection.prepareStatement("create table if not exists engine (id serial primary key, engine_code text not null, make text not null, piston_id int references piston(id) not null, rod_id int references rod(id) not null);").executeUpdate();
			connection.prepareStatement("create table if not exists car (id serial primary key, make text not null, model text not null, engine_id int references engine(id) not null);").executeUpdate();

//			Piston piston = new Piston(1, 5, "Steel");
//			Piston.insert(connection, piston);
//			Rod rod = new Rod(1, 10, "Steel");
//			Rod.insert(connection, rod);
//			Engine engine = new Engine(1, "VW150hp", "VW-Engines", piston, rod);
//			Engine.insert(connection, engine);
//			Car car = new Car(1, "VW", "Tiguan", engine);
//			Car.insert(connection, car);

			ResultSet resultSet = connection.prepareStatement("select * from car join engine on car.engine_id = engine.id;").executeQuery();
            for (Car myCar : Car.parseFromResultSet(connection, resultSet, 2)) {
//				System.out.printf("%d %s %s | %d %s %s (%d %f %s | %d %f %s)\n", myCar.getId(), myCar.getMake(), myCar.getModel(), myCar.getEngine().getId(), myCar.getEngine().getEngineCode(), myCar.getEngine().getMake(), myCar.getEngine().getPiston().getId(), myCar.getEngine().getPiston().getBore(), myCar.getEngine().getPiston().getMaterial(), myCar.getEngine().getRod().getId(), myCar.getEngine().getRod().getLength(), myCar.getEngine().getRod().getMaterial());
				System.out.println(myCar);
			}

			resultSet = connection.prepareStatement("select * from car join engine on car.engine_id = engine.id;").executeQuery();
            for (Car myCar : Car.parseFromResultSet(connection, resultSet, 1)) {
//				System.out.printf("%d %s %s | %d %s %s (%d %f %s | %d %f %s)\n", myCar.getId(), myCar.getMake(), myCar.getModel(), myCar.getEngine().getId(), myCar.getEngine().getEngineCode(), myCar.getEngine().getMake(), myCar.getEngine().getPiston().getId(), myCar.getEngine().getPiston().getBore(), myCar.getEngine().getPiston().getMaterial(), myCar.getEngine().getRod().getId(), myCar.getEngine().getRod().getLength(), myCar.getEngine().getRod().getMaterial());
				System.out.println(myCar);
			}

			resultSet = connection.prepareStatement("select * from car join engine on car.engine_id = engine.id;").executeQuery();
            for (Car myCar : Car.parseFromResultSet(connection, resultSet, 0)) {
//				System.out.printf("%d %s %s | %d %s %s (%d %f %s | %d %f %s)\n", myCar.getId(), myCar.getMake(), myCar.getModel(), myCar.getEngine().getId(), myCar.getEngine().getEngineCode(), myCar.getEngine().getMake(), myCar.getEngine().getPiston().getId(), myCar.getEngine().getPiston().getBore(), myCar.getEngine().getPiston().getMaterial(), myCar.getEngine().getRod().getId(), myCar.getEngine().getRod().getLength(), myCar.getEngine().getRod().getMaterial());
				System.out.println(myCar);
			}

			connection.close();
		}
		catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
