package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Model.Movie;

public class BancoDados {
	
	public Connection conectaBanco() {
		try {
			return DriverManager.getConnection(
					"jdbc:mysql://localhost/matc93?useTimezone=true&serverTimezone=UTC&useSSL=false", "root", "");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void insereFilme(Movie movie) {
		Connection conn = conectaBanco();
		PreparedStatement ps = null;

		String query = "INSERT INTO `movie` (`name`, `country`, `abstract`, `budget`, `releaseDate`, `runtime`, `alternateTitle`, `starring`, `director`, `producer`, `genre`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, movie.getName());
			ps.setString(2, movie.getCountry());
			ps.setString(3, movie.getVarAbstract());
			ps.setString(4, movie.getBudget());
			ps.setString(5, movie.getReleaseDate());
			ps.setString(6, movie.getRuntime());
			ps.setString(7, movie.getAlternateTitle());
			ps.setString(8, movie.getStarring());
			ps.setString(9, movie.getDirector());
			ps.setString(10, movie.getProducer());
			ps.setString(11, movie.getGenre());
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
