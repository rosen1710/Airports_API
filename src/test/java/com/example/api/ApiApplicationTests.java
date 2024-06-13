package com.example.api;

import com.example.api.carmodels.Car;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@SpringBootTest
class ApiApplicationTests {
	@Autowired
	private EntityManager entityManager;

	@Test
	void testOrm() {
		List<Car> cars = entityManager.createQuery("select c from Car c join fetch c.engine eng join fetch eng.rod rod", Car.class).getResultList();
		for (Car car : cars) {
			System.out.println(car);
		}
	}

//	@Test
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
