package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BancoDados {
	
	public Connection conectaBanco() {
		try {
			return DriverManager.getConnection(
					"jdbc:mysql://localhost/matc93?useTimezone=true&serverTimezone=UTC", "root", "kyugin");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void insereFilme(String name, String country, String varAbstract, String budget, String  releaseDate, String runtime, String alternateTitle, String starring , String director , String producer) {
		Connection conn = conectaBanco();
		PreparedStatement ps = null;

		String query = "INSERT INTO `movie` (`name`, `country`, `abstract`, `budget`, `releaseDate`, `runtime`, `alternateTitle`, `starring`, `director`, `producer`) VALUES (?,?,?,?,?,?,?,?,?,?)";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, name);
			ps.setString(2, country);
			ps.setString(3, varAbstract);
			ps.setString(4, budget);
			ps.setString(5, releaseDate);
			ps.setString(6, runtime);
			ps.setString(7, alternateTitle);
			ps.setString(8, starring);
			ps.setString(9, director);
			ps.setString(10, producer);
			ps.execute();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
