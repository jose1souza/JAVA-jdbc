package view;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Scanner;

public class Main {

	// Driver
	private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

	// URL de conexão
	private static final String DB_URL = "jdbc:mysql://localhost/facebook";

	// Credenciais
	private static final String USER = "root";
	private static final String PW = "";

	public static void main(String[] args) throws Exception {
		Scanner reader = new Scanner(System.in);
		// read();
		System.out.println("Id do post a ser excluido:");
		int number = reader.nextInt();
		delete(number);
	}

	public static void delete(int postId) throws Exception {
		Connection connection = null;
		Statement statement = null;
		PreparedStatement preparedStatement = null;

		// Carga do driver do Mysql
		try {
			Class.forName(DRIVER_NAME);
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Driver JDBC não encontrado.");
			throw cnfe;
		}

		try {
			// Estabelecimento da conexão com o BD
			connection = DriverManager.getConnection(DB_URL, USER, PW);

			String sqlDelete = "DELETE FROM posts \r\n" + "WHERE\r\n" + "    id = ?;";

			preparedStatement = connection.prepareStatement(sqlDelete);
			preparedStatement.setInt(1, postId);

			int rowsAfected = preparedStatement.executeUpdate();

			String message = rowsAfected > 0 ? "Deletado com sucesso" : "Post não existe";
			System.out.println(message);

		} catch (SQLException sqle) {
			System.out.println("Erro ao conectar no Banco de Dados");

			if (sqle.getCause() != null)
				if (sqle.getCause().getCause() != null) {
					if (sqle.getCause().getCause() instanceof ConnectException)
						System.err.println("Banco de dados fora do ar");
					throw sqle;
				}
			System.err.println(sqle.getMessage());
			throw sqle;
		} finally {

			if (statement != null)
				statement.close();

			if (connection != null)
				connection.close();
		}

		System.out.println("Execução sem erros.");
	}

	public static void read() throws Exception {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		// Carga do driver do Mysql
		try {
			Class.forName(DRIVER_NAME);
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Driver JDBC não encontrado.");
			throw cnfe;
		}

		try {
			// Estabelecimento da conexão com o BD
			connection = DriverManager.getConnection(DB_URL, USER, PW);

			statement = connection.createStatement();
			String sqlSelect = "SELECT \r\n" + "    u.nome, p.content, p.post_date\r\n" + "FROM\r\n" + "    users u\r\n"
					+ "        INNER JOIN\r\n" + "    posts p ON u.id = p.user_id\r\n" + "ORDER BY p.post_date;";

			resultSet = statement.executeQuery(sqlSelect);

			while (resultSet.next()) {
				String userName = resultSet.getString("nome");
				String postsContent = resultSet.getString("content");
				Date postsDate = resultSet.getDate("post_date");

				System.out.println("Nome: " + userName);
				System.out.println("Post: " + postsContent);
				System.out.println("Data do post: " + postsDate.toString());
				System.out.println("------------");
			}

		} catch (SQLException sqle) {
			System.out.println("Erro ao conectar no Banco de Dados");

			if (sqle.getCause() != null)
				if (sqle.getCause().getCause() != null) {
					if (sqle.getCause().getCause() instanceof ConnectException)
						System.err.println("Banco de dados fora do ar");
					throw sqle;
				}
			System.err.println(sqle.getMessage());
			throw sqle;
		} finally {
			if (resultSet != null)
				resultSet.close();

			if (statement != null)
				statement.close();

			if (connection != null)
				connection.close();
		}

		System.out.println("Execução sem erros.");
	}
}