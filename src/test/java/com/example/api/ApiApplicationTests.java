package com.example.api;

import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@SpringBootTest
class ApiApplicationTests {

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
			ResultSet resultSet = connection.prepareStatement("select * from city").executeQuery();
			while (resultSet.next()) {
				System.out.println(resultSet.getBigDecimal("id"));
				System.out.println(resultSet.getString("name"));
				System.out.println(resultSet.getString("timezone"));
				System.out.println(resultSet.getString("country_iso2country_code"));
			}
		} catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
