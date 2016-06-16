package testeDB.br.com.mv.infra.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class Db {
	
	private String login;
	private String url;
	private String password;
	
	public void setLogin(String login) {
		this.login = login;
	}
	public String getLogin() {
		return login;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	


	public static void main(String[] args) throws InterruptedException {
		long maxTimeConnection = 0;
		long qtdMaxConnection = 0;
		String x = "1";
		if  (args.length > 0) {
			x = args[0];
		};
		
		for (int i = 0; i < Integer.parseInt(x); i++) {
			long f = getConnection();
			if (f > maxTimeConnection){
				maxTimeConnection = f;
				
			}
			Thread.sleep(1000);
		}
		System.out.printf("Maior Tempo de Acesso: "+"%.3f ms%n", (maxTimeConnection) / 1000d);
		//System.out.println(qtdMaxConnection);

	}

	private static long getConnection() {
		long tempoInicial=0;
		long tempoFinal=0;
		try {

			Properties prop = getProp();
			String sql;
			

			String login = prop.getProperty("prop.server.login");
			String url = prop.getProperty("prop.server.url");
			String password = prop.getProperty("prop.server.password");
			// String url = "jdbc:oracle:thin:" + urlJodbc;
			tempoInicial = System.currentTimeMillis();

			// Abre-se a conexão com o Banco de Dados
			Connection con = DriverManager.getConnection(url, login, password);
			tempoFinal = System.currentTimeMillis();
			System.out.printf("Tempo de Acesso: "+"%.3f ms%n", (tempoFinal - tempoInicial) / 1000d);
			// Cria-se Statement com base na conexão con
			Statement stmt = con.createStatement();
			// System.out.println(stmt.getConnection().);

			sql = "SELECT INSTANCE_NUMBER, INSTANCE_NAME,  VERSION FROM v$instance";
			ResultSet res = stmt.executeQuery(sql);
			while (res.next()) {
				System.out.println("Instancia - "+res.getString("INSTANCE_NUMBER")+" - "+res.getString("INSTANCE_NAME") +"\n");

			}
			

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return (tempoFinal - tempoInicial);

	}

	public static Properties getProp() throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream("dados.properties");
		props.load(file);
		return props;

	}

}
